package com.thiyagu_7.adventofcode.year2021.day1;

import java.util.List;
import java.util.stream.IntStream;

public class SolutionDay1 {
    public int part1(List<Integer> inputs) {
        return (int) IntStream.range(1, inputs.size())
                .filter(i -> inputs.get(i) > inputs.get(i - 1))
                .count();
    }

    public int part2(List<Integer> inputs) {
        int previousSum = IntStream.rangeClosed(0, 2)
                .map(inputs::get)
                .sum();
        int res = 0;
        for (int i = 3; i < inputs.size(); i++) {
            int newSum = previousSum + inputs.get(i) - inputs.get(i - 3);
            if (newSum > previousSum) {
                res++;
            }
            previousSum = newSum;
        }
        return res;
    }
}
