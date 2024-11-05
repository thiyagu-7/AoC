package com.thiyagu_7.adventofcode.year2023.day10.model;

import java.util.Set;

// position is valid if the current location has one of the specified pipes
public record PositionAndConnectablePipes(Position position, Set<Character> allowedPipes) {

}