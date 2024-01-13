package com.thiyagu_7.adventofcode.year2023.day12;

import java.util.Arrays;

public class InputParser {
     static SpringDetail parseLine(String line) {
        String[] parts = line.split(" ");
        return new SpringDetail(parts[0], Arrays.stream(parts[1].split(","))
                .map(Integer::parseInt)
                .toList()
        );
    }
}
