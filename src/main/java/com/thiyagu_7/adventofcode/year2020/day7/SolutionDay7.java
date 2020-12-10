package com.thiyagu_7.adventofcode.year2020.day7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class SolutionDay7 {
    private Map<Node, List<Node>> tree = new HashMap<>();
    private Set<String> result = new HashSet<>();

    //How many bag colors can eventually contain at least one shiny gold bag?
    public int part1(List<String> input) {
        //populate tree
        input.forEach(this::insert);

        Set<String> visitedNodes = new HashSet<>();

        Optional<Node> unvisitedNode = getAnUnvisitedNode(visitedNodes);
        while (unvisitedNode.isPresent()) {
            dfs(unvisitedNode.get(), visitedNodes, new ArrayList<>());
            unvisitedNode = getAnUnvisitedNode(visitedNodes);
        }
        return result.size();
    }

    private void insert(String rule) {
        String trimmedRule = rule.substring(0, rule.length() - 1); //remove last full-stop.
        String[] parts = trimmedRule.split(" contain ");
        Node key = new Node(parts[0].substring(0, parts[0].indexOf(" bag")), 1);
        if (parts[1].startsWith("no other")) {
            return;
        }

        Arrays.stream(parts[1].split(", "))
                .map(this::buildNode)
                .forEach(node -> tree.merge(
                        key, new ArrayList<>(Collections.singletonList(node)),
                        (list, newListWithOneElement) -> {
                            list.addAll(newListWithOneElement);
                            return list;
                        }));
    }

    //1 dark olive bag or 5 faded blue bags
    private Node buildNode(String s) {
        int index = s.indexOf(" ");
        int count = Integer.parseInt(s.substring(0, index));
        String bagName = s.substring(index).trim();
        String name = bagName.substring(0, bagName.indexOf(" bag"));
        return new Node(name, count);
    }


    private Optional<Node> getAnUnvisitedNode(Set<String> visitedNodes) {
        return tree.entrySet()
                .stream()
                .flatMap(entry -> Stream.concat(
                        Stream.of(entry.getKey()), entry.getValue().stream()))
                .distinct()
                .filter(node -> !visitedNodes.contains(node.name))
                .findFirst();
    }

    private void dfs(Node current, Set<String> visitedNodes, List<String> currentPath) {
        if (current.name.equals("shiny gold") && currentPath.size() > 1) {
            result.addAll(currentPath);
        }
        visitedNodes.add(current.name);
        currentPath.add(current.name);

        if (tree.containsKey(current)) {
            for (Node node : tree.get(current)) {
                dfs(node, visitedNodes, currentPath);
            }
        }
        currentPath.remove(current.name);
    }

    //How many individual bags are required inside your single shiny gold bag?
    public int part2(List<String> input) {
        //populate tree
        input.forEach(this::insert);
        System.out.println(tree);

        return dfsPart2(new Node("shiny gold", 1));
    }

    private int dfsPart2(Node current) {
        int totalBags = 0;

        if (tree.containsKey(current)) {
            for (Node node : tree.get(current)) {
               totalBags += node.count;
               totalBags += node.count * dfsPart2(node);
            }
        }
        return totalBags;
    }

    private static class Node {
        private String name;
        private int count;

        private Node(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != this.getClass()) {
                return false;
            }
            Node other = (Node) obj;
            return other.name.equals(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
