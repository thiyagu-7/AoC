package com.thiyagu_7.adventofcode.year2022.day24;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionDay24 {
    private static final int[] X_COOR = new int[]{1, 0, -1, 0, 0};
    private static final int[] Y_COOR = new int[]{0, 1, 0, -1, 0};
    private static final Map<Character, PositionCalculatorFunc> POSITION_CALC_FUNCTIONS;

    static {
        POSITION_CALC_FUNCTIONS = new HashMap<>();
        POSITION_CALC_FUNCTIONS.put('>',
                new PositionCalculatorFunc(
                        (p, m, n) -> new Position(p.x, p.y + 1),
                        (p, m, n) -> new Position(p.x, 1)
                ));
        POSITION_CALC_FUNCTIONS.put('<',
                new PositionCalculatorFunc(
                        (p, m, n) -> new Position(p.x, p.y - 1),
                        (p, m, n) -> new Position(p.x, n - 2)
                ));
        POSITION_CALC_FUNCTIONS.put('^',
                new PositionCalculatorFunc(
                        (p, m, n) -> new Position(p.x - 1, p.y),
                        (p, m, n) -> new Position(m - 2, p.y)
                ));
        POSITION_CALC_FUNCTIONS.put('v',
                new PositionCalculatorFunc(
                        (p, m, n) -> new Position(p.x + 1, p.y),
                        (p, m, n) -> new Position(1, p.y)
                ));
    }

    record PositionCalculatorFunc(TriFunction<Position, Integer, Integer, Position> defaultNextFunc,
                                  TriFunction<Position, Integer, Integer, Position> endOfGridNextFunc) {
    }

    @FunctionalInterface
    public interface TriFunction<T, U, R, V> {

        V apply(T t, U u, R r);
    }

    public int part1(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        Set<BlizzardWithPosition> initialPositions = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != '.' && grid[i][j] != '#') {
                    initialPositions.add(new BlizzardWithPosition(grid[i][j], i, j));
                }
            }
        }
        List<Set<BlizzardWithPosition>> blizzardPositions = computeBlizzardPositions(m, n,
                initialPositions);
        return bfs(blizzardPositions, m, n,
                0, 1,
                m - 1, n - 2,
                0);
    }

    private List<Set<BlizzardWithPosition>> computeBlizzardPositions(int m,
                                                                     int n, Set<BlizzardWithPosition> initialPositions) {
        List<Set<BlizzardWithPosition>> blizzardPositions = new ArrayList<>();
        Set<BlizzardWithPosition> currentPositions = initialPositions;
        blizzardPositions.add(currentPositions);
        while (true) {
            Set<BlizzardWithPosition> nextPositions = new HashSet<>();
            for (BlizzardWithPosition blizzardWithPosition : currentPositions) {
                BlizzardWithPosition nextPosition = getNextPosition(m, n, blizzardWithPosition);
                nextPositions.add(nextPosition);
            }
            if (blizzardPositions.stream()
                    .anyMatch(e -> e.equals(nextPositions))) { // same grid configuration
                return blizzardPositions;
            }
            blizzardPositions.add(nextPositions);
            currentPositions = nextPositions;
        }
    }

    private BlizzardWithPosition getNextPosition(int m, int n, BlizzardWithPosition blizzardWithPosition) {
        char blizzard = blizzardWithPosition.blizzard();
        int x = blizzardWithPosition.x();
        int y = blizzardWithPosition.y();

        Position currentPosition = new Position(x, y);
        PositionCalculatorFunc positionCalculatorFunc = POSITION_CALC_FUNCTIONS.get(blizzard);
        Position newPosition = positionCalculatorFunc.defaultNextFunc()
                .apply(currentPosition, m, n);
        if (isValidPositionForBlizzard(newPosition.x(), newPosition.y(), m, n)) {
            return new BlizzardWithPosition(blizzard, newPosition.x(), newPosition.y());
        } else {
            newPosition = positionCalculatorFunc.endOfGridNextFunc()
                    .apply(currentPosition, m, n);
            return new BlizzardWithPosition(blizzard, newPosition.x(), newPosition.y());
        }
    }

    private boolean isValidPositionForBlizzard(int x, int y, int m, int n) {
        return x >= 1 && x < m - 1 && y >= 1 && y < n - 1;
    }

    private boolean isValidPositionForMe(int x, int y, int m, int n) {
        return isValidPositionForBlizzard(x, y, m, n)
                || (x == 0 && y == 1)
                || (x == m - 1 && y == n - 2);
    }


    private int bfs(List<Set<BlizzardWithPosition>> blizzardPositions,
                    int m, int n, int x, int y, int targetX, int targetY,
                    int currentMinute) {
        Map<Integer, Set<Position>> minToSetOfBlizzardPositions = IntStream.range(0, blizzardPositions.size())
                .boxed()
                .collect(Collectors.toMap(e -> e, e -> blizzardPositions.get(e).stream()
                        .map(b -> new Position(b.x(), b.y()))
                        .collect(Collectors.toSet())));

        int size = blizzardPositions.size();
        Queue<BFSState> queue = new LinkedList<>();
        queue.add(new BFSState(x, y, currentMinute));
        Set<BFSState> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            BFSState current = queue.poll();
            int minute = current.minute();
            Set<Position> nextPositions = minToSetOfBlizzardPositions.get((minute + 1) % size);

            for (int k = 0; k < 5; k++) {
                int newX = current.x() + X_COOR[k];
                int newY = current.y() + Y_COOR[k];
                if (newX == targetX && newY == targetY) {
                    return minute + 1;
                }
                BFSState bfsState = new BFSState(newX, newY, minute + 1);
                if (isValidPositionForMe(newX, newY, m, n)) {
                    if (noBlizzardClash(nextPositions, newX, newY)
                            && !visited.contains(bfsState)) {
                        queue.add(bfsState);
                        visited.add(bfsState);
                    }
                }
            }
        }
        throw new RuntimeException();
    }

    private boolean noBlizzardClash(Set<Position> nextPositions, int newX, int newY) {
        return !nextPositions.contains(new Position(newX, newY));
    }

    public int part2(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        Set<BlizzardWithPosition> initialPositions = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != '.' && grid[i][j] != '#') {
                    initialPositions.add(new BlizzardWithPosition(grid[i][j], i, j));
                }
            }
        }
        List<Set<BlizzardWithPosition>> blizzardPositions = computeBlizzardPositions(m, n,
                initialPositions);
        int move1 = bfs(blizzardPositions, m, n,
                0, 1,
                m - 1, n - 2,
                0);
        int move2 = bfs(blizzardPositions, m, n,
                m - 1, n - 2,
                0, 1,
                move1);
        return bfs(blizzardPositions, m, n,
                0, 1,
                m - 1, n - 2,
                move2);
    }

    record BFSState(int x, int y, int minute) {
    }

    record BlizzardWithPosition(char blizzard, int x, int y) {

    }

    record Position(int x, int y) {

    }
}
