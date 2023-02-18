package com.thiyagu_7.adventofcode.year2022.day22;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class CommonUtil {
    static char[][] parseAsGrid(List<String> input) {
        int m = input.size() - 2;
        // find max column
        int n = input.stream()
                .map(String::length)
                .max(Comparator.naturalOrder())
                .get();

        char[][] grid = new char[m][n];
        for (int i = 0; i < m; i++) {
            String row = input.get(i);
            for (int j = 0; j < row.length(); j++) {
                grid[i][j] = row.charAt(j);
            }
        }
        return grid;
    }

    static List<Instruction> parseInstructions(String instructions) {
        List<Instruction> parsedInstructions = new ArrayList<>();
        for (int k = 0; k < instructions.length(); ) {
            String amount = "";
            while (k < instructions.length() && instructions.charAt(k) != 'L' && instructions.charAt(k) != 'R') {
                amount += instructions.charAt(k++);
            }
            parsedInstructions.add(new Move(Integer.parseInt(amount)));
            if (k < instructions.length()) {
                char direction = instructions.charAt(k++);
                parsedInstructions.add(new Turn(direction == 'R'));
            }
        }
        return parsedInstructions;
    }

}
