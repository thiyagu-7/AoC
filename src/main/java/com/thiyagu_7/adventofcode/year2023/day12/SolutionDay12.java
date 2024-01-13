package com.thiyagu_7.adventofcode.year2023.day12;

import java.util.ArrayList;
import java.util.List;

//Brute force
public class SolutionDay12 {
    public int part1(List<String> input) {
        List<SpringDetail> springDetails = input.stream()
                .map(InputParser::parseLine)
                .toList();
        int arrangements = 0;
        for (SpringDetail springDetail : springDetails) {
            Result result = new Result();
            process(springDetail.spring(), springDetail.damagedSpringsGroupCount(), 0, result);
            arrangements += result.arrangements;
        }
        return arrangements;
    }

    private void process(String spring, List<Integer> damagedSpringsGroupCount, int i, Result result) {
        if (i == spring.length()) {
            int damaged = 0;
            List<Integer> groups = new ArrayList<>();

            for (int j = 0; j < spring.length(); j++) {
                if (spring.charAt(j) == '#') {
                    damaged++;
                } else if (spring.charAt(j) == '.' && damaged > 0) {
                    groups.add(damaged);
                    damaged = 0;
                }
            }
            if (damaged > 0) {
                groups.add(damaged);
            }
            if (groups.equals(damagedSpringsGroupCount)) {
                result.arrangements++;
            }
            return;
        }

        if (spring.charAt(i) == '?') {
            char[] arr = spring.toCharArray();
            arr[i] = '.';
            process(new String(arr), damagedSpringsGroupCount, i + 1, result);
            arr[i] = '#';
            process(new String(arr), damagedSpringsGroupCount, i + 1, result);
        } else {
            process(spring, damagedSpringsGroupCount, i + 1, result);
        }
    }

    private static class Result {
        int arrangements = 0;
    }
}
