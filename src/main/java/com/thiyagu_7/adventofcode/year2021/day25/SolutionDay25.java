package com.thiyagu_7.adventofcode.year2021.day25;

import java.util.Arrays;
import java.util.List;

public class SolutionDay25 {
    public int part1(List<String> input) {
        int n = input.size();
        int m = input.get(0).length();
        char[][] map = buildMap(input, n, m);
        int step = 0;
        while (true) {
            boolean moved = false;
            step++;
            char[][] newMap = copy(map);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    int y = getNextCol(j, m);
                    if (map[i][j] == '>' && map[i][y] == '.') {
                        newMap[i][j] = '.';
                        newMap[i][y] = '>';
                        moved = true;
                    }
                }
            }
            map = newMap;
            newMap = copy(map);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    int x = getNextRow(i, n);
                    if (map[i][j] == 'v' && map[x][j] == '.') {
                        newMap[i][j] = '.';
                        newMap[x][j] = 'v';
                        moved = true;
                    }
                }
            }
            map = newMap;
            if (!moved) {
                return step;
            }
        }
    }

    private int getNextCol(int j, int m) {
        return (j + 1) % m;
    }

    private int getNextRow(int i, int n) {
        return (i + 1) % n;
    }

    private char[][] buildMap(List<String> input, int n, int m) {
        char[][] map = new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                map[i][j] = input.get(i).charAt(j);
            }
        }
        return map;
    }

    private char[][] copy(char[][] c) {
        char[][] r = new char[c.length][c[0].length];
        for (int i = 0; i < c.length; i++) {
            r[i] = Arrays.copyOf(c[i], c[i].length);
        }
        return r;
    }

}
