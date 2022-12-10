package com.thiyagu_7.adventofcode.year2022.day8;

public class SolutionDay8 {
    public int part1(int[][] grid) {
        int row = grid.length;
        int col = grid[0].length;
        int visible = 2 * row + ((row - 2) * 2);

        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                if (isVisible(grid, i, j)) {
                    visible++;
                }
            }
        }
        return visible;
    }

    private boolean isVisible(int[][] grid, int x, int y) {
        int currHeight = grid[x][y];
        int row = grid.length;
        int col = grid[0].length;
        int i, j;
        //left side
        for (j = 0; j < y; j++) {
            if (grid[x][j] >= currHeight) {
                break;
            }
        }
        if (j == y) {
            return true;
        }
        //right side
        for (j = col - 1; j > y; j--) {
            if (grid[x][j] >= currHeight) {
                break;
            }
        }
        if (j == y) {
            return true;
        }
        //top
        for (i = 0; i < x; i++) {
            if (grid[i][y] >= currHeight) {
                break;
            }
        }
        if (i == x) {
            return true;
        }

        //bottom
        for (i = row - 1; i > x; i--) {
            if (grid[i][y] >= currHeight) {
                return false;
            }
        }
        return i == x;
    }

    public int part2(int[][] grid) {
        int row = grid.length;
        int col = grid[0].length;
        int scenicScore = 0;

        // Don't have to consider the edges as one of the 'viewing distance' will be 0.
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                if (isVisible(grid, i, j)) {
                    scenicScore = Math.max(scenicScore, scenicScore(grid, i, j));
                }
            }
        }
        return scenicScore;
    }

    private int scenicScore(int[][] grid, int x, int y) {
        int currHeight = grid[x][y];
        int row = grid.length;
        int col = grid[0].length;
        int i, j;
        // viewing distances
        int numOfTreesVisibleOnLeft = 0;
        int numOfTreesVisibleOnRight = 0;
        int numOfTreesVisibleOnTop = 0;
        int numOfTreesVisibleOnBottom = 0;

        //left side
        for (j = y - 1; j >= 0; j--) {
            numOfTreesVisibleOnLeft++;
            if (grid[x][j] >= currHeight) {
                break;
            }
        }

        //right side
        for (j = y + 1; j < col; j++) {
            numOfTreesVisibleOnRight++;
            if (grid[x][j] >= currHeight) {
                break;
            }
        }

        //top
        for (i = x - 1; i >= 0; i--) {
            numOfTreesVisibleOnTop++;
            if (grid[i][y] >= currHeight) {
                break;
            }
        }

        //bottom
        for (i = x + 1; i < row; i++) {
            numOfTreesVisibleOnBottom++;
            if (grid[i][y] >= currHeight) {
                break;
            }
        }
        return numOfTreesVisibleOnLeft * numOfTreesVisibleOnRight * numOfTreesVisibleOnTop * numOfTreesVisibleOnBottom;
    }
}
