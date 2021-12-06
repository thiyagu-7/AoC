package com.thiyagu_7.adventofcode.year2021.day6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolutionDay6 {
    public int part1BruteForce(List<Integer> input) {
        List<Integer> ages = new ArrayList<>(input);
        int day = 0;
        while (day < 80) {
            int newFishes = 0;
            for (int i = 0; i < ages.size(); i++) {
                int timer = ages.get(i);
                if (timer == 0) {
                    ages.set(i, 6);
                    newFishes++;
                } else {
                    ages.set(i, timer - 1);
                }
            }
            for (int i = 0; i < newFishes; i++) {
                ages.add(8);
            }
            day++;
        }
        return ages.size();
    }

    public long part1(List<Integer> input) {
        return simulate(input, 80);
    }

    public long part2(List<Integer> input) {
        return simulate(input, 256);
    }

    public long simulate(List<Integer> input, int numDays) {
        //index - 0 to 8 (possible age/timer)
        long[] ageToCount = new long[9];
        input.forEach(age -> ageToCount[age]++);

        int day = 0;
        while (day < numDays) {
            // returns number of fishes that has 0 as age - add them to age six
            long numZeroAge = simulateDay(ageToCount, 0);
            ageToCount[6] = ageToCount[6] + numZeroAge;
            day++;
        }
        return Arrays.stream(ageToCount)
                .sum();
    }

    private long simulateDay(long[] ageToCount, int age) {
        long count = ageToCount[age];
        if (age == 8) {
            ageToCount[age] = ageToCount[0];
            return count;
        }
        ageToCount[age] = simulateDay(ageToCount, age + 1);
        return count;
    }
}
