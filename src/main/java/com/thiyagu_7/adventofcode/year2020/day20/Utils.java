package com.thiyagu_7.adventofcode.year2020.day20;

public class Utils {
    static char[] getCol(char[][] tile, int col) {
        char[] res = new char[tile.length];
        for (int i = 0; i < tile.length; i++) {
            res[i] = tile[i][col];
        }
        return res;
    }
}
