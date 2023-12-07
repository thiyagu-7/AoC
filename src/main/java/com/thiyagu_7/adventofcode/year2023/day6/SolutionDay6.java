package com.thiyagu_7.adventofcode.year2023.day6;

import java.util.Map;

public class SolutionDay6 {
    public long part1(Map<Integer, Integer> timeAndDistance) {
        return timeAndDistance.entrySet()
                .stream()
                .mapToLong(e -> numWaysToWin2(e.getKey(), e.getValue()))
                .reduce(1L, (a, b) -> a * b);
    }

    private int numWaysToWin(long time, long distance) {
        int win = 0;
        for (long i = 1; i < time; i++) {
            // going at 'i' mm/ms
            if (i * (time - i) > distance) {
                win++;
            }
        }
        return win;
    }

    //bit optimized
    private int numWaysToWin2(long time, long distance) {
        int win = 0;
        for (long i = 1; i <= time / 2; i++) {
            // going at 'i' mm/ms
            if (i * (time - i) > distance) {
                win++;
            }
        }
        win = win * 2;
        if (time % 2 == 0) {
            win--;
        }
        return win;
    }

    public long part2(long time, long distance) {
        return numWaysToWin2(time, distance);
    }
}
