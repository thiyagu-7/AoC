package com.thiyagu_7.adventofcode.year2024.day23;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay23 {
    public int part1(List<String> input) {
        Map<String, Set<String>> adjList = buildAdjList(input);

        Set<Triplet> triplets = new HashSet<>();
        int result = 0;

        for (Map.Entry<String, Set<String>> entry : adjList.entrySet()) {
            String node1 = entry.getKey();
            List<String> connectedNodes = new ArrayList<>(entry.getValue());
            for (int i = 0; i < connectedNodes.size(); i++) {
                String node2 = connectedNodes.get(i);
                for (int j = i + 1; j < connectedNodes.size(); j++) {
                    String node3 = connectedNodes.get(j);
                    Set<String> node3Dep = adjList.get(node3);

                    if (node3Dep.contains(node2) && node3Dep.contains(node1)) {
                        List<String> triplet = new ArrayList<>(List.of(node1, node2, node3));
                        triplet.sort(Comparator.naturalOrder());
                        if (triplets.add(new Triplet(triplet))) {
                            if (node1.startsWith("t") || node2.startsWith("t") || node3.startsWith("t")) {
                                result++;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    record Triplet(List<String> nodes) {

    }

    private Map<String, Set<String>> buildAdjList(List<String> input) {
        Map<String, Set<String>> adjList = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split("-");
            adjList.computeIfAbsent(parts[0], ignoreMe -> new HashSet<>())
                    .add(parts[1]);
            adjList.computeIfAbsent(parts[1], ignoreMe -> new HashSet<>())
                    .add(parts[0]);
        }
        return adjList;
    }

    public String part2(List<String> input) {
        Map<String, Set<String>> adjList = buildAdjList(input);

        for (Map.Entry<String, Set<String>> entry : adjList.entrySet()) {
            List<String> nodes = new ArrayList<>();
            nodes.add(entry.getKey());
            nodes.addAll(entry.getValue());
            // For input: Each node is connected to 14 other nodes.
            // The result should have 13 nodes. Try removing one-by-one and check for connectedness
            for (int i = 1; i < nodes.size(); i++) {
                List<String> nodesCopy = new ArrayList<>(nodes);
                nodesCopy.remove(i);
                if (checkIfAllConnected(nodesCopy, adjList)) {
                    nodesCopy.sort(Comparator.naturalOrder());
                    return nodesCopy.stream()
                            .collect(Collectors.joining(","));
                }
            }
        }
        throw new RuntimeException("Can't reach here");
    }

    private boolean checkIfAllConnected(List<String> nodes, Map<String, Set<String>> adjList) {
        for (int i = 0; i < nodes.size(); i++) {
            String node1 = nodes.get(i);
            Set<String> toCheckAgainst = adjList.get(node1);
            for (int j = i + 1; j < nodes.size(); j++) {
                String node2 = nodes.get(j);
                if (node1.equals(node2)) {
                    continue;
                }
                if (!toCheckAgainst.contains(node2)) {
                    return false;
                }
            }
        }
        return true;
    }
}
