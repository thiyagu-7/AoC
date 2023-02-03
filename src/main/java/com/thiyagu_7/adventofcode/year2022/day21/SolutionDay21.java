package com.thiyagu_7.adventofcode.year2022.day21;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionDay21 {
    public long part1(List<String> input) {
        ParsedInput parsedInput = buildInput(input);
        return recur("root", parsedInput.expressionMonkeys, parsedInput.numberMonkeys);
    }

    public ParsedInput buildInput(List<String> input) {
        Map<String, Expression> expressionMonkeys = new HashMap<>();
        Map<String, Number> numberMonkeys = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split(":");
            String monkey = parts[0];
            String rhs = parts[1].trim();
            if (rhs.matches("\\d+")) {
                numberMonkeys.put(monkey, new Number(Integer.parseInt(rhs)));
            } else {
                String[] expressionParts = rhs.split(" ");
                expressionMonkeys.put(monkey,
                        new Expression(expressionParts[0], expressionParts[2], expressionParts[1].charAt(0)));
            }
        }
        return new ParsedInput(expressionMonkeys, numberMonkeys);
    }

    private long recur(String currentMonkey,
                       Map<String, Expression> expressionMonkeys,
                       Map<String, Number> numberMonkeys){
        if (numberMonkeys.containsKey(currentMonkey)) {
            return numberMonkeys.get(currentMonkey).number;
        }
        Expression expression = expressionMonkeys.get(currentMonkey);
        long v1 = recur(expression.monkey1, expressionMonkeys, numberMonkeys);
        long v2 = recur(expression.monkey2, expressionMonkeys, numberMonkeys);

        return switch (expression.operation) {
            case '+' -> v1 + v2;
            case '-' -> v1 - v2;
            case '*' -> v1 * v2;
            case '/' -> v1 / v2;
            default -> throw new IllegalStateException("Unexpected value: " + expression.operation);
        };
    }

    private record ParsedInput(Map<String, Expression> expressionMonkeys,
                               Map<String, Number> numberMonkeys) {
    }

    private record Expression(String monkey1, String monkey2, char operation) {
    }

    private record Number(int number) {
    }
}
