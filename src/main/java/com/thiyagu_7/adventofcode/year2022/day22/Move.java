package com.thiyagu_7.adventofcode.year2022.day22;

public record Move(int amount) implements Instruction {

    @Override
    public InstructionType getType() {
        return InstructionType.MOVE;
    }
}