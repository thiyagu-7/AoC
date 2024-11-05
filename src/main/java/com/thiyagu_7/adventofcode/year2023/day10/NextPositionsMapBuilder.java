package com.thiyagu_7.adventofcode.year2023.day10;

import com.thiyagu_7.adventofcode.util.Pair;
import com.thiyagu_7.adventofcode.year2023.day10.model.Position;
import com.thiyagu_7.adventofcode.year2023.day10.model.PositionAndConnectablePipes;
import com.thiyagu_7.adventofcode.year2023.day10.model.PossibleNextPositionsFunction;

import java.util.Map;
import java.util.Set;

public class NextPositionsMapBuilder {
    public static Map<Character, PossibleNextPositionsFunction> buildNextPositionsMap() {
        return Map.of(
                '|', new PossibleNextPositionsFunction(p -> new Pair<>(
                        new PositionAndConnectablePipes(
                                new Position(p.x() - 1, p.y()),
                                Set.of('|', '7', 'F')),
                        new PositionAndConnectablePipes(
                                new Position(p.x() + 1, p.y()),
                                Set.of('|', 'L', 'J')
                        )
                )),
                '-', new PossibleNextPositionsFunction(p -> new Pair<>(
                        new PositionAndConnectablePipes(
                                new Position(p.x(), p.y() - 1),
                                Set.of('-', 'L', 'F')),
                        new PositionAndConnectablePipes(
                                new Position(p.x(), p.y() + 1),
                                Set.of('-', 'J', '7')
                        )
                )),
                'L', new PossibleNextPositionsFunction(p -> new Pair<>(
                        new PositionAndConnectablePipes(
                                new Position(p.x(), p.y() + 1),
                                Set.of('-', 'J', '7')),
                        new PositionAndConnectablePipes(
                                new Position(p.x() - 1, p.y()),
                                Set.of('|', '7', 'F')
                        )
                )),
                // think of J as reverse L ('_|')
                'J', new PossibleNextPositionsFunction(p -> new Pair<>(
                        new PositionAndConnectablePipes(
                                new Position(p.x(), p.y() - 1),
                                Set.of('-', 'L', 'F')),
                        new PositionAndConnectablePipes(
                                new Position(p.x() - 1, p.y()),
                                Set.of('|', '7', 'F')
                        )
                )),
                '7', new PossibleNextPositionsFunction(p -> new Pair<>(
                        new PositionAndConnectablePipes(
                                new Position(p.x(), p.y() - 1),
                                Set.of('-', 'L', 'F')),
                        new PositionAndConnectablePipes(
                                new Position(p.x() + 1, p.y()),
                                Set.of('|', 'L', 'J')
                        )
                )),
                'F', new PossibleNextPositionsFunction(p -> new Pair<>(
                        new PositionAndConnectablePipes(
                                new Position(p.x(), p.y() + 1),
                                Set.of('-', 'J', '7')),
                        new PositionAndConnectablePipes(
                                new Position(p.x() + 1, p.y()),
                                Set.of('|', 'L', 'J')
                        )
                ))
        );
    }

}
