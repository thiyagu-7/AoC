package com.thiyagu_7.adventofcode.year2023.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public long part2a(List<String> input) {
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

    record Range(Long sourceStartRange, Long destinationStartRange, Long length) {

    }

    record Mapping(List<Range> ranges) {

    }

}
