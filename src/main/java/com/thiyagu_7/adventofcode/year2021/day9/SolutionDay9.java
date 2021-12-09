package com.thiyagu_7.adventofcode.year2021.day9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SolutionDay9 {
    private final int[] xCoor = new int[]{-1, 1, 0, 0};
    private final int[] yCoor = new int[]{0, 0, -1, 1};

    public int part1(List<String> input) {
        int[][] grid = buildGrid(input);
        AtomicInteger riskLevel = new AtomicInteger();
        findLowPoints(grid, idx -> riskLevel.addAndGet(grid[idx[0]][idx[1]] + 1));
        return riskLevel.get();
    }

    private int[][] buildGrid(List<String> input) {
        return input.stream()
                .map(line -> Arrays.stream(line.split(""))
                        .mapToInt(Integer::valueOf)
                        .toArray())
                .toArray(int[][]::new);
    }

    private void findLowPoints(int[][] grid, Consumer<int[]> onLowPoint) {
        int n = grid.length;
        int m = grid[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int numNeighbour = 0;
                int numLower = 0;
                for (int k = 0; k < 4; k++) {
                    int x = i + xCoor[k];
                    int y = j + yCoor[k];
                    if (isValid(x, y, n, m)) {
                        numNeighbour++;
                        if (grid[x][y] > grid[i][j]) {
                            // low points - the locations that are lower than any of its adjacent locations.
                            numLower++;
                        }
                    }
                }
                if (numNeighbour == numLower) {
                    // Pass the low point's index
                    onLowPoint.accept(new int[] {i, j});
                }
            }
        }
    }

    private boolean isValid(int i, int j, int n, int m) {
        return i >= 0 && i < n && j >= 0 && j < m;
    }


    public int part2(List<String> input) {
        int n = input.size();
        int m = input.get(0).length();
        int[][] grid = buildGrid(input);

        List<int[]> lowPoints = new ArrayList<>();
        findLowPoints(grid, lowPoints::add);

        List<Integer> basinSizes = new ArrayList<>();
        for (int[] lowPoint : lowPoints) {
            BasinSize basinSize = new BasinSize();
            dfs(grid, lowPoint[0], lowPoint[1], n, m, basinSize);
            basinSizes.add(basinSize.count);
        }
        basinSizes.sort(Comparator.reverseOrder());
        return basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2);
    }

    private static class BasinSize {
        int count = 0;
    }

    private void dfs(int[][] grid, int i, int j, int n, int m, BasinSize basinSize) {
        grid[i][j] = -1;
        basinSize.count++;
        for (int k = 0; k < 4; k++) {
            int x = i + xCoor[k];
            int y = j + yCoor[k];
            if (isValid(x, y, n, m) && grid[x][y] != -1 && grid[x][y] != 9
                    && grid[x][y] > grid[i][j]) {
                dfs(grid, x, y, n, m, basinSize);
            }
        }
    }
}
