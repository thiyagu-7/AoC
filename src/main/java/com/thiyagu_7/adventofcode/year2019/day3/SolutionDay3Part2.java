package com.thiyagu_7.adventofcode.year2019.day3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SolutionDay3Part2 {
    private static final Map<Character, UnaryOperator<Coordinate>> COORDINATE_DIRECTION;
    //Optimized grid approach
    private Map<Coordinate, Integer> costMap = new HashMap<>();
    //number of steps for a wire
    private Integer steps = 0;

    //Current location
    private Coordinate centralPort = new Coordinate(0, 0);
    private Coordinate current = new Coordinate(0, 0);
    private int result = Integer.MAX_VALUE;

    static {
        COORDINATE_DIRECTION = new HashMap<>();
        COORDINATE_DIRECTION.put('U', c -> new Coordinate(c.getX() - 1, c.getY()));
        COORDINATE_DIRECTION.put('D', c -> new Coordinate(c.getX() + 1, c.getY()));
        COORDINATE_DIRECTION.put('L', c -> new Coordinate(c.getX(), c.getY() - 1));
        COORDINATE_DIRECTION.put('R', c -> new Coordinate(c.getX(), c.getY() + 1));
    }

    public int part2(String wire1Paths, String wire2Paths) {
        Arrays.stream(wire1Paths.split(","))
                .forEach(path -> processPath(path, this::updateStep));

        //reset current back to central port
        current = centralPort;
        steps = 0; //reset steps
        Arrays.stream(wire2Paths.split(","))
                .forEach(path -> processPath(path, this::updateSumOfSteps));

        return result;
    }

    private void processPath(String path,
                             Consumer<Coordinate> coordinateConsumer) {
        char direction = path.charAt(0);
        int length = Integer.parseInt(path.substring(1));
        UnaryOperator<Coordinate> directionMap = COORDINATE_DIRECTION.get(direction);

        for (int i = 0; i < length; i++) {
            current = directionMap.apply(current);
            steps++;
            coordinateConsumer.accept(current);
        }
    }

    private void updateStep(Coordinate newCoordinate) { //newCoordinate same as current
        int cost = steps;
        if (costMap.containsKey(newCoordinate)) {
            cost = Math.min(cost, costMap.get(newCoordinate));
        }
        costMap.put(newCoordinate, cost);
    }

    private void updateSumOfSteps(Coordinate newCoordinate) {
        //steps is steps for wire2
        if (costMap.containsKey(newCoordinate)) { //Intersection if costMap has that point
            result = Math.min(result, steps + costMap.get(newCoordinate));
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

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != Coordinate.class) {
                return false;
            }
            Coordinate o = (Coordinate) obj;
            return x == o.x && y == o.y;
        }
    }
}
