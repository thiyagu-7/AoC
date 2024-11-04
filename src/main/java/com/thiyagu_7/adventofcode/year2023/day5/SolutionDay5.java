package com.thiyagu_7.adventofcode.year2023.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionDay5 {
    public long part1(List<String> input) {
        List<Mapping> almanac = parseInput(input);
        List<Long> seeds = Arrays.stream(input.get(0)
                        .split(": ")[1]
                        .split(" "))
                .map(Long::parseLong)
                .toList();

        Map<Long, Long> seedToLocation = new HashMap<>();

        for (Long seed : seeds) {
            long current = seed;
            // go through each of the maps in order
            for (Mapping mapping : almanac) {
                for (Range range : mapping.ranges) {
                    if (current >= range.sourceStartRange
                            && current <= range.sourceStartRange + range.length - 1) {
                        long offset = current - range.sourceStartRange;
                        current = offset + range.destinationStartRange;
                        break;
                    }
                }
            }
            seedToLocation.put(seed, current);
        }
        return seedToLocation.values()
                .stream()
                .min(Comparator.naturalOrder())
                .get();
    }

    private List<Mapping> parseInput(List<String> input) {
        List<Mapping> almanac = new ArrayList<>();
        List<Range> ranges = new ArrayList<>();

        for (int i = 3; i < input.size(); i++) {
            String line = input.get(i);
            if (line.isEmpty()) {
                almanac.add(new Mapping(ranges));
                ranges = new ArrayList<>();
                i++;
            } else {
                String[] parts = line.split(" ");
                ranges.add(new Range(
                        Long.parseLong(parts[1]),
                        Long.parseLong(parts[0]),
                        Long.parseLong(parts[2])
                ));
            }
        }
        almanac.add(new Mapping(ranges));
        return almanac;
    }

    public long part2Brute(List<String> input) {
        List<Mapping> almanac = parseInput(input);
        // pairs of <seedNumStart lengthOfRange>
        Map<Long, Long> seedNumStartToLength = new HashMap<>();
        String[] parts = input.get(0)
                .split(": ")[1]
                .split(" ");

        for (int i = 0; i < parts.length; i += 2) {
            seedNumStartToLength.put(Long.parseLong(parts[i]), Long.parseLong(parts[i + 1]));
        }
        long location = 1;
        long current = location;
        while (true) {
            // go through each of the maps in reverse order
            for (int i = almanac.size() - 1; i >= 0; i--) {
                Mapping mapping = almanac.get(i);
                for (Range range : mapping.ranges) {
                    //reverse map
                    if (current >= range.destinationStartRange
                            && current <= range.destinationStartRange + range.length - 1) {
                        long offset = current - range.destinationStartRange;
                        current = offset + range.sourceStartRange;
                        break;
                    }
                }
            }
            //check if a seed number exists with value of 'current'
            for (Map.Entry<Long, Long> entry : seedNumStartToLength.entrySet()) {
                long seedStart = entry.getKey();
                long length = entry.getValue();
                if (current >= seedStart && current <= seedStart + length - 1) {
                    return location;
                }
            }
            current = ++location;
        }
    }

    public long part2(List<String> input) {
        List<Mapping> almanac = parseInput(input);
        // pairs of <seedNumStart lengthOfRange>
        Map<Long, Long> seedNumStartToLength = new HashMap<>();
        String[] parts = input.get(0)
                .split(": ")[1]
                .split(" ");

        for (int i = 0; i < parts.length; i += 2) {
            seedNumStartToLength.put(Long.parseLong(parts[i]), Long.parseLong(parts[i + 1]));
        }
        List<PlainRange> resultRanges = new ArrayList<>();
        check(almanac, almanac.size() - 1, null, seedNumStartToLength, null, resultRanges);

        //part 1
        long min = Long.MAX_VALUE;
        for (PlainRange pr : resultRanges) {
            for (long a = pr.start; a <= pr.end; a++) {
                long current = a;
                // go through each of the maps in order
                for (Mapping mapping : almanac) {
                    for (Range range : mapping.ranges) {
                        if (current >= range.sourceStartRange
                                && current <= range.sourceStartRange + range.length - 1) {
                            long offset = current - range.sourceStartRange;
                            current = offset + range.destinationStartRange;
                            break;
                        }
                    }
                }
                min = Math.min(min, current);
                break;
            }
        }
        //
        return min;
    }

    record PlainRange(long start, long end) {

    }

    private boolean check(List<Mapping> almanac, int i,
                          PlainRange rangeToCheckInDest,
                          Map<Long, Long> seedNumStartToLength,
                          PlainRange locationRange,
                          List<PlainRange> resultRanges) {

        if (i == -1) {
            long start = rangeToCheckInDest.start;
            long end = rangeToCheckInDest.end;

            boolean added = false;
            for (Map.Entry<Long, Long> entry : seedNumStartToLength.entrySet()) {
                long seedStart = entry.getKey();
                long seedEnd = seedStart + entry.getValue() - 1;

                long dS, dE;
                if (end < seedStart || start > seedEnd) { //outside
                    continue;
                }
                if (seedStart <= start && seedEnd >= end) { //inside
                    dS = start - seedStart;
                    dE = seedEnd - end;

                } else if (start < seedStart && end <= seedEnd) { //left extend
                    dS = 0;
                    dE = seedEnd - end;

                } else if (start >= seedStart && end > seedEnd) { //right extend
                    dS = start - seedStart;
                    dE = 0;

                } else { //extend on both side
                    dS = 0;
                    dE = 0;
                }
                //TODO - need only first value in this (seedStart + dS)
                //Q: how seedStart + dS is resulting in the first (min) location value?
                resultRanges.add(new PlainRange(seedStart + dS, seedEnd - dE));
                added = true;
                break;
            }
            return added;
        } else {
            List<RangeWithStartEnd> rangeWithStartEnds = new ArrayList<>(getRanges(almanac.get(i)));
            rangeWithStartEnds.sort(Comparator.comparingLong(RangeWithStartEnd::destStart));

            boolean flag = false;

            //last map
            if (i == almanac.size() - 1) {
                for (RangeWithStartEnd rangeWithStartEnd : rangeWithStartEnds) {
                    flag = check(almanac, i - 1,
                            new PlainRange(rangeWithStartEnd.sourceStart, rangeWithStartEnd.sourceEnd),
                            seedNumStartToLength,
                            new PlainRange(rangeWithStartEnd.destStart, rangeWithStartEnd.destEnd),
                            resultRanges);
                    if (flag) {
                        break;
                    }
                }
                return flag;
            }

            long start = rangeToCheckInDest.start;
            long end = rangeToCheckInDest.end;
            long dS;
            long dE;
            //for location range update
            long dS1;
            long dE1;
            for (RangeWithStartEnd rangeWithStartEnd : rangeWithStartEnds) {
                long currStart = rangeWithStartEnd.destStart;
                long currEnd = rangeWithStartEnd.destEnd;

                if (end < currStart || start > currEnd) { //outside
                    continue;
                }
                if (currStart <= start && currEnd >= end) { //curr is bigger one
                    dS = start - currStart;
                    dE = currEnd - end;
                    dS1 = 0;
                    dE1 = 0;
                } else if (start < currStart && end <= currEnd) { //left extend
                    dS = 0;
                    dE = currEnd - end;

                    dS1 = currStart - start;
                    dE1 = 0;

                } else if (start >= currStart && end > currEnd) { //right extend
                    dS = start - currStart;
                    dE = 0;

                    dS1 = 0;
                    dE1 = end - currEnd;
                } else { //extend on both side
                    dS = 0;
                    dE = 0;

                    dS1 = currStart - start;
                    dE1 = end - currEnd;
                }
                PlainRange plainRange = new PlainRange(
                        rangeWithStartEnd.sourceStart + dS,
                        rangeWithStartEnd.sourceEnd - dE);
                if (i == almanac.size() - 2) {
                    //locationRange NOT NEEDED - key is sorted location dest ranges
                    //update locationRange
                    locationRange = new PlainRange(
                            locationRange.start + dS1,
                            locationRange.end - dE1
                    );
                }
                flag = check(almanac, i - 1, plainRange, seedNumStartToLength, locationRange, resultRanges);
                if (flag) {
                    break;
                }
            }
            return flag;
        }
    }

    private List<RangeWithStartEnd> getRanges(Mapping mapping) {
        List<RangeWithStartEnd> rangeWithStartEnd = new ArrayList<>();
        List<Range> ranges = new ArrayList<>(mapping.ranges);
        ranges.sort(Comparator.comparingLong(Range::sourceStartRange));

        long currentStart = 1;
        long length;
        for (Range range : ranges) {
            if (range.sourceStartRange > currentStart) {
                length = range.sourceStartRange - currentStart - 1;
                rangeWithStartEnd.add(new RangeWithStartEnd(
                        currentStart, currentStart + length,
                        currentStart, currentStart + length));
            }
            length = range.length - 1;
            rangeWithStartEnd.add(new RangeWithStartEnd(
                    range.sourceStartRange, range.sourceStartRange + length,
                    range.destinationStartRange, range.destinationStartRange + length));

            currentStart = range.sourceStartRange + length + 1;
        }
        //inf?
        rangeWithStartEnd.add(new RangeWithStartEnd(
                currentStart, Long.MAX_VALUE,
                currentStart, Long.MAX_VALUE));
        return rangeWithStartEnd;
    }

    record Range(Long sourceStartRange, Long destinationStartRange, Long length) {

    }

    record RangeWithStartEnd(long sourceStart, long sourceEnd, long destStart, long destEnd) {

    }

    record Mapping(List<Range> ranges) {

    }

}
