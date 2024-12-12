package com.thiyagu_7.adventofcode.year2024.day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SolutionDay12 {
    private static final int[] X_COOR = new int[]{-1, 1, 0, 0};
    private static final int[] Y_COOR = new int[]{0, 0, -1, 1};

    public int part1(List<String> input) {
        char[][] grid = parseInput(input);
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];

        int price = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!visited[i][j]) {
                    Result result = new Result();
                    dfs(grid, i, j, visited, result);
                    price += result.area * result.perimeter;
                }
            }
        }
        return price;
    }

    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private void dfs(char[][] grid, int x, int y, boolean[][] visited, Result result) {
        int m = grid.length;
        int n = grid[0].length;

        visited[x][y] = true;
        result.area++;

        for (int k = 0; k < 4; k++) {
            int newX = x + X_COOR[k];
            int newY = y + Y_COOR[k];

            if (isValid(newX, newY, m, n) && grid[newX][newY] == grid[x][y]) {
                if (!visited[newX][newY]) {
                    dfs(grid, newX, newY, visited, result);
                }
            } else {
                result.perimeter++;
            }
        }
    }

    private boolean isValid(int x, int y, int m, int n) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    private static class Result {
        int area;
        int perimeter;
    }

    private static class ResultPart2 {
        int area;
        Map<Integer, Set<Integer>> map1 = new HashMap<>();
        Map<Integer, Set<Integer>> map2 = new HashMap<>();
        Map<Integer, Set<Integer>> map3 = new HashMap<>();
        Map<Integer, Set<Integer>> map4 = new HashMap<>();
    }

    public int part2(List<String> input) {
        char[][] grid = parseInput(input);
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];

        int price = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!visited[i][j]) {
                    ResultPart2 result = new ResultPart2();
                    dfsPart2(grid, i, j, visited, result);

                    int sides = computeSides(result.map1) + computeSides(result.map2)
                            + computeSides(result.map3) + computeSides(result.map4);
                    price += sides * result.area;
                }
            }
        }
        return price;
    }

    private void dfsPart2(char[][] grid, int x, int y, boolean[][] visited, ResultPart2 result) {
        int m = grid.length;
        int n = grid[0].length;

        visited[x][y] = true;
        result.area++;

        for (int k = 0; k < 4; k++) {
            int newX = x + X_COOR[k];
            int newY = y + Y_COOR[k];

            if (isValid(newX, newY, m, n) && grid[newX][newY] == grid[x][y]) {
                if (!visited[newX][newY]) {
                    dfsPart2(grid, newX, newY, visited, result);
                }
            } else {
                if (x == newX) {
                    if (newY > y) {
                        result.map1.computeIfAbsent(newY, ig -> new TreeSet<>())
                                .add(newX);
                    } else {
                        result.map2.computeIfAbsent(newY, ig -> new TreeSet<>())
                                .add(newX);
                    }
                } else {
                    if (newX > x) {
                        result.map3.computeIfAbsent(newX, ig -> new TreeSet<>())
                                .add(newY);
                    } else {
                        result.map4.computeIfAbsent(newX, ig -> new TreeSet<>())
                                .add(newY);
                    }
                }
            }
        }
    }

    //group consecutive values
    private int computeSides(Map<Integer, Set<Integer>> map) {
        int side = 0;
        for (Map.Entry<Integer, Set<Integer>> entry : map.entrySet()) {
            List<Integer> vals = new ArrayList<>(entry.getValue());
            side++;
            for (int k = 1; k < vals.size(); k++) {
                if (!vals.get(k).equals(vals.get(k - 1) + 1)) {
                    side++;
                }
            }
        }
        return side;
    }
}
