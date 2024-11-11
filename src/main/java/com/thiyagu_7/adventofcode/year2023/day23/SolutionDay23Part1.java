package com.thiyagu_7.adventofcode.year2023.day23;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class SolutionDay23Part1 {
    public int part1(List<String> input) {
        char[][] grid = parseGrid(input);
        Result result = new Result();
        dfs(grid, new Position(0, 1), new HashSet<>(), result);
        return result.steps;
    }

    private char[][] parseGrid(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private void dfs(char[][] grid, Position currentPosition,
                     Set<Position> path,
                     Result result) {
        int m = grid.length;
        int n = grid[0].length;

        if (currentPosition.x == m - 1) {
            result.steps = Math.max(path.size(), result.steps);
            return;
        }

        Direction[] directions = new Direction[]{
                Direction.UP,
                Direction.DOWN,
                Direction.LEFT,
                Direction.RIGHT
        };
        Map<Character, Direction> directionToSlope = Map.of(
                '^', Direction.UP,
                'v', Direction.DOWN,
                '<', Direction.LEFT,
                '>', Direction.RIGHT
        );

        path.add(currentPosition);

        Position newPosition;
        //current position is a slope - so can only move in the direction of the slope
        if (directionToSlope.containsKey(grid[currentPosition.x][currentPosition.y])) {
            Direction direction = directionToSlope.get(grid[currentPosition.x][currentPosition.y]);
            newPosition = direction.getMoveFunction()
                    .apply(currentPosition);

            if (isValid(newPosition, m, n)
                    && grid[newPosition.x][newPosition.y] != '#'
                    && !path.contains(newPosition)) {
                dfs(grid, newPosition, path, result);
            }

        } else {
            for (int k = 0; k < 4; k++) {
                Direction direction = directions[k];
                newPosition = direction.getMoveFunction()
                        .apply(currentPosition);

                if (isValid(newPosition, m, n)
                        && grid[newPosition.x][newPosition.y] != '#'
                        && !path.contains(newPosition)) {
                    dfs(grid, newPosition, path, result);
                }
            }
        }
        path.remove(currentPosition);
    }

    private boolean isValid(Position position, int m, int n) {
        return position.x >= 0 && position.x < m && position.y >= 0 && position.y < n;
    }

    private static class Result {
        int steps = 0;
    }

    record Position(int x, int y) {

    }

    enum Direction {
        UP(p -> new Position(p.x - 1, p.y)),
        DOWN(p -> new Position(p.x + 1, p.y)),
        LEFT(p -> new Position(p.x, p.y - 1)),
        RIGHT(p -> new Position(p.x, p.y + 1));

        private final Function<Position, Position> moveFunction;

        Direction(Function<Position, Position> moveFunction) {
            this.moveFunction = moveFunction;
        }

        public Function<Position, Position> getMoveFunction() {
            return moveFunction;
        }
    }
}
