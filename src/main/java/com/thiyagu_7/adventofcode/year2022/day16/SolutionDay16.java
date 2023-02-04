package com.thiyagu_7.adventofcode.year2022.day16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay16 {
    public int part1(List<String> input) {
        InputData inputData = parseInput(input);
        Map<String, Integer> flowRates = inputData.flowRates;
        Map<String, List<String>> adjList = inputData.adjList;
        Map<String, Map<String, Integer>> allDistances = new HashMap<>();
        for (String valve : flowRates.keySet()) {
            allDistances.put(valve, distance(valve, adjList));
        }
        Result result = new Result();
        Set<String> valvesWithNonZeroFlowRate = flowRates.entrySet()
                .stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        recur("AA", flowRates, allDistances,
                valvesWithNonZeroFlowRate,
                30, 0, result);

        return result.pressureReleased;
    }

    private InputData parseInput(List<String> input) {
        Map<String, Integer> flowRates = new HashMap<>();
        Map<String, List<String>> adjList = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split(";");
            String valve = parts[0].substring(6, 8);
            String flowRate = parts[0].substring(23);
            String neighboursAsString = parts[1].contains("valves")
                    ? parts[1].split("valves ")[1]
                    : parts[1].split("valve ")[1];
            flowRates.put(valve, Integer.parseInt(flowRate));
            adjList.put(valve, Arrays.stream(neighboursAsString.split(", "))
                    .collect(Collectors.toList()));
        }
        return new InputData(flowRates, adjList);
    }

    //dijkstra
    private Map<String, Integer> distance(String source, Map<String, List<String>> adjList) {
        Map<String, Integer> dist = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::distance));

        dist.put(source, 0);
        priorityQueue.add(new Node(source, 0));

        while (!priorityQueue.isEmpty()) {
            Node node = priorityQueue.poll();
            for (String neighbour : adjList.get(node.valve)) {
                int cost = node.distance + 1; // moving from current node to its neighbour is 1

                if (!dist.containsKey(neighbour) || dist.get(neighbour) > cost) {
                    dist.put(neighbour, cost);
                    priorityQueue.add(new Node(neighbour, cost));
                }
            }
        }
        return dist;
    }

    private void recur(String currentValve, Map<String, Integer> flowRates,
                       Map<String, Map<String, Integer>> allDistances,
                       Set<String> unvisitedValves, int timeRemaining, int pressureReleased, Result result) {
        unvisitedValves.remove(currentValve);

        // placing check here as we don't have to visit all the nodes
        result.pressureReleased = Math.max(result.pressureReleased, pressureReleased);
        if (unvisitedValves.isEmpty()) {
            return;
        }
        Map<String, Integer> distances = allDistances.get(currentValve);

        for (String destinationValve : unvisitedValves) {
            if (timeRemaining >= distances.get(destinationValve) - 1) {
                int newTimeRemaining = timeRemaining - distances.get(destinationValve) - 1;
                int newValvePressureRelease = pressureReleased
                        + flowRates.get(destinationValve) * newTimeRemaining;
                recur(destinationValve, flowRates, allDistances,
                        new HashSet<>(unvisitedValves), //copy to avoid CME
                        newTimeRemaining, newValvePressureRelease, result
                );
            }
        }
    }

    public int part2(List<String> input) {
        InputData inputData = parseInput(input);
        Map<String, Integer> flowRates = inputData.flowRates;
        Map<String, List<String>> adjList = inputData.adjList;
        Map<String, Map<String, Integer>> allDistances = new HashMap<>();
        for (String valve : flowRates.keySet()) {
            allDistances.put(valve, distance(valve, adjList));
        }
        Set<String> valvesWithNonZeroFlowRate = flowRates.entrySet()
                .stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        List<Set<String>> allValvesWithNonZeroFlowRateCombinations = new ArrayList<>();
        findCombinations(new HashSet<>(), new ArrayList<>(valvesWithNonZeroFlowRate),
                valvesWithNonZeroFlowRate.size() / 2,
                allValvesWithNonZeroFlowRateCombinations);

        int res = 0;
        for (int i = 0; i < allValvesWithNonZeroFlowRateCombinations.size(); i++) {
            Set<String> set1 = allValvesWithNonZeroFlowRateCombinations.get(i);
            Set<String> set2 = valvesWithNonZeroFlowRate.stream()
                    .filter(v -> !set1.contains(v))
                    .collect(Collectors.toSet());

            Result result1 = new Result();
            recur("AA", flowRates, allDistances,
                    set1, 26, 0, result1);

            Result result2 = new Result();
            recur("AA", flowRates, allDistances,
                    set2, 26, 0, result2);
            res = Math.max(res, result1.pressureReleased + result2.pressureReleased);
        }
        return res;
    }

    //nCr
    private static void findCombinations(Set<String> current, List<String> source, int size, List<Set<String>> result) {
        findCombinations(current, source, size, result, 0);
    }


    private static void findCombinations(Set<String> current, List<String> source, int size,
                                         List<Set<String>> result, int i) {
        if (current.size() == size) {
            result.add(current);
            return;
        }
        if (i == source.size()) {
            return;
        }

        findCombinations(current, source, size, result, i + 1);
        Set<String> newCurr = new HashSet<>(current);
        newCurr.add(source.get(i));
        findCombinations(newCurr, source, size, result, i + 1);
    }


    record InputData(Map<String, Integer> flowRates, Map<String, List<String>> adjList) {
    }

    record Node(String valve, int distance) {

    }

    private static class Result {
        int pressureReleased = Integer.MIN_VALUE;
    }
}
