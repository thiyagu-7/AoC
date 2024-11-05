package com.thiyagu_7.adventofcode.year2023.day18;


import com.thiyagu_7.adventofcode.util.Pair;
import com.thiyagu_7.adventofcode.util.Shoelace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SolutionDay18 {
    public long part1(List<String> input) {
        List<DigInstruction> digInstructions = input.stream()
                .map(this::parseInstructionPart1)
                .toList();
        return solve(digInstructions);
    }

    public long part2(List<String> input) {
        List<DigInstruction> digInstructions = input.stream()
                .map(this::parseInstructionPart2)
                .toList();
        return solve(digInstructions);
    }

    private long solve(List<DigInstruction> digInstructions) {
        List<Pair<Position, Position>> fromToList = new ArrayList<>();
        Position currentPosition = new Position(0, 0);

        for (DigInstruction digInstruction : digInstructions) {
            Position newPosition = move(digInstruction, currentPosition);
            fromToList.add(new Pair<>(currentPosition, newPosition));
            currentPosition = newPosition;
        }
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));

        fromToList.stream()
                .limit(fromToList.size() - 1) //skip last position which is same as starting
                .map(Pair::getValue)
                .forEach(positions::add);


        long area = Math.abs(Shoelace.shoeLace(positions.stream()
                .map(p -> new com.thiyagu_7.adventofcode.util.Position(p.x(), p.y()))
                .toList()));

        int b = 0; //num of boundary points
        for (Pair<Position, Position> pair : fromToList) {
            Position p1 = pair.getKey();
            Position p2 = pair.getValue();
            b += p1.x == p2.x ? Math.abs(p1.y - p2.y) : Math.abs(p1.x - p2.x);
        }

        // Pick's theorem
        long interiorPoints = (area - (b / 2) + 1);
        return interiorPoints + b;
    }

    private Position move(DigInstruction digInstruction, Position currentPosition) {
        int amount = digInstruction.amountToMove;
        return switch (digInstruction.direction) {
            case UP -> new Position(currentPosition.x() - amount, currentPosition.y());
            case DOWN -> new Position(currentPosition.x() + amount, currentPosition.y());
            case LEFT -> new Position(currentPosition.x(), currentPosition.y() - amount);
            case RIGHT -> new Position(currentPosition.x(), currentPosition.y() + amount);
        };
    }

    private DigInstruction parseInstructionPart1(String line) {
        String[] parts = line.split(" ");
        return new DigInstruction(
                Direction.getDirection(parts[0]),
                Integer.parseInt(parts[1]),
                parts[2].substring(1, parts[2].length() - 1)
        );
    }

    private DigInstruction parseInstructionPart2(String line) {
        String[] parts = line.split(" ");
        String instruction = parts[2].substring(2, parts[2].length() - 1);

        Direction direction = switch (Integer.parseInt(instruction.substring(5))) {
            case 0 -> Direction.RIGHT;
            case 1 -> Direction.DOWN;
            case 2 -> Direction.LEFT;
            case 3 -> Direction.UP;
            default -> throw new IllegalStateException("Unexpected value: ");
        };
        int amountToMove = Integer.parseInt(instruction.substring(0, 5), 16);
        return new DigInstruction(
                direction,
                amountToMove,
                null
        );
    }

    record DigInstruction(Direction direction, int amountToMove, String colorCode) {

    }

    enum Direction {
        UP("U"),
        DOWN("D"),
        LEFT("L"),
        RIGHT("R");
        private final String direction;
        private static final Map<String, Direction> MAP;

        static {
            MAP = Arrays.stream(Direction.values())
                    .collect(Collectors.toMap(d -> d.direction, Function.identity()));
        }

        Direction(String direction) {
            this.direction = direction;
        }

        private static Direction getDirection(String direction) {
            return MAP.get(direction);
        }
    }

    record Position(int x, int y) {

    }
}
