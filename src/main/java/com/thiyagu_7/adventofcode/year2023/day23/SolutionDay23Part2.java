package com.thiyagu_7.adventofcode.year2023.day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolutionDay23Part2 {
    private static final int[] X_COOR = new int[]{-1, 1, 0, 0};
    private static final int[] Y_COOR = new int[]{0, 0, -1, 1};

    public int part2(List<String> input) {
        char[][] grid = parseGrid(input);
        AdjListAndIntersection adjListAndIntersection = buildAdjList(grid);
        Map<Position, List<PositionWithCost>> adjListWithCost =
                edgeContract(adjListAndIntersection, new Position(0, 1));
        Result result = new Result();
        dfs(adjListWithCost, new Position(0, 1), new HashSet<>(), 0,
                new Position(grid.length - 1, grid[0].length - 2),
                result);
        return result.steps;
    }

    private char[][] parseGrid(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private AdjListAndIntersection buildAdjList(char[][] grid) {
        Map<Position, List<Position>> adjList = new HashMap<>();
        List<Position> intersections = new ArrayList<>();
        int m = grid.length;
        int n = grid[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '#') {
                    continue;
                }
                Position currentPosition = new Position(i, j);
                for (int k = 0; k < 4; k++) {
                    Position newPosition = new Position(
                            i + X_COOR[k],
                            j + Y_COOR[k]
                    );
                    if (isValid(newPosition, m, n) && grid[newPosition.x][newPosition.y] != '#') {
                        adjList.computeIfAbsent(currentPosition, p -> new ArrayList<>())
                                .add(newPosition);
                    }
                }
                if (adjList.get(currentPosition).size() > 2) {
                    intersections.add(currentPosition);
                }
            }
        }
        return new AdjListAndIntersection(adjList, intersections);
    }

    private boolean isValid(Position position, int m, int n) {
        return position.x >= 0 && position.x < m && position.y >= 0 && position.y < n;
    }

    private Map<Position, List<PositionWithCost>> edgeContract(AdjListAndIntersection adjListAndIntersection,
                                                               Position start) {
        Map<Position, List<Position>> adjList = adjListAndIntersection.adjList;
        List<Position> intersections = adjListAndIntersection.intersections;

        Map<Position, List<PositionWithCost>> adjListWithCost = new HashMap<>();

        for (Position intersection : intersections) {
            List<Position> adjPositions = adjList.get(intersection);
            for (Position adjPosition : adjPositions) {
                Position current = adjPosition;
                Position previous = intersection;
                int cost = 1;
                while (adjList.get(current).size() == 2) {
                    Position tmp = current;
                    current = adjList.get(current).get(0).equals(previous)
                            ? adjList.get(current).get(1) : adjList.get(current).get(0);
                    previous = tmp;
                    cost++;
                }
                adjListWithCost.computeIfAbsent(intersection, p -> new ArrayList<>())
                        .add(new PositionWithCost(current, cost)); //current is an intersection or start or end

                if (current.equals(start)) { //add in other direction
                    adjListWithCost.computeIfAbsent(current, p -> new ArrayList<>())
                            .add(new PositionWithCost(intersection, cost));
                }
            }
        }
        return adjListWithCost;
    }

    private void dfs(Map<Position, List<PositionWithCost>> adjListWithCost,
                     Position currentPosition,
                     Set<Position> path,
                     int cost,
                     Position end,
                     Result result) {
        if (currentPosition.equals(end)) {
            result.steps = Math.max(result.steps, cost);
            return;
        }
        path.add(currentPosition);
        for (PositionWithCost positionWithCost : adjListWithCost.get(currentPosition)) {
            if (!path.contains(positionWithCost.destination)) {
                dfs(adjListWithCost, positionWithCost.destination, path, cost + positionWithCost.cost,
                        end, result);
            }
        }
        path.remove(currentPosition);

    }

    record Position(int x, int y) {

    }

    record AdjListAndIntersection(Map<Position, List<Position>> adjList, List<Position> intersections) {
    }

    record PositionWithCost(Position destination, int cost) {

    }

    private static class Result {
        int steps = 0;
    }
}
