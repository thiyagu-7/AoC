package com.thiyagu_7.adventofcode.year2022.day4;

import java.util.List;
import java.util.function.BiFunction;

public class SolutionDay4 {
    public int part1(List<String> input) {
        return process(input, this::isFullyContained);
    }

    public int part2(List<String> input) {
        return process(input, this::doesOverlap);
    }

    private int process(List<String> input, BiFunction<Range, Range, Boolean> func) {
        return (int) input.stream()
                .map(this::getPairs)
                .map(pairs -> func.apply(pairs[0], pairs[1]))
                .filter(b -> b)
                .count();
    }

    private Range[] getPairs(String line) {
        String[] parts = line.split(",");
        String[] range1 = parts[0].split("-");
        String[] range2 = parts[1].split("-");
        return new Range[]{
                new Range(Integer.parseInt(range1[0]), Integer.parseInt(range1[1])),
                new Range(Integer.parseInt(range2[0]), Integer.parseInt(range2[1])),
        };
    }

    /**
     * Checks if one range is fully contained within the other
     *
     * @param r1 tbe first range.
     * @param r2 the second range.
     * @return true if one range is fully contained within the other and false otherwise.
     */
    private boolean isFullyContained(Range r1, Range r2) {
        return (r1.start() <= r2.start() && r1.end() >= r2.end()) ||
                (r1.start() >= r2.start() && r1.end() <= r2.end());
    }

    /**
     * Checks if one range overlaps with another.
     *
     * @param r1 the first range.
     * @param r2 the second range.
     * @return true if one range overlaps with another and false otherwise.
     */
    private boolean doesOverlap(Range r1, Range r2) {
        Range range1 = r1.start() <= r2.start() ? r1 : r2;
        Range range2 = r1.start() <= r2.start() ? r2 : r1;
        return !(range2.start() > range1.end());
    }

    private record Range(int start, int end) {
    }
}
