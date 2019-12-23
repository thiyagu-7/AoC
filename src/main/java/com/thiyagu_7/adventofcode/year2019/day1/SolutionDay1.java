package com.thiyagu_7.adventofcode.year2019.day1;

import java.util.List;

/**
 * https://adventofcode.com/2019/day/1
 */
public class SolutionDay1 {
    private int[] arr = new int[149010];

    public int part1(List<String> input) {
        return input
                .stream()
                .map(Integer::valueOf)
                .mapToInt(fuel -> fuel / 3 - 2)
                .sum();
    }

    public int part2(List<String> input) {
        return input
                .stream()
                .map(Integer::valueOf)
                .mapToInt(this::getFuelMemoized)//or this::getFuel
                .sum();
    }

    private int getFuel(int num) {
        int sum = 0;
        int fuel = num;
        while (fuel / 3 > 2) {
            fuel = fuel / 3 - 2;
            sum += fuel;
        }
        return sum;
    }

    private int getFuelMemoized(int num) {
        if (arr[num] != 0) {
            return arr[num];
        }
        if (num / 3 > 2) {
            int fuel = num / 3 - 2;
            arr[num] = fuel + getFuelMemoized(fuel);
        }
        return arr[num];
    }
}
