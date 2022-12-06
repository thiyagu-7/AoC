package com.thiyagu_7.adventofcode.year2022.day6;

import java.util.HashSet;
import java.util.Set;

public class SolutionDay6 {
    public int part1(String input) {
       return process(input, 4);
    }

    public int part2(String input) {
        return process(input, 14);
    }

    private int process(String input, int windowLength) {
        int start = 0;
        int end = windowLength - 1;
        while (end < input.length()) {
            if (noDuplicate(input, start, end)) {
                return end + 1;
            }
            start++;
            end++;
        }
        throw new RuntimeException();
    }

    private boolean noDuplicate(String s, int start, int end) {
        Set<Character> chars = new HashSet<>();
        for (int i = start; i <= end; i++) {
            if (!chars.add(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
