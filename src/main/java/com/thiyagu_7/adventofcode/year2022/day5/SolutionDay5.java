package com.thiyagu_7.adventofcode.year2022.day5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionDay5 {
    public String part1(List<String> input) {
        return process(input, UnaryOperator.identity());
    }

    public String part2(List<String> input) {
        return process(input, list -> {
            Collections.reverse(list); //Re-orders in the original list
            return list;
        });
    }

    /**
     * Processes the input as per problem requirement.
     * @param input the input.
     * @param transformerFunction used to get the elements ordered in a certain way before moving to the destination stack.
     * @return result.
     */
    private String process(List<String> input, UnaryOperator<List<String>> transformerFunction) {
        int emptyLineIndex = input.indexOf("");
        List<Stack<String>> stacks = parseStackConfiguration(input.subList(0, emptyLineIndex));
        List<Instruction> instructions = parseInstructions(input.subList(emptyLineIndex + 1, input.size()));

        for (Instruction instruction : instructions) {
            int from = instruction.fromStack;
            int to = instruction.toStack;
            int numberOfCreatesToMove = instruction.numberOfCreatesToMove;
            List<String> stackElements = new ArrayList<>();
            for (int i = 0; i < numberOfCreatesToMove; i++) {
                String element = stacks.get(from)
                        .pop();
                stackElements.add(element);
            }
            List<String> elements = transformerFunction.apply(stackElements);
            elements
                    .forEach(e -> stacks.get(to).push(e));
        }
        return stacks.stream()
                .map(Stack::peek)
                .map(s -> s.substring(1, s.length() - 1))
                .collect(Collectors.joining());
    }
    private List<Stack<String>> parseStackConfiguration(List<String> stackConfigInput) {
        int len = stackConfigInput.size();
        String lastLine = stackConfigInput.get(len - 1);
        int numOfStacks = lastLine.trim()
                .split("\\s+").length;
        /*
        3 char followed by a space. Split into groups of 3.
        Examples:
                 [C] [N] [B] [W] [D]     [D] [M]
             [G] [L] [M] [S] [S] [C]     [T] [V]
             [P] [B] [B] [P] [Q] [S] [L] [H] [B]
         */
        List<Stack<String>> stacks = new ArrayList<>();
        IntStream.range(0, numOfStacks)
                .forEach(i -> stacks.add(new Stack<>()));

        for (int i = stackConfigInput.size() - 2; i >= 0; i--) {
            String line = stackConfigInput.get(i);
            String[] parts = line.split("(?<=\\G... )");

            for (int j = 0; j < parts.length; j++) {
                String crate = parts[j];
                if (!crate.trim().isEmpty()) {
                    stacks.get(j).push(crate.trim());
                }
            }

        }
        return stacks;
    }

    private List<Instruction> parseInstructions(List<String> instructionsInput) {
        List<Instruction> instructions = new ArrayList<>();
        for (String line : instructionsInput) {
            String[] parts = line.replace("move ", "")
                    .replace("from ", "")
                    .replace("to ", "").split(" ");
            Instruction instruction = new Instruction(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]) - 1); //convert to 0-based
            instructions.add(instruction);
        }
        return instructions;
    }

    private record Instruction(int numberOfCreatesToMove, int fromStack, int toStack) {
    }
}
