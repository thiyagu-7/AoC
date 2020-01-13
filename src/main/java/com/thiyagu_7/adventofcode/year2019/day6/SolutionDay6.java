package com.thiyagu_7.adventofcode.year2019.day6;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SolutionDay6 {
    public int part1(List<String> lines) {
        Map<String, List<String>> orbits = prepareOrbits(lines);
        return dfs(orbits, 0, "COM");
    }

    private Map<String, List<String>> prepareOrbits(List<String> lines) {
        return lines.stream()
                .map(row -> row.split("\\)"))
                .collect(Collectors.groupingBy(arr -> arr[0],
                        Collectors.mapping(arr -> arr[1], Collectors.toList())));
    }

    private int dfs(Map<String, List<String>> orbits, int count, String current) {
        if (!orbits.containsKey(current)) {
            return count;
        }
        int result = 0;
        for (String obj : orbits.get(current)) {
            result += dfs(orbits, count + 1, obj);
        }
        return result + count;
    }
}
