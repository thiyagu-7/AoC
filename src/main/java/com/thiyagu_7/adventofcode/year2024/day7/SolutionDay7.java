package com.thiyagu_7.adventofcode.year2024.day7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolutionDay7 {
    public long part1(List<String> input) {
        List<Equation> equations = parseInput(input);
        return equations.stream()
                .filter(equation -> couldBeValid(equation, equation.numbers.get(0), 1))
                .mapToLong(equation -> equation.result)
                .sum();
    }

    private List<Equation> parseInput(List<String> input) {
        List<Equation> equations = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(": ");
            equations.add(new Equation(Long.parseLong(parts[0]),
                    Arrays.stream(parts[1].split(" "))
                            .map(Integer::parseInt)
                            .toList()));
        }
        return equations;
    }

    private boolean couldBeValid(Equation equation, long runningResult, int idx) {
        if (idx == equation.numbers.size()) {
            return runningResult == equation.result;
        }
        long num = equation.numbers.get(idx);
        return couldBeValid(equation, runningResult + num, idx + 1)
                || couldBeValid(equation, runningResult * num, idx + 1);
    }

    private boolean couldBeValidPart2(Equation equation, long runningResult, int idx) {
        if (idx == equation.numbers.size()) {
            return runningResult == equation.result;
        }

        long num = equation.numbers.get(idx);
        long concatenated = Long.parseLong(runningResult + "" + num);
        return couldBeValidPart2(equation, runningResult + num, idx + 1)
                || couldBeValidPart2(equation, runningResult * num, idx + 1)
                || couldBeValidPart2(equation, concatenated, idx + 1);
    }

    record Equation(long result, List<Integer> numbers) {

    }

    public long part2(List<String> input) {
        List<Equation> equations = parseInput(input);
        return equations.stream()
                .filter(equation -> couldBeValidPart2(equation, equation.numbers.get(0), 1))
                .mapToLong(equation -> equation.result)
                .sum();
    }

}

