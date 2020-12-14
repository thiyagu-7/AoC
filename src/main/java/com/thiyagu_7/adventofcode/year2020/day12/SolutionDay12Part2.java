package com.thiyagu_7.adventofcode.year2020.day12;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionDay12Part2 {
    private Map<Character, Integer> waypointPosition = new HashMap<>();
    private Map<Character, Integer> shipPosition;

    private List<Character> directions = Arrays.asList('E', 'S', 'W', 'N');

    SolutionDay12Part2() {
        waypointPosition.put('E', 10);
        waypointPosition.put('W', 0);
        waypointPosition.put('N', 1);
        waypointPosition.put('S', 0);

        shipPosition = new HashMap<>();
        shipPosition.put('E', 0);
        shipPosition.put('W', 0);
        shipPosition.put('N', 0);
        shipPosition.put('S', 0);

    }

    public int part2(List<String> input) {
        input.forEach(this::move);
        return shipPosition.get('E') + shipPosition.get('W')
                + shipPosition.get('N') + shipPosition.get('S');
    }

    private void move(String line) {
        char c = line.charAt(0);
        int amount = Integer.parseInt(line.substring(1));

        switch (c) {
            case 'F':
                for (char dir: directions) {
                    if (waypointPosition.get(dir) != 0) {
                        int movement = waypointPosition.get(dir) * amount;
                        move(dir, movement, shipPosition);

                    }
                }
                break;
            case 'L':
                Map<Character, Integer> waypointPositionResult = new HashMap<>();
                waypointPositionResult.put('E', 0);
                waypointPositionResult.put('W', 0);
                waypointPositionResult.put('N', 0);
                waypointPositionResult.put('S', 0);
                for (char dir: directions) {
                    if (waypointPosition.get(dir) != 0) { //don't mutate original waypointPosition
                        int idx = directions.indexOf(dir);
                        char otherDir = directions.get((4 + idx - (amount / 90)) % 4);
                        waypointPositionResult.put(otherDir, waypointPosition.get(dir));
                    }
                }
                waypointPosition = waypointPositionResult;
                break;
            case 'R':
                waypointPositionResult = new HashMap<>();
                waypointPositionResult.put('E', 0);
                waypointPositionResult.put('W', 0);
                waypointPositionResult.put('N', 0);
                waypointPositionResult.put('S', 0);
                for (char dir: directions) {
                    if (waypointPosition.get(dir) != 0) {
                        int idx = directions.indexOf(dir);
                        char otherDir = directions.get((idx + (amount / 90)) % 4);
                        waypointPositionResult.put(otherDir, waypointPosition.get(dir));
                    }
                }
                waypointPosition = waypointPositionResult;
                break;
            default:
                move(c, amount, waypointPosition);
                break;
        }
    }

    private void move(char direction, int amount, Map<Character, Integer> position) {
        if (direction == 'E') {
            move('E', 'W', amount, position);
        } else if (direction == 'W') {
            move('W', 'E', amount, position);
        } else if (direction == 'N') {
            move('N', 'S', amount, position);
        } else {
            move('S', 'N', amount, position);
        }
    }

    private void move(char direction, char otherDirection, int amount, Map<Character, Integer> position) {
        int otherDirectionAmt = position.get(otherDirection);
        if (otherDirectionAmt > 0) {
            if (otherDirectionAmt >= amount) {
                position.put(otherDirection, otherDirectionAmt - amount);
            } else {
                position.put(otherDirection, 0);
                position.put(direction, (amount - otherDirectionAmt));
            }
        } else {
            position.put(direction, position.get(direction) + amount);
        }
    }

}
