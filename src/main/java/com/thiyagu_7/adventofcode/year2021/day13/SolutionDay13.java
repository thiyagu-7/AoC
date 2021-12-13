package com.thiyagu_7.adventofcode.year2021.day13;

import java.util.ArrayList;
import java.util.List;

public class SolutionDay13 {
    public int part1(List<String> input) {
        FoldResult foldResult = fold(input, false);
        char[][] grid = foldResult.grid;
        int foldedN = foldResult.foldedN;
        int foldedM = foldResult.foldedM;

        int result = 0;
        for (int i = 0; i < foldedN; i++) {
            for (int j = 0; j < foldedM; j++) {
                if (grid[i][j] == '#') {
                    result++;
                }
            }
        }
        return result;
    }

    private InputData parseInput(List<String> input) {
        List<int[]> coordinates = new ArrayList<>();
        List<FoldInstruction> foldInstructions = new ArrayList<>();
        int i;
        int n = Integer.MIN_VALUE, m = Integer.MIN_VALUE;
        for (i = 0; i < input.size(); i++) {
            if (input.get(i).isEmpty()) {
                break;
            }
            String[] parts = input.get(i).split(",");

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            //reverse
            n = Math.max(n, y);
            m = Math.max(m, x);
            coordinates.add(new int[]{x, y}); //not reversing here
        }
        i++;
        for (; i < input.size(); i++) {
            String[] parts = input.get(i).split("fold along ");
            String[] coordinateAndPosition = parts[1].split("=");
            foldInstructions.add(new FoldInstruction(coordinateAndPosition[0],
                    Integer.parseInt(coordinateAndPosition[1])));
        }
        return new InputData(coordinates, foldInstructions, n, m);
    }

    private void populate(char[][] grid, List<int[]> coordinates) {
        // x, y reversed
        coordinates.forEach(pair -> grid[pair[1]][pair[0]] = '#');
    }

    private FoldResult fold(List<String> input, boolean foldAll) {
        InputData inputData = parseInput(input);
        int n = inputData.n + 1;
        int m = inputData.m + 1;
        char[][] grid = new char[n][m];
        populate(grid, inputData.coordinates);

        int foldedN = n, foldedM = m;
        for (FoldInstruction foldInstruction : inputData.foldInstructions) {
            int i1, i2, j, j1, j2, i;

            if (foldInstruction.coordinate.equals("y")) {
                i1 = foldInstruction.position - 1;
                i2 = foldInstruction.position + 1;
                foldedN = foldInstruction.position;
                while (i1 >= 0 && i2 < n) {
                    for (j = 0; j < foldedM; j++) {
                        grid[i1][j] = grid[i1][j] == '#' ? '#' : grid[i2][j];
                    }
                    i1--;
                    i2++;
                }
            } else {
                j1 = foldInstruction.position - 1;
                j2 = foldInstruction.position + 1;
                foldedM = foldInstruction.position;
                while (j1 >= 0 && j2 < m) {
                    for (i = 0; i < foldedN; i++) {
                        grid[i][j1] = grid[i][j1] == '#' ? '#' : grid[i][j2];
                    }
                    j1--;
                    j2++;
                }
            }
            if (!foldAll) {
                // fold only one time
                break;
            }
        }
        return new FoldResult(grid, foldedN, foldedM);
    }

    public char[][] part2(List<String> input) {
        FoldResult foldResult = fold(input, true);
        char[][] grid = foldResult.grid;
        int foldedN = foldResult.foldedN;
        int foldedM = foldResult.foldedM;

        char[][] result = new char[foldedN][foldedM];
        for (int i = 0; i < foldedN; i++) {
            for (int j = 0; j < foldedM; j++) {
                System.out.print(grid[i][j] == '#' ? '#' : '.');
                result[i][j] = grid[i][j] == '#' ? '#' : '.';
            }
            System.out.println();
        }
        System.out.println();
        return result;
    }

    private record InputData(List<int[]> coordinates,
                             List<FoldInstruction> foldInstructions,
                             int n, int m) {

    }

    private record FoldInstruction(String coordinate, int position) {

    }

    private record FoldResult(char[][] grid, int foldedN, int foldedM){

    }

}
