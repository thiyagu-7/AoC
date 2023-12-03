package com.thiyagu_7.adventofcode.year2023.day3;

import java.util.List;

public class SolutionDay3 {
    public int part1(List<String> input) {
        char[][] grid = parseInput(input);
        int sumOfAllPartNumbers = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; ) {
                if (Character.isDigit(grid[i][j])) {
                    int possiblePartNumber = grid[i][j] - '0';
                    int startCol = j++;
                    while (j < grid[0].length && Character.isDigit(grid[i][j])) {
                        possiblePartNumber = (possiblePartNumber * 10) + (grid[i][j] - '0');
                        j++;
                    }
                    int endCol = j - 1;
                    if (isThereASymbolAdjacent(grid, i, startCol, endCol)) {
                        // possiblePartNumber is a part number because it has at least one adjacent symbol
                        sumOfAllPartNumbers += possiblePartNumber;
                    }
                } else {
                    j++;
                }
            }
        }
        return sumOfAllPartNumbers;
    }

    private boolean isThereASymbolAdjacent(char[][] grid, int row, int startCol, int endCol) {
        int n = grid.length;
        int m = grid[0].length;
        // up and down
        for (int j = startCol; j <= endCol; j++) {
            if (isValid(row - 1, j, n, m) && isASymbol(grid, row - 1, j)) {
                return true;
            }
            if (isValid(row + 1, j, n, m) && isASymbol(grid, row + 1, j)) {
                return true;
            }
        }

        int[] x = new int[]{-1, 0, 1};
        int[] y = new int[]{-1, -1, -1};
        // three adj. locations on the left from [row][startCol]
        for (int i = 0; i < 3; i++) {
            int newX = row + x[i];
            int newY = startCol + y[i];
            if (isValid(newX, newY, n, m) && isASymbol(grid, newX, newY)) {
                return true;
            }
        }
        // three adj. locations on the right from [row][endCol]
        x = new int[]{-1, 0, 1};
        y = new int[]{1, 1, 1};
        for (int i = 0; i < 3; i++) {
            int newX = row + x[i];
            int newY = endCol + y[i];
            if (isValid(newX, newY, n, m) && isASymbol(grid, newX, newY)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValid(int i, int j, int n, int m) {
        return i >= 0 && i < n & j >= 0 && j < m;
    }

    // '.' is not a symbol
    private boolean isASymbol(char[][] grid, int i, int j) {
        return grid[i][j] != '.' && !Character.isLetterOrDigit(grid[i][j]);
    }

    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }
}
