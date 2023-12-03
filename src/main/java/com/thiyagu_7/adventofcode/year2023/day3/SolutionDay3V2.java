package com.thiyagu_7.adventofcode.year2023.day3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Updating approach for part1 to return list of adjacent symbols. This way it will be easier to solve
for part 2.

Part 2:
A gear is any * symbol that is adjacent to exactly two part numbers.
Its gear ratio is the result of multiplying those two numbers together.
 */
public class SolutionDay3V2 {
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
                    List<SymbolPosition> adjacentSymbolPositions = findAllAdjacentSymbols(grid, i, startCol, endCol);
                    if (!adjacentSymbolPositions.isEmpty()) {
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

    private List<SymbolPosition> findAllAdjacentSymbols(char[][] grid, int row, int startCol, int endCol) {
        List<SymbolPosition> adjacentSymbolPositions = new ArrayList<>();
        int n = grid.length;
        int m = grid[0].length;
        // up and down
        for (int j = startCol; j <= endCol; j++) {
            if (isValid(row - 1, j, n, m) && isASymbol(grid, row - 1, j)) {
                adjacentSymbolPositions.add(new SymbolPosition(row - 1, j, grid[row - 1][j]));
            }

            if (isValid(row + 1, j, n, m) && isASymbol(grid, row + 1, j)) {
                adjacentSymbolPositions.add(new SymbolPosition(row + 1, j, grid[row + 1][j]));
                //return true;

            }
        }
        int[] x = new int[]{-1, 0, 1};
        int[] y = new int[]{-1, -1, -1};
        // three adj. locations on the left from [row][startCol]
        for (int i = 0; i < 3; i++) {
            int newX = row + x[i];
            int newY = startCol + y[i];
            if (isValid(newX, newY, n, m) && isASymbol(grid, newX, newY)) {
                adjacentSymbolPositions.add(new SymbolPosition(newX, newY, grid[newX][newY]));
            }
        }
        // three adj. locations on the right from [row][endCol]
        x = new int[]{-1, 0, 1};
        y = new int[]{1, 1, 1};
        for (int i = 0; i < 3; i++) {
            int newX = row + x[i];
            int newY = endCol + y[i];
            if (isValid(newX, newY, n, m) && isASymbol(grid, newX, newY)) {
                adjacentSymbolPositions.add(new SymbolPosition(newX, newY, grid[newX][newY]));
            }
        }
        return adjacentSymbolPositions;
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
    record SymbolPosition(int x, int y, char symbol) {
        boolean isAPossibleGear() {
            return symbol() == '*';
        }
    }

    public int part2(List<String> input) {
        char[][] grid = parseInput(input);
        Map<SymbolPosition, List<Integer>> possibleGearToAdjacentPartNumbers = new HashMap<>();

        //starting same as part 1
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
                    List<SymbolPosition> adjacentSymbolPositions = findAllAdjacentSymbols(grid, i, startCol, endCol);
                    if (!adjacentSymbolPositions.isEmpty()) {
                        int partNumber = possiblePartNumber;
                        // populate reverse map from possibleGear to adj part numbers
                        adjacentSymbolPositions
                                .stream()
                                .filter(SymbolPosition::isAPossibleGear)
                                .forEach(possibleGear -> possibleGearToAdjacentPartNumbers.computeIfAbsent(
                                                possibleGear, _g -> new ArrayList<>())
                                        .add(partNumber));
                    }
                } else {
                    j++;
                }
            }
        }
        //sum of gear ratios
        return possibleGearToAdjacentPartNumbers.values()
                .stream()
                .filter(l -> l.size() == 2)
                .mapToInt(l -> l.get(0) * l.get(1))
                .sum();
    }
}
