package com.thiyagu_7.adventofcode.year2023.day17;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.IntStream;

public class SolutionDay17Old {
    public int part1(List<String> input) {
        //return bfs(parseInput(input));

        Result result = new Result();
        dfs(parseInput(input), new PositionAndDirectionWithMovementCount(
                new Position(0, 0),
                Direction.RIGHT,//dummy values for starting cell
                1
        ), 0, result, new HashSet<>());
        return result.c;
    }

    private int[][] parseInput(List<String> input) {
        return input.stream()
                .map(line -> line.chars()
                        .map(c -> c - '0')
                        .toArray())
                .toArray(int[][]::new);
    }

    private static class Result {
        int c = Integer.MAX_VALUE;
    }

    Map<PositionAndDirection, Integer> positionToSameDirectionCount = new HashMap<>();
    Map<PositionAndDirection, Integer> positionToScore = new HashMap<>();

    private void dfs(int[][] grid,
                     PositionAndDirectionWithMovementCount p,
                     int curr, Result result,
                     Set<Position> visited) {
        int m = grid.length;
        int n = grid[0].length;
        Position currentPosition = p.position;
        Direction currentDirection = p.direction;
        int numTimesMovedInTheSameDirection = p.numTimesMovedInTheSameDirection;

        if (currentPosition.x == m - 1 && currentPosition.y == n - 1) {
            result.c = Math.min(result.c, curr);
            return;
        }
        if (curr >= result.c) {
            return;
        }

        //
        PositionAndDirection pp = new PositionAndDirection(p.position, p.direction);
        if (positionToSameDirectionCount.containsKey(pp)
                && numTimesMovedInTheSameDirection >= positionToSameDirectionCount.get(pp)
                && curr >= positionToScore.get(pp)) {
            return;
        }
        positionToSameDirectionCount.put(pp, numTimesMovedInTheSameDirection);
        positionToScore.put(pp, curr);
        //

        PositionAndDirection newPositionAndDirection;

        visited.add(p.position);
        if (numTimesMovedInTheSameDirection <= 2) {
            //straight in the same direction
            newPositionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.ST);
            if (!visited.contains(newPositionAndDirection.position)
                    && isValid(newPositionAndDirection.position.x, newPositionAndDirection.position.y, m, n)
                       /* &&
                        heatLoss[newPositionAndDirection.position.x][newPositionAndDirection.position.y]
                                > heatLoss[currentPosition.x][currentPosition.y] + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y]*/
            ) {
                dfs(grid,
                        new PositionAndDirectionWithMovementCount(
                                newPositionAndDirection.position, newPositionAndDirection.direction,
                                numTimesMovedInTheSameDirection + 1),
                        curr + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y],
                        result, visited);
            }
        }

        newPositionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.LEFT);
        if (!visited.contains(newPositionAndDirection.position)
                && isValid(newPositionAndDirection.position.x, newPositionAndDirection.position.y, m, n)
                       /* &&
                        heatLoss[newPositionAndDirection.position.x][newPositionAndDirection.position.y]
                                > heatLoss[currentPosition.x][currentPosition.y] + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y]*/
        ) {
            dfs(grid,
                    new PositionAndDirectionWithMovementCount(
                            newPositionAndDirection.position, newPositionAndDirection.direction,
                            1),
                    curr + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y],
                    result, visited);

        }

        newPositionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.RIGHT);
        if (!visited.contains(newPositionAndDirection.position)
                && isValid(newPositionAndDirection.position.x, newPositionAndDirection.position.y, m, n)
                       /* &&
                        heatLoss[newPositionAndDirection.position.x][newPositionAndDirection.position.y]
                                > heatLoss[currentPosition.x][currentPosition.y] + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y]*/
        ) {
            dfs(grid,
                    new PositionAndDirectionWithMovementCount(
                            newPositionAndDirection.position, newPositionAndDirection.direction,
                            1),
                    curr + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y],
                    result, visited);

        }
        visited.remove(p.position);

    }

    private int bfs(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        Queue<PositionAndDirectionWithMovementCount2> queue = new LinkedList<>();
        queue.add(new PositionAndDirectionWithMovementCount2(
                new Position(0, 0),
                Direction.RIGHT, 0,//dummy values for starting cell
                1
        ));
        Set<PositionAndDirection> visited = new HashSet<>();
        int[][] heatLoss = new int[m][n];
        IntStream.range(0, m)
                .forEach(i -> Arrays.fill(heatLoss[i], Integer.MAX_VALUE));
        heatLoss[0][0] = 0;

        int ans = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            var positionAndDirectionWithMovementCount = queue.remove();
            Position currentPosition = positionAndDirectionWithMovementCount.position;
            Direction currentDirection = positionAndDirectionWithMovementCount.direction;
            int numTimesMovedInTheSameDirection = positionAndDirectionWithMovementCount.numTimesMovedInTheSameDirection;

            int c = positionAndDirectionWithMovementCount.c;
            //heatLoss[currentPosition.x][currentPosition.y] = c;
            if (currentPosition.x == m - 1 && currentPosition.y == n - 1) {
                ans = Math.min(ans, c);
            }
            PositionAndDirection newPositionAndDirection;
            if (numTimesMovedInTheSameDirection <= 2) {
                //straight in the same direction
                newPositionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.ST);
                if (!visited.contains(newPositionAndDirection)
                        && isValid(newPositionAndDirection.position.x, newPositionAndDirection.position.y, m, n)
                       /* &&
                        heatLoss[newPositionAndDirection.position.x][newPositionAndDirection.position.y]
                                > heatLoss[currentPosition.x][currentPosition.y] + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y]*/
                ) {
                    queue.add(new PositionAndDirectionWithMovementCount2(
                            newPositionAndDirection.position, newPositionAndDirection.direction,
                            c + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y],
                            numTimesMovedInTheSameDirection + 1));
                    visited.add(newPositionAndDirection);
                }
            }
            //left
            newPositionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.LEFT);
            if (!visited.contains(newPositionAndDirection)
                    && isValid(newPositionAndDirection.position.x, newPositionAndDirection.position.y, m, n)
                       /* &&
                        heatLoss[newPositionAndDirection.position.x][newPositionAndDirection.position.y]
                                > heatLoss[currentPosition.x][currentPosition.y] + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y]*/
            ) {
                queue.add(new PositionAndDirectionWithMovementCount2(
                        newPositionAndDirection.position, newPositionAndDirection.direction,
                        c + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y],
                        1));
                visited.add(newPositionAndDirection);
            }
            //right
            newPositionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.RIGHT);
            if (!visited.contains(newPositionAndDirection)
                    && isValid(newPositionAndDirection.position.x, newPositionAndDirection.position.y, m, n)
                       /* &&
                        heatLoss[newPositionAndDirection.position.x][newPositionAndDirection.position.y]
                                > heatLoss[currentPosition.x][currentPosition.y] + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y]*/
            ) {
                queue.add(new PositionAndDirectionWithMovementCount2(
                        newPositionAndDirection.position, newPositionAndDirection.direction,
                        c + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y],
                        1));
                visited.add(newPositionAndDirection);
            }
        }
        //  IntStream.range(0, m)
        //        .forEach(i -> System.out.println(Arrays.toString(heatLoss[i])));
        return ans;
    }

    private boolean isValid(int x, int y, int m, int n) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    private PositionAndDirection moveInDirection(Position position, Direction currentDirection,
                                                 Direction newDirection) {
        return switch (currentDirection) {
            case UP -> switch (newDirection) {
                case LEFT -> new PositionAndDirection(
                        new Position(position.x, position.y - 1),
                        Direction.LEFT);
                case RIGHT -> new PositionAndDirection(
                        new Position(position.x, position.y + 1),
                        Direction.RIGHT);
                case ST -> new PositionAndDirection(
                        new Position(position.x - 1, position.y),
                        Direction.UP);
                default -> throw new IllegalStateException("Invalid value: " + newDirection); //cannot move up/down
            };
            case DOWN -> switch (newDirection) {
                case LEFT -> new PositionAndDirection(
                        new Position(position.x, position.y + 1),
                        Direction.RIGHT);
                case RIGHT -> new PositionAndDirection(
                        new Position(position.x, position.y - 1),
                        Direction.LEFT);
                case ST -> new PositionAndDirection(
                        new Position(position.x + 1, position.y),
                        Direction.DOWN);
                default -> throw new IllegalStateException("Invalid value: " + newDirection); //cannot move up/down
            };

            case LEFT -> switch (newDirection) {
                case LEFT -> new PositionAndDirection(
                        new Position(position.x + 1, position.y),
                        Direction.DOWN);
                case RIGHT -> new PositionAndDirection(
                        new Position(position.x - 1, position.y),
                        Direction.UP);
                case ST -> new PositionAndDirection(
                        new Position(position.x, position.y - 1),
                        Direction.LEFT);
                default -> throw new IllegalStateException("Invalid value: " + newDirection); //cannot move up/down
            };
            case RIGHT -> switch (newDirection) {
                case LEFT -> new PositionAndDirection(
                        new Position(position.x - 1, position.y),
                        Direction.UP);
                case RIGHT -> new PositionAndDirection(
                        new Position(position.x + 1, position.y),
                        Direction.DOWN);
                case ST -> new PositionAndDirection(
                        new Position(position.x, position.y + 1),
                        Direction.RIGHT);
                default -> throw new IllegalStateException("Invalid value: " + newDirection); //cannot move up/down
            };
            case ST -> throw new RuntimeException();
        };
    }

    public int part2(List<String> input) {
        Result result = new Result();
        dfs2(parseInput(input), new PositionAndDirectionWithMovementCount(
                new Position(0, 0),
                Direction.RIGHT,//dummy values for starting cell
                1
        ), 0, result, new HashSet<>());
        return result.c;
    }

    record Position(int x, int y) {

    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        ST;
    }

    // reached 'position' by moving in direction 'direction'
    record PositionAndDirection(Position position, Direction direction) {

    }

    record PositionAndDirectionWithMovementCount(Position position, Direction direction,
                                                 int numTimesMovedInTheSameDirection) {

    }

    record PositionAndDirectionWithMovementCount2(Position position, Direction direction,
                                                  int c,
                                                  int numTimesMovedInTheSameDirection) {

    }

    Map<PositionAndDirection, Integer> positionToSameDirectionCount1 = new HashMap<>();

    private void dfs2(int[][] grid,
                      PositionAndDirectionWithMovementCount p,
                      int curr, Result result,
                      Set<Position> visited) {
        int m = grid.length;
        int n = grid[0].length;
        Position currentPosition = p.position;
        Direction currentDirection = p.direction;
        int numTimesMovedInTheSameDirection = p.numTimesMovedInTheSameDirection;


        if (currentPosition.x == m - 1 && currentPosition.y == n - 1
                && numTimesMovedInTheSameDirection >= 4) {
            result.c = Math.min(result.c, curr);
            System.out.println(result.c);
            return;
        }
        //
        if (currentPosition.x == m-4 && currentPosition.y == n-1
                && numTimesMovedInTheSameDirection <=7
                && currentDirection ==Direction.DOWN) {
            //move straight
            curr += grid[m-3][currentPosition.y];
            curr += grid[m-2][currentPosition.y];
            curr += grid[m-1][currentPosition.y];
            if (curr < result.c) {
                System.out.println(curr);
            }
            result.c = Math.min(result.c, curr);
            return;
        }
        if (currentPosition.x == m-1 && currentPosition.y == n-4
                && numTimesMovedInTheSameDirection <=7
                && currentDirection ==Direction.RIGHT) {
            //move straight
            curr += grid[currentPosition.x][n-3];
            curr += grid[currentPosition.x][n-2];
            curr += grid[currentPosition.x][n-1];
            if (curr < result.c) {
                System.out.println(curr);
            }
            result.c = Math.min(result.c, curr);
            return;
        }
        //

        // TODO - refix
        /*if (numTimesMovedInTheSameDirection < 4 && (
                currentDirection == Direction.RIGHT)) {
            int d = 4 - numTimesMovedInTheSameDirection;

        }*/
        //
        if (curr >= result.c) {
            return;
        }

        PositionAndDirection pp = new PositionAndDirection(p.position, p.direction);
        if (positionToSameDirectionCount.containsKey(pp) && result.c != Integer.MAX_VALUE) {
            if (positionToSameDirectionCount.get(pp) >= 4) { //last time turned
                if (numTimesMovedInTheSameDirection >= positionToSameDirectionCount.get(pp)
                        && curr >= positionToScore.get(pp)) {
                    return;
                }
            } else {
                if (numTimesMovedInTheSameDirection == positionToSameDirectionCount.get(pp)
                        && curr >= positionToScore.get(pp)) {
                    return;
                }
            }
        }
        positionToSameDirectionCount.put(pp, numTimesMovedInTheSameDirection);
        positionToScore.put(pp, curr);
        //

        PositionAndDirection newPositionAndDirection;

        visited.add(p.position);
        if (numTimesMovedInTheSameDirection < 10) {
            //straight in the same direction
            newPositionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.ST);
            if (!visited.contains(newPositionAndDirection.position)
                    && isValid(newPositionAndDirection.position.x, newPositionAndDirection.position.y, m, n)
                       /* &&
                        heatLoss[newPositionAndDirection.position.x][newPositionAndDirection.position.y]
                                > heatLoss[currentPosition.x][currentPosition.y] + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y]*/
            ) {
                dfs2(grid,
                        new PositionAndDirectionWithMovementCount(
                                newPositionAndDirection.position, newPositionAndDirection.direction,
                                numTimesMovedInTheSameDirection + 1),
                        curr + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y],
                        result, visited);
            }
        }

        if (numTimesMovedInTheSameDirection >= 4) {
            newPositionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.LEFT);
            if (!visited.contains(newPositionAndDirection.position)
                    && isValid(newPositionAndDirection.position.x, newPositionAndDirection.position.y, m, n)
                       /* &&
                        heatLoss[newPositionAndDirection.position.x][newPositionAndDirection.position.y]
                                > heatLoss[currentPosition.x][currentPosition.y] + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y]*/
            ) {
                dfs2(grid,
                        new PositionAndDirectionWithMovementCount(
                                newPositionAndDirection.position, newPositionAndDirection.direction,
                                1),
                        curr + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y],
                        result, visited);

            }

            newPositionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.RIGHT);
            if (!visited.contains(newPositionAndDirection.position)
                    && isValid(newPositionAndDirection.position.x, newPositionAndDirection.position.y, m, n)
                       /* &&
                        heatLoss[newPositionAndDirection.position.x][newPositionAndDirection.position.y]
                                > heatLoss[currentPosition.x][currentPosition.y] + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y]*/
            ) {
                dfs2(grid,
                        new PositionAndDirectionWithMovementCount(
                                newPositionAndDirection.position, newPositionAndDirection.direction,
                                1),
                        curr + grid[newPositionAndDirection.position.x][newPositionAndDirection.position.y],
                        result, visited);

            }
        }
        visited.remove(p.position);

    }

}
