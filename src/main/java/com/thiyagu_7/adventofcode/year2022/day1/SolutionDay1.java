package com.thiyagu_7.adventofcode.year2022.day1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class SolutionDay1 {
    public int part1(List<String> input) {
        List<Integer> calories = getCaloriesForEachElf(input);
        calories.sort(Comparator.reverseOrder());
        return calories.get(0);
    }

    public int part2(List<String> input) {
        List<Integer> calories = getCaloriesForEachElf(input);
        calories.sort(Comparator.reverseOrder());
        return IntStream.rangeClosed(0, 2)
                .map(calories::get)
                .sum();
    }
    private List<Integer> getCaloriesForEachElf(List<String> input) {
        int curr = 0;
        List<Integer> result = new ArrayList<>();
        for (String line : input) {
            if (line.isEmpty()) {
                result.add(curr);
                curr = 0;
            } else {
                curr += Integer.parseInt(line);
            }
        }
        result.add(curr);
        return result;
    }
}
