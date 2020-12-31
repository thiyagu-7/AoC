package com.thiyagu_7.adventofcode.year2020.day20;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay20MyTry {
    public long part1(List<String> input, int size) {
        Map<String, char[][]> allTiles = new HashMap<>();
        parseInput(input, allTiles);
        /*allTiles.entrySet()
                .forEach(e -> {
                    System.out.println(e.getKey());
                    System.out.println(Arrays.deepToString(e.getValue()));
                });*/

        Map<String, Map<Alignment, char[][]>> allTilesAllAlignments = allTiles.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> get8Permutations(e.getValue())));

        Map<String, MatchingTiles> matchingTilesMap = new HashMap<>();
        for (Map.Entry<String, char[][]> entry : allTiles.entrySet()) {
            String tileName = entry.getKey();
            MatchingTiles matchingTiles = getMatchingTiles(tileName, allTiles);

            matchingTilesMap.put(tileName, matchingTiles);
        }


        MatchingTileAndAlignment[][] grid = new MatchingTileAndAlignment[size][size];
        System.out.println(recur(allTilesAllAlignments, grid,
                new HashSet<>(), 0, 0, size));
        return Long.parseLong(grid[0][0].tileName)
                * Long.parseLong(grid[0][size - 1].tileName)
                * Long.parseLong(grid[size - 1][0].tileName)
                * Long.parseLong(grid[size - 1][size - 1].tileName);
    }

    private boolean recur(Map<String, Map<Alignment, char[][]>> allTilesAllAlignments,
                          MatchingTileAndAlignment[][] grid, Set<String> usedTiles, int i, int j, int size) {

        if (i == size) {
            System.out.println("SUCCESS");
            System.out.println(Arrays.deepToString(grid));
            return true;
        }
        for (Map.Entry<String, Map<Alignment, char[][]>> entryAllTilesAllAlignments : allTilesAllAlignments.entrySet()) {
            String tileName = entryAllTilesAllAlignments.getKey();

            //System.out.println(usedTiles.size());
            if (!usedTiles.contains(tileName)) {
                Set<String> usedTilesCopy = new HashSet<>(usedTiles);
                usedTilesCopy.add(tileName);

                Map<Alignment, char[][]> allAlignments = entryAllTilesAllAlignments.getValue();

                for (Map.Entry<Alignment, char[][]> entryAllAlignments : allAlignments.entrySet()) {
                    MatchingTileAndAlignment mta = new MatchingTileAndAlignment();
                    mta.tileName = tileName;
                    mta.alignment = entryAllAlignments.getKey();
                    grid[i][j] = mta;

                    boolean validAlignment = checkAlignmentWithNeighbors(allTilesAllAlignments, grid, i, j);
                    if (validAlignment) {
                        boolean success;
                        if (j + 1 == size) {
                            success =  recur(allTilesAllAlignments, grid, usedTilesCopy, i + 1, 0, size);
                        } else {
                            success = recur(allTilesAllAlignments, grid, usedTilesCopy, i, j + 1, size);
                        }
                        if (success) {
                            return true;
                        }
                    }
                }
            }
        }
        grid[i][j] = null;
        return false;
    }

    private boolean checkAlignmentWithNeighbors(Map<String, Map<Alignment, char[][]>> allTilesAllAlignments,
                                                MatchingTileAndAlignment[][] grid, int i, int j) {

        int l = 10;
        char[][] curr = allTilesAllAlignments.get(grid[i][j].tileName).get(grid[i][j].alignment);
        if (i - 1 >= 0) {
            char[][] up = allTilesAllAlignments.get(grid[i - 1][j].tileName).get(grid[i - 1][j].alignment);
            if (!equals(curr[0], up[l - 1])) {
                return false;
            }
        }

        if (j - 1 >= 0) {
            char[][] left = allTilesAllAlignments.get(grid[i][j - 1].tileName).get(grid[i][j - 1].alignment);
            return equals(getCol(curr, 0), getCol(left, l - 1));
        }
        return true;
    }

    private void parseInput(List<String> input, Map<String, char[][]> allTiles) {
        String tileName = "";
        char[][] tile = new char[10][10];
        int i = 0;
        for (String line : input) {
            if (line.isEmpty()) {
                allTiles.put(tileName, tile);
                i = 0;
                tile = new char[10][10];
            } else if (line.startsWith("Tile")) {
                String[] p = line.split(" ");
                tileName = p[1].substring(0, p[1].length() - 1); //remove last ':'
            } else {
                tile[i++] = line.toCharArray();
            }
        }
        //for last data set
        allTiles.put(tileName, tile);
    }

    private MatchingTiles getMatchingTiles(String tileName, Map<String, char[][]> allTiles) {
        Map<Alignment, char[][]> currentTileAllCombinations = get8Permutations(allTiles.get(tileName));

        MatchingTiles matchingTiles = new MatchingTiles();
        for (Map.Entry<Alignment, char[][]> alignmentEntry : currentTileAllCombinations.entrySet()) {
            char[][] currentTile = currentTileAllCombinations.get(Alignment.AS_IS_IS);//todo

            for (Map.Entry<String, char[][]> allTilesEntry : allTiles.entrySet()) {
                if (!allTilesEntry.getKey().equals(tileName)) {
                    Map<Alignment, char[][]> allCombinationOtherTiles = get8Permutations(allTilesEntry.getValue());

                    MatchingTiles matchingTilesTmp = match(allTilesEntry.getKey(), currentTile, allCombinationOtherTiles);

                    if (matchingTilesTmp.left != null) {
                        matchingTiles.left = matchingTilesTmp.left;
                    }
                    if (matchingTilesTmp.right != null) {
                        matchingTiles.right = matchingTilesTmp.right;
                    }
                    if (matchingTilesTmp.top != null) {
                        matchingTiles.top = matchingTilesTmp.top;
                    }
                    if (matchingTilesTmp.bottom != null) {
                        matchingTiles.bottom = matchingTilesTmp.bottom;
                    }
                }
            }
           // break;
            //should continue for other alignments of current tile?
        }
        return matchingTiles;

    }

    //compare current tile in 4 directions and 8 combinations of other (single) tile
    private MatchingTiles match(String tileName, char[][] tile, Map<Alignment, char[][]> allCombinationOtherTiles) {

        MatchingTileAndAlignment top = checkTopAlignment(tileName, tile, allCombinationOtherTiles);
        MatchingTileAndAlignment bottom = checkBottomAlignment(tileName, tile, allCombinationOtherTiles);
        MatchingTileAndAlignment left = checkLeftAlignment(tileName, tile, allCombinationOtherTiles);
        MatchingTileAndAlignment right = checkRightAlignment(tileName, tile, allCombinationOtherTiles);

        MatchingTiles matchingTiles = new MatchingTiles();
        matchingTiles.top = top;
        matchingTiles.bottom = bottom;
        matchingTiles.left = left;
        matchingTiles.right = right;
        return matchingTiles;
    }


    private MatchingTileAndAlignment checkTopAlignment(String tileName, char[][] tile, Map<Alignment, char[][]> allCombinationOtherTiles) {
        MatchingTileAndAlignment matchingTileAndAlignment = new MatchingTileAndAlignment();
        matchingTileAndAlignment.tileName = tileName;

        for (Map.Entry<Alignment, char[][]> entry : allCombinationOtherTiles.entrySet()) {
            int l = entry.getValue().length;
            if (equals(tile[0], entry.getValue()[l - 1])) {
                matchingTileAndAlignment.alignment = entry.getKey();
                return matchingTileAndAlignment;
            }
        }
        return null;
    }

    private MatchingTileAndAlignment checkBottomAlignment(String tileName, char[][] tile, Map<Alignment, char[][]> allCombinationOtherTiles) {
        MatchingTileAndAlignment matchingTileAndAlignment = new MatchingTileAndAlignment();
        matchingTileAndAlignment.tileName = tileName;

        for (Map.Entry<Alignment, char[][]> entry : allCombinationOtherTiles.entrySet()) {
            int l = tile.length;
            if (equals(tile[l - 1], entry.getValue()[0])) {
                matchingTileAndAlignment.alignment = entry.getKey();
                return matchingTileAndAlignment;
            }
        }
        return null;
    }

    private MatchingTileAndAlignment checkLeftAlignment(String tileName, char[][] tile, Map<Alignment, char[][]> allCombinationOtherTiles) {
        MatchingTileAndAlignment matchingTileAndAlignment = new MatchingTileAndAlignment();
        matchingTileAndAlignment.tileName = tileName;

        for (Map.Entry<Alignment, char[][]> entry : allCombinationOtherTiles.entrySet()) {
            int c = tile[0].length;
            if (equals(getCol(tile, 0), getCol(entry.getValue(), c - 1))) {
                matchingTileAndAlignment.alignment = entry.getKey();
                return matchingTileAndAlignment;
            }
        }
        return null;
    }


    private MatchingTileAndAlignment checkRightAlignment(String tileName, char[][] tile, Map<Alignment, char[][]> allCombinationOtherTiles) {
        MatchingTileAndAlignment matchingTileAndAlignment = new MatchingTileAndAlignment();
        matchingTileAndAlignment.tileName = tileName;

        for (Map.Entry<Alignment, char[][]> entry : allCombinationOtherTiles.entrySet()) {
            int c = tile[0].length;
            if (equals(getCol(tile, c - 1), getCol(entry.getValue(), 0))) {
                matchingTileAndAlignment.alignment = entry.getKey();
                return matchingTileAndAlignment;
            }
        }
        return null;
    }

    private boolean equals(char[] src, char[] target) {
        return Arrays.equals(src, target);
    }

    private char[] getCol(char[][] tile, int col) {
        char[] res = new char[tile.length];
        for (int i = 0; i < tile.length; i++) {
            res[i++] = tile[i][col];
        }
        return res;
    }


    private Map<Alignment, char[][]> get8Permutations(char[][] tile) {
        int l = tile.length;
        int i, j, k, m;
        //as it is
        char[][] asItIs = new char[l][l];
        for (i = 0; i < l; i++) {
            for (j = 0; j < l; j++) {
                asItIs[i][j] = tile[i][j];
            }
        }

        //reversed
        char[][] reversed = new char[l][l];
        for (i = 0; i < l; i++) {
            for (k = 0, j = l - 1; j >= 0; k++, j--) {
                reversed[i][k] = tile[i][j];
            }
        }

        //flipped
        char[][] flipped = new char[l][l];
        for (k = 0, i = l - 1; i >= 0; k++, i--) {
            for (j = 0; j < l; j++) {
                flipped[k][j] = tile[i][j];
            }
        }

        //reversed and flipped
        char[][] reversedAndFlipped = new char[l][l];
        for (k = 0, i = l - 1; i >= 0; k++, i--) {
            for (m = 0, j = l - 1; j >= 0; m++, j--) {
                reversedAndFlipped[k][m] = tile[i][j];
            }
        }
        Map<Alignment, char[][]> res = new HashMap<>();
        res.put(Alignment.AS_IS_IS, asItIs);
        res.put(Alignment.REVERSED, reversed);
        res.put(Alignment.FLIPPED, flipped);
        res.put(Alignment.FLIPPED_AND_REVERSED, reversedAndFlipped);

        char[][] n1 = new char[l][l];
        for (i = 0; i < l; i++) {
            for (j = 0; j < l; j++) {
                n1[i][j] = tile[j][i];
            }
        }

        char[][] n2 = new char[l][l];
        for (i = 0; i < l; i++) {
            for (k = 0, j = l - 1; j >= 0; k++, j--) {
                n2[i][k] = tile[j][i];
            }
        }

        char[][] n3 = new char[l][l];
        for (k = 0, i = l - 1; i >= 0; k++, i--) {
            for (j = 0; j < l; j++) {
                n3[k][j] = tile[j][i];
            }
        }

        //flipped
        char[][] n4 = new char[l][l];
        for (k = 0, i = l - 1; i >= 0; k++, i--) {
            for (m = 0, j = l - 1; j >= 0; m++, j--) {
                n4[k][m] = tile[j][i];
            }
        }
        res.put(Alignment.N1, n1);
        res.put(Alignment.N2, n2);
        res.put(Alignment.N3, n3);
        res.put(Alignment.N4, n4);
        return res;
    }

    private static class MatchingTiles {
        private MatchingTileAndAlignment top;
        private MatchingTileAndAlignment bottom;
        private MatchingTileAndAlignment left;
        private MatchingTileAndAlignment right;
    }

    private static class MatchingTileAndAlignment {
        private String tileName;
        private Alignment alignment;

        @Override
        public String toString() {
            return tileName + " " + alignment;
        }
    }

    private enum Alignment {
        AS_IS_IS,
        REVERSED,
        FLIPPED,
        FLIPPED_AND_REVERSED,

        N1,
        N2,N3,N4;
    }
}
