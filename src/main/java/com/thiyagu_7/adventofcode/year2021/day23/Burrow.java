package com.thiyagu_7.adventofcode.year2021.day23;

import java.util.Arrays;

public class Burrow {
    private final char[] hallway;
    private final char[][] rooms;

    public Burrow(char[] hallway, char[][] rooms) {
        this.hallway = hallway;
        this.rooms = rooms;
    }

    public Burrow(char[][] rooms) {
        hallway = new char[11];
        Arrays.fill(hallway, '.');
        this.rooms = rooms;
    }

    public char[] getHallway() {
        return hallway;
    }

    public char[][] getRooms() {
        return rooms;
    }
}