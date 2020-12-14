package com.thiyagu_7.adventofcode.year2020.day10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay10 {
    public int part1(List<String> input) {
        Map<Integer, Integer> differencesCount = findAllDifferences(input);
        return differencesCount.get(1) * differencesCount.get(3);
    }

    Map<Integer, Integer> findAllDifferences(List<String> input) {
        List<Integer> joltageRatings = input.stream()
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

        int currentRating = 0;
        Map<Integer, Integer> differencesCount = new HashMap<>();

        for (Integer rating : joltageRatings) {
            int diff = rating - currentRating;
            differencesCount.merge(diff, 1, (o, n) -> o + 1);
            currentRating += diff;
        }

        differencesCount.merge(3, 1, (o, n) -> o + 1);
        return differencesCount;
    }

    //total number of distinct ways you can arrange the adapters to connect the charging outlet to your device
    public long part2(List<String> input) {
        /*Set<Integer> joltageRatings = input.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        int maxRating = new TreeSet<>(joltageRatings).last();
        Result result = new Result();
        recur(joltageRatings, new ArrayList<>(), 0, maxRating + 3, result);
        return result.result;*/
        return dp(input.stream()
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList()));
    }

    private void recur(Set<Integer> joltageRatings, List<Integer> currentSetup, int currentRating, int maxRating, Result result) {
        if (currentRating + 3 == maxRating) {
            result.result++;
        }
        for (int i = 1; i <= 3; i++) {
            if (joltageRatings.contains(currentRating + i)) {
                List<Integer> current = new ArrayList<>(currentSetup);
                current.add(currentRating + i);

                Set<Integer> jolts = new HashSet<>(joltageRatings);
                jolts.remove(currentRating + i);
                recur(jolts, current, currentRating + i, maxRating, result);
            }
        }
    }

    private long dp(List<Integer> joltageRatings) {
        LinkedHashMap<Integer, Long> dp = new LinkedHashMap<>();
        dp.put(0, 1L);
        for (int current : joltageRatings) {
            long a = dp.getOrDefault(current - 1, 0L);
            long b = dp.getOrDefault(current - 2, 0L);
            long c = dp.getOrDefault(current - 3, 0L);
            dp.put(current, a + b + c);
        }
        return dp.get(joltageRatings.get(joltageRatings.size() - 1));//get last value
    }
    private static class Result {
        int result;
    }
}
