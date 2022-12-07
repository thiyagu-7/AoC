package com.thiyagu_7.adventofcode.year2022.day7;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SolutionDay7 {
    public int part1(List<String> input) {
        Node root = parseInput(input);
        computeSizeOfDirs(root);

        List<Integer> directorySizes = new ArrayList<>();
        getDirectorySizes(root, directorySizes);
        return directorySizes.stream()
                .mapToInt(i -> i)
                .filter(size -> size <= 100000)
                .sum();
    }

    private Node parseInput(List<String> input) {
        Node root = new Node("/");
        buildTree(root, new CommandIterator(input.subList(1, input.size()))); //remove first entry
        return root;
    }

    private void buildTree(Node node, CommandIterator commandIterator) {
        while (commandIterator.hasNext()) {
            String command = commandIterator.next(); //should start with $
            String operation = command.substring(2, 4);

            if (operation.equals("cd")) {
                String directory = command.substring(5);
                if (directory.equals("..")) {
                    return;
                } else {
                    Node child = node.getChildNode(directory);
                    buildTree(child, commandIterator);
                }
            } else {
                processListCommand(node, commandIterator);
            }
        }
    }

    private void processListCommand(Node node, CommandIterator commandIterator) {
        while (commandIterator.hasNext() && commandIterator.peek().charAt(0) != '$') {
            String content = commandIterator.next();
            String[] parts = content.split(" ");
            Node child;
            if (content.startsWith("dir")) {
                child = new Node(parts[1]);
            } else {
                child = new Node(parts[1], Integer.parseInt(parts[0]));
            }
            node.addChildNode(child);
        }
    }

    private int computeSizeOfDirs(Node node) {
        if (node == null) {
            return 0;
        }
        if (node.children.isEmpty()) {
            return node.size;
        }
        int childrenSize = node.children.stream()
                .mapToInt(this::computeSizeOfDirs)
                .sum();
        node.size = childrenSize;
        return childrenSize;
    }

    private void getDirectorySizes(Node node, List<Integer> directorySizes) {
        if (node == null || node.children.isEmpty()) {
            return;
        }
        directorySizes.add(node.size);
        node.children
                .forEach(child -> getDirectorySizes(child, directorySizes));
    }

    public int part2(List<String> input) {
        Node root = parseInput(input);
        computeSizeOfDirs(root);

        int unusedSpace = 70000000 - root.size;
        int neededSpace = 30000000 - unusedSpace;

        Map<Integer, String> sizes = new TreeMap<>(); //sorts by lowest to highest
        populateSizeOfDirs(root, sizes);

        return sizes.entrySet()
                .stream()
                .filter(entry -> entry.getKey() >= neededSpace)
                .findFirst()
                .map(Map.Entry::getKey)
                .get();
    }

    private void populateSizeOfDirs(Node node, Map<Integer, String> sizes) {
        if (node == null || node.children.isEmpty()) {
            return;
        }
        sizes.put(node.size, node.name); //If same size, let it be overwritten

        node.children
                .forEach(child -> populateSizeOfDirs(child, sizes));
    }
    
    private static class Node {
        private final String name;
        private int size;
        private final List<Node> children;

        private Node(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        private Node(String name, int size) {
            this.name = name;
            this.size = size;
            this.children = new ArrayList<>();
        }

        public void addChildNode(Node child) {
            this.children.add(child);
        }

        public Node getChildNode(String name) {
            return this.children.stream()
                    .filter(node -> node.name.equals(name))
                    .findFirst()
                    .get();
        }
    }

    private static class CommandIterator implements Iterator<String> {
        private final List<String> instructions;
        private int i = 0;

        private CommandIterator(List<String> instructions) {
            this.instructions = instructions;
        }

        @Override
        public boolean hasNext() {
            return i < instructions.size();
        }

        @Override
        public String next() {
            return instructions.get(i++);
        }

        public String peek() {
            return hasNext() ? instructions.get(i) : null;
        }
    }
}
