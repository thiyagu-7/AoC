package com.thiyagu_7.adventofcode.year2019.day3.gridbased;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SolutionTry1Part2 {
    private static final Map<Character, UnaryOperator<Coordinate>> COORDINATE_DIRECTION;
    private int[][] gridPanel = new int[35000][39000];
    //Central port
    private int x = gridPanel.length / 2;
    private int y = gridPanel[0].length / 2;

    //number of steps for a wire
    private Integer steps = 0;

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

    public int part2(String wire1Paths, String wire2Paths) {
        Arrays.stream(wire1Paths.split(","))
                .forEach(path -> processPath(path, this::updateStepInGrid));

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

    private void updateStepInGrid(Coordinate newCoordinate) { //newCoordinate same as current
        if (gridPanel[newCoordinate.getX()][newCoordinate.getY()] == 0) {
            gridPanel[newCoordinate.getX()][newCoordinate.getY()] = steps;
        } else {
            gridPanel[newCoordinate.getX()][newCoordinate.getY()] =
                    Math.min(gridPanel[newCoordinate.getX()][newCoordinate.getY()], steps);
        }
    }

    private void updateSumOfSteps(Coordinate newCoordinate) {
        //Check for intersection
        //steps is steps for wire2
        if (gridPanel[newCoordinate.getX()][newCoordinate.getY()] != 0) {
            result = Math.min(result, steps + gridPanel[newCoordinate.getX()][newCoordinate.getY()]);
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
        SolutionTry1Part2 solutionTry1Part2 = new SolutionTry1Part2();
        //System.out.println(solutionTry1Part2.part2("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"));

        List<String> lines = readFile("/year2019/day3/input.txt");
         System.out.println(solutionTry1Part2.part2(lines.get(0), lines.get(1)));
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
