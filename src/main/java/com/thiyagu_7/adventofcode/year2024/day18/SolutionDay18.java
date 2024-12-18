package com.thiyagu_7.adventofcode.year2024.day18;

import com.thiyagu_7.adventofcode.util.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SolutionDay18 {
    private static final int[] X_COOR = new int[]{-1, 1, 0, 0};
    private static final int[] Y_COOR = new int[]{0, 0, -1, 1};

    public int part1(List<String> input, int m, int n, int numBytes) {
        char[][] grid = new char[m][n];
        int[][] stepCost = new int[m][n];
        populateGridAndSteps(input.subList(0, numBytes), m, n, grid, stepCost);
        stepCost[0][0] = 0;

        dfs(grid, new Position(0, 0), 0, stepCost, new HashSet<>());

        return stepCost[m - 1][n - 1];
    }

    private void populateGridAndSteps(List<String> input, int m, int n, char[][] grid, int[][] steps) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (input.contains(j + "," + i)) { //x, y reversed
                    grid[i][j] = '#';
                } else {
                    grid[i][j] = '.';
                }
                steps[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    //Same as day 16#part1
    private void dfs(char[][] grid, Position current, int currentStepCount,
                     int[][] stepCost, Set<Position> visited) {
        stepCost[current.x()][current.y()] = currentStepCount;

        int m = grid.length;
        int n = grid[0].length;

        if (current.x() == m - 1 && current.y() == n - 1) {
            return;
        }
        visited.add(current);

        for (int k = 0; k < 4; k++) {
            int newX = current.x() + X_COOR[k];
            int newY = current.y() + Y_COOR[k];
            Position newPosition = new Position(newX, newY);
            if (isValid(newPosition, m, n)
                    && grid[newX][newY] != '#'
                    && !visited.contains(newPosition)
                    && currentStepCount + 1 < stepCost[newX][newY]) {
                dfs(grid, newPosition, currentStepCount + 1, stepCost, visited);
            }
        }
        visited.remove(current);
    }


    private boolean isValid(Position position, int m, int n) {
        return position.x() >= 0 && position.x() < m && position.y() >= 0 && position.y() < n;
    }

    //process top down adding one byte at a time till path becomes blocked
    //uses DFS to check if *any* path exists
    public String part2(List<String> input, int m, int n, int numBytes) {
        char[][] grid = new char[m][n];
        int[][] steps = new int[m][n];
        populateGridAndSteps(input.subList(0, numBytes), m, n, grid, steps);
        steps[0][0] = 0;

        for (int i = numBytes; i < input.size(); i++) {
            String line = input.get(i);
            int y = Integer.parseInt(line.split(",")[0]);
            int x = Integer.parseInt(line.split(",")[1]);
            grid[x][y] = '#';

            initializeSteps(m, n, steps);
            steps[0][0] = 0;

            if (!checkPathExists(grid, new Position(0, 0), new HashSet<>())) {
                return input.get(i);
            }
        }
        throw new RuntimeException("Can't reach here");
    }

    private void initializeSteps(int m, int n, int[][] steps) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                steps[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    //visited is 'global visited'
    private boolean checkPathExists(char[][] grid, Position current, Set<Position> visited) {
        int m = grid.length;
        int n = grid[0].length;

        if (current.x() == m - 1 && current.y() == n - 1) {
            return true;
        }

        visited.add(current);

        for (int k = 0; k < 4; k++) {
            int newX = current.x() + X_COOR[k];
            int newY = current.y() + Y_COOR[k];
            Position newPosition = new Position(newX, newY);
            if (isValid(newPosition, m, n)
                    && grid[newX][newY] != '#'
                    && !visited.contains(newPosition)) {
                boolean r = checkPathExists(grid, newPosition, visited);
                if (r) {
                    return true;
                }
            }
        }
        return false;
    }
}
