package com.thiyagu_7.adventofcode.year2024.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolutionDay5 {
    private Map<String, Set<String>> allDependenciesCache = new HashMap<>();

    public int part1(List<String> input) {
        ParsedInput parsedInput = parseInput(input);
        Map<String, List<String>> pageDependencyRules = invertOrderingRules(parsedInput.pageOrderingRules);

        int middlePagesSum = 0;
        for (List<String> update : parsedInput.updates) {
            if (isValidUpdate(pageDependencyRules, update)) {
                middlePagesSum += Integer.parseInt(update.get(update.size() / 2));
            }
        }
        return middlePagesSum;
    }

    private ParsedInput parseInput(List<String> input) {
        int i;
        Map<String, List<String>> pageOrderingRules = new HashMap<>();
        for (i = 0; i < input.size(); i++) {
            String line = input.get(i);
            if (line.isEmpty()) {
                i++;
                break;
            }
            String[] parts = line.split("\\|");
            pageOrderingRules.computeIfAbsent(parts[0], ignoreMe -> new ArrayList<>())
                    .add(parts[1]);
        }

        List<List<String>> updates = new ArrayList<>();
        for (; i < input.size(); i++) {
            String line = input.get(i);
            String[] parts = line.split(",");
            updates.add(Arrays.stream(parts)
                    .toList());
        }
        return new ParsedInput(pageOrderingRules, updates);
    }

    /**
     * Input A -> C, B -> C (A before C and B before C)
     * Output C -> [A, B] (C before A and B)
     */
    private Map<String, List<String>> invertOrderingRules(Map<String, List<String>> pageOrderingRules) {
        Map<String, List<String>> invertedOrderingRules = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : pageOrderingRules.entrySet()) {
            List<String> values = entry.getValue();
            for (String value : values) {
                invertedOrderingRules.computeIfAbsent(value, ignoreMe -> new ArrayList<>())
                        .add(entry.getKey());
            }
        }
        return invertedOrderingRules;
    }

    private boolean isValidUpdate(Map<String, List<String>> pageDependencyRules, List<String> update) {
        allDependenciesCache = new HashMap<>(); //re-initialize
        Set<String> previousPages = new HashSet<>();

        for (String page : update) {
            Set<String> allDependenciesForPage = getAllDependenciesForPage(pageDependencyRules, page, update);
            for (String pageDependency : allDependenciesForPage) { //all dependencies should be before this in the update
                if (!previousPages.contains(pageDependency)) {
                    return false;
                }
            }
            previousPages.add(page);
        }
        return true;
    }

    private Set<String> getAllDependenciesForPage(Map<String, List<String>> pageDependencyRules,
                                                  String currentPage,
                                                  List<String> currentUpdate) {
        if (allDependenciesCache.containsKey(currentPage)) {
            return allDependenciesCache.get(currentPage);
        }
        Set<String> allDependenciesForPage = new HashSet<>();

        if (pageDependencyRules.containsKey(currentPage)) {
            List<String> dependencies = pageDependencyRules.get(currentPage);

            for (String pageDependency : dependencies) {
                if (currentUpdate.contains(pageDependency)) { //only if present in the current update
                    allDependenciesForPage.add(pageDependency);
                    allDependenciesForPage.addAll(getAllDependenciesForPage(pageDependencyRules, pageDependency, currentUpdate));
                }
            }
        }
        allDependenciesCache.put(currentPage, allDependenciesForPage);
        return allDependenciesForPage;
    }

    //if valid order, return empty list
    private List<String> findPageOrder(Map<String, List<String>> pageDependencyRules, List<String> update) {
        if (!isValidUpdate(pageDependencyRules, update)) {
            Map<String, Set<String>> allDependencies = new HashMap<>();
            for (String page : update) {
                Set<String> allDependenciesForPage = getAllDependenciesForPage(pageDependencyRules, page, update);
                allDependencies.put(page, allDependenciesForPage);
            }
            //sort them based on number of dependencies to find the order
            return allDependencies.entrySet()
                    .stream()
                    .sorted(Comparator.comparingInt(e -> e.getValue().size()))
                    .map(Map.Entry::getKey)
                    .toList();
        }

        return List.of();
    }

    record ParsedInput(Map<String, List<String>> pageOrderingRules, List<List<String>> updates) {

    }

    public int part2(List<String> input) {
        ParsedInput parsedInput = parseInput(input);
        Map<String, List<String>> pageDependencyRules = invertOrderingRules(parsedInput.pageOrderingRules);

        int middlePagesSum = 0;
        for (List<String> update : parsedInput.updates) {
            List<String> pageOrder = findPageOrder(pageDependencyRules, update);
            if (!pageOrder.isEmpty()) {
                middlePagesSum += Integer.parseInt(pageOrder.get(pageOrder.size() / 2));
            }

        }
        return middlePagesSum;
    }
}
