package com.thiyagu_7.adventofcode.year2020.day20;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.thiyagu_7.adventofcode.year2020.day20.Utils.getCol;


public class SolutionDay20Part2 {
    private static int size = 10;
    private static int dimension;

    public int part2(List<String> input, List<String> edgeTiles, int dim) {
        dimension = dim;

        Map<String, char[][]> allTiles = new HashMap<>();
        parseInput(input, allTiles);

        Map<Border, List<MatchingTileAndAlignment>> tileEdges = tileEdges(allTiles);

        MatchingTileAndAlignment[][] grid = null;
        for (String edgeTile : edgeTiles) {
            Map<Alignment, char[][]> permutations = get8Permutations(allTiles.get(edgeTile));

            boolean done = false;
            for (Map.Entry<Alignment, char[][]> entry : permutations.entrySet()) {
                grid = new MatchingTileAndAlignment[dimension][dimension];
                //try each alignment of the edge tile
                grid[0][0] = new MatchingTileAndAlignment(edgeTile, entry.getKey(), entry.getValue());
                done = tryPopulate(grid, tileEdges, new HashSet<>(Collections.singleton(edgeTile)), 0, 1);
                if (done) {
                    System.out.println("Found");
                    break;
                }
            }
            if (done) {
                break;
            }
        }

        SeaMonsterFinder seaMonsterFinder = new SeaMonsterFinder();
        return seaMonsterFinder.findRoughness(grid, dimension);
    }

    private boolean tryPopulate(MatchingTileAndAlignment[][] grid,
                                Map<Border, List<MatchingTileAndAlignment>> tileEdges,
                                Set<String> usedTiles,
                                int x, int y) {

        if (x == dimension) {
            return true;
        }
        MatchingTileAndAlignment currentAsPerLeft = null;
        MatchingTileAndAlignment currentAsPerTop = null;

        if (y - 1 >= 0) {
            //check left
            MatchingTileAndAlignment leftTile = grid[x][y - 1];
            char[] rightCol = getCol(leftTile.tile, size - 1);
            Border rightColBorder = new Border(rightCol);

            if (tileEdges.containsKey(rightColBorder)) {
                List<MatchingTileAndAlignment> l = tileEdges.get(rightColBorder);

                Optional<MatchingTileAndAlignment> o = l.stream()
                        .filter(m -> !m.tileName.equals(leftTile.tileName)) //pick the other one
                        .filter(m -> Arrays.equals(getCol(m.tile, 0), rightCol)) //pick the right alignment
                        .findFirst();
                if (!o.isPresent()) {
                    return false;
                }
                currentAsPerLeft = o.get();

            } else {
                return false;
            }
        }
        if (x - 1 >= 0) {
            //check top
            MatchingTileAndAlignment topTile = grid[x - 1][y];
            char[] bottomRow = topTile.tile[size - 1];
            Border bottomRowBorder = new Border(bottomRow);

            if (tileEdges.containsKey(bottomRowBorder)) {
                List<MatchingTileAndAlignment> t = tileEdges.get(bottomRowBorder);

                Optional<MatchingTileAndAlignment> o = t.stream()
                        .filter(m -> !m.tileName.equals(topTile.tileName)) //pick the other one
                        .filter(m -> Arrays.equals(m.tile[0], bottomRow)) //pick the right alignment
                        .findFirst();

                if (!o.isPresent()) {
                    return false;
                }
                currentAsPerTop = o.get();
            } else {
                return false;
            }
        }
        if (currentAsPerTop != null && currentAsPerLeft != null && currentAsPerTop != currentAsPerLeft) {
            //shouldn't happen
            throw new RuntimeException("More than one matching tile found");
        }

        MatchingTileAndAlignment current = currentAsPerLeft != null ? currentAsPerLeft : currentAsPerTop;
        grid[x][y] = current;
        Set<String> usedTilesCopy = new HashSet<>(usedTiles);
        usedTilesCopy.add(current.tileName);
        boolean success;
        if (y + 1 == dimension) {
            success = tryPopulate(grid, tileEdges, usedTilesCopy, x + 1, 0);
        } else {
            success = tryPopulate(grid, tileEdges, usedTilesCopy, x, y + 1);
        }
        if (success) {
            return true;
        } else {
            grid[x][y] = null;
            return false;
        }
    }

    private static class Border {
        private char[] border;

        public Border(char[] border) {
            this.border = border;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(border);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != Border.class) {
                return false;
            }
            return Arrays.equals(border, ((Border) obj).border);
        }
    }

    private Map<Border, List<MatchingTileAndAlignment>> tileEdges(Map<String, char[][]> allTiles) {
        Map<Border, List<MatchingTileAndAlignment>> tileEdges = new HashMap<>();

        for (Map.Entry<String, char[][]> allTilesEntry : allTiles.entrySet()) {
            String tileName = allTilesEntry.getKey();
            Map<Alignment, char[][]> permutations = get8Permutations(allTilesEntry.getValue());

            List<MatchingTileAndAlignment> matchingTileAndAlignments = permutations.entrySet()
                    .stream()
                    .map(e -> new MatchingTileAndAlignment(tileName, e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            for (MatchingTileAndAlignment m : matchingTileAndAlignments) {
                char[][] tile = m.tile;

                add(tileEdges, new Border(tile[0]), m);
                add(tileEdges, new Border(tile[size - 1]), m);
                add(tileEdges, new Border(getCol(tile, 0)), m);
                add(tileEdges, new Border(getCol(tile, size - 1)), m);

                //reverse each
                add(tileEdges, new Border(reverse(tile[0])), m);
                add(tileEdges, new Border(reverse(tile[size - 1])), m);
                add(tileEdges, new Border(reverse(getCol(tile, 0))), m);
                add(tileEdges, new Border(reverse(getCol(tile, size - 1))), m);

            }
        }
        return tileEdges;
    }

    private void add(Map<Border, List<MatchingTileAndAlignment>> tileEdges, Border border,
                     MatchingTileAndAlignment matchingTileAndAlignment) {
        tileEdges.computeIfAbsent(border, b -> new ArrayList<>())
                .add(matchingTileAndAlignment);
    }

    private char[] reverse(char[] c) {
        char[] res = new char[c.length];
        int j = 0;
        for (int i = c.length - 1; i >= 0; i--) {
            res[j++] = c[i];
        }
        return res;
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
        res.put(Alignment.AS_IT_IS, asItIs);
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

    public static class MatchingTileAndAlignment {
        public String tileName;
        public char[][] tile;
        public Alignment alignment;

        private MatchingTileAndAlignment(String tileName, Alignment alignment, char[][] tile) {
            this.tileName = tileName;
            this.tile = tile;
            this.alignment = alignment;
        }

        @Override
        public String toString() {
            return tileName + " " + alignment;
        }
    }

    private enum Alignment {
        AS_IT_IS, //todo
        REVERSED,
        FLIPPED,
        FLIPPED_AND_REVERSED,

        N1,
        N2, N3, N4;
    }
}
