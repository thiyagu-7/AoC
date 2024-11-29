package com.thiyagu_7.adventofcode.year2023.day22;

import com.thiyagu_7.adventofcode.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SolutionDay22 {
    public int part1(List<String> input) {
        List<Brick> bricks = new ArrayList<>(parseBricks(input));
        bricks.sort(Comparator.comparingInt((Brick brick) -> brick.start.z)
                .thenComparingInt(brick -> brick.start.x)
                .thenComparingInt(brick -> brick.start.y));

        int[][][] grid = new int[300][300][300];
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            markBrick(grid, brick.start, brick.end, i + 1);
        }

        List<Brick> newBricks = new ArrayList<>();
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            Brick newBrick = settleDown(grid, brick.start, brick.end);
            newBricks.add(newBrick);
        }

        int result = 0;
        newBricks.sort(Comparator.comparingInt((Brick brick) -> brick.start.z)
                .thenComparingInt(brick -> brick.start.x)
                .thenComparingInt(brick -> brick.start.y));
        for (int i = 0; i < newBricks.size(); i++) {
            Brick brick = newBricks.get(i);
            if (canDisintegrate(grid, brick.start, brick.end, newBricks)) {
                result++;
            }
        }
        return result;
    }

    public int part2(List<String> input) {
        List<Brick> bricks = new ArrayList<>(parseBricks(input));
        bricks.sort(Comparator.comparingInt((Brick brick) -> brick.start.z)
                .thenComparingInt(brick -> brick.start.x)
                .thenComparingInt(brick -> brick.start.y));

        int[][][] grid = new int[300][300][300];
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            markBrick(grid, brick.start, brick.end, i + 1);
        }

        List<Brick> newBricks = new ArrayList<>();
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            Brick newBrick = settleDown(grid, brick.start, brick.end);
            newBricks.add(newBrick);
        }

        int result = 0;
        newBricks.sort(Comparator.comparingInt((Brick brick) -> brick.start.z)
                .thenComparingInt(brick -> brick.start.x)
                .thenComparingInt(brick -> brick.start.y));
        for (int i = 0; i < newBricks.size(); i++) {
            Brick brick = newBricks.get(i);

            int[][][] gridCopy = new int[300][300][300];
            for (int ii = 0; ii < grid.length; ii++) {
                for (int jj = 0; jj < grid[0].length; jj++) {
                    for (int kk = 0; kk < grid[0][0].length; kk++) {
                        gridCopy[ii][jj][kk] = grid[ii][jj][kk];
                    }
                }
            }
            result += disintegrate(gridCopy, brick.start, brick.end, newBricks);
        }
        return result;
    }

    private List<Brick> parseBricks(List<String> input) {
        return input.stream()
                .map(s -> s.split("~"))
                .map(a -> new Pair<>(parseLocation(a[0]), parseLocation(a[1])))
                .map(p -> new Brick(p.getKey(), p.getValue()))
                .toList();
    }

    private Position parseLocation(String location) {
        String[] parts = location.split(",");
        return new Position(Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]));
    }

    private void markBrick(int[][][] grid, Position start, Position end, int brickNum) {
        if (start.x != end.x) {
            for (int i = start.x; i <= end.x; i++) {
                grid[i][start.y][start.z] = brickNum;
            }
        } else if (start.y != end.y) {
            for (int j = start.y; j <= end.y; j++) {
                grid[start.x][j][start.z] = brickNum;
            }
        } else {
            for (int k = start.z; k <= end.z; k++) {
                grid[start.x][start.y][k] = brickNum;
            }
        }
    }

    private Brick settleDown(int[][][] grid, Position start, Position end) {
        return settleDown(grid, start, end, false);
    }

    private boolean canDisintegrate(int[][][] grid, Position start, Position end,
                                    List<Brick> bricks) {
        Brick currentBrick = new Brick(start, end);
        int brickNum = grid[start.x][start.y][start.z];
        if (start.x != end.x) {
            for (int i = start.x; i <= end.x; i++) {
                grid[i][start.y][start.z] = 0;
            }
        } else if (start.y != end.y) {
            for (int j = start.y; j <= end.y; j++) {
                grid[start.x][j][start.z] = 0;
            }
        } else {
            for (int k = start.z; k <= end.z; k++) {
                grid[start.x][start.y][k] = 0;
            }
        }
        boolean canDisintegrate = true;
        for (Brick brick : bricks) {
            if (!brick.equals(currentBrick)) {
                Brick newPosition = settleDown(grid, brick.start, brick.end, true);
                if (!brick.equals(newPosition)) {
                    canDisintegrate = false;
                    break;
                }
            }
        }
        //reset
        if (start.x != end.x) {
            for (int i = start.x; i <= end.x; i++) {
                grid[i][start.y][start.z] = brickNum;
            }
        } else if (start.y != end.y) {
            for (int j = start.y; j <= end.y; j++) {
                grid[start.x][j][start.z] = brickNum;
            }
        } else {
            for (int k = start.z; k <= end.z; k++) {
                grid[start.x][start.y][k] = brickNum;
            }
        }
        //reset
        return canDisintegrate;
    }

    private int disintegrate(int[][][] grid, Position start, Position end,
                             List<Brick> bricks) {
        Brick currentBrick = new Brick(start, end);
        int brickNum = grid[start.x][start.y][start.z];
        if (start.x != end.x) {
            for (int i = start.x; i <= end.x; i++) {
                grid[i][start.y][start.z] = 0;
            }
        } else if (start.y != end.y) {
            for (int j = start.y; j <= end.y; j++) {
                grid[start.x][j][start.z] = 0;
            }
        } else {
            for (int k = start.z; k <= end.z; k++) {
                grid[start.x][start.y][k] = 0;
            }
        }

        int num = 0;
        for (Brick brick : bricks) {
            if (!brick.equals(currentBrick)) {
                Brick newPosition = settleDown(grid, brick.start, brick.end, false); //NEW
                if (!brick.equals(newPosition)) {
                    num++;
                }
            }
        }
        //reset
        if (start.x != end.x) {
            for (int i = start.x; i <= end.x; i++) {
                grid[i][start.y][start.z] = brickNum;
            }
        } else if (start.y != end.y) {
            for (int j = start.y; j <= end.y; j++) {
                grid[start.x][j][start.z] = brickNum;
            }
        } else {
            for (int k = start.z; k <= end.z; k++) {
                grid[start.x][start.y][k] = brickNum;
            }
        }
        //reset
        return num;
    }

    private Brick settleDown(int[][][] grid, Position start, Position end, boolean simulate) {
        boolean canMove = true;
        Brick currentBrick = new Brick(start, end);
        int brickNum = grid[start.x][start.y][start.z];
        if (start.z == 1) {
            return new Brick(start, end);
        }
        do {
            if (start.x != end.x) {
                for (int i = start.x; i <= end.x; i++) {
                    if (grid[i][start.y][start.z - 1] != 0) {
                        canMove = false;
                        break;
                    }
                }
                if (canMove && !simulate) {
                    for (int i = start.x; i <= end.x; i++) {
                        grid[i][start.y][start.z] = 0;
                        grid[i][start.y][start.z - 1] = brickNum;
                    }
                }
            } else if (start.y != end.y) {
                for (int j = start.y; j <= end.y; j++) {
                    if (grid[start.x][j][start.z - 1] != 0) {
                        canMove = false;
                        break;
                    }
                }
                if (canMove && !simulate) {
                    for (int j = start.y; j <= end.y; j++) {
                        grid[start.x][j][start.z] = 0;
                        grid[start.x][j][start.z - 1] = brickNum;
                    }
                }
            } else {
                if (grid[start.x][start.y][start.z - 1] != 0) {
                    canMove = false;
                }
                if (canMove && !simulate) {
                    grid[start.x][start.y][start.z - 1] = brickNum;
                    grid[start.x][start.y][end.z] = 0;
                }
            }
            if (simulate) {
                return canMove ? new Brick(new Position(start.x, start.y, start.z - 1), new Position(end.x, end.y, end.z - 1))
                        : currentBrick;
            }
            if (canMove) {
                start = new Position(start.x, start.y, start.z - 1);
                end = new Position(end.x, end.y, end.z - 1);
            }
        } while (canMove && start.z > 1);
        return new Brick(start, end);
    }

    record Brick(Position start, Position end) {

    }

    record Position(int x, int y, int z) {

    }
}
