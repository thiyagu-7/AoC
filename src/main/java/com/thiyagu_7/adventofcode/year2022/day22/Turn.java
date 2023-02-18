package com.thiyagu_7.adventofcode.year2022.day22;

public record Turn(boolean clockwise) implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.TURN;
    }
}
