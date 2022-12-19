package com.thiyagu_7.adventofcode.year2022.day17;

public interface Rock {
    int beginRock(char[][] chamber, int highestRock);

    boolean moveRight(char[][] chamber);

    boolean moveLeft(char[][] chamber);

    boolean moveDown(char[][] chamber);

    int getRow();
}
