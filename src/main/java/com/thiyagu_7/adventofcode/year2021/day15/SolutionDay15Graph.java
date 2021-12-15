package com.thiyagu_7.adventofcode.year2021.day15;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SolutionDay15Graph {
    private final int[] xCoor = new int[]{-1, 1, 0, 0};
    private final int[] yCoor = new int[]{0, 0, -1, 1};

    public int part1(List<String> input) {
        int[][] grid = buildGrid(input);
        return dijkstra(grid);
    }

    private int[][] buildGrid(List<String> input) {
        return input.stream()
                .map(line -> Arrays.stream(line.split(""))
                        .mapToInt(Integer::valueOf)
                        .toArray())
                .toArray(int[][]::new);
    }

    private int dijkstra(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int[][] dist = new int[n][m];
        for (int[] arr : dist) {
            Arrays.fill(arr, Integer.MAX_VALUE);
        }
        dist[0][0] = 0;

        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Vertex::getCost));
        priorityQueue.add(new Vertex(0, 0, 0));

        while (!priorityQueue.isEmpty()) {
            Vertex vertex = priorityQueue.poll();
            for (int k = 0; k < 4; k++) {
                int i = vertex.i;
                int j = vertex.j;
                int x = i + xCoor[k];
                int y = j + yCoor[k];
                if (isValid(x, y, n, m)) {
                    int newCost = dist[i][j] + grid[x][y];
                    if (newCost < dist[x][y]) {
                        dist[x][y] = newCost;
                        priorityQueue.add(new Vertex(x, y, newCost));
                    }
                }
            }
        }
        return dist[n - 1][m - 1];
    }

    private boolean isValid(int i, int j, int n, int m) {
        return i >= 0 && i < n && j >= 0 && j < m;
    }

    public int part2(List<String> input) {
        int[][] grid = buildGrid(input);
        int[][] fullGrid = buildFullGrid(grid, grid.length, grid[0].length);
        return dijkstra(fullGrid);
    }

    private int[][] buildFullGrid(int[][] initialGrid, int n, int m) {
        int[][] fullGrid = new int[n * 5][m * 5];
        // first block
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                fullGrid[i][j] = initialGrid[i][j];
            }
        }

        // left to right
        for (int l = 1; l < 5; l++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    fullGrid[i][m * l + j] = fullGrid[i][(m * l + j) - m] + 1 == 10
                            ? 1 : fullGrid[i][(m * l + j) - m] + 1;
                }
            }
        }
        // top to bottom
        for (int k = 1; k < 5; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    fullGrid[n * k + i][j] = fullGrid[(n * k + i) - n][j] + 1 == 10
                            ? 1 : fullGrid[(n * k + i) - n][j] + 1;
                }
            }
        }

        for (int k = 1; k < 5; k++) {
            for (int l = 1; l < 5; l++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        // take from up
                        fullGrid[n * k + i][m * l + j] = fullGrid[(n * k + i) - n][m * l + j] + 1 == 10
                                ? 1 : fullGrid[(n * k + i) - n][m * l + j] + 1;
                    }
                }
            }
        }
        return fullGrid;
    }

    private static class Vertex {
        private final int i;
        private final int j;
        private final int cost;

        private Vertex(int i, int j, int cost) {
            this.i = i;
            this.j = j;
            this.cost = cost;
        }

        public int getCost() {
            return cost;
        }
    }
}
