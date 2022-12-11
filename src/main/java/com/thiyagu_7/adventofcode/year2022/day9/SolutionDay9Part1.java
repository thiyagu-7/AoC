package com.thiyagu_7.adventofcode.year2022.day9;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class SolutionDay9Part1 {
    private static final Map<Character, Function<Position, Position>> DIR_TO_COMPUTATION_FN = Map.of(
            'U', (p) -> new Position(p.x() - 1, p.y()),
            'D', (p) -> new Position(p.x() + 1, p.y()),
            'L',(p) -> new Position(p.x(), p.y() - 1),
            'R', (p) -> new Position(p.x(), p.y() + 1)
    );

    public int part1(List<String> input) {
        Set<Position> allTailPositions = new HashSet<>();
        Position headPosition = new Position(0, 0);
        Position tailPosition = new Position(0, 0);
        allTailPositions.add(tailPosition);

        for (String line : input) {
            String[] parts = line.split(" ");
            HeadTailPosition headTailPosition =
                    move(headPosition, tailPosition, allTailPositions,
                            parts[0].charAt(0), Integer.parseInt(parts[1]));

            headPosition = headTailPosition.headPosition();
            tailPosition = headTailPosition.tailPosition();
        }
        return allTailPositions.size();
    }

    private HeadTailPosition move(Position headPosition, Position tailPosition,
                                  Set<Position> allTailPositions, char direction, int amount) {

        Position currentHeadPosition = headPosition;
        Position newHeadPosition;
        Position currentTailPosition = tailPosition;
        Position newTailPosition;

        Function<Position, Position> computeNewHead = DIR_TO_COMPUTATION_FN.get(direction);
        for (int k = 0; k < amount; k++) {
            newHeadPosition = computeNewHead.apply(currentHeadPosition);

            newTailPosition = updateTail(currentHeadPosition, currentTailPosition,
                    newHeadPosition, allTailPositions);

            currentTailPosition = newTailPosition;
            currentHeadPosition = newHeadPosition;
        }
        return new HeadTailPosition(currentHeadPosition, currentTailPosition);
    }

    // compares newHeadPosition and currentTailPosition and returns new tail position
    private Position updateTail(Position currentHeadPosition, Position currentTailPosition,
                                Position newHeadPosition, Set<Position> allTailPositions) {
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
        } else { //head and tail are diagonal - tail moves diagonally (for part-1 , moving to previous head position works)
            newTailPosition = currentHeadPosition;
        }
        allTailPositions.add(newTailPosition);
        return newTailPosition;
    }
}
