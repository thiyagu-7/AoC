package com.thiyagu_7.adventofcode.year2019.day3.gridbased;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

//Does not work as have to allocate a big array - Out of heap space
//Works with 8GB heap
public class SolutionTry1 {
    private static final Map<Character, UnaryOperator<Coordinate>> COORDINATE_DIRECTION;
    private int[][] gridPanel = new int[35000][39000];
    //Central port
    private int x = gridPanel.length / 2;
    private int y = gridPanel[0].length / 2;

    //Current location
    private Coordinate centralPort = new Coordinate(x, y);
    private Coordinate current = new Coordinate(x, y);
    private int result = Integer.MAX_VALUE;

    static {
        COORDINATE_DIRECTION = new HashMap<>();
        COORDINATE_DIRECTION.put('U', c -> new Coordinate(c.getX() - 1, c.getY()));
        COORDINATE_DIRECTION.put('D', c -> new Coordinate(c.getX() + 1, c.getY()));
        COORDINATE_DIRECTION.put('L', c -> new Coordinate(c.getX(), c.getY() - 1));
        COORDINATE_DIRECTION.put('R', c -> new Coordinate(c.getX(), c.getY() + 1));
    }

    public int part1(String wire1Paths, String wire2Paths) {
        Arrays.stream(wire1Paths.split(","))
                .forEach(path -> processPath(path, 1, Optional.empty()));

        Optional<Consumer<Coordinate>> updateManhattanDistance = Optional.of(this::updateManhattanDistance);

        //reset current back to central port
        current = centralPort;
        Arrays.stream(wire2Paths.split(","))
                .forEach(path -> processPath(path, 2, updateManhattanDistance));

        return result;
    }

    private void processPath(String path, int wireNum,
                             Optional<Consumer<Coordinate>> coordinateConsumer) {
        char direction = path.charAt(0);
        int length = Integer.parseInt(path.substring(1));
        UnaryOperator<Coordinate> directionMap = COORDINATE_DIRECTION.get(direction);

        for (int i = 0; i < length; i++) {
            current = directionMap.apply(current);
            coordinateConsumer.ifPresent(consumer -> consumer.accept(current));

            gridPanel[current.getX()][current.getY()] = wireNum;
        }
    }


    private void updateManhattanDistance(Coordinate newCoordinate) {
        //Check for intersection
        if (gridPanel[newCoordinate.getX()][newCoordinate.getY()] == 1) {
            System.out.println(result + " " + (Math.abs(newCoordinate.getX() - x) + Math.abs(newCoordinate.getY() - y)));
            result = Math.min(result, Math.abs(newCoordinate.getX() - x) + Math.abs(newCoordinate.getY() - y));
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

    public static void main(String[] args) {
        SolutionTry1 solutionTry1 = new SolutionTry1();
        List<String> lines = readFile("/year2019/day3/input.txt");
        System.out.println(solutionTry1.part1(lines.get(0), lines.get(1)));
    }

    public static List<String> readFile(String resourcePath) {
        try {
            return Files.readAllLines(Paths.get(SolutionTry1.class
                    .getResource(resourcePath)
                    .toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
