package com.thiyagu_7.adventofcode.year2020.day17;

import java.util.List;

public class SolutionDay17Part2 {
    public int part2(List<String> input) {
        boolean[][][][] grid = new boolean[30][30][30][30];
        int origin = 15; //zero/origin

        //setup
        int i = origin, j;
        for (String line : input) {
            char[] chars = line.toCharArray();
            j = origin;
            for (char c: chars) {
                grid[i][j++][origin][origin] = c == '#';
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
                    for (int m = 0; m < l; m++) {
                        if (grid[i][j][k][m]) {
                            numActive++;
                        }
                    }
                }
            }
        }
        return numActive;
    }

    private boolean[][][][] applyCycle(boolean[][][][] grid) {
        int l = grid.length;
        boolean[][][][] gridNew = new boolean[l][l][l][l];

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                for (int k = 0; k < l; k++) {
                    for (int m = 0; m < l; m++) {
                        int numActive = getNumberOfActiveCubesFromNeighbors(grid, i, j, m, k);
                        if (grid[i][j][k][m]) { //active
                            if (numActive == 2 || numActive == 3) {
                                gridNew[i][j][k][m] = true;
                            }
                        } else {
                            if (numActive == 3) {
                                gridNew[i][j][k][m] = true;
                            }
                        }
                    }
                }
            }
        }
        return gridNew;
    }

    private boolean isValid(int x, int y, int z, int w, int l) {
        return x >= 0 && x < l
                && y >= 0 && y < l
                && z >= 0 && z < l
                && w >= 0 && w < l;
    }

    private int getNumberOfActiveCubesFromNeighbors(boolean[][][][] grid, int x, int y, int z, int w) {
        int numActive = 0;
        int l = grid.length;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    for (int m = -1; m <= 1; m++) {
                        if (!(i == 0 && j == 0 && k == 0 && m == 0)
                                && isValid(x + i, y + j, z + k, w + m, l)
                                && grid[x + i][y + j][z + k][w + m]) {
                            numActive++;
                        }
                    }
                }
            }
        }
        return numActive;
    }
}
