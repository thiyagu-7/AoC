package com.thiyagu_7.adventofcode.year2021.day5;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionDay5 {
    public int part1(List<String> input) {
        return process(input, false);
    }

    public int part2(List<String> input) {
        return process(input, true);
    }

    private int process(List<String> input, boolean processDiagonal) {
        List<Pair<Coordinate, Coordinate>> lineSegments = parseInput(input);
        int[][] grid = new int[1000][1000];

        for (Pair<Coordinate, Coordinate> coordinate : lineSegments) {
            Coordinate point1 = coordinate.getKey();
            Coordinate point2 = coordinate.getValue();
            if (point1.x == point2.x) {
                if (point1.y > point2.y) {
                    Coordinate tmp = point1;
                    point1 = point2;
                    point2 = tmp;
                }
                for (int i = point1.y; i <= point2.y; i++) {
                    grid[point1.x][i]++;
                }
            } else if (point1.y == point2.y) {
                if (point1.x > point2.x) {
                    Coordinate tmp = point1;
                    point1 = point2;
                    point2 = tmp;
                }
                for (int i = point1.x; i <= point2.x; i++) {
                    grid[i][point1.y]++;
                }
            } else if (processDiagonal) { //diagonal lines (45°)
                if (point1.x > point2.x) {
                    Coordinate tmp = point1;
                    point1 = point2;
                    point2 = tmp;
                }
                int x = point1.x;
                if (point1.y > point2.y) { //downward
                    for (int i = point1.y; i >= point2.y; i--) {
                        grid[x++][i]++;
                    }
                } else {
                    for (int i = point1.y; i <= point2.y; i++) {
                        grid[x++][i]++;
                    }
                }
            }
        }

        return (int) Arrays.stream(grid)
                .map(Arrays::stream) //Stream<IntStream>
                .mapToLong(intStream -> intStream
                        .filter(e -> e > 1)
                        .count())
                .sum();
    }

    private List<Pair<Coordinate, Coordinate>> parseInput(List<String> input) {
        return input.stream()
                .map(line -> line.split(" -> ")) // line: 0,9 -> 5,9
                .map(parts -> new Pair<>(buildCoordinate(parts[0]), buildCoordinate(parts[1])))
                .collect(Collectors.toList());
    }

    private Coordinate buildCoordinate(String point) {
        String[] parts = point.split(",");
        return new Coordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    private static class Coordinate {
        private final int x;
        private final int y;

        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
