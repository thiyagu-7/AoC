package com.thiyagu_7.adventofcode.year2019.day4;

import java.util.stream.IntStream;

/**
 * https://adventofcode.com/2019/day/4
 */
public class SolutionDay4 {
    public long part1(int start, int end) {
        return IntStream.rangeClosed(start, end)
                .filter(this::numberSatisfiesCondition)
                .count();
    }

    public long part2(int start, int end) {
        return IntStream.rangeClosed(start, end)
                .filter(this::numberSatisfiesConditionForPart2)
                .count();
    }
    private boolean numberSatisfiesCondition(int number) {
        String num = String.valueOf(number);
        int adjacentSame = 1;
        for (int i = 0; i < num.length() - 1; i++) {
            if (num.charAt(i) == num.charAt(i + 1)) {
                adjacentSame++;
            }
            //Check if increasing or same
            if (num.charAt(i + 1) < num.charAt(i)) {
                return false;
            }
        }
        return adjacentSame > 1;
    }

    private boolean numberSatisfiesConditionForPart2(int number) {
        String num = String.valueOf(number);
        int adj = Integer.MAX_VALUE;
        int adjacentSame = 1;

        for (int i = 0; i < num.length() - 1; i++) {
            if (num.charAt(i) == num.charAt(i + 1)) {
                adjacentSame++;
            } else if (adjacentSame > 1) {
                adj = Math.min(adj, adjacentSame);
                adjacentSame = 1;
            }
            //Check if increasing or same
            if (num.charAt(i + 1) < num.charAt(i)) {
                return false;
            }
        }
        if (adjacentSame > 1) {
            adj = Math.min(adj, adjacentSame);
        }
        return adj == 2; //atleast one (exact) pair
    }
}
