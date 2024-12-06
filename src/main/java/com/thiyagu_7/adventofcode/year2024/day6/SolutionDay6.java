package com.thiyagu_7.adventofcode.year2024.day6;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SolutionDay6 {
    public int part1(List<String> input) {
        char[][] grid = parseInput(input);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '^') {
                    patrol(grid, new PositionAndDirection(
                            new Position(i, j),
                            Direction.UP
                    ));
                }
            }
        }
        int distinctPositionsVisited = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'X') {
                    distinctPositionsVisited++;
                }
            }
        }
        return distinctPositionsVisited;
    }

    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private void patrol(char[][] grid, PositionAndDirection positionAndDirection) {
        int m = grid.length;
        int n = grid[0].length;
        Position currentPosition = positionAndDirection.position;
        Direction currentDirection = positionAndDirection.direction;
        grid[currentPosition.x][currentPosition.y] = 'X';

        while (true) {
            Position nextPosition = currentPosition.getNextPosition(currentDirection);
            if (!isValid(nextPosition, m, n)) {
                break;
            }
            if (grid[nextPosition.x][nextPosition.y] != '#') { //not an obstacle
                grid[nextPosition.x][nextPosition.y] = 'X'; //mark as visited
                currentPosition = nextPosition;
            } else {
                //turn 90 degrees
                currentDirection = currentDirection.turn90Degrees();
            }
        }
    }

    private boolean isValid(Position position, int m, int n) {
        return position.x >= 0 && position.x < m && position.y >= 0 && position.y < n;
    }

    record Position(int x, int y) {
        public Position getNextPosition(Direction currentDirection) {
            return switch (currentDirection) {
                case UP -> new Position(x - 1, y);
                case DOWN -> new Position(x + 1, y);
                case LEFT -> new Position(x, y - 1);
                case RIGHT -> new Position(x, y + 1);
            };
        }
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;

        public Direction turn90Degrees() {
            return switch (this) {
                case UP -> Direction.RIGHT;
                case RIGHT -> Direction.DOWN;
                case DOWN -> Direction.LEFT;
                case LEFT -> Direction.UP;
            };
        }
    }


    record PositionAndDirection(Position position, Direction direction) {

    }

    public int part2(List<String> input) {
        char[][] grid = parseInput(input);
        PositionAndDirection intialPositionAndDirection = null;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '^') {
                    intialPositionAndDirection = new PositionAndDirection(
                            new Position(i, j),
                            Direction.UP);
                    break;
                }
            }
        }

        int loops = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '.') {
                    grid[i][j] = '#';
                    if (simulatePatrolForLoop(grid, intialPositionAndDirection)) {
                        loops++;
                    }
                    grid[i][j] = '.';
                }
            }
        }
        return loops;
    }

    private boolean simulatePatrolForLoop(char[][] grid, PositionAndDirection positionAndDirection) {
        int m = grid.length;
        int n = grid[0].length;
        Position currentPosition = positionAndDirection.position;
        Direction currentDirection = positionAndDirection.direction;

        Set<PositionAndDirection> visited = new HashSet<>();
        visited.add(positionAndDirection);

        while (true) {
            Position nextPosition = currentPosition.getNextPosition(currentDirection);
            if (!isValid(nextPosition, m, n)) {
                break;
            }
            if (grid[nextPosition.x][nextPosition.y] != '#') {
                currentPosition = nextPosition;
            } else {
                //turn 90 degrees
                currentDirection = currentDirection.turn90Degrees();
            }

            PositionAndDirection currentPositionAndDirection = new PositionAndDirection(currentPosition, currentDirection);
            if (visited.contains(currentPositionAndDirection)) {
                return true; //loop
            }
            visited.add(currentPositionAndDirection);
        }
        return false; // no loop
    }
}
