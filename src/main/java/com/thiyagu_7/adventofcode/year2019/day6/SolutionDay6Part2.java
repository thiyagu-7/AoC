package com.thiyagu_7.adventofcode.year2019.day6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay6Part2 {
    public int part2(List<String> lines) {
        Map<String, List<String>> orbits = prepareOrbits(lines);
        String you = orbits.get("YOU").get(0);
        String san = orbits.get("SAN").get(0);
        Result result = new Result();

        dfs(orbits, san, 0, you, new HashSet<>(), result);
        return result.res;
    }

    private Map<String, List<String>> prepareOrbits(List<String> lines) {
        Map<String, List<String>> orbits = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split("\\)");
            orbits.computeIfAbsent(parts[0], e -> new ArrayList<>());
            orbits.computeIfAbsent(parts[1], e -> new ArrayList<>());
            orbits.get(parts[0]).add(parts[1]);
            orbits.get(parts[1]).add(parts[0]);
        }
        return orbits;
    }

    private String getObjectOrbitingBy(List<String> lines, String person) {
        return lines.stream()
                .filter(e -> e.endsWith(person))
                .findFirst()
                .get()
                .split("\\)")[0];
    }

    private void dfs(Map<String, List<String>> orbits, String dest, int count, String current, Set<String> visited,
                     Result result) {
        visited.add(current);
        if (!orbits.containsKey(current)) {
            return;
        }
        if (current.equals(dest)) {
            result.res = Math.min(result.res, count);
        }
        for (String obj : orbits.get(current)) {
            if (!visited.contains(obj)) {
                dfs(orbits, dest, count + 1, obj, visited, result);
                visited.remove(obj);
            }
        }
    }

    private static class Result {
        int res = Integer.MAX_VALUE;
    }
}
