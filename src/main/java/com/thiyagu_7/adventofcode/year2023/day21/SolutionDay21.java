package com.thiyagu_7.adventofcode.year2023.day21;

import com.thiyagu_7.adventofcode.year2023.day9.SolutionDay9;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class SolutionDay21 {
    public int part1(List<String> input, int numSteps) {
        char[][] grid = parseGrid(input);
        Position startPosition = null;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'S') {
                    startPosition = new Position(i, j);
                    break;
                }
            }
            if (startPosition != null) {
                break;
            }
        }
        return bfs(grid, startPosition, numSteps);
    }

    private char[][] parseGrid(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private int bfs(char[][] grid, Position startPosition, int numSteps) {
        int m = grid.length;
        int n = grid[0].length;

        int[] x = new int[]{-1, 1, 0, 0};
        int[] y = new int[]{0, 0, -1, 1};

        Queue<PositionAndStep> queue = new LinkedList<>();
        queue.add(new PositionAndStep(startPosition, 0));

        //uniquePositions same as queue but has only position (and not step)
        Set<Position> uniquePositions = new HashSet<>(Set.of(startPosition));

        while (!queue.isEmpty()) {
            PositionAndStep positionAndStep = queue.poll();
            Position currentPosition = positionAndStep.position;
            int step = positionAndStep.step;

            //if we have reached the max steps, break (should take any more steps)
            if (step == numSteps) {
                break;
            }
            uniquePositions.remove(currentPosition);

            for (int k = 0; k < 4; k++) {
                int newX = x[k] + currentPosition.x;
                int newY = y[k] + currentPosition.y;
                //p2
                if (newX == -1) {
                    newX = m - 1;
                } else if (newX == m) {
                    newX = 0;
                }

                if (newY == -1) {
                    newY = n - 1;
                } else if (newY == n) {
                    newY = 0;
                }
                //p2
                Position newPosition = new Position(newX, newY);
                PositionAndStep newPositionAndStep = new PositionAndStep(
                        newPosition, step + 1);

                /*
                Note: This cannot happen for part 1
                queue has a positionAndStep whose position equals newPosition but its step is different from step + 1 (newPositionAndStep.step).
                Hence, we can just check contains in uniquePositions
                 */
                /*queue.stream()
                        .filter(ps -> ps.position.equals(newPosition) && ps.step != step + 1)
                        .findFirst()
                        .ifPresent(s -> System.out.println("O"));*/

                if (isValid(newX, newY, m, n) && grid[newX][newY] != '#'
                        && !uniquePositions.contains(newPosition)) { //can check '!queue.contains(newPositionAndStep)', but will be slower
                    queue.add(newPositionAndStep);
                    uniquePositions.add(newPosition);
                }
            }
        }
        return uniquePositions.size();
    }

    private boolean isValid(int x, int y, int m, int n) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    private char[][] dup(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        char[][] res = new char[m * 5][n * 5];

        for (int i = 0; i < m * 5; i += m) {
            for (int j = 0; j < n * 5; j += n) {

                for (int ii = 0; ii < m; ii++) {
                    for (int jj = 0; jj < n; jj++) {
                        res[i + ii][j + jj] = grid[ii][jj];
                    }
                }
            }
        }

        return res;
    }

    //Solution dependent on main input puzzle format/structure
    //Runs long time - using Day 9 extrapolation
    public int part2(List<String> input) {
        char[][] grid = parseGrid(input);
        Position startPosition = null;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'S') {
                    startPosition = new Position(i, j);
                    break;
                }
            }
            if (startPosition != null) {
                break;
            }
        }
        grid = dup(grid);


        System.out.println(bfs(grid, startPosition, 65)); //3819
        System.out.println(bfs(grid, startPosition, 196)); //34099
        System.out.println(bfs(grid, startPosition, 327)); //94549
        //System.out.println(bfs(grid, startPosition, 458));

        SolutionDay9 solutionDay9 = new SolutionDay9();
        List<Long> list = new ArrayList<>(List.of(3819L, 34099L, 94549L));

        long res = 0;
        for (int i = 3; i <= 202300; i++) {
            res = solutionDay9.part1For21(list);
            list.add(res);
        }
        System.out.println(res);
        return 1;
    }

    record Position(int x, int y) {

    }

    //step is num of steps to reach the position
    record PositionAndStep(Position position, int step) {

    }
}
