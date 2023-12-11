package com.thiyagu_7.adventofcode.year2023.day8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SolutionDay8 {
    public int part1(List<String> input) {
        String instructions = input.get(0);
        InstructionsIterator instructionsIterator = new InstructionsIterator(instructions);
        Map<String, LeftRightNode> map = parseInput(input);
        String currentNode = "AAA";
        int moves = 0;

        while (!currentNode.equals("ZZZ")) {
            LeftRightNode leftRightNode = map.get(currentNode);
            char instruction = instructionsIterator.next();
            currentNode = instruction == 'L' ? leftRightNode.left : leftRightNode.right;
            moves++;
        }
        return moves;
    }

    private Map<String, LeftRightNode> parseInput(List<String> input) {
        Map<String, LeftRightNode> map = new HashMap<>();
        for (int i = 2; i < input.size(); i++) {
            String line = input.get(i);
            String[] parts = line.split(" = ");
            String leftRightNodesInfo = parts[1].substring(1, parts[1].length() - 1);
            String[] leftRightNodes = leftRightNodesInfo.split(", ");
            map.put(parts[0], new LeftRightNode(leftRightNodes[0], leftRightNodes[1]));
        }
        return map;
    }

    private static class InstructionsIterator implements Iterator<Character> {
        private final String instruction;
        private int index = 0;

        private InstructionsIterator(String instruction) {
            this.instruction = instruction;
        }

        @Override
        public boolean hasNext() {
            //infinite iterator
            return true;
        }

        @Override
        public Character next() {
            char c = instruction.charAt(index++);
            if (index == instruction.length()) {
                index = 0;
            }
            return c;
        }
    }

    // Start at every node that ends with A and follow all of the paths at the same time
    // until they all simultaneously end up at nodes that end with Z.
    public long part2Brute(List<String> input) {
        String instructions = input.get(0);
        InstructionsIterator instructionsIterator = new InstructionsIterator(instructions);
        Map<String, LeftRightNode> map = parseInput(input);
        List<String> currentNodes = map.keySet().stream()
                .filter(node -> node.endsWith("A"))
                .toList();
        long moves = 0;

        while (!allNodesEndWithZ(currentNodes)) {
            List<String> newCurrentNodes = new ArrayList<>();
            char instruction = instructionsIterator.next();

            for (String node : currentNodes) {
                LeftRightNode leftRightNode = map.get(node);
                newCurrentNodes.add(
                        instruction == 'L' ? leftRightNode.left : leftRightNode.right
                );
            }
            currentNodes = newCurrentNodes;
            moves++;
        }
        return moves;
    }

    public long part2(List<String> input) {
        String instructions = input.get(0);
        Map<String, LeftRightNode> map = parseInput(input);
        List<String> currentNodes = map.keySet().stream()
                .filter(node -> node.endsWith("A"))
                .toList();
        int moves;

        List<Integer> diffs = new ArrayList<>();
        for (String node : currentNodes) {
            moves = 0;
            InstructionsIterator instructionsIterator = new InstructionsIterator(instructions);
            int firstZ = -1;
            int secondZ = -1;
            //find two Z nodes and find the index difference
            while (firstZ == -1 || secondZ == -1) {
                LeftRightNode leftRightNode = map.get(node);
                char instruction = instructionsIterator.next();
                node = instruction == 'L' ? leftRightNode.left : leftRightNode.right;
                moves++;
                if (node.endsWith("Z")) {
                    if (firstZ == -1) {
                        firstZ = moves;
                    } else {
                        secondZ = moves;
                    }
                }
            }
            diffs.add(secondZ - firstZ);
        }
        //find LCM
        return LCMHelper.findLCM(diffs);
    }

    private boolean allNodesEndWithZ(List<String> nodes) {
        return nodes.stream()
                .allMatch(node -> node.endsWith("Z"));
    }

    record LeftRightNode(String left, String right) {

    }
}
