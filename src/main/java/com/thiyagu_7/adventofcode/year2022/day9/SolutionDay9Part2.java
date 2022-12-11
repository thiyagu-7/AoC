package com.thiyagu_7.adventofcode.year2022.day9;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

public class SolutionDay9Part2 {
    private static final Map<Character, Function<Position, Position>> DIR_TO_COMPUTATION_FN = Map.of(
            'U', (p) -> new Position(p.x() - 1, p.y()),
            'D', (p) -> new Position(p.x() + 1, p.y()),
            'L', (p) -> new Position(p.x(), p.y() - 1),
            'R', (p) -> new Position(p.x(), p.y() + 1)
    );

    private static final int NUM_KNOTS = 10; //includes head

    public int part2(List<String> input) {
        Set<Position> allTailPositions = new HashSet<>();
        List<Position> knots = new ArrayList<>(IntStream.range(0, NUM_KNOTS)
                .mapToObj(i -> new Position(0, 0))
                .toList());
        allTailPositions.add(knots.get(0));

        for (String line : input) {
            String[] parts = line.split(" ");
            move(knots, allTailPositions, parts[0].charAt(0), Integer.parseInt(parts[1]));
        }
        return allTailPositions.size();
    }

    private void move(List<Position> knots,
                      Set<Position> allTailPositions, char direction, int amount) {
        Position newHeadPosition;
        Position newTailPosition;
        Function<Position, Position> computeNewHead = DIR_TO_COMPUTATION_FN.get(direction);

        for (int k = 0; k < amount; k++) {
            newHeadPosition = computeNewHead.apply(knots.get(0));
            knots.set(0, newHeadPosition);
            for (int i = 1; i < NUM_KNOTS; i++) {
                // Update knot i following knot i - 1
                newTailPosition = updateTail(knots.get(i), knots.get(i - 1), allTailPositions,
                        i == NUM_KNOTS - 1);
                knots.set(i, newTailPosition);
            }
        }
    }

    // for part 2 - newHeadPosition is actually the previous knot
    private Position updateTail(Position currentTailPosition,
                                Position newHeadPosition, Set<Position> allTailPositions,
                                boolean shouldTrack) {
        // head and tail touching - no need to move tail
        if (Math.abs(newHeadPosition.x() - currentTailPosition.x()) <= 1
                && Math.abs(newHeadPosition.y() - currentTailPosition.y()) <= 1) {
            return currentTailPosition;
        }

        Position newTailPosition;
        //The head is two steps directly up, down, left, or right.
        if (newHeadPosition.x() == currentTailPosition.x() || newHeadPosition.y() == currentTailPosition.y()) {
            if (newHeadPosition.x() == currentTailPosition.x()) { // left or right
                if (newHeadPosition.y() < currentTailPosition.y()) {
                    newTailPosition = new Position(currentTailPosition.x(), currentTailPosition.y() - 1);
                } else {
                    newTailPosition = new Position(currentTailPosition.x(), currentTailPosition.y() + 1);
                }
            } else { //up or down
                if (newHeadPosition.x() < currentTailPosition.x()) {
                    newTailPosition = new Position(currentTailPosition.x() - 1, currentTailPosition.y());
                } else {
                    newTailPosition = new Position(currentTailPosition.x() + 1, currentTailPosition.y());
                }
            }
        } else { //head and tail are diagonal - tail moves diagonally
            if (newHeadPosition.x() < currentTailPosition.x() &&
                    newHeadPosition.y() < currentTailPosition.y()) {
                //up left
                newTailPosition = new Position(currentTailPosition.x() - 1, currentTailPosition.y() - 1);
            } else if (newHeadPosition.x() < currentTailPosition.x() &&
                    newHeadPosition.y() > currentTailPosition.y()) {
                //up right
                newTailPosition = new Position(currentTailPosition.x() - 1, currentTailPosition.y() + 1);
            } else if (newHeadPosition.x() > currentTailPosition.x() &&
                    newHeadPosition.y() < currentTailPosition.y()) {
                //down right
                newTailPosition = new Position(currentTailPosition.x() + 1, currentTailPosition.y() - 1);
            } else {
                //down right
                newTailPosition = new Position(currentTailPosition.x() + 1, currentTailPosition.y() + 1);
            }
        }
        if (shouldTrack) {
            allTailPositions.add(newTailPosition);
        }
        return newTailPosition;
    }
}
