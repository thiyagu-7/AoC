package com.thiyagu_7.adventofcode.year2024.day10;

import com.thiyagu_7.adventofcode.util.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SolutionDay10 {
    private static final int[] X_COOR = new int[]{-1, 1, 0, 0};
    private static final int[] Y_COOR = new int[]{0, 0, -1, 1};

    public int part1(List<String> input) {
        char[][] grid = parseMap(input);
        int trailheadScoresSum = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '0') {
                    PathCountResult result = new PathCountResult();
                    dfs(grid, new Position(i, j), result);
                    trailheadScoresSum += result.endPositionLocations.size();
                }
            }
        }
        return trailheadScoresSum;
    }


    private static class PathCountResult {
        Set<Position> endPositionLocations = new HashSet<>(); //location of 9-height positions
        int paths = 0;
    }

    private char[][] parseMap(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    //visited not needed as the next position we move to is current + 1
    private void dfs(char[][] grid, Position currentPosition,
                     PathCountResult result) {
        int current = Integer.parseInt(grid[currentPosition.x()][currentPosition.y()] + "");
        if (current == 9) {
            result.paths++; //for part-2
            result.endPositionLocations.add(currentPosition); //for part-1
            return;
        }
        int m = grid.length;
        int n = grid[0].length;

        for (int k = 0; k < 4; k++) {
            int newX = currentPosition.x() + X_COOR[k];
            int newY = currentPosition.y() + Y_COOR[k];
            Position newPosition = new Position(newX, newY);
            if (isValid(newX, newY, m, n)
                    && Integer.parseInt(grid[newX][newY] + "") == current + 1) {
                dfs(grid, newPosition, result);
            }
        }
    }

    private boolean isValid(int x, int y, int m, int n) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    public int part2(List<String> input) {
        char[][] grid = parseMap(input);
        int trailheadRatingsSum = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '0') {
                    PathCountResult result = new PathCountResult();
                    dfs(grid, new Position(i, j), result);
                    trailheadRatingsSum += result.paths;
                }
            }
        }
        return trailheadRatingsSum;
    }
}
