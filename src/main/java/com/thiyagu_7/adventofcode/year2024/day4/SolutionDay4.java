package com.thiyagu_7.adventofcode.year2024.day4;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class SolutionDay4 {
    private static final char[] XMAS = new char[]{'X', 'M', 'A', 'S'};
    private static final Set<Character> SET_M_S = Set.of('M', 'S');

    private static final Map<Direction, Function<Location, Location>> LOCATION_MAP = Map.of(
            Direction.UP, location -> new Location(location.x - 1, location.y),
            Direction.DOWN, location -> new Location(location.x + 1, location.y),
            Direction.LEFT, location -> new Location(location.x, location.y - 1),
            Direction.RIGHT, location -> new Location(location.x, location.y + 1),
            Direction.UP_LEFT, location -> new Location(location.x - 1, location.y - 1),
            Direction.UP_RIGHT, location -> new Location(location.x - 1, location.y + 1),
            Direction.DOWN_LEFT, location -> new Location(location.x + 1, location.y - 1),
            Direction.DOWN_RIGHT, location -> new Location(location.x + 1, location.y + 1)
    );

    public int part1(List<String> input) {
        char[][] grid = parseInput(input);
        int result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'X') {
                    result += navigate(grid, new Location(i, j));
                }
            }
        }
        return result;
    }

    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }


    private int navigate(char[][] grid, Location location) {
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        int i;

        for (Map.Entry<Direction, Function<Location, Location>> entry : LOCATION_MAP.entrySet()) {
            Location currentLocation = location;
            for (i = 1; i < 4; i++) {
                Location newLocation = entry.getValue()
                        .apply(currentLocation);
                if (isValid(newLocation, m, n) && grid[newLocation.x][newLocation.y] == XMAS[i]) {
                    currentLocation = newLocation;
                } else {
                    break;
                }
            }
            if (i == 4) {
                count++;
            }
        }
        return count;
    }

    private boolean isValid(Location location, int m, int n) {
        return location.x >= 0 && location.x < m && location.y >= 0 && location.y < n;
    }

    private record Location(int x, int y) {

    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UP_LEFT,
        UP_RIGHT,
        DOWN_LEFT,
        DOWN_RIGHT
    }


    public int part2(List<String> input) {
        char[][] grid = parseInput(input);
        int result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'A') {
                    if (checkMAndS(grid, new Location(i, j))) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    private boolean checkMAndS(char[][] grid, Location location) {
        int m = grid.length;
        int n = grid[0].length;
        Location upLeft = LOCATION_MAP.get(Direction.UP_LEFT).apply(location);
        Location upRight = LOCATION_MAP.get(Direction.UP_RIGHT).apply(location);
        Location downLeft = LOCATION_MAP.get(Direction.DOWN_LEFT).apply(location);
        Location downRight = LOCATION_MAP.get(Direction.DOWN_RIGHT).apply(location);

        if (isValid(upLeft, m, n) && isValid(upRight, m, n) && isValid(downLeft, m, n) && isValid(downRight, m, n)) {
            return new HashSet<>(List.of(grid[upLeft.x][upLeft.y], grid[downRight.x][downRight.y]))
                    .equals(SET_M_S)
                    && new HashSet<>(List.of(grid[upRight.x][upRight.y], grid[downLeft.x][downLeft.y]))
                    .equals(SET_M_S);
        }
        return false;
    }
}
