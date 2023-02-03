package com.thiyagu_7.adventofcode.year2022.day21;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionDay21Part2 {
    public long part2(List<String> input) {
        ParsedInput parsedInput = buildInput(input);
        Map<String, Expression> expressionMonkeys = parsedInput.expressionMonkeys;
        Map<String, Number> numberMonkeys = parsedInput.numberMonkeys;

        String leftMonkey = expressionMonkeys.get("root").monkey1;
        String rightMonkey = expressionMonkeys.get("root").monkey2;
        Long evalResult, expectedResult = null;
        String humnSide = null;

        evalResult = evaluateNodes(leftMonkey, expressionMonkeys, numberMonkeys);
        if (evalResult == null) {
            humnSide = leftMonkey;
        } else {
            expectedResult = evalResult;
        }
        evalResult = evaluateNodes(rightMonkey, expressionMonkeys, numberMonkeys);
        if (evalResult == null) {
            humnSide = rightMonkey;
        } else {
            expectedResult = evalResult;
        }
        return solve(humnSide, expressionMonkeys, numberMonkeys, expectedResult);
    }

    private Long solve(String currentMonkey,
                       Map<String, Expression> expressionMonkeys,
                       Map<String, Number> numberMonkeys,
                       Long expected) {
        Expression expression = expressionMonkeys.get(currentMonkey);
        String lhsMonkey = expression.monkey1;
        String rhsMonkey = expression.monkey2;
        char operation = expression.operation;

        if (lhsMonkey.equals("humn")) {
            long other = expressionMonkeys.get(rhsMonkey) != null
                    ? expressionMonkeys.get(rhsMonkey).result //computed result
                    : numberMonkeys.get(rhsMonkey).number();
            return switch (operation) {
                case '+' -> expected - other;
                case '-' -> expected + other;
                case '*' -> expected / other;
                case '/' -> expected * other;
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            };
        } else if (rhsMonkey.equals("humn")) {
            long other = expressionMonkeys.get(lhsMonkey) != null
                    ? expressionMonkeys.get(lhsMonkey).result
                    : numberMonkeys.get(lhsMonkey).number();
            return switch (operation) {
                case '+' -> expected - other;
                case '-' -> other - expected;
                case '*' -> expected / other;
                case '/' -> other / expected;
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            };
        }

        Long lhsResult = null, rhsResult = null;
        if (expressionMonkeys.containsKey(lhsMonkey)
                && expressionMonkeys.get(lhsMonkey).result != null) { //computed result
            lhsResult = expressionMonkeys.get(lhsMonkey).result;
        } else if (numberMonkeys.containsKey(lhsMonkey)) {
            lhsResult = (long) numberMonkeys.get(lhsMonkey).number;
        }

        if (expressionMonkeys.containsKey(rhsMonkey)
                && expressionMonkeys.get(rhsMonkey).result != null) {
            rhsResult = expressionMonkeys.get(rhsMonkey).result;
        } else if (numberMonkeys.containsKey(rhsMonkey)) {
            rhsResult = (long) numberMonkeys.get(rhsMonkey).number;
        }

        if (rhsResult != null) {
            expected = switch (operation) {
                case '+' -> expected - rhsResult;
                case '-' -> expected + rhsResult;
                case '*' -> expected / rhsResult;
                case '/' -> expected * rhsResult;
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            };
            return solve(lhsMonkey, expressionMonkeys, numberMonkeys, expected);
        } else {
            expected = switch (operation) {
                case '+' -> expected - lhsResult;
                case '-' -> lhsResult - expected;
                case '*' -> expected / lhsResult;
                case '/' -> lhsResult / expected;
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            };
            return solve(rhsMonkey, expressionMonkeys, numberMonkeys, expected);
        }
    }

    private Long evaluateNodes(String currentMonkey,
                               Map<String, Expression> expressionMonkeys,
                               Map<String, Number> numberMonkeys) {
        if (currentMonkey.equals("humn")) {
            return null;
        }
        if (numberMonkeys.containsKey(currentMonkey)) {
            return (long) numberMonkeys.get(currentMonkey).number();
        }
        Expression expression = expressionMonkeys.get(currentMonkey);
        Long v1 = evaluateNodes(expression.monkey1, expressionMonkeys, numberMonkeys);
        Long v2 = evaluateNodes(expression.monkey2, expressionMonkeys, numberMonkeys);

        if (v1 == null || v2 == null) {
            return null;
        }
        long res = switch (expression.operation) {
            case '+' -> v1 + v2;
            case '-' -> v1 - v2;
            case '*' -> v1 * v2;
            case '/' -> v1 / v2;
            default -> throw new IllegalStateException("Unexpected value: " + expression.operation);
        };
        expression.result = res;
        return res;
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


    private record ParsedInput(Map<String, Expression> expressionMonkeys,
                               Map<String, Number> numberMonkeys) {
    }

    private static final class Expression {
        private final String monkey1;
        private final String monkey2;
        private final char operation;

        private Long result; //computed result at this node

        private Expression(String monkey1, String monkey2, char operation) {
            this.monkey1 = monkey1;
            this.monkey2 = monkey2;
            this.operation = operation;
        }

    }

    private record Number(int number) {
    }
}
