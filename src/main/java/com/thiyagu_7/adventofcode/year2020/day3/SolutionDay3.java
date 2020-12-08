package com.thiyagu_7.adventofcode.year2020.day3;

import java.util.Arrays;
import java.util.List;

public class SolutionDay3 {
    public long part1(List<String> map) {
        char[][] gridMap = map.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        return findNumTreesEncountered(gridMap, 1, 3);
    }

    public long part2(List<String> map) {
        char[][] gridMap = map.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        return findNumTreesEncountered(gridMap, 1, 1)
                * findNumTreesEncountered(gridMap, 1, 3)
                * findNumTreesEncountered(gridMap, 1, 5)
                * findNumTreesEncountered(gridMap, 1, 7)
                * findNumTreesEncountered(gridMap, 2, 1);
    }

    private long findNumTreesEncountered(char[][] gridMap, int x, int y) {
        int numTrees = 0;
        int col = gridMap[0].length;
        int j = 0;

        for (int i = 0; i < gridMap.length; i += x) {
            if (gridMap[i][j] == '#') {
                numTrees++;
            }
            j = (j + y) % col;
        }
        return numTrees;
    }
}
