package com.thiyagu_7.adventofcode.year2024.day3;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolutionDay3 {
    public int part1(List<String> input) {
        return input.stream()
                .flatMap(line -> parseMemory(line).stream())
                .reduce(0, (res, mi) -> res + mi.a * mi.b,
                        Integer::sum);
    }

    private List<MultiplyInstruction> parseMemory(String line) {
        Pattern pattern = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)");
        Matcher matcher = pattern.matcher(line);
        List<MultiplyInstruction> instructions = new ArrayList<>();
        while (matcher.find()) {
            String instruction = matcher.group(0);
            int idx = instruction.indexOf("(");
            String numbers = instruction.substring(idx + 1, instruction.length() - 1);
            String[] parts = numbers.split(",");
            instructions.add(new MultiplyInstruction(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]))
            );
        }
        return instructions;
    }

    public int part2(List<String> input) {
        enabled = true;
        return input.stream()
                .flatMap(line -> parseMemoryPart2(line).stream())
                .reduce(0, (res, mi) -> res + mi.a * mi.b,
                        Integer::sum);
    }

    static boolean enabled = true;

    private List<MultiplyInstruction> parseMemoryPart2(String line) {
        Pattern pattern = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)|don't\\(\\)|do\\(\\)");
        Matcher matcher = pattern.matcher(line);
        List<MultiplyInstruction> instructions = new ArrayList<>();

        while (matcher.find()) {
            String instruction = matcher.group(0);
            if (instruction.equals("don't()")) {
                enabled = false;
            } else if (instruction.equals("do()")) {
                enabled = true;
            } else if (enabled) {
                int idx = instruction.indexOf("(");
                String numbers = instruction.substring(idx + 1, instruction.length() - 1);
                String[] parts = numbers.split(",");
                instructions.add(new MultiplyInstruction(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]))
                );
            }
        }
        return instructions;
    }

    record MultiplyInstruction(Integer a, Integer b) {

    }
}
