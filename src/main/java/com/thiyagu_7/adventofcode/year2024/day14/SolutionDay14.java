package com.thiyagu_7.adventofcode.year2024.day14;

import com.thiyagu_7.adventofcode.util.Position;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolutionDay14 {
    public int part1(List<String> input, int m, int n) {
        List<RobotPositionAndVelocity> robotPositionAndVelocities = parseInput(input);
        int[][] grid = new int[m][n];

        //mark initial robot positions
        robotPositionAndVelocities.forEach(
                robotPositionAndVelocity -> markRobotInGrid(grid, robotPositionAndVelocity.position)
        );

        //move each for 100 sec
        for (int k = 0; k < 100; k++) {
            List<RobotPositionAndVelocity> newRobotPositionAndVelocities = new ArrayList<>();

            for (RobotPositionAndVelocity robotPositionAndVelocity : robotPositionAndVelocities) {
                Position newPosition = moveRobot(grid, robotPositionAndVelocity.position, robotPositionAndVelocity.velocity);
                newRobotPositionAndVelocities.add(new RobotPositionAndVelocity(
                        newPosition,
                        robotPositionAndVelocity.velocity
                ));
            }
            robotPositionAndVelocities = newRobotPositionAndVelocities;
        }

        //find number of robots in each of the four quadrants
        int mHalf = m / 2;
        int nHalf = n / 2;
        int a = countNumRobots(grid, 0, 0, mHalf - 1, nHalf - 1);
        int b = countNumRobots(grid, 0, nHalf + 1, mHalf - 1, n - 1);
        int c = countNumRobots(grid, mHalf + 1, 0, m - 1, nHalf - 1);
        int d = countNumRobots(grid, mHalf + 1, nHalf + 1, m - 1, n - 1);

        return a * b * c * d;
    }

    private List<RobotPositionAndVelocity> parseInput(List<String> input) {
        List<RobotPositionAndVelocity> robotPositionAndVelocities = new ArrayList<>();
        Pattern pattern = Pattern.compile("p=([-]*\\d+),([-]*\\d+) v=([-]*\\d+),([-]*\\d+)");

        for (String line : input) {
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            //x and y reversed in question
            robotPositionAndVelocities.add(
                    new RobotPositionAndVelocity(
                            new Position(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1))),
                            new Velocity(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(3)))
                    )
            );
        }
        return robotPositionAndVelocities;
    }

    private void markRobotInGrid(int[][] grid, Position position) {
        grid[position.x()][position.y()]++;
    }

    private Position moveRobot(int[][] grid, Position position, Velocity velocity) {
        int m = grid.length;
        int n = grid[0].length;
        int x = position.x();
        int y = position.y();
        grid[x][y]--;

        int newX = (x + velocity.x() + m) % m;
        int newY = (y + velocity.y() + n) % n;
        grid[newX][newY]++;

        return new Position(newX, newY);
    }

    private int countNumRobots(int[][] grid, int x1, int y1, int x2, int y2) {
        int robots = 0;
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                robots += grid[i][j];
            }
        }
        return robots;
    }

    record RobotPositionAndVelocity(Position position, Velocity velocity) {

    }

    record Velocity(int x, int y) {

    }

    public int part2(List<String> input) {
        List<RobotPositionAndVelocity> robotPositionAndVelocities = parseInput(input);
        int[][] grid = new int[103][101];

        robotPositionAndVelocities.forEach(
                robotPositionAndVelocity -> markRobotInGrid(grid, robotPositionAndVelocity.position)
        );


        for (int k = 0; ; k++) {
            List<RobotPositionAndVelocity> newRobotPositionAndVelocities = new ArrayList<>();

            for (RobotPositionAndVelocity robotPositionAndVelocity : robotPositionAndVelocities) {
                Position newPosition = moveRobot(grid, robotPositionAndVelocity.position, robotPositionAndVelocity.velocity);
                newRobotPositionAndVelocities.add(new RobotPositionAndVelocity(
                        newPosition,
                        robotPositionAndVelocity.velocity
                ));
            }
            robotPositionAndVelocities = newRobotPositionAndVelocities;


            for (int i = 0; i < grid.length; i++) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < grid[0].length; j++) {
                    builder.append(grid[i][j] > 0 ? 'X' : ' ');
                }
                if (builder.toString().contains("XXXXXXXX")) {
                    return k + 1;
                }
            }
        }
    }

    private void part2WriteToFile(List<String> input) {
        List<RobotPositionAndVelocity> robotPositionAndVelocities = parseInput(input);
        int[][] grid = new int[103][101];

        robotPositionAndVelocities.forEach(
                robotPositionAndVelocity -> markRobotInGrid(grid, robotPositionAndVelocity.position)
        );

        Set<ArrayWrapper> allGrids = new HashSet<>();
        allGrids.add(new ArrayWrapper(grid));

        for (int k = 0; ; k++) {
            List<RobotPositionAndVelocity> newRobotPositionAndVelocities = new ArrayList<>();

            for (RobotPositionAndVelocity robotPositionAndVelocity : robotPositionAndVelocities) {
                Position newPosition = moveRobot(grid, robotPositionAndVelocity.position, robotPositionAndVelocity.velocity);
                newRobotPositionAndVelocities.add(new RobotPositionAndVelocity(
                        newPosition,
                        robotPositionAndVelocity.velocity
                ));
            }
            robotPositionAndVelocities = newRobotPositionAndVelocities;

            ArrayWrapper wrapper = new ArrayWrapper(grid);
            if (allGrids.contains(wrapper)) {
                System.out.println(k);
                break;
            } else {
                allGrids.add(wrapper);
            }
            StringBuilder builder = new StringBuilder();
            builder.append("----").append(k);
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    builder.append(grid[i][j] > 0 ? 'X' : ' ');
                }
                builder.append("\n");
            }
            try {
                Files.write(Paths.get("file.txt"),
                        builder.toString().getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static class ArrayWrapper {
        final int[][] grid;

        private ArrayWrapper(int[][] grid) {
            this.grid = grid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ArrayWrapper arrayWrapper = (ArrayWrapper) o;
            return Objects.deepEquals(grid, arrayWrapper.grid);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(grid);
        }
    }
}
