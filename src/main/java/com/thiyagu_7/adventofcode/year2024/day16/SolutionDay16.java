package com.thiyagu_7.adventofcode.year2024.day16;

import com.thiyagu_7.adventofcode.util.Position;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SolutionDay16 {
    private static final int[] X_COOR = new int[]{-1, 1, 0, 0};
    private static final int[] Y_COOR = new int[]{0, 0, -1, 1};
    private static final Direction[] DIRECTIONS = new Direction[]{Direction.UP, Direction.DOWN,
            Direction.LEFT, Direction.RIGHT};

    public int part1(List<String> input) {
        char[][] grid = parseGrid(input);
        int m = grid.length;
        int n = grid[0].length;
        int[][] scores = new int[m][n];
        Position start = null;
        Position end = null;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'S') {
                    start = new Position(i, j);
                } else if (grid[i][j] == 'E') {
                    end = new Position(i, j);
                }
                if (grid[i][j] != '#') {
                    scores[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        dfs(grid, start, 0, Direction.RIGHT, scores, new HashSet<>());
        return scores[end.x()][end.y()];
    }

    private char[][] parseGrid(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private void dfs(char[][] grid, Position current, int score, Direction currentDirection,
                     int[][] scores, Set<Position> visited) {
        scores[current.x()][current.y()] = score;

        if (grid[current.x()][current.y()] == 'E') {
            return;
        }
        visited.add(current);

        int m = grid.length;
        int n = grid[0].length;

        for (int k = 0; k < 4; k++) {
            int newX = current.x() + X_COOR[k];
            int newY = current.y() + Y_COOR[k];
            Direction newDirection = DIRECTIONS[k];
            Position newPosition = new Position(newX, newY);
            int additionalScore = 1 + (newDirection != currentDirection ? 1000 : 0);

            if (isValid(newPosition, m, n)
                    && grid[newX][newY] != '#'
                    && !visited.contains(newPosition)
                    && score + additionalScore <= scores[newX][newY]) {
                dfs(grid, newPosition, score + additionalScore, newDirection, scores, visited);
            }
        }
        visited.remove(current);
    }

    private boolean isValid(Position position, int m, int n) {
        return position.x() >= 0 && position.x() < m && position.y() >= 0 && position.y() < n;
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    public int part2(List<String> input) {
        char[][] grid = parseGrid(input);
        int m = grid.length;
        int n = grid[0].length;
        Position start = null;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'S') {
                    start = new Position(i, j);
                }
            }
        }

        MinScore minScore = new MinScore();
        //maps score to all positions on the path to reach that score (across multiple paths)
        Map<Integer, Set<Position>> scoreToPositions = new TreeMap<>();

        dfsPart2(grid, start, 0, Direction.RIGHT, new HashMap<>(), minScore, scoreToPositions, new HashSet<>());

        return scoreToPositions.values().stream().findFirst().get()
                .size() + 1; //adding start position
    }

    record PositionAndDirection(Position position, Direction direction) {

    }

    private static class MinScore {
        int minScore = Integer.MAX_VALUE;
    }

    private void dfsPart2(char[][] grid, Position current, int score, Direction currentDirection,
                          Map<PositionAndDirection, Integer> scores,
                          MinScore minScore,
                          Map<Integer, Set<Position>> scoreToPositions,
                          Set<Position> visited) {
        scores.put(new PositionAndDirection(current, currentDirection), score);

        if (grid[current.x()][current.y()] == 'E') {
            minScore.minScore = score;
            scoreToPositions.computeIfAbsent(score, ig -> new HashSet<>())
                    .addAll(visited);
            return;
        }

        //This optimization saves ~8 sec
        if (score > minScore.minScore) {
            return;
        }

        visited.add(current);

        int m = grid.length;
        int n = grid[0].length;

        for (int k = 0; k < 4; k++) {
            int newX = current.x() + X_COOR[k];
            int newY = current.y() + Y_COOR[k];
            Direction newDirection = DIRECTIONS[k];
            Position newPosition = new Position(newX, newY);
            PositionAndDirection newPositionAndDirection = new PositionAndDirection(newPosition, newDirection);
            int additionalScore = 1 + (newDirection != currentDirection ? 1000 : 0);

            if (isValid(newPosition, m, n)
                    && grid[newX][newY] != '#'
                    && !visited.contains(newPosition)
                    && (!scores.containsKey(newPositionAndDirection) || score + additionalScore <= scores.get(newPositionAndDirection))) {
                dfsPart2(grid, newPosition, score + additionalScore, newDirection, scores,
                        minScore, scoreToPositions, visited);
            }
        }
        visited.remove(current);
    }
}
