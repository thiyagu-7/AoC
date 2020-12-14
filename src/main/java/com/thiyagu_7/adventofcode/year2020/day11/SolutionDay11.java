package com.thiyagu_7.adventofcode.year2020.day11;

import java.util.List;

public class SolutionDay11 {
    private static int[] xCoor = {-1, 1, 0, 0, -1, -1, 1, 1};
    private static int[] yCoor = {0, 0, -1, 1, -1, 1, -1, 1};

    public int part1(List<String> input) {
        char[][] grid = input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        char [][] modifiedGrid = new char[grid.length][grid[0].length];
        while (true) {
            int numChanges = 0;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == 'L') {
                        modifiedGrid[i][j] = noAdjacentOccupiedSeats(grid, i, j) ? '#' : 'L';
                    } else if (grid[i][j] == '#') {
                        modifiedGrid[i][j] = numAdjacentOccupiedSeats(grid, i, j) >= 4 ? 'L' : '#';
                    } else {
                        modifiedGrid[i][j] = '.';
                    }
                    numChanges += (grid[i][j] == modifiedGrid[i][j]) ? 0 : 1;
                }
            }
            if (numChanges == 0) {
                return countNumOccupiedSeats(modifiedGrid);
            }

            grid = modifiedGrid;
            modifiedGrid = new char[grid.length][grid[0].length];
        }
    }

    private boolean noAdjacentOccupiedSeats(char[][] grid, int x, int y) {
        for (int k = 0; k < 8; k++) {
            int newX = x + xCoor[k];
            int newY = y + yCoor[k];

            if (newX >= 0 && newX < grid.length && newY >=0 && newY < grid[0].length) {
                if (grid[newX][newY] == '#') {
                    return false;
                }
            }
        }
        return true;
    }

    private int numAdjacentOccupiedSeats(char[][] grid, int x, int y) {
        int numAdjacentOccupiedSeats = 0;
        for (int k = 0; k < 8; k++) {
            int newX = x + xCoor[k];
            int newY = y + yCoor[k];

            if (newX >= 0 && newX < grid.length && newY >=0 && newY < grid[0].length) {
                if (grid[newX][newY] == '#') {
                    numAdjacentOccupiedSeats++;
                }
            }
        }
        return numAdjacentOccupiedSeats;
    }

    private int countNumOccupiedSeats(char[][] grid) {
        int c = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '#') {
                    c++;
                }
            }
        }
        return c;
    }

    public int part2(List<String> input) {
        char[][] grid = input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        char [][] modifiedGrid = new char[grid.length][grid[0].length];
        while (true) {
            int numChanges = 0;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == 'L') {
                        modifiedGrid[i][j] = noAdjacentOccupiedSeats2(grid, i, j) ? '#' : 'L';
                    } else if (grid[i][j] == '#') {
                        modifiedGrid[i][j] = numAdjacentOccupiedSeats2(grid, i, j) >= 5 ? 'L' : '#';
                    } else {
                        modifiedGrid[i][j] = '.';
                    }
                    numChanges += (grid[i][j] == modifiedGrid[i][j]) ? 0 : 1;
                }
            }
            if (numChanges == 0) {
                return countNumOccupiedSeats(modifiedGrid);
            }

            grid = modifiedGrid;
            modifiedGrid = new char[grid.length][grid[0].length];
        }
    }

    private boolean noAdjacentOccupiedSeats2(char[][] grid, int x, int y) {
        for (int k = 0; k < 8; k++) {
            int mult = 1;
            while (true) {
                int newX = x + mult * xCoor[k];
                int newY = y + mult * yCoor[k];
                if (newX >= 0 && newX < grid.length && newY >=0 && newY < grid[0].length) {
                    if (grid[newX][newY] == '#') {
                        return false;
                    }
                    if (grid[newX][newY] == 'L') {
                        break;
                    }
                } else {
                    break;
                }
                mult++;
            }
        }
        return true;
    }

    private int numAdjacentOccupiedSeats2(char[][] grid, int x, int y) {
        int numAdjacentOccupiedSeats = 0;
        for (int k = 0; k < 8; k++) {
            int mult = 1;
            while (true) {
                int newX = x + mult * xCoor[k];
                int newY = y + mult * yCoor[k];
                if (newX >= 0 && newX < grid.length && newY >=0 && newY < grid[0].length) {
                    if (grid[newX][newY] == '#') {
                        numAdjacentOccupiedSeats++;
                        break;
                    }
                    if (grid[newX][newY] == 'L') {
                        break;
                    }
                } else {
                    break;
                }
                mult++;
            }
        }
        return numAdjacentOccupiedSeats;
    }
}
