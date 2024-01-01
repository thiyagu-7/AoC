package com.thiyagu_7.adventofcode.year2023.day25;

import com.thiyagu_7.adventofcode.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SolutionDay25 {
    public int part1Brute(List<String> input) {
        Map<String, List<String>> adjList = parse(input);
        List<Edge> allEdges = adjList.entrySet().stream()
                .flatMap(e -> e.getValue()
                        .stream()
                        .map(dest -> new Edge(e.getKey(), dest)))
                .toList();

        Set<String> allVertices = allEdges.stream()
                .flatMap(e -> Stream.of(e.source, e.dest))
                .collect(Collectors.toSet());

        for (int i = 0; i < allEdges.size(); i++) {
            for (int j = i + 1; j < allEdges.size(); j++) {
                for (int k = j + 1; k < allEdges.size(); k++) {
                    Edge edge1 = allEdges.get(i);
                    Edge edge2 = allEdges.get(j);
                    Edge edge3 = allEdges.get(k);

                    Optional<Integer> res = removeThreeEdgesAndCheckConnectivity(adjList, allVertices,
                            edge1, edge2, edge3);
                    if (res.isPresent()) {
                        return res.get();
                    }
                }
            }
        }
        throw new RuntimeException();
    }

    private Map<String, List<String>> parse(List<String> input) {
        Map<String, List<String>> adjList = new HashMap<>();

        for (String line : input) {
            String[] parts = line.split(": ");

            adjList.put(parts[0], Arrays.stream(parts[1].split(" "))
                    .toList());
        }
        return adjList;
    }

    private Optional<Integer> removeThreeEdgesAndCheckConnectivity(Map<String, List<String>> adjList,
                                                                   Set<String> allVertices,
                                                                   Edge edge1, Edge edge2, Edge edge3) {
        Map<String, List<String>> adjListCopy = adjList.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue())));
        removeEdge(adjListCopy, edge1);
        removeEdge(adjListCopy, edge2);
        removeEdge(adjListCopy, edge3);

        adjListCopy = populateEdgesInOtherDirection(adjListCopy);

        Set<String> allVerticesCopy = new HashSet<>(allVertices);
        List<Integer> sizes = new ArrayList<>();

        while (!allVerticesCopy.isEmpty()) {
            String vertex = allVerticesCopy.iterator().next();
            Set<String> visited = new HashSet<>();

            dfs(vertex, adjListCopy, visited);

            sizes.add(visited.size());
            allVerticesCopy.removeAll(visited);

            //if we get exactly two components
            if (sizes.size() == 2) {
                return Optional.of(sizes.get(0) * sizes.get(1));
            }
        }
        return Optional.empty();
    }

    private void removeEdge(Map<String, List<String>> adjList, Edge edge) {
        String source = edge.source;
        String dest = edge.dest;
        if (adjList.containsKey(source)) {
            adjList.get(source).remove(dest);
        } else {
            adjList.get(dest).remove(source);
        }
    }

    private Map<String, List<String>> populateEdgesInOtherDirection(Map<String, List<String>> adjList) {
        Map<String, List<String>> adjListCopy = adjList.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue())));

        for (Map.Entry<String, List<String>> entry : adjList.entrySet()) {
            String source = entry.getKey();
            for (String dest : entry.getValue()) {
                adjListCopy.computeIfAbsent(dest, d -> new ArrayList<>())
                        .add(source);
            }
        }
        return adjListCopy;
    }

    private void dfs(String curr,
                     Map<String, List<String>> adjList,
                     Set<String> visited) {
        visited.add(curr);

        for (String adjNode : adjList.getOrDefault(curr, List.of())) {
            if (!visited.contains(adjNode)) {
                dfs(adjNode, adjList, visited);
            }
        }
    }

    public int part1(List<String> input) {
        Set<String> allVertices = new HashSet<>();
        Map<String, Integer> nodeToNum = new HashMap<>();
        Map<Integer, String> numToNode = new HashMap<>();
        int numEdges = 0;

        for (String l : input) {
            String[] parts = l.split(": ");
            String[] parts2 = parts[1].split(" ");
            numEdges += parts2.length;

            allVertices.add(parts[0]);
            allVertices.addAll(Arrays.asList(parts2));
        }
        int numVertices = allVertices.size();

        int n = 0;
        for (String vertex : allVertices) {
            if (!nodeToNum.containsKey(vertex)) {
                nodeToNum.put(vertex, n);
                numToNode.put(n++, vertex);
            }
        }

        Edge edge1;
        Edge edge2;
        Edge edge3;
        while (true) {
            int k = 0;
            MinCut minCut = new MinCut(numVertices, numEdges);
            MinCut.Edge[] edge = new MinCut.Edge[numEdges];
            for (String l : input) {
                String[] parts = l.split(": ");
                String[] parts2 = parts[1].split(" ");
                for (String dest : parts2) {
                    edge[k++] = new MinCut.Edge(
                            nodeToNum.get(parts[0]), nodeToNum.get(dest));
                }
            }
            List<Pair<Integer, Integer>> edgesToBeRemoved = minCut.minCutKarger(edge);

            if (edgesToBeRemoved.size() == 3) {
                edge1 = new Edge(
                        numToNode.get(edgesToBeRemoved.get(0).getKey()),
                        numToNode.get(edgesToBeRemoved.get(0).getValue())
                );
                edge2 = new Edge(
                        numToNode.get(edgesToBeRemoved.get(1).getKey()),
                        numToNode.get(edgesToBeRemoved.get(1).getValue())
                );
                edge3 = new Edge(
                        numToNode.get(edgesToBeRemoved.get(2).getKey()),
                        numToNode.get(edgesToBeRemoved.get(2).getValue())
                );
                break;
            }
        }
        Map<String, List<String>> adjList = parse(input);
        Optional<Integer> res = removeThreeEdgesAndCheckConnectivity(adjList, allVertices, edge1, edge2, edge3);
        return res.get();
    }

    record Edge(String source, String dest) {

    }
}
