package com.thiyagu_7.adventofcode.year2023.day17;

import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class SolutionDay17 {
    public int part1(List<String> input) {
        return dijkstra(parseInput(input));
    }

    private int dijkstra(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        Map<PositionAndDirectionAndTimes, Integer> costMap = new HashMap<>();
        costMap.put(new PositionAndDirectionAndTimes(new Position(0, 0),
                Direction.RIGHT, 1), 0);
        costMap.put(new PositionAndDirectionAndTimes(new Position(0, 0),
                Direction.DOWN, 1), 0);


        PriorityQueue<State> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(State::cost));
        priorityQueue.add(new State(new Position(0, 0), 0,
                Direction.RIGHT, 1));
        priorityQueue.add(new State(new Position(0, 0), 0, Direction.DOWN, 1));

        while (!priorityQueue.isEmpty()) {
            State state = priorityQueue.poll();
            Position currentPosition = state.position;
            Direction currentDirection = state.direction;
            int cost = state.cost;
            int numTimesMovedInTheSameDirection = state.numTimesMovedInTheSameDirection;

            PositionAndDirection positionAndDirection;
            Position newPosition;
            Direction newDirection;
            int newCost;
            State newState;
            PositionAndDirectionAndTimes positionAndDirectionAndTimes;
            //todo
            PositionAndDirectionAndTimes tmp = new PositionAndDirectionAndTimes(currentPosition, currentDirection, numTimesMovedInTheSameDirection);

            //move straight
            if (numTimesMovedInTheSameDirection <= 2) {
                positionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.SAME_DIR);
                newPosition = positionAndDirection.position;
                newDirection = positionAndDirection.direction;
                positionAndDirectionAndTimes = new PositionAndDirectionAndTimes(newPosition,
                        newDirection, numTimesMovedInTheSameDirection + 1);

                if (isValid(newPosition, m, n)) {
                    //see costMap#get OR state.cost
                    //newCost = cost + grid[newPosition.x][newPosition.y];
                    newCost = costMap.get(tmp) + grid[newPosition.x][newPosition.y];
                    if (!costMap.containsKey(positionAndDirectionAndTimes)
                            || costMap.get(positionAndDirectionAndTimes) > newCost) {
                        newState = new State(newPosition,
                                newCost, currentDirection,
                                numTimesMovedInTheSameDirection + 1);
                        costMap.put(positionAndDirectionAndTimes, newCost);
                        priorityQueue.add(newState);
                    }
                }
            }
            //move left
            positionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.LEFT);
            newPosition = positionAndDirection.position;
            newDirection = positionAndDirection.direction;
            positionAndDirectionAndTimes = new PositionAndDirectionAndTimes(newPosition,
                    newDirection, 1);

            if (isValid(newPosition, m, n)) {
                //see costMap#get OR state.cost
                //newCost = cost + grid[newPosition.x][newPosition.y];
                newCost = costMap.get(tmp) + grid[newPosition.x][newPosition.y];
                if (!costMap.containsKey(positionAndDirectionAndTimes)
                        || costMap.get(positionAndDirectionAndTimes) > newCost) {
                    newState = new State(newPosition,
                            newCost, newDirection,
                            1);
                    costMap.put(positionAndDirectionAndTimes, newCost);
                    priorityQueue.add(newState);
                }
            }
            //move right
            positionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.RIGHT);
            newPosition = positionAndDirection.position;
            newDirection = positionAndDirection.direction;
            positionAndDirectionAndTimes = new PositionAndDirectionAndTimes(newPosition,
                    newDirection, 1);

            if (isValid(newPosition, m, n)) {
                //see costMap#get OR state.cost
                //newCost = cost + grid[newPosition.x][newPosition.y];
                newCost = costMap.get(tmp) + grid[newPosition.x][newPosition.y];
                if (!costMap.containsKey(positionAndDirectionAndTimes)
                        || costMap.get(positionAndDirectionAndTimes) > newCost) {
                    newState = new State(newPosition,
                            newCost, newDirection,
                            1);
                    costMap.put(positionAndDirectionAndTimes, newCost);
                    priorityQueue.add(newState);
                }
            }
        }
        Position end = new Position(m - 1, n - 1);
        return costMap.entrySet()
                .stream()
                .filter(e -> e.getKey().position.equals(end))
                .map(Map.Entry::getValue)
                .min(Comparator.naturalOrder())
                .orElse(0);

    }

    private boolean isValid(Position position, int m, int n) {
        return position.x >= 0 && position.x < m && position.y >= 0 && position.y < n;
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
                case SAME_DIR -> new PositionAndDirection(
                        new Position(position.x - 1, position.y),
                        Direction.UP);
                default -> throw new IllegalStateException("Invalid value: " + newDirection);
            };
            case DOWN -> switch (newDirection) {
                case LEFT -> new PositionAndDirection(
                        new Position(position.x, position.y + 1),
                        Direction.RIGHT);
                case RIGHT -> new PositionAndDirection(
                        new Position(position.x, position.y - 1),
                        Direction.LEFT);
                case SAME_DIR -> new PositionAndDirection(
                        new Position(position.x + 1, position.y),
                        Direction.DOWN);
                default -> throw new IllegalStateException("Invalid value: " + newDirection);
            };

            case LEFT -> switch (newDirection) {
                case LEFT -> new PositionAndDirection(
                        new Position(position.x + 1, position.y),
                        Direction.DOWN);
                case RIGHT -> new PositionAndDirection(
                        new Position(position.x - 1, position.y),
                        Direction.UP);
                case SAME_DIR -> new PositionAndDirection(
                        new Position(position.x, position.y - 1),
                        Direction.LEFT);
                default -> throw new IllegalStateException("Invalid value: " + newDirection);
            };
            case RIGHT -> switch (newDirection) {
                case LEFT -> new PositionAndDirection(
                        new Position(position.x - 1, position.y),
                        Direction.UP);
                case RIGHT -> new PositionAndDirection(
                        new Position(position.x + 1, position.y),
                        Direction.DOWN);
                case SAME_DIR -> new PositionAndDirection(
                        new Position(position.x, position.y + 1),
                        Direction.RIGHT);
                default -> throw new IllegalStateException("Invalid value: " + newDirection);
            };
            case SAME_DIR -> throw new RuntimeException();
        };
    }

    public int part2(List<String> input) {
        return dijkstra2(parseInput(input));
    }

    private int dijkstra2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        Map<PositionAndDirectionAndTimes, Integer> costMap = new HashMap<>();
        costMap.put(new PositionAndDirectionAndTimes(new Position(0, 0),
                Direction.RIGHT, 1), 0);
        costMap.put(new PositionAndDirectionAndTimes(new Position(0, 0),
                Direction.DOWN, 1), 0);


        PriorityQueue<State> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(State::cost));
        priorityQueue.add(new State(new Position(0, 0), 0,
                Direction.RIGHT, 1));
        priorityQueue.add(new State(new Position(0, 0), 0, Direction.DOWN, 1));

        while (!priorityQueue.isEmpty()) {
            State state = priorityQueue.poll();
            Position currentPosition = state.position;
            Direction currentDirection = state.direction;
            int cost = state.cost;
            int numTimesMovedInTheSameDirection = state.numTimesMovedInTheSameDirection;

            PositionAndDirection positionAndDirection;
            Position newPosition;
            Direction newDirection;
            int newCost;
            State newState;
            PositionAndDirectionAndTimes positionAndDirectionAndTimes;
            //todo
            PositionAndDirectionAndTimes tmp = new PositionAndDirectionAndTimes(currentPosition, currentDirection,numTimesMovedInTheSameDirection);
            //move straight
            if (numTimesMovedInTheSameDirection <= 9) {//2 in part-1
                positionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.SAME_DIR);
                newPosition = positionAndDirection.position;
                newDirection = positionAndDirection.direction;
                positionAndDirectionAndTimes = new PositionAndDirectionAndTimes(newPosition,
                        newDirection, numTimesMovedInTheSameDirection + 1);

                //new
                if (newPosition.x == m - 1 && newPosition.y == n - 1) {
                    if (numTimesMovedInTheSameDirection + 1 < 4) {
                        continue;
                    }
                }
                //new
                if (isValid(newPosition, m, n)) {
                    //see costMap#get OR state.cost
                    //newCost = cost + grid[newPosition.x][newPosition.y];
                    newCost = costMap.get(tmp) + grid[newPosition.x][newPosition.y];
                    if (!costMap.containsKey(positionAndDirectionAndTimes)
                            || costMap.get(positionAndDirectionAndTimes) > newCost) {
                        newState = new State(newPosition,
                                newCost, currentDirection,
                                numTimesMovedInTheSameDirection + 1);
                        costMap.put(positionAndDirectionAndTimes, newCost);
                        priorityQueue.add(newState);
                    }
                }
            }
            if (numTimesMovedInTheSameDirection >= 4) {//extra
                //move left
                positionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.LEFT);
                newPosition = positionAndDirection.position;
                newDirection = positionAndDirection.direction;
                positionAndDirectionAndTimes = new PositionAndDirectionAndTimes(newPosition,
                        newDirection, 1);

                //new
                if (newPosition.x == m - 1 && newPosition.y == n - 1) {
                    // continue; //not needed as we cannot end in bottom last by turning left

                }
                //new
                if (isValid(newPosition, m, n)) {
                    //see costMap#get OR state.cost
                    //newCost = cost + grid[newPosition.x][newPosition.y];
                    newCost = costMap.get(tmp) + grid[newPosition.x][newPosition.y];
                    if (!costMap.containsKey(positionAndDirectionAndTimes)
                            || costMap.get(positionAndDirectionAndTimes) > newCost) {
                        newState = new State(newPosition,
                                newCost, newDirection,
                                1);
                        costMap.put(positionAndDirectionAndTimes, newCost);
                        priorityQueue.add(newState);
                    }
                }
                //move right
                positionAndDirection = moveInDirection(currentPosition, currentDirection, Direction.RIGHT);
                newPosition = positionAndDirection.position;
                newDirection = positionAndDirection.direction;
                positionAndDirectionAndTimes = new PositionAndDirectionAndTimes(newPosition,
                        newDirection, 1);

                //new
                if (newPosition.x == m - 1 && newPosition.y == n - 1) {
                    continue;
                }
                //new
                if (isValid(newPosition, m, n)) {
                    //see costMap#get OR state.cost
                    //newCost = cost + grid[newPosition.x][newPosition.y];
                    newCost = costMap.get(tmp) + grid[newPosition.x][newPosition.y];
                    if (!costMap.containsKey(positionAndDirectionAndTimes)
                            || costMap.get(positionAndDirectionAndTimes) > newCost) {
                        newState = new State(newPosition,
                                newCost, newDirection,
                                1);
                        costMap.put(positionAndDirectionAndTimes, newCost);
                        priorityQueue.add(newState);
                    }
                }
            }
        }
        Position end = new Position(m - 1, n - 1);
        return costMap.entrySet()
                .stream()
                .filter(e -> e.getKey().position.equals(end))
                .map(Map.Entry::getValue)
                .min(Comparator.naturalOrder())
                .orElse(0);

    }


    private int[][] parseInput(List<String> input) {
        return input.stream()
                .map(line -> line.chars()
                        .map(c -> c - '0')
                        .toArray())
                .toArray(int[][]::new);
    }

    record State(Position position, int cost, Direction direction, int numTimesMovedInTheSameDirection) {

    }

    record Position(int x, int y) {

    }

    // reached 'position' by moving in direction 'direction'
    record PositionAndDirection(Position position, Direction direction) {

    }

    record PositionAndDirectionAndTimes(Position position, Direction direction, int numTimesMovedInTheSameDirection) {

    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        SAME_DIR
    }

    public static void main(String[] args) {
        PriorityQueue<PS> pq = new PriorityQueue<>(Comparator.comparingInt(PS::getX));
        pq.add(new PS(2, 10));
        PS p = new PS(5, 20);
        pq.add(p);
        System.out.println(pq);
        p.x=1;
        System.out.println(pq);
    }
    @Getter
    @ToString
    private static class PS {
        private int x;
        private int y;

        private PS(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
}
