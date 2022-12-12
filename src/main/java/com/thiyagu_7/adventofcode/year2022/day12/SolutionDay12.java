package com.thiyagu_7.adventofcode.year2022.day12;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SolutionDay12 {
    private static final int[] X_COOR = new int[]{0, 0, -1, 1};
    private static final int[] Y_COOR = new int[]{-1, 1, 0, 0};

    public int part1(List<String> input) {
        int m = input.size();
        int n = input.get(0).length();
        char[][] grid = new char[m][n];
        int x = 0, y = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = input.get(i).charAt(j);
                if (grid[i][j] == 'S') {
                    x = i;
                    y = j;
                }
            }
        }
        return bfs(grid, x, y);
    }

    private static class Result {
        private Integer count = Integer.MAX_VALUE;
    }

    private int bfs(char[][] grid, int startX, int startY) {
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];

        Queue<Location> queue = new LinkedList<>();
        queue.add(new Location(startX, startY, 0));
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            Location location = queue.poll();
            int x = location.x;
            int y = location.y;
            if (grid[x][y] == 'E') {
                return location.cost;
            }
            for (int k = 0; k < 4; k++) {
                int newX = x + X_COOR[k];
                int newY = y + Y_COOR[k];
                if (!isValid(grid, newX, newY)) {
                    continue;
                }
                int curr = grid[x][y] == 'S' ? 'a' : grid[x][y];
                int next = grid[newX][newY] == 'E' ? 'z' : grid[newX][newY];
                if (!visited[newX][newY]
                        && next <= curr + 1) { //curr >= next || curr + 1 == next
                    queue.add(new Location(newX, newY, location.cost + 1));
                    visited[newX][newY] = true;
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private record Location(int x, int y, int cost) {

    }

    private boolean isValid(char[][] grid, int x, int y) {
        int m = grid.length;
        int n = grid[0].length;
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    public int part2(List<String> input) {
        int m = input.size();
        int n = input.get(0).length();
        char[][] grid = new char[m][n];

        List<int[]> allAs = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = input.get(i).charAt(j);
                if (grid[i][j] == 'S' || grid[i][j] == 'a') {
                    allAs.add(new int[]{i, j});
                }
            }
        }
        int res = Integer.MAX_VALUE;
        for (int[] idx : allAs) {
            res = Math.min(res, bfs(grid, idx[0], idx[1]));
        }
        return res;
    }
}
