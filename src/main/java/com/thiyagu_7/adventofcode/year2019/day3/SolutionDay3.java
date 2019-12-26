package com.thiyagu_7.adventofcode.year2019.day3;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;


public class SolutionDay3 {
    private static final Map<Character, BiFunction<Coordinate, Integer, Coordinate>> COORDINATE_DIRECTION;

    //Current location
    private Coordinate centralPort = new Coordinate(0, 0);
    private Coordinate current = centralPort;
    private int result = Integer.MAX_VALUE;

    static {
        COORDINATE_DIRECTION = new HashMap<>();
        COORDINATE_DIRECTION.put('U', (c, length) -> new Coordinate(c.getX() - length, c.getY()));
        COORDINATE_DIRECTION.put('D', (c, length) -> new Coordinate(c.getX() + length, c.getY()));
        COORDINATE_DIRECTION.put('L', (c, length) -> new Coordinate(c.getX(), c.getY() - length));
        COORDINATE_DIRECTION.put('R', (c, length) -> new Coordinate(c.getX(), c.getY() + length));
    }

    public int part1(String wire1Paths, String wire2Paths) {
        List<Pair<Coordinate, Coordinate>> lineSegments = new ArrayList<>();
        BiConsumer<Coordinate, Coordinate> addLineSegment = (start, end) -> lineSegments.add(new Pair<>(start, end));

        Arrays.stream(wire1Paths.split(","))
                .forEach(path -> processPath(path, addLineSegment));

        BiConsumer<Coordinate, Coordinate> updateManhattanDistance = (start, end) -> findIntersectionAndUpdateResult(lineSegments, start, end);

        //reset current back to central port
        current = centralPort;
        Arrays.stream(wire2Paths.split(","))
                .forEach(path -> processPath(path, updateManhattanDistance));

        return result;
    }

    private void processPath(String path,
                             BiConsumer<Coordinate, Coordinate> lineSegmentConsumer) {
        //System.out.println(current.getX() + " " + current.getY() + " " + path);
        char direction = path.charAt(0);
        int length = Integer.parseInt(path.substring(1));
        BiFunction<Coordinate, Integer, Coordinate> directionMap = COORDINATE_DIRECTION.get(direction);

        Coordinate newPoint = directionMap.apply(current, length);
        lineSegmentConsumer.accept(current, newPoint);
        current = newPoint;
    }


    //https://stackoverflow.com/questions/15726825/find-overlap-between-collinear-lines
    private void findIntersectionAndUpdateResult(List<Pair<Coordinate, Coordinate>> wire1LineSegments,
                                                 Coordinate start, Coordinate end) {
        //Check for intersection
        for(Pair<Coordinate, Coordinate> pair: wire1LineSegments) {
            Coordinate wire1LineStart = pair.getKey();
            Coordinate wire1LineEnd= pair.getValue();

            if (wire1LineStart.getX() > wire1LineEnd.getX() || wire1LineStart.getY() > wire1LineEnd.getY()) {
                //swap
                Coordinate tmp = wire1LineStart;
                wire1LineStart = wire1LineEnd;
                wire1LineEnd = tmp;
            }

            if (start.getX() > end.getX() || start.getY() > end.getY()) {
                //swap
                Coordinate tmp = start;
                start = end;
                end = tmp;
            }

            boolean condition1 = wire1LineEnd.getX() >= start.getX() && wire1LineEnd.getY() >= start.getY();
            boolean condition2 = wire1LineStart.getX() <= end.getX() && wire1LineStart.getY() <= end.getY();

            if (condition1 && condition2) {
                //intersection point
                int intX = Math.max(wire1LineStart.getX(), start.getX());
                int intY = Math.min(wire1LineEnd.getY(), end.getY());

                int dist = Math.abs(centralPort.getX() - intX) + Math.abs(centralPort.getY() - intY);
                if (dist != 0) {
                    result = Math.min(result, dist);
                }
            }
        }
    }

    private static class Coordinate {
        private int x;
        private int y;

        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
