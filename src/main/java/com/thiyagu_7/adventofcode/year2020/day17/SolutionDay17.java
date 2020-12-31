package com.thiyagu_7.adventofcode.year2020.day17;

import java.util.List;

public class SolutionDay17 {
    public int part1(List<String> input) {
        boolean[][][] grid = new boolean[30][30][30];
        int origin = 15; //zero/origin

        //setup
        int i = origin, j;
        for (String line : input) {
            char[] chars = line.toCharArray();
            j = origin;
            for (char c: chars) {
                grid[i][j++][origin] = c == '#';
            }
            i++;
        }

        for (i = 0; i < 6; i++) {
            grid = applyCycle(grid);
        }
        //return number of active
        int numActive = 0;
        int l = grid.length;
        for (i = 0; i < l; i++) {
            for (j = 0; j < l; j++) {
                for (int k = 0; k < l; k++) {
                    if (grid[i][j][k]) {
                        numActive++;
                    }
                }
            }
                }
        return numActive;
    }

    private boolean[][][] applyCycle(boolean[][][] grid) {
        int l = grid.length;
        boolean[][][] gridNew = new boolean[l][l][l];

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                for (int k = 0; k < l; k++) {
                    int numActive = getNumberOfActiveCubesFromNeighbors(grid, i, j, k);
                    if (grid[i][j][k]) { //active
                        if (numActive == 2 || numActive == 3) {
                            gridNew[i][j][k] = true;
                        }
                    } else {
                        if (numActive == 3) {
                            gridNew[i][j][k] = true;
                        }
                    }
                }
            }
        }
        return gridNew;
    }

    private boolean isValid(int x, int y, int z, int l) {
        return x >= 0 && x < l
                && y >= 0 && y < l
                && z >= 0 && z < l;
    }

    private int getNumberOfActiveCubesFromNeighbors(boolean[][][] grid, int x, int y, int z) {
        int numActive = 0;
        int l = grid.length;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (!(i == 0 && j == 0 && k == 0)
                            && isValid(x + i, y + j, z + k, l)
                            && grid[x + i][y + j][z + k]) {
                        numActive++;
                    }
                }
            }
        }
        return numActive;
    }
}
