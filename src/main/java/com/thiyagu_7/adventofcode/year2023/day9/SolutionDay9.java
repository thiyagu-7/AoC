package com.thiyagu_7.adventofcode.year2023.day9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;

public class SolutionDay9 {
    public int part1(List<String> input) {
        return process(input, (diff, extrapolatedValue) -> diff[diff.length - 1] + extrapolatedValue);
    }

    public long part1For21(List<Long> input) {
        return process2(input, (diff, extrapolatedValue) -> diff.get(diff.size() - 1) + extrapolatedValue);
    }

    public int part2(List<String> input) {
        return process(input, (diff, extrapolatedValue) -> diff[0] - extrapolatedValue);
    }
    private int process(List<String> input, BiFunction<int[], Integer, Integer> extrapolatedValueFunc) {
        List<int[]> list = parseInput(input);
        int sumOfExtrapolatedValues = 0;
        for (int[] history : list) {
            int[] diff;
            int[] current = history;
            Stack<int[]> diffsStack = new Stack<>();
            diffsStack.push(current);
            do {
                diff = new int[current.length - 1];
                for (int i = 0; i < current.length - 1; i++) {
                    diff[i] = current[i + 1] - current[i];
                }
                diffsStack.push(diff);
                current = diff;
            } while (!areAllElementsZero(current));

            int extrapolatedValue = 0;
            diffsStack.pop(); //pop off the last row with all 0's
            while (!diffsStack.isEmpty()) {
                diff = diffsStack.pop();
                extrapolatedValue = extrapolatedValueFunc.apply(diff, extrapolatedValue);//diff[0] - extrapolatedValue;
            }
            sumOfExtrapolatedValues += extrapolatedValue;
        }
        return sumOfExtrapolatedValues;
    }

    private long process2(List<Long> input, BiFunction<List<Long>, Long, Long> extrapolatedValueFunc) {
        List<Long> diff;
        List<Long> current = input;
        Stack<List<Long>> diffsStack = new Stack<>();
        diffsStack.push(current);
        do {
            diff = new ArrayList<>();
            for (int i = 0; i < current.size() - 1; i++) {
                diff.add(current.get(i + 1) - current.get(i));
            }
            diffsStack.push(diff);
            current = diff;
        } while (!areAllElementsZero2(current));

        long extrapolatedValue = 0;
        diffsStack.pop(); //pop off the last row with all 0's
        while (!diffsStack.isEmpty()) {
            diff = diffsStack.pop();
            extrapolatedValue = extrapolatedValueFunc.apply(diff, extrapolatedValue);//diff[0] - extrapolatedValue;
        }

        return extrapolatedValue;
    }

    private boolean areAllElementsZero(int[] arr) {
        return Arrays.stream(arr)
                .allMatch(e -> e == 0);
    }

    private boolean areAllElementsZero2(List<Long> arr) {
        return arr.stream()
                .allMatch(e -> e == 0);
    }

    private List<int[]> parseInput(List<String> input) {
        return input.stream()
                .map(line -> line.split(" "))
                .map(strArr -> Arrays.stream(strArr)
                        .mapToInt(Integer::parseInt)
                        .toArray())
                .toList();
    }
}
