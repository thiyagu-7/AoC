package com.thiyagu_7.adventofcode.year2021.day11;

import com.thiyagu_7.adventofcode.util.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SolutionDay11 {
    private final int[] xCoor = new int[]{-1, 1, 0, 0, -1, -1, 1, 1};
    private final int[] yCoor = new int[]{0, 0, -1, 1, -1, 1, -1, 1};

    public int part1(List<String> input, int numSteps) {
        int[][] grid = parseInput(input);
        int n = grid.length, m = grid[0].length;
        int totalFlashes = 0;

        for (int step = 0; step < numSteps; step++) {
            Set<Pair<Integer, Integer>> flashesInCurrentStep = new HashSet<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    process(grid, i, j, n, m, flashesInCurrentStep);
                }
            }
            totalFlashes += flashesInCurrentStep.size();
        }
        return totalFlashes;
    }

    private int[][] parseInput(List<String> input) {
        return input.stream()
                .map(line -> Arrays.stream(line.split(""))
                        .mapToInt(Integer::valueOf)
                        .toArray())
                .toArray(int[][]::new);
    }

    private void process(int[][] grid, int i, int j, int n, int m, Set<Pair<Integer, Integer>> flashesInCurrentStep) {
        Pair<Integer, Integer> location = new Pair<>(i, j);
        // if this location was already flashed in this step, shouldn't increment it when we visit it freshly from part1/part2 method
        // i.e., shouldn't change from 0 -> 1
        if (flashesInCurrentStep.contains(location)) {
            return;
        }
        grid[i][j]++;
        if (grid[i][j] == 10) {
            flashesInCurrentStep.add(location);
            for (int k = 0; k < 8; k++) {
                int x = i + xCoor[k];
                int y = j + yCoor[k];
                location = new Pair<>(x, y);
                if (isValid(x, y, n, m) && !flashesInCurrentStep.contains(location)) {
                    process(grid, x, y, n, m, flashesInCurrentStep);
                }
            }
            grid[i][j] = 0;
        }
    }

    private boolean isValid(int i, int j, int n, int m) {
        return i >= 0 && i < n && j >= 0 && j < m;
    }

    public int part2(List<String> input) {
        int[][] grid = parseInput(input);
        int n = grid.length, m = grid[0].length;

        int step = 0;
        while (true) {
            Set<Pair<Integer, Integer>> flashesInCurrentStep = new HashSet<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    process(grid, i, j, n, m, flashesInCurrentStep);
                }
            }
            // all flashes in the current step
            if (flashesInCurrentStep.size() == n * m) {
                return step + 1;
            }
            step++;
        }
    }
}

