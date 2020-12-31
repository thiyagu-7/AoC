package com.thiyagu_7.adventofcode.year2020.day20;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thiyagu_7.adventofcode.year2020.day20.Utils.getCol;

public class SolutionDay20 {
    public long part1(List<String> input) {
        Map<String, char[][]> allTiles = new HashMap<>();
        parseInput(input, allTiles);

        long res = 1;
        for (String tile : allTiles.keySet()) {
            int n = getMaxNumMatches(tile, allTiles);
            if (n == 2) { //border tile
                System.out.println(tile);
                res *= Long.parseLong(tile);
            }
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

    //find max number of tiles matching borders
    private int getMaxNumMatches(String currentTileName, Map<String, char[][]> allTiles) {
        char[][] currentTile = allTiles.get(currentTileName);
        int len = currentTile.length;
        int matches = 0;
        for (Map.Entry<String, char[][]> allTilesEntry : allTiles.entrySet()) {

            if (!allTilesEntry.getKey().equals(currentTileName)) {
                char[][] otherTile = allTilesEntry.getValue();
                //bottom
                if (isMatch(currentTile[0], otherTile[0])
                        || isMatch(currentTile[0], otherTile[len - 1])
                        || isMatch(currentTile[0], getCol(otherTile, 0))
                        || isMatch(currentTile[0], getCol(otherTile, len - 1))) {
                    matches++;
                }
                //top
               else if (isMatch(currentTile[len - 1], otherTile[0])
                        || isMatch(currentTile[len - 1], otherTile[len - 1])
                        || isMatch(currentTile[len - 1], getCol(otherTile, 0))
                        || isMatch(currentTile[len - 1], getCol(otherTile, len - 1))) {
                    matches++;
                }
               //right
               else if (isMatch(getCol(currentTile, 0), otherTile[0])
                        || isMatch(getCol(currentTile, 0), otherTile[len - 1])
                        || isMatch(getCol(currentTile, 0), getCol(otherTile, 0))
                        || isMatch(getCol(currentTile, 0), getCol(otherTile, len - 1))) {
                    matches++;
                }
               //left
               else if (isMatch(getCol(currentTile, len - 1), otherTile[0])
                        || isMatch(getCol(currentTile, len - 1), otherTile[len - 1])
                        || isMatch(getCol(currentTile, len - 1), getCol(otherTile, 0))
                        || isMatch(getCol(currentTile, len - 1), getCol(otherTile, len - 1))) {
                    matches++;
                }
            }
        }
        return matches;
    }

    private boolean isMatch(char[] c1, char[] c2) {
        char[] c2Reverse = reverse(c2);
        return Arrays.equals(c1, c2) || Arrays.equals(c1, c2Reverse);
    }

    private char[] reverse(char[] c) {
        char[] res = new char[c.length];
        int j = 0;
        for (int i = c.length - 1; i >= 0; i--) {
            res[j++] = c[i];
        }
        return res;
    }
}
