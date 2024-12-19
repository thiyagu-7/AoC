package com.thiyagu_7.adventofcode.year2024.day19;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay19 {
    public int part1(List<String> input) {
        TowelPatternsAndDesigns towelPatternsAndDesigns = parseInput(input);
        //max length of a pattern
        int maxLength = towelPatternsAndDesigns.towelPatterns
                .stream()
                .mapToInt(String::length)
                .max()
                .getAsInt();

        int possibleDesigns = 0;

        for (String design : towelPatternsAndDesigns.requiredDesigns) {
            if (recur(towelPatternsAndDesigns.towelPatterns, design, 0, maxLength)) {
                possibleDesigns++;
            }
        }
        return possibleDesigns;
    }

    private TowelPatternsAndDesigns parseInput(List<String> input) {
        Set<String> towelPatterns = Arrays.stream(input.get(0).split(", "))
                .collect(Collectors.toSet());

        List<String> requiredDesigns = input.subList(2, input.size());
        return new TowelPatternsAndDesigns(towelPatterns, requiredDesigns);
    }

    private boolean recur(Set<String> towelPatterns, String design, int idx, int maxLength) {
        if (idx == design.length()) {
            return true;
        }

        for (int l = 1; l <= maxLength && idx + l <= design.length(); l++) {
            String substring = design.substring(idx, idx + l); //endIndex exclusive
            if (towelPatterns.contains(substring)) {
                boolean r = recur(towelPatterns, design, idx + l, maxLength);
                if (r) {
                    return true;
                }
            }
        }
        return false;
    }

    record TowelPatternsAndDesigns(Set<String> towelPatterns, List<String> requiredDesigns) {

    }

    public long part2(List<String> input) {
        TowelPatternsAndDesigns towelPatternsAndDesigns = parseInput(input);
        //max length of a pattern
        int maxLength = towelPatternsAndDesigns.towelPatterns
                .stream()
                .mapToInt(String::length)
                .max()
                .getAsInt();

        Map<String, Long> stringToNumWaysCache = new HashMap<>();
        long result = 0;
        for (String design : towelPatternsAndDesigns.requiredDesigns) {
            result += recurPart2(towelPatternsAndDesigns.towelPatterns, design, 0, maxLength, stringToNumWaysCache);
        }
        return result;
    }


    private long recurPart2(Set<String> towelPatterns, String design, int idx, int maxLength,
                            Map<String, Long> stringToNumWaysCache) {
        if (idx == design.length()) {
            return 1;
        }
        String rest = design.substring(idx); //[idx...design.length - 1]
        if (stringToNumWaysCache.containsKey(rest)) {
            return stringToNumWaysCache.get(rest);
        }

        long numWays = 0;
        for (int l = 1; l <= maxLength && idx + l <= design.length(); l++) {
            String substring = design.substring(idx, idx + l); //endIndex exclusive
            if (towelPatterns.contains(substring)) {
                numWays += recurPart2(towelPatterns, design, idx + l, maxLength,stringToNumWaysCache );
            }
        }
        stringToNumWaysCache.put(rest, numWays);
        return numWays;
    }
}
