package com.thiyagu_7.adventofcode.year2020.day18;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class SolutionDay18 {
    public long part1(List<String> input) {
        return input.stream()
                .map(expr -> expr.replaceAll(" ", ""))
                .mapToLong(this::evaluate)
                .sum();
    }

    private long evaluate(String expression) {
        Stack<Character> operators = new Stack<>();
        Stack<Long> operands = new Stack<>();

        for (char c : expression.toCharArray()) {
            if (isOperator(c) || c == '(') {
                operators.push(c);
            } else if (operators.isEmpty()) {
                operands.push(Long.parseLong(c + ""));
            } else if (c == ')') {
                char topOperator = operators.peek();
                if (topOperator != '(') {
                    while (topOperator != '(') {
                        if (!operators.isEmpty() && operands.size() >= 2) {
                            operands.push(evaluate(operators.pop(),
                                    operands.pop(),
                                    operands.pop()));
                        }
                        topOperator = operators.peek();
                    }
                    operators.pop();
                } else {
                    operators.pop();
                    while (!operators.isEmpty() && operators.peek() != '(' && operands.size() >= 2) {
                        operands.push(evaluate(operators.pop(),
                                operands.pop(),
                                operands.pop()));
                    }
                }
            } else if (operators.peek() != '(') { //c is operand and top operator is not '('
                operands.push(evaluate(operators.pop(), operands.pop(), Integer.parseInt(c + "")));
            } else {
                operands.push(Long.parseLong(c + ""));
            }
        }
        long result = operands.pop();
        while (!operands.isEmpty()) {
            result = evaluate(operators.pop(),
                    result,
                    operands.pop());
        }
        return result;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '*';
    }

    private long evaluate(char o, long val1, long val2) {
        return o == '+' ? val1 + val2 : val1 * val2;
    }

    public long part2(List<String> input) {
        return input.stream()
                .map(expr -> expr.replaceAll(" ", ""))
                .mapToLong(this::evaluatePart2Simplified)
                .sum();
    }

    private long evaluatePart2Simplified(String expression) {
        Stack<String> stack = new Stack<>();

        List<String> sections = new ArrayList<>();
        List<String> paran;

        for (char c : expression.toCharArray()) {
            if (c == ')') {
                char topOperand = stack.peek().charAt(0);
                paran = new ArrayList<>();
                if (topOperand != '(') {
                    while (topOperand != '(') {
                        paran.add(stack.pop());
                        topOperand = stack.peek().charAt(0);
                    }
                    stack.pop();
                    stack.add(String.valueOf(solve(paran)));
                }
            } else {
                stack.add(c + "");
            }
        }
       while (!stack.isEmpty()) {
           sections.add(stack.pop());
       }
       return solve(sections);

    }

    private long solve(List<String> sections) {
        StringBuilder builder = new StringBuilder("");
        for (int i = sections.size() - 1; i >= 0; i--) {
            builder.append(sections.get(i));
        }
        String[] p = builder.toString().split("\\*");
        List<Long> termsToMul = new ArrayList<>();

        for (String s : p) {
            if (s.contains("+")) {
                termsToMul.add(Arrays.stream(s.split("\\+"))
                        .mapToLong(Long::parseLong)
                        .reduce(0L, Long::sum));
            } else {
                termsToMul.add(Long.parseLong(s));
            }
        }

        return termsToMul.stream()
                .reduce(1L, (a, b) -> a * b);
    }
}
