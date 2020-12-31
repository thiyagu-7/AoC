package com.thiyagu_7.adventofcode.year2020.day24;

import java.util.List;
import java.util.stream.Stream;

public class SolutionDay24 {
    public int part1(List<String> input) {
        boolean[][] grid = new boolean[200][200];
        input.forEach(l -> move(l, grid, 100, 100));

        int numBlack = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j]) {
                    numBlack++;
                }
            }
        }
        return numBlack;
    }

    private void move(String instruction, boolean[][] grid, int x, int y) {
        for (int i = 0; i < instruction.length(); i++) {
            if (instruction.charAt(i) == 'e') {
                y++;
            } else if (instruction.charAt(i) == 'w') {
                y--;
            } else if (instruction.charAt(i) == 's') {
                if (instruction.charAt(i + 1) == 'w') {
                    if (x % 2 == 0) {
                        y--;
                    }
                } else {
                    if (x % 2 == 1) {
                        y++;
                    }
                }
                x++;
                i++;
            } else if (instruction.charAt(i) == 'n') {
                if (instruction.charAt(i + 1) == 'w') {
                    if (x % 2 == 0) {
                        y--;
                    }
                } else {
                    if (x % 2 == 1) {
                        y++;
                    }
                }
                x--;
                i++;
            }
        }
        grid[x][y] = !grid[x][y];
    }

    public int part2(List<String> input) {
        boolean[][] grid = new boolean[200][200];
        boolean[][] finalGrid = grid;
        input.forEach(l -> move(l, finalGrid, 100, 100));

        for (int i = 0; i < 100; i++) {
            grid = flipByRules(grid);
        }

        int numBlack = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j]) {
                    numBlack++;
                }
            }
        }
        return numBlack;
    }

    private boolean[][] flipByRules(boolean[][] grid) {
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        for (int i = 1; i < grid.length - 1; i++) {
            for (int j = 1; j < grid[0].length - 1; j++) {
                int neighbourBlack = getNumNeighbouringBlackTiles(grid, i, j);
                if (grid[i][j]) {//black
                    newGrid[i][j] = neighbourBlack != 0 && neighbourBlack <= 2; //neighbourBlack == 0 || neighbourBlack > 2 ? false : true;
                } else {
                    newGrid[i][j] = neighbourBlack == 2;
                }
            }
        }
        return newGrid;
    }

    private int getNumNeighbouringBlackTiles(boolean[][] grid, int x, int y) {
        if (x % 2 == 0) {
            return (int) Stream.of(
                    grid[x][y + 1],
                    grid[x][y - 1],
                    grid[x - 1][y - 1],
                    grid[x - 1][y],
                    grid[x + 1][y - 1],
                    grid[x + 1][y])
                    .filter(a -> a) //true
                    .count();
        } else {
            return (int) Stream.of(
                    grid[x][y + 1],
                    grid[x][y - 1],
                    grid[x - 1][y],
                    grid[x - 1][y + 1],
                    grid[x + 1][y],
                    grid[x + 1][y + 1])
                    .filter(a -> a) //true
                    .count();
        }
    }
}
