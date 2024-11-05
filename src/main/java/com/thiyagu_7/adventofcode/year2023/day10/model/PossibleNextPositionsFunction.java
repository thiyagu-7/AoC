package com.thiyagu_7.adventofcode.year2023.day10.model;

import com.thiyagu_7.adventofcode.util.Pair;

import java.util.function.Function;

// for a pipe, given the current position, this returns the two possible next positions (based on the pipe connectivity)
public record PossibleNextPositionsFunction(Function<Position,
        Pair<PositionAndConnectablePipes, PositionAndConnectablePipes>> function) {

}
