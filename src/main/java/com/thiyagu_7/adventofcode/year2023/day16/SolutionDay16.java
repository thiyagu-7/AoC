package com.thiyagu_7.adventofcode.year2023.day16;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//SEE - Nice one
public class SolutionDay16 {
    public int part1(List<String> input) {
        char[][] grid = parseInput(input);
        boolean[][] energizedTiles = new boolean[grid.length][grid[0].length];
        simulate(grid, new PositionAndDirection(new Position(0, 0),
                        Direction.RIGHT),
                energizedTiles);

        return findNumberOfEnergizedTitles(energizedTiles);
    }

    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private void simulate(char[][] grid, PositionAndDirection currentPositionAndDirection,
                          boolean[][] energizedTiles) {
        int m = grid.length;
        int n = grid[0].length;
        Position currentPosition = currentPositionAndDirection.position;
        Direction currentDirection = currentPositionAndDirection.direction;

        List<PositionAndDirection> beamPositionAndDirections = List.of(
                new PositionAndDirection(currentPosition, currentDirection)
        );
        energizedTiles[currentPosition.x][currentPosition.y] = true;

        //don't proceed if visiting a cell in the same direction
        /*
        Can be optimized more as hitting splitter cells '|' from LEFT/RIGHT and '-' from UP/DOWN have the same effect
        and hence can be considered already visited for the other direction as well.
         */
        Set<PositionAndDirection> visited = new HashSet<>();

        while (!beamPositionAndDirections.isEmpty()) {
            List<PositionAndDirection> newBeamPositionAndDirections = new ArrayList<>();

            for (PositionAndDirection beamPositionAndDirection : beamPositionAndDirections) {
                Position beamPosition = beamPositionAndDirection.position;
                energizedTiles[beamPosition.x][beamPosition.y] = true;

                List<PositionAndDirection> nextPositionAndDirections =
                        move(grid[beamPosition.x][beamPosition.y], beamPositionAndDirection);
                nextPositionAndDirections.stream()
                        .filter(p -> !visited.contains(p))
                        .filter(p -> isValid(p.position, m, n))
                        .forEach(p -> {
                            visited.add(p);
                            newBeamPositionAndDirections.add(p);
                        });
            }
            beamPositionAndDirections = newBeamPositionAndDirections;
        }
    }

    private boolean isValid(Position position, int m, int n) {
        return position.x >= 0 && position.x < m && position.y >= 0 && position.y < n;
    }

    private List<PositionAndDirection> move(char currentCell, PositionAndDirection currentPositionAndDirection) {
        Position currentPosition = currentPositionAndDirection.position;
        Direction currentDirection = currentPositionAndDirection.direction;
        return switch (currentCell) {
            case '.' -> List.of(
                    new PositionAndDirection(
                            moveInDirection(currentPosition, currentDirection),
                            currentDirection)
            );
            case '|' -> switch (currentDirection) {
                case UP, DOWN -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, currentDirection),
                        currentDirection)
                );
                case RIGHT, LEFT -> List.of(
                        new PositionAndDirection(
                                moveInDirection(currentPosition, Direction.UP),
                                Direction.UP),
                        new PositionAndDirection(
                                moveInDirection(currentPosition, Direction.DOWN),
                                Direction.DOWN)
                );
            };
            case '-' -> switch (currentDirection) {
                case LEFT, RIGHT -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, currentDirection),
                        currentDirection)
                );
                case UP, DOWN -> List.of(
                        new PositionAndDirection(
                                moveInDirection(currentPosition, Direction.LEFT),
                                Direction.LEFT),
                        new PositionAndDirection(
                                moveInDirection(currentPosition, Direction.RIGHT),
                                Direction.RIGHT)
                );
            };
            case '/' -> switch (currentDirection) {
                case RIGHT -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, Direction.UP),
                        Direction.UP)
                );
                case LEFT -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, Direction.DOWN),
                        Direction.DOWN)
                );
                case UP -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, Direction.RIGHT),
                        Direction.RIGHT)
                );
                case DOWN -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, Direction.LEFT),
                        Direction.LEFT)
                );
            };
            case '\\' -> switch (currentDirection) {
                case RIGHT -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, Direction.DOWN),
                        Direction.DOWN)
                );
                case LEFT -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, Direction.UP),
                        Direction.UP)
                );
                case UP -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, Direction.LEFT),
                        Direction.LEFT)
                );
                case DOWN -> List.of(new PositionAndDirection(
                        moveInDirection(currentPosition, Direction.RIGHT),
                        Direction.RIGHT)
                );
            };
            default -> throw new RuntimeException();
        };
    }

    private Position moveInDirection(Position position, Direction direction) {
        return switch (direction) {
            case UP -> new Position(position.x - 1, position.y);
            case DOWN -> new Position(position.x + 1, position.y);
            case LEFT -> new Position(position.x, position.y - 1);
            case RIGHT -> new Position(position.x, position.y + 1);
        };
    }

    private int findNumberOfEnergizedTitles(boolean[][] energizedTiles) {
        int energized = 0;
        for (boolean[] energizedTile : energizedTiles) {
            for (int j = 0; j < energizedTiles[0].length; j++) {
                if (energizedTile[j]) {
                    energized++;
                }
            }
        }
        return energized;
    }

    public int part2(List<String> input) {
        char[][] grid = parseInput(input);
        boolean[][] energizedTiles;
        int maxEnergized = 0;
        // check all tiles in the top row (heading downward)
        for (int j = 0; j < grid[0].length; j++) {
            energizedTiles = new boolean[grid.length][grid[0].length];
            simulate(grid, new PositionAndDirection(new Position(0, j),
                            Direction.DOWN),
                    energizedTiles);
            maxEnergized = Math.max(maxEnergized, findNumberOfEnergizedTitles(energizedTiles));
        }
        // check all tiles in the bottom row (heading upward)
        for (int j = 0; j < grid[0].length; j++) {
            energizedTiles = new boolean[grid.length][grid[0].length];
            simulate(grid, new PositionAndDirection(new Position(grid.length - 1, j),
                            Direction.UP),
                    energizedTiles);
            maxEnergized = Math.max(maxEnergized, findNumberOfEnergizedTitles(energizedTiles));
        }
        // check all tiles in the leftmost column (heading right)
        for (int i = 0; i < grid.length; i++) {
            energizedTiles = new boolean[grid.length][grid[0].length];
            simulate(grid, new PositionAndDirection(new Position(i, 0),
                            Direction.RIGHT),
                    energizedTiles);
            maxEnergized = Math.max(maxEnergized, findNumberOfEnergizedTitles(energizedTiles));
        }
        // check all tiles in the rightmost column (heading left)
        for (int i = 0; i < grid.length; i++) {
            energizedTiles = new boolean[grid.length][grid[0].length];
            simulate(grid, new PositionAndDirection(new Position(i, grid[0].length - 1),
                            Direction.LEFT),
                    energizedTiles);
            maxEnergized = Math.max(maxEnergized, findNumberOfEnergizedTitles(energizedTiles));
        }
        return maxEnergized;
    }


    record Position(int x, int y) {

    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    record PositionAndDirection(Position position, Direction direction) {

    }
}
