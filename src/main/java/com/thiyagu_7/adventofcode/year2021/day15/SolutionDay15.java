package com.thiyagu_7.adventofcode.year2021.day15;

import java.util.Arrays;
import java.util.List;

/*
 DP will not work as we can move in four directions.
 It worked for part-1 and sample case part-2 only

 https://www.reddit.com/r/adventofcode/comments/rgt1sx/help_with_day_15_part_2_using_dynamic_programming/
 */
public class SolutionDay15 {
    public int part1(List<String> input) {
        int[][] grid = buildGrid(input);
        int n = grid.length;
        int m = grid[0].length;
        int[][] dp = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 && j == 0) {
                    dp[i][j] = 0;
                    continue;
                }
                dp[i][j] = Math.min(
                        i - 1 >= 0 ? dp[i - 1][j] : Integer.MAX_VALUE,
                        j - 1 >= 0 ? dp[i][j - 1] : Integer.MAX_VALUE
                ) + grid[i][j];
            }
        }
        return dp[n - 1][m - 1];
    }

    private int[][] buildGrid(List<String> input) {
        return input.stream()
                .map(line -> Arrays.stream(line.split(""))
                        .mapToInt(Integer::valueOf)
                        .toArray())
                .toArray(int[][]::new);
    }
}
