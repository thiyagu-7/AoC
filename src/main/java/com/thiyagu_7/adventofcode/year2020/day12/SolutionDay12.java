package com.thiyagu_7.adventofcode.year2020.day12;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionDay12 {
    private Map<Character, Integer> positions = new HashMap<>();
    private char direction = 'E';
    private List<Character> directions = Arrays.asList('E', 'S', 'W', 'N');

    SolutionDay12() {
        positions.put('E', 0);
        positions.put('W', 0);
        positions.put('N', 0);
        positions.put('S', 0);
    }

    public int part1(List<String> input) {
        input.forEach(this::move);
        return positions.get('E') + positions.get('W')
                + positions.get('N') + positions.get('S');
    }

    private void move(String line) {
        char c = line.charAt(0);
        int amount = Integer.parseInt(line.substring(1));

        switch (c) {
            case 'F':
                move(direction, amount);
                break;
            case 'L':
                int idx = directions.indexOf(direction);
                direction = directions.get((4 + idx - (amount / 90)) % 4);
                break;
            case 'R':
                idx = directions.indexOf(direction);
                direction = directions.get((idx + (amount / 90)) % 4);
                break;
            default:
                move(c, amount);
                break;
        }

    }
    private void move(char direction, int amount) {
        if (direction == 'E') {
            move('E', 'W', amount);
        } else if (direction == 'W') {
            move('W', 'E', amount);
        } else if (direction == 'N') {
            move('N', 'S', amount);
        } else {
            move('S', 'N', amount);
        }
    }
    private void move(char direction, char otherDirection, int amount) {
        int otherDirectionAmt = positions.get(otherDirection);
        if (otherDirectionAmt > 0) {
            if (otherDirectionAmt >= amount) {
                positions.put(otherDirection, otherDirectionAmt - amount);
            } else {
                positions.put(otherDirection, 0);
                positions.put(direction, (amount - otherDirectionAmt));
            }
        } else {
            positions.put(direction, positions.get(direction) + amount);
        }
    }
}
