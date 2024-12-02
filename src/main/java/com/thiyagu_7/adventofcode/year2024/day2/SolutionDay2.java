package com.thiyagu_7.adventofcode.year2024.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolutionDay2 {

    public long part1(List<String> input) {
        return input.stream()
                .map(this::parseLine)
                .filter(this::isReportSafe)
                .count();
    }

    private int[] parseLine(String line) {
        return Arrays.stream(line.split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();

    }

    /*
    A report only counts as safe if both of the following are true:
      - The levels are either all increasing or all decreasing.
      - Any two adjacent levels differ by at least one and at most three.
     */
    private boolean isReportSafe(int[] levels) {
        if (levels[0] == levels[1]) {
            return false;
        }
        boolean isIncreasing = levels[1] > levels[0];

        for (int i = 1; i < levels.length; i++) {
            if (levels[i] == levels[i - 1]) {
                return false;
            }
            if (isIncreasing) {
                if (levels[i] < levels[i - 1] || levels[i] - levels[i - 1] > 3) {
                    return false;
                }
            } else if (levels[i] > levels[i - 1] || levels[i - 1] - levels[i] > 3) {
                return false;
            }
        }
        return true;
    }

    public long part2(List<String> input) {
        long res = 0;
        for (String line : input) {
            int[] levels = parseLine(line);
            if (isReportSafe(levels)) {
                res++;
            } else {
                for (int i = 0; i < levels.length; i++) {
                    List<Integer> levelsCopy = new ArrayList<>(Arrays.stream(levels)
                            .boxed()
                            .toList());
                    levelsCopy.remove(i);
                    int[] levelsCopyArr = levelsCopy.stream()
                            .mapToInt(l -> l)
                            .toArray();
                    if (isReportSafe(levelsCopyArr)) {
                        res++;
                        break;
                    }
                }

            }
        }
        return res;
    }
}
