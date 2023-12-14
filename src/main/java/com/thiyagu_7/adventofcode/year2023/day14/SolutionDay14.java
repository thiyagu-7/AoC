package com.thiyagu_7.adventofcode.year2023.day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SolutionDay14 {
    public int part1(List<String> input) {
        char[][] grid = parseInput(input);
        char[][] newGrid = moveAllRocksNorth(grid);
        return calculateTotalLoad(newGrid);
    }

    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private char[][] moveAllRocksNorth(char[][] grid) {
        char[][] newGrid = new char[grid.length][grid[0].length];
        Map<Integer, Queue<Integer>> columnToFirstEmptyRow = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'O') {
                    if (columnToFirstEmptyRow.containsKey(j)
                            && !columnToFirstEmptyRow.get(j).isEmpty()) {
                        int firstEmptyRowInSameCol = columnToFirstEmptyRow.get(j)
                                .remove();
                        newGrid[firstEmptyRowInSameCol][j] = 'O';

                        newGrid[i][j] = '.'; //make current cell as empty
                        columnToFirstEmptyRow.get(j)
                                .add(i);
                    } else {
                        newGrid[i][j] = 'O';
                    }
                } else if (grid[i][j] == '#') {
                    newGrid[i][j] = '#';
                    columnToFirstEmptyRow.remove(j); //remove all entries against col 'j' as we have a cube-shaped rock
                } else { //grid[i][j] = '.'
                    if (!columnToFirstEmptyRow.containsKey(j)) {
                        columnToFirstEmptyRow.put(j, new LinkedList<>());
                    }
                    columnToFirstEmptyRow.get(j)
                            .add(i);
                    newGrid[i][j] = '.';
                }
            }
        }
        return newGrid;
    }

    private char[][] moveAllRocksWest(char[][] grid) {
        char[][] newGrid = new char[grid.length][grid[0].length];
        Map<Integer, Queue<Integer>> rowToFirstEmptyCol = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'O') {
                    if (rowToFirstEmptyCol.containsKey(i)
                            && !rowToFirstEmptyCol.get(i).isEmpty()) {
                        int firstEmptyColInSameRow = rowToFirstEmptyCol.get(i)
                                .remove();
                        newGrid[i][firstEmptyColInSameRow] = 'O';

                        newGrid[i][j] = '.'; //make current cell as empty
                        rowToFirstEmptyCol.get(i)
                                .add(j);
                    } else {
                        newGrid[i][j] = 'O';
                    }
                } else if (grid[i][j] == '#') {
                    newGrid[i][j] = '#';
                    rowToFirstEmptyCol.remove(i); //remove all entries against row 'i' as we have a cube-shaped rock
                } else { //grid[i][j] = '.'
                    if (!rowToFirstEmptyCol.containsKey(i)) {
                        rowToFirstEmptyCol.put(i, new LinkedList<>());
                    }
                    rowToFirstEmptyCol.get(i)
                            .add(j);
                    newGrid[i][j] = '.';
                }
            }
        }
        return newGrid;
    }

    private char[][] moveAllRocksSouth(char[][] grid) {
        char[][] newGrid = new char[grid.length][grid[0].length];
        Map<Integer, Queue<Integer>> columnToLastEmptyRow = new HashMap<>();

        for (int i = grid.length - 1; i >= 0; i--) { //reverse iterate
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'O') {
                    if (columnToLastEmptyRow.containsKey(j)
                            && !columnToLastEmptyRow.get(j).isEmpty()) {
                        int lastEmptyRowInSameCol = columnToLastEmptyRow.get(j)
                                .remove();
                        newGrid[lastEmptyRowInSameCol][j] = 'O';

                        newGrid[i][j] = '.'; //make current cell as empty
                        columnToLastEmptyRow.get(j)
                                .add(i);
                    } else {
                        newGrid[i][j] = 'O';
                    }
                } else if (grid[i][j] == '#') {
                    newGrid[i][j] = '#';
                    columnToLastEmptyRow.remove(j); //remove all entries against col 'j' as we have a cube-shaped rock
                } else { //grid[i][j] = '.'
                    if (!columnToLastEmptyRow.containsKey(j)) {
                        columnToLastEmptyRow.put(j, new LinkedList<>());
                    }
                    columnToLastEmptyRow.get(j)
                            .add(i);
                    newGrid[i][j] = '.';
                }
            }
        }
        return newGrid;
    }

    private char[][] moveAllRocksEast(char[][] grid) {
        char[][] newGrid = new char[grid.length][grid[0].length];
        Map<Integer, Queue<Integer>> rowToLastEmptyCol = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = grid[0].length - 1; j >= 0; j--) { //reverse iterate
                if (grid[i][j] == 'O') {
                    if (rowToLastEmptyCol.containsKey(i)
                            && !rowToLastEmptyCol.get(i).isEmpty()) {
                        int lastEmptyColInSameRow = rowToLastEmptyCol.get(i)
                                .remove();
                        newGrid[i][lastEmptyColInSameRow] = 'O';

                        newGrid[i][j] = '.'; //make current cell as empty
                        rowToLastEmptyCol.get(i)
                                .add(j);
                    } else {
                        newGrid[i][j] = 'O';
                    }
                } else if (grid[i][j] == '#') {
                    newGrid[i][j] = '#';
                    rowToLastEmptyCol.remove(i); //remove all entries against row 'i' as we have a cube-shaped rock
                } else { //grid[i][j] = '.'
                    if (!rowToLastEmptyCol.containsKey(i)) {
                        rowToLastEmptyCol.put(i, new LinkedList<>());
                    }
                    rowToLastEmptyCol.get(i)
                            .add(j);
                    newGrid[i][j] = '.';
                }
            }
        }
        return newGrid;
    }

    private int calculateTotalLoad(char[][] grid) {
        int load = 0;
        for (int i = 0; i < grid.length; i++) {
            int loadForThisRow = grid.length - i;
            int numOfRoundedRocks = 0;
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'O') {
                    numOfRoundedRocks++;
                }
            }
            load += loadForThisRow * numOfRoundedRocks;
        }
        return load;
    }

    public int part2(List<String> input) {
        char[][] grid = parseInput(input);
        List<ArrayWrapper> wrappers = new ArrayList<>();
        int numCycles = 1_000_000_000;
        for (long k = 0; k < numCycles; k++) {
            grid = moveAllRocksNorth(grid);
            grid = moveAllRocksWest(grid);
            grid = moveAllRocksSouth(grid);
            grid = moveAllRocksEast(grid);

            ArrayWrapper w = new ArrayWrapper(grid);
            if (wrappers.contains(w)) { //it will keep looping from here on.
                long remainingCycles = numCycles - k - 1;
                int idx = wrappers.indexOf(w);
                int loopLength = wrappers.size() - idx;
                int mod = (int) (remainingCycles % loopLength);
                grid = wrappers.get(idx + mod).grid;
                return calculateTotalLoad(grid);
            } else {
                wrappers.add(w);
            }
        }
        return -1; // doesn't reach here
    }

    private record ArrayWrapper(char[][] grid) {
            private ArrayWrapper(char[][] grid) {
                this.grid = new char[grid.length][grid[0].length];
                for (int i = 0; i < grid.length; i++) {
                    this.grid[i] = Arrays.copyOf(grid[i], grid[i].length);
                }
            }

            @Override
            public int hashCode() {
                return Arrays.deepHashCode(grid);
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof ArrayWrapper)) {
                    return false;
                }
                return Arrays.deepEquals(grid, ((ArrayWrapper) obj).grid);
            }
        }
}
