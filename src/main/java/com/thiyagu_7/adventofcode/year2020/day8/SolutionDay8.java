package com.thiyagu_7.adventofcode.year2020.day8;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class SolutionDay8 {
    private int accumulator = 0;

    public int part1(List<String> instructions) {
        Set<Integer> instructionsExecuted = new HashSet<>();

        int index = 0;
        while (true) {
            index = processInstruction(instructions.get(index), index);
            if (instructionsExecuted.contains(index)) {
                return accumulator;
            }
            instructionsExecuted.add(index);
        }
    }

    //returns next instruction to execute
    private int processInstruction(String instruction, int currentInstruction) {
        String[] parts = instruction.split(" ");
        String argument = parts[1];
        int num = Integer.parseInt(argument.substring(1));
        if (argument.charAt(0) == '-') {
            num = -num;
        }
        if (parts[0].equals("acc")) {
            accumulator += num;
        } else if (parts[0].equals("jmp")) {
            return currentInstruction + num;
        }
        return currentInstruction + 1;
    }

    public int part2(List<String> instructions) {
        int instructionToBeModified = -1;
        while (true) {
            instructionToBeModified = getNextNoOpOrJmp(instructions, instructionToBeModified + 1);
            boolean terminated = process(instructions, instructionToBeModified);
            if (terminated) {
                return accumulator;
            } else {
                accumulator = 0;
            }
        }
    }

    private boolean process(List<String> instructions, int indexToFlip) {
        Set<Integer> instructionsExecuted = new HashSet<>();
        int index = 0;

        while (index < instructions.size()) {
            String instruction = index == indexToFlip
                    ? flipInstruction(instructions.get(index))
                    : instructions.get(index);

            index = processInstruction(instruction, index);
            if (instructionsExecuted.contains(index)) {
               return false;
            }
            instructionsExecuted.add(index);
        }
        return true;
    }

    private String flipInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        String argument = parts[1];
        String command = parts[0];
        if (command.equals("acc")) {
            return instruction;
        }
        String flippedCommand = command.equals("nop") ? "jmp" : "nop";
        return flippedCommand + " " + argument;
    }

    private int getNextNoOpOrJmp(List<String> instructions, int start) {
        return IntStream.range(start, instructions.size())
                .filter(i -> instructions.get(i).startsWith("nop") || instructions.get(i).startsWith("jmp"))
                .findFirst()
                .getAsInt();

    }
}
