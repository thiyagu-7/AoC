package com.thiyagu_7.adventofcode.year2024.day20;

import com.thiyagu_7.adventofcode.util.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class SolutionDay20 {
    private static final int[] X_COOR = new int[]{-1, 1, 0, 0};
    private static final int[] Y_COOR = new int[]{0, 0, -1, 1};

    public Result part1(List<String> input) {
        char[][] grid = parseInput(input);
        int m = grid.length;
        int n = grid[0].length;
        Position start = null, end = null;
        int[][] time = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'S') {
                    start = new Position(i, j);
                } else if (grid[i][j] == 'E') {
                    end = new Position(i, j);
                }
                time[i][j] = Integer.MAX_VALUE;
            }
        }
        time[start.x()][start.y()] = 0;

        /*
        dfs(grid, start, 0, time, new HashSet<>());
        int originalTime = time[end.x()][end.y()];
        */

        int originalTime = bfs(grid, start, end);

        int cheatsSavingAtleast100Picoseconds = 0;

        //skip first, last row and col
        Map<Integer, Integer> savedPicoSecondsToNumberOfCheats = new HashMap<>();
        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (grid[i][j] == '#') {
                    grid[i][j] = '.';
                    initializeTime(time, m, n, start);
                   /*
                    dfs(grid, start, 0, time, new HashSet<>());
                    int newTime = time[end.x()][end.y()];
                    */
                    int newTime = bfs(grid, start, end);
                    if (originalTime > newTime) {
                        savedPicoSecondsToNumberOfCheats.merge(originalTime - newTime, 1, (o, ignoreMe) -> o + 1);
                    }
                    if (originalTime - newTime >= 100) {
                        cheatsSavingAtleast100Picoseconds++;
                    }
                    grid[i][j] = '#';
                }
            }
        }

        return new Result(savedPicoSecondsToNumberOfCheats, cheatsSavingAtleast100Picoseconds);
    }

    record Result(Map<Integer, Integer> savedPicoSecondsToNumberOfCheats, int cheatsSavingAtleast100Picoseconds) {

    }


    private void initializeTime(int[][] time, int m, int n, Position start) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                time[i][j] = Integer.MAX_VALUE;
            }
        }
        time[start.x()][start.y()] = 0;
    }

    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    //same as day 18
    private void dfs(char[][] grid, Position current, int currentTime,
                     int[][] time, Set<Position> visited) {
        time[current.x()][current.y()] = currentTime;

        int m = grid.length;
        int n = grid[0].length;

        if (grid[current.x()][current.y()] == 'E') {
            return;
        }

        visited.add(current);

        for (int k = 0; k < 4; k++) {
            int newX = current.x() + X_COOR[k];
            int newY = current.y() + Y_COOR[k];
            Position newPosition = new Position(newX, newY);
            if (isValid(newPosition, m, n)
                    && grid[newX][newY] != '#'
                    && !visited.contains(newPosition)
                    && currentTime + 1 < time[newX][newY]) {
                dfs(grid, newPosition, currentTime + 1, time, visited);
            }
        }
        visited.remove(current);
    }


    private boolean isValid(Position position, int m, int n) {
        return position.x() >= 0 && position.x() < m && position.y() >= 0 && position.y() < n;
    }

    private int bfs(char[][] grid, Position start, Position end) {
        int m = grid.length;
        int n = grid[0].length;

        Queue<PositionAndTime> queue = new LinkedList<>();
        queue.add(new PositionAndTime(start, 0));

        Set<Position> visited = new HashSet<>();
        visited.add(start);

        while (!queue.isEmpty()) {
            PositionAndTime positionAndTime = queue.remove();
            Position current = positionAndTime.position;

            int currentTime = positionAndTime.time;
            for (int k = 0; k < 4; k++) {
                int newX = current.x() + X_COOR[k];
                int newY = current.y() + Y_COOR[k];
                Position newPosition = new Position(newX, newY);
                if (newPosition.equals(end)) {
                    return currentTime + 1;
                }

                if (isValid(newPosition, m, n)
                        && grid[newX][newY] != '#'
                        && !visited.contains(newPosition)) {
                    visited.add(newPosition);
                    queue.add(new PositionAndTime(newPosition, currentTime + 1));
                }
            }
        }
        throw new RuntimeException("Can't reach here");
    }

    record PositionAndTime(Position position, int time) {

    }


    public Result part2(List<String> input) {
        char[][] grid = parseInput(input);
        int m = grid.length;
        int n = grid[0].length;
        Position start = null, end = null;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'S') {
                    start = new Position(i, j);
                } else if (grid[i][j] == 'E') {
                    end = new Position(i, j);
                }
            }
        }

        Map<Position, Integer> distancesToAllPointsFromStart = dijkstra(grid, start);
        distancesToAllPointsFromStart.put(start, 0);

        int originalTime = distancesToAllPointsFromStart.get(end);

        //distance to E from every point - use 'end' as start point and use dijkstra
        Map<Position, Integer> distancesFromAllPointsToEnd = dijkstra(grid, end);
        distancesFromAllPointsToEnd.put(end, 0);

        //find all possible cheats
        List<Cheat> cheats = getAllCheats(grid);

        Map<Integer, Integer> savedPicoSecondsToNumberOfCheats = new HashMap<>();
        int cheatsSavingAtleast100Picoseconds = 0;
        for (Cheat cheat : cheats) {
            //S to cheat.start - cheat.start to cheat.end - cheat.end to E
            int newTime = distancesToAllPointsFromStart.get(cheat.start)
                    + cheat.len
                    + distancesFromAllPointsToEnd.get(cheat.end);
            if (originalTime > newTime) {
                savedPicoSecondsToNumberOfCheats.merge(originalTime - newTime, 1, (o, ignoreMe) -> o + 1);
            }
            if (originalTime - newTime >= 100) {
                cheatsSavingAtleast100Picoseconds++;
            }
        }
        return new Result(savedPicoSecondsToNumberOfCheats, cheatsSavingAtleast100Picoseconds);
    }

    record Cheat(Position start, Position end, int len) {

    }

    private List<Cheat> getAllCheats(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        List<Cheat> cheats = new ArrayList<>();

        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (grid[i][j] == '.' || grid[i][j] == 'S' || grid[i][j]=='E') {
                    for (int k = i; k < m - 1; k++) {
                        for (int l = 1; l < n - 1; l++) {
                            if (i == k && l <= j) {
                                continue;
                            }
                            if (grid[k][l] == '#') {
                                continue;
                            }

                            //Cheat from grid[i][j] to grid[k][l]
                            int distance = Math.abs(i - k) + Math.abs(j - l); //manhattan distance
                            if (distance > 0 && distance <= 20) {
                                if (grid[i][j]!='E' && grid[k][l] != 'S') {
                                    cheats.add(new Cheat(new Position(i, j), new Position(k, l), distance));
                                }
                                if (grid[i][j] != 'S' && grid[k][l] != 'E') {
                                    cheats.add(new Cheat(new Position(k, l), new Position(i, j), distance));
                                }
                            }
                        }
                    }
                }
            }
        }
        return cheats;
    }

    private Map<Position, Integer> dijkstra(char[][] grid, Position start) {
        int m = grid.length;
        int n = grid[0].length;
        Map<Position, Integer> allCosts = new HashMap<>();
        PriorityQueue<PositionAndCost> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(pc -> pc.cost)
        );

        priorityQueue.add(new PositionAndCost(start, 0));

        while (!priorityQueue.isEmpty()){
            PositionAndCost positionAndCost = priorityQueue.poll();
            Position current = positionAndCost.position;

            for (int k = 0; k < 4; k++) {
                int newX = current.x() + X_COOR[k];
                int newY = current.y() + Y_COOR[k];
                Position newPosition = new Position(newX, newY);
                if (isValid(newPosition, m,n)
                        && grid[newPosition.x()][newPosition.y()] != '#'
                        && (!allCosts.containsKey(newPosition) || allCosts.get(newPosition) > positionAndCost.cost + 1)) {
                    allCosts.put(newPosition, positionAndCost.cost + 1);
                    priorityQueue.add(new PositionAndCost(newPosition, positionAndCost.cost + 1));
                }
            }
        }
        return allCosts;
    }

    record PositionAndCost(Position position, int cost) {

    }
}

