package com.thiyagu_7.adventofcode.year2021.day12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SolutionDay12 {
    public int part1(List<String> input) {
        Map<String, List<String>> adjacencyList = buildAdjacencyList(input);
        Result result = new Result();
        dfs1(adjacencyList, "start", new ArrayList<>(), result);
        return result.count;
    }

    private  Map<String, List<String>> buildAdjacencyList(List<String> input) {
        Map<String, List<String>> adjacencyListLToR = input.stream()
                .map(line -> line.split("-"))
                .collect(Collectors.groupingBy(parts -> parts[0],
                        Collectors.mapping(parts -> parts[1], Collectors.toList())));
        Map<String, List<String>> adjacencyListRToL = input.stream()
                .map(line -> line.split("-"))
                .collect(Collectors.groupingBy(parts -> parts[1],
                        Collectors.mapping(parts -> parts[0], Collectors.toList())));
        Map<String, List<String>> adjacencyList = new HashMap<>(adjacencyListLToR);
        adjacencyListRToL
                .forEach((k, v) -> adjacencyList.merge(k, v, (o,n) -> {
                    o.addAll(n);
                    return o;
                }));
        return adjacencyList;
    }

    private void dfs1(Map<String, List<String>> adjacencyList, String current, List<String> currentPath, Result result) {
        currentPath.add(current);
        if (current.equals("end")) {
            result.count++;
            return;
        }
        for (String neighbourCave: adjacencyList.getOrDefault(current, Collections.emptyList())) {
            if (!isSmallCave(neighbourCave) || !currentPath.contains(neighbourCave)) {
                dfs1(adjacencyList, neighbourCave, new ArrayList<>(currentPath), result);
            }
        }
    }

    private boolean isSmallCave(String neighbourCave) {
        return Character.isLowerCase(neighbourCave.charAt(0));
    }

    public int part2(List<String> input) {
        Map<String, List<String>> adjacencyList = buildAdjacencyList(input);
        Result result = new Result();
        dfs2(adjacencyList, "start", new ArrayList<>(), result);
        return result.count;
    }

    private void dfs2(Map<String, List<String>> adjacencyList, String current, List<String> currentPath, Result result) {
        currentPath.add(current);
        if (current.equals("end")) {
            result.count++;
            return;
        }
        for (String neighbourCave: adjacencyList.getOrDefault(current, Collections.emptyList())) {
            if (!isSmallCave(neighbourCave) || canVisitSmallCave(currentPath, neighbourCave)) {
                dfs2(adjacencyList, neighbourCave, new ArrayList<>(currentPath), result);
            }
        }
    }

    private boolean canVisitSmallCave(List<String> currentPath, String neighbourCave) {
        if (neighbourCave.equals("start")) {
            return false;
        }

        if (neighbourCave.equals("end")) {
            return true;
        }

        Map<String, Long> smallCavesVisitedCount = currentPath.stream()
                .filter(this::isSmallCave)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long maxTimeASmallCaveIsVisited = smallCavesVisitedCount
                .values()
                .stream()
                .max(Comparator.naturalOrder())
                .orElse(0L);

        /*
         if max is 0 or 1, then can visit neighbourCave
         else (means some small cave has already been visited more than once)
            neighbourCave must be the 1st time visited
         */
        return maxTimeASmallCaveIsVisited <= 1 || !smallCavesVisitedCount.containsKey(neighbourCave);
    }

    private static class Result {
        private int count;
    }
}
