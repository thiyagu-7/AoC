package com.thiyagu_7.adventofcode.year2024.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionDay17 {
    public String part1(List<String> input) {
        ParsedInput parsedInput = parseInput(input);

        List<Integer> output = execute(parsedInput.program, parsedInput.registers);

        return output.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private ParsedInput parseInput(List<String> input) {
        Registers registers = new Registers(
                Long.parseLong(input.get(0).split(": ")[1]),
                Long.parseLong(input.get(1).split(": ")[1]),
                Long.parseLong(input.get(2).split(": ")[1])
        );
        List<Integer> program = Arrays.stream(input.get(4).split(": ")[1]
                        .split(","))
                .map(Integer::parseInt)
                .toList();
        return new ParsedInput(registers, program);
    }

    private List<Integer> execute(List<Integer> program, Registers registers) {
        int instructionPointer = 0;
        List<Integer> output = new ArrayList<>();

        while (instructionPointer < program.size()) {
            int opcode = program.get(instructionPointer);
            int operand = program.get(instructionPointer + 1);

            instructionPointer = executeInstruction(instructionPointer, opcode, operand, registers, output);
        }
        return output;
    }

    private int executeInstruction(int instructionPointer, int opcode, int operand, Registers registers, List<Integer> output) {
        if (opcode == 0) {
            registers.a = (long) (registers.a / Math.pow(2, getComboOperand(operand, registers)));
        } else if (opcode == 1) {
            registers.b = registers.b ^ operand;
        } else if (opcode == 2) {
            registers.b = getComboOperand(operand, registers) % 8;
        } else if (opcode == 3) {
            if (registers.a != 0) {
                return operand;
            }
        } else if (opcode == 4) {
            registers.b = registers.b ^ registers.c;
        } else if (opcode == 5) {
            output.add((int) (getComboOperand(operand, registers) % 8));
        } else if (opcode == 6) {
            registers.b = (long) (registers.a / Math.pow(2, getComboOperand(operand, registers)));
        } else {
            registers.c = (long) (registers.a / Math.pow(2, getComboOperand(operand, registers)));
        }
        return instructionPointer + 2;
    }

    private long getComboOperand(int operand, Registers registers) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> registers.a;
            case 5 -> registers.b;
            case 6 -> registers.c;
            default -> throw new IllegalStateException("Unexpected value: " + operand);
        };
    }

    private static class Registers {
        long a;
        long b;
        long c;

        public Registers(long a, long b, long c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    record ParsedInput(Registers registers, List<Integer> program) {

    }

    public long part2(List<String> input) {
        ParsedInput parsedInput = parseInput(input);
        List<Integer> program = parsedInput.program;

        return recur(program, 0, program.size() - 1);
    }

    private long recur(List<Integer> program, long a, int idx) {
        for (long aVal = a; aVal < a + 8; aVal++) {
            Registers registers = new Registers(aVal, 0, 0);

            List<Integer> output = execute(program, registers);

            if (program.get(idx).equals(output.get(0))) {
                if (idx == 0) {
                    return aVal;
                }
                long result = recur(program, aVal * 8, idx - 1);
                if (result != -1) {
                    return result;
                }
            }
        }
        return -1;
    }
}
