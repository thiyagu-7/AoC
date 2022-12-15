package com.thiyagu_7.adventofcode.year2022.day14;

import java.util.ArrayList;
import java.util.List;

public class SolutionDay14 {
    private static final char EMPTY = '\u0000';

    public int part1(List<String> input) {
        char[][] grid = new char[600][600];
        int maxRowNum = 0;
        for (String line : input) {
            List<Location> coordinates = parsePaths(line);
            maxRowNum = coordinates.get(0).x;
            for (int i = 1; i < coordinates.size(); i++) {
                drawRock(grid, coordinates.get(i - 1), coordinates.get(i));
                maxRowNum = Math.max(maxRowNum, coordinates.get(i).x);
            }
        }
        return simulateSandDropPart1(grid, maxRowNum);
    }

    private List<Location> parsePaths(String path) {
        String[] parts = path.split(" -> ");
        List<Location> coordinates = new ArrayList<>();
        for (String xy : parts) {
            String[] xyParts = xy.split(",");
            // x, y are reversed, so flipping them
            coordinates.add(new Location(
                    Integer.parseInt(xyParts[1]),
                    Integer.parseInt(xyParts[0])));
        }
        return coordinates;
    }

    private void drawRock(char[][] grid, Location start, Location end) {
        if (start.x == end.x) {
            for (int j = Math.min(start.y, end.y); j <= Math.max(start.y, end.y); j++) {
                grid[start.x][j] = '#';
            }
        } else {
            for (int i = Math.min(start.x, end.x); i <= Math.max(start.x, end.x); i++) {
                grid[i][end.y] = '#';
            }
        }
    }

    private int simulateSandDropPart1(char[][] grid, int maxRowNum) {
        int numSand = 0;
        while (true) {
            int x = 0, y = 500;
            numSand++;
            while (true) {
                if (grid[x + 1][y] == EMPTY) { //down
                    x++;
                } else if (grid[x + 1][y - 1] == EMPTY) { //down left
                    x++;
                    y--;
                } else if (grid[x + 1][y + 1] == EMPTY) { //down right
                    x++;
                    y++;
                } else { //settle
                    grid[x][y] = 'o';
                    break;
                }
                if (x > maxRowNum) {
                    //started falling into the endless void. Return number of units of sand.
                    return numSand - 1;
                }
            }
        }
    }

    private record Location(int x, int y) {
    }

    public int part2(List<String> input) {
        //column width min 673 needed for the input
        char[][] grid = new char[600][700];
        int maxRowNum = 0;
        for (String line : input) {
            List<Location> coordinates = parsePaths(line);
            maxRowNum = coordinates.get(0).x;
            for (int i = 1; i < coordinates.size(); i++) {
                drawRock(grid, coordinates.get(i - 1), coordinates.get(i));
                maxRowNum = Math.max(maxRowNum, coordinates.get(i).x);
            }
        }
        //add floor
        for (int j = 0; j < grid[0].length; j++) {
            grid[maxRowNum + 2][j] = '#';
        }
        return simulateSandDropPart2(grid, maxRowNum + 2);
    }

    private int simulateSandDropPart2(char[][] grid, int floorNum) {
        int numSand = 0;
        while (true) {
            int x = 0, y = 500;
            numSand++;
            while (true) {
                if (grid[x + 1][y] == EMPTY) { //down
                    x++;
                } else if (grid[x + 1][y - 1] == EMPTY) { //down left
                    x++;
                    y--;
                } else if (grid[x + 1][y + 1] == EMPTY) { //down right
                    x++;
                    y++;
                } else if (x == 0 && y == 500) { //blocked at source
                    return numSand;
                } else { //settle
                    grid[x][y] = 'o';
                    break;
                }
                if (x >= floorNum) {
                    throw new RuntimeException(); //y dimension of grid (floor) not wide enough. Didn't hit this case.
                }
            }
        }
    }
}
