package com.thiyagu_7.adventofcode.year2021.day7;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class SolutionDay7Brute {
    public int part1(List<Integer> input) {
        return findOptimalPosition(input, Function.identity());
    }

    public int part2(List<Integer> input) {
        return findOptimalPosition(input, dist -> (dist * (dist + 1)) / 2);
    }

    /**
     * Private helper method to compute the optimal horizontal position.
     *
     * @param input       The input
     * @param computeCost function which takes the distance and returns the cost.
     * @return optimal position to align on.
     */
    private int findOptimalPosition(List<Integer> input, Function<Integer, Integer> computeCost) {
        int max = input.stream()
                .max(Comparator.naturalOrder())
                .orElse(0);
        int fuel = Integer.MAX_VALUE;
        for (int position = 0; position < max; position++) {
            int current = 0;
            for (int j = 0; j < input.size(); j++) {
                int dist = Math.abs(input.get(j) - position);
                current += computeCost.apply(dist);
                if (current >= fuel) {
                    break;
                }
            }
            if (current < fuel) {
                fuel = current;
            }
        }
        return fuel;
    }
}
