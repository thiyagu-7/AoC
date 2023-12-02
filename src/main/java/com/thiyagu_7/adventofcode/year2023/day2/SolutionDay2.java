package com.thiyagu_7.adventofcode.year2023.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SolutionDay2 {
    public int part1(List<String> input) {
        int sum = 0;
        for (int i = 0; i < input.size(); i++) {
            if (isValid(parseInput(input.get(i)))) {
                sum += i + 1;
            }
        }
        return sum;
    }

    private List<CubeDraw> parseInput(String line) {
        int i = line.indexOf(": ");
        String[] cubeConfigs = line.substring(i + 2)
                .split("; ");
        Pattern pattern = Pattern.compile("((\\d+) (\\w+))*");
        List<CubeDraw> cubeDraws = new ArrayList<>();

        for (String cubeConfig : cubeConfigs) {
            Matcher matcher = pattern.matcher(cubeConfig);
            Map<CubeColour, Integer> map = new HashMap<>();
            while (matcher.find()) {
                if (!matcher.group(0).isEmpty()) {
                    map.put(CubeColour.getCubeColour(matcher.group(3)),
                            Integer.parseInt(matcher.group(2)));
                }
            }
            cubeDraws.add(new CubeDraw(map));
        }
        return cubeDraws;
    }

    private boolean isValid(List<CubeDraw> cubeDraws) {
        return cubeDraws.stream()
                .allMatch(this::isValid);
    }

    private boolean isValid(CubeDraw cubeDraw) {
        Map<CubeColour, Integer> cubes = cubeDraw.cubes;
        return cubes.getOrDefault(CubeColour.RED, 0) <= 12
                && cubes.getOrDefault(CubeColour.GREEN, 0) <= 13
                && cubes.getOrDefault(CubeColour.BLUE, 0) <= 14;

    }

    public int part2(List<String> input) {
        int sum = 0;
        for (int i = 0; i < input.size(); i++) {
            Map<CubeColour, Integer> min = new HashMap<>(Map.of(
                    CubeColour.RED, 0,
                    CubeColour.GREEN, 0,
                    CubeColour.BLUE, 0
            ));
            List<CubeDraw> cubeDraws = parseInput(input.get(i));
            for (CubeDraw cubeDraw : cubeDraws) {
                Map<CubeColour, Integer> cubes = cubeDraw.cubes;
                // fewest number of cubes of each color
                cubes.forEach((k, v) -> min.merge(k, v, (o, n) -> Math.max(o, v)));
            }
            sum += min.values()
                    .stream()
                    .reduce(1, (a, b) -> a * b);
        }
        return sum;
    }

    private record CubeDraw(Map<CubeColour, Integer> cubes) {

    }

    enum CubeColour {
        RED("red"),
        GREEN("green"),
        BLUE("blue");

        private final String colour;

        private static final Map<String, CubeColour> LOOKUP = Arrays.stream(CubeColour.values())
                .collect(Collectors.toMap(cubeColour -> cubeColour.colour, Function.identity()));

        CubeColour(String colour) {
            this.colour = colour;
        }

        private static CubeColour getCubeColour(String colour) {
            return LOOKUP.get(colour);
        }
    }
}
