package com.thiyagu_7.adventofcode.year2024.day15;

import com.thiyagu_7.adventofcode.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SolutionDay15 {
    private static final Map<Character, Function<Position, Position>> MOVEMENT_MAP;
    private static final Map<Character, BiFunction<Position, Integer, Position>> MOVEMENT_MAP_2;

    static {
        MOVEMENT_MAP = Map.of(
                '^', p -> new Position(p.x() - 1, p.y()),
                'v', p -> new Position(p.x() + 1, p.y()),
                '<', p -> new Position(p.x(), p.y() - 1),
                '>', p -> new Position(p.x(), p.y() + 1)
        );
        MOVEMENT_MAP_2 = Map.of(
                '^', (p, i) -> new Position(p.x() - i, p.y()),
                'v', (p, i) -> new Position(p.x() + i, p.y()),
                '<', (p, i) -> new Position(p.x(), p.y() - i),
                '>', (p, i) -> new Position(p.x(), p.y() + i)
        );
    }

    public int part1(List<String> input) {
        GridAndMovements gridAndMovements = parseInput(input);
        char[][] grid = gridAndMovements.grid;
        Position robotPosition = parseRobotPosition(grid);

        for (char direction : gridAndMovements.movements) {
            robotPosition = movePart1(grid, robotPosition, direction);
        }

        return computeGPSCoordinates(grid, 'O');
    }

    private GridAndMovements parseInput(List<String> input) {
        List<String> gridLines = new ArrayList<>();

        int i;
        for (i = 0; i < input.size(); i++) {
            if (input.get(i).isEmpty()) {
                i++;
                break;
            }
            gridLines.add(input.get(i));
        }
        char[][] grid = gridLines.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        List<Character> movements = new ArrayList<>();
        while (i < input.size()) {
            String movementsLine = input.get(i);
            Arrays.stream(movementsLine.split(""))
                    .forEach(m -> movements.add(m.charAt(0)));
            i++;
        }

        return new GridAndMovements(grid, movements);
    }

    private Position parseRobotPosition(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '@') {
                    return new Position(i, j);
                }
            }
        }
        throw new RuntimeException(); //can't reach here

    }

    //currentPosition is currentPosition of robot
    private Position movePart1(char[][] grid, Position currentPosition, char direction) {
        Position newPosition = MOVEMENT_MAP.get(direction)
                .apply(currentPosition);

        if (grid[newPosition.x()][newPosition.y()] == '.') { //free space
            grid[currentPosition.x()][currentPosition.y()] = '.';
            grid[newPosition.x()][newPosition.y()] = '@';
            return newPosition;

        } else if (grid[newPosition.x()][newPosition.y()] == 'O') { //box
            //check if box(es) can be moved
            Position firstBoxPosition = newPosition;
            while (grid[newPosition.x()][newPosition.y()] == 'O') {
                newPosition = MOVEMENT_MAP.get(direction)
                        .apply(newPosition);
            }

            if (grid[newPosition.x()][newPosition.y()] == '.') { //if found one free space at the end of box(es)
                grid[newPosition.x()][newPosition.y()] = 'O'; //last box moved to free space
                grid[firstBoxPosition.x()][firstBoxPosition.y()] = '@'; //robot moved to first box position
                grid[currentPosition.x()][currentPosition.y()] = '.'; //robot space becomes free
                return firstBoxPosition;
            }
        }
        return currentPosition;
    }


    private int computeGPSCoordinates(char[][] grid, char boxChar) {
        int result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == boxChar) {
                    result += 100 * i + j;
                }
            }
        }
        return result;
    }

    record GridAndMovements(char[][] grid, List<Character> movements) {

    }

    public int part2(List<String> input) {
        GridAndMovements gridAndMovements = parseInput(input);
        char[][] grid = buildLargeGrid(gridAndMovements.grid);
        Position robotPosition = parseRobotPosition(grid);

        for (char direction : gridAndMovements.movements) {
            robotPosition = movePart2(grid, robotPosition, direction);
        }

        return computeGPSCoordinates(grid, '[');
    }

    private char[][] buildLargeGrid(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        char[][] newGrid = new char[m][n * 2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '#') {
                    newGrid[i][2 * j] = '#';
                    newGrid[i][2 * j + 1] = '#';
                } else if (grid[i][j] == 'O') {
                    newGrid[i][2 * j] = '[';
                    newGrid[i][2 * j + 1] = ']';
                } else if (grid[i][j] == '.') {
                    newGrid[i][2 * j] = '.';
                    newGrid[i][2 * j + 1] = '.';
                } else { //@
                    newGrid[i][2 * j] = '@';
                    newGrid[i][2 * j + 1] = '.';
                }
            }
        }
        return newGrid;
    }

    private Position movePart2(char[][] grid, Position currentPosition, char direction) {
        Position newPosition = MOVEMENT_MAP.get(direction)
                .apply(currentPosition);

        if (grid[newPosition.x()][newPosition.y()] == '#') {
            return currentPosition;
        }

        if (grid[newPosition.x()][newPosition.y()] == '.') { //free space
            grid[currentPosition.x()][currentPosition.y()] = '.';
            grid[newPosition.x()][newPosition.y()] = '@';
            return newPosition;

        } else if (grid[newPosition.x()][newPosition.y()] == '[' && direction == '>') { //moving left to right
            int firstY = newPosition.y();
            while (grid[newPosition.x()][newPosition.y()] == '[') {
                newPosition = MOVEMENT_MAP_2.get(direction)
                        .apply(newPosition, 2);
            }
            //@f
            //@[][].
            //.@[][]
            if (grid[newPosition.x()][newPosition.y()] == '.') { //if empty space at the end of box(es)
                grid[newPosition.x()][newPosition.y()] = ']';

                for (int j = firstY + 1; j < newPosition.y(); j++) {
                    grid[newPosition.x()][j] = grid[newPosition.x()][j] == '[' ? ']' : '[';//flip
                }
                grid[newPosition.x()][firstY] = '@'; //robot moves to first box position ([)
                grid[currentPosition.x()][currentPosition.y()] = '.'; //robot space becomes free

                return new Position(newPosition.x(), firstY);
            }

        } else if (grid[newPosition.x()][newPosition.y()] == ']' && direction == '<') { //moving right to left
            int firstY = newPosition.y();
            while (grid[newPosition.x()][newPosition.y()] == ']') {
                newPosition = MOVEMENT_MAP_2.get(direction)
                        .apply(newPosition, 2);
            }
            //    f@
            //.[][]@
            //[][]@.
            if (grid[newPosition.x()][newPosition.y()] == '.') { //if empty space at the end of box(es)
                grid[newPosition.x()][newPosition.y()] = '[';

                for (int j = firstY - 1; j > newPosition.y(); j--) {
                    grid[newPosition.x()][j] = grid[newPosition.x()][j] == '[' ? ']' : '[';//flip
                }
                grid[newPosition.x()][firstY] = '@'; //robot moves to first box position (])
                grid[currentPosition.x()][currentPosition.y()] = '.'; //robot space becomes free

                return new Position(newPosition.x(), firstY);
            }

        } else { //moving up or down
            Position firstBoxPosition = newPosition;

            if (grid[newPosition.x()][newPosition.y()] == ']') {
                newPosition = new Position(newPosition.x(), newPosition.y() - 1);
            }
            if (checkIfMovementPossible(grid, newPosition, direction)) {
                move(grid, newPosition, direction);

                grid[currentPosition.x()][currentPosition.y()] = '.';
                grid[firstBoxPosition.x()][firstBoxPosition.y()] = '@';

                return firstBoxPosition;
            }

        }
        return currentPosition;
    }

    //recursive
    private boolean checkIfMovementPossible(char[][] grid, Position current, char direction) {
        Position next = MOVEMENT_MAP.get(direction)
                .apply(current);

        if (grid[next.x()][next.y()] == '.' && grid[next.x()][next.y() + 1] == '.') {
            return true;
        } else if (grid[next.x()][next.y()] == '#' || grid[next.x()][next.y() + 1] == '#') {
            return false;
        } else if (grid[next.x()][next.y()] == '[' && grid[next.x()][next.y() + 1] == ']') { //align
            return checkIfMovementPossible(grid, next, direction);
        } else if (grid[next.x()][next.y()] == '.' && grid[next.x()][next.y() + 1] == '[') { //one on right
            return checkIfMovementPossible(grid, new Position(next.x(), next.y() + 1), direction);
        } else if (grid[next.x()][next.y()] == ']' && grid[next.x()][next.y() + 1] == '.') { //one on left
            return checkIfMovementPossible(grid, new Position(next.x(), next.y() - 1), direction);
        } else if (grid[next.x()][next.y()] == ']' && grid[next.x()][next.y() + 1] == '[') { //2 mis-align
            return checkIfMovementPossible(grid, new Position(next.x(), next.y() - 1), direction)
                    &&
                    checkIfMovementPossible(grid, new Position(next.x(), next.y() + 1), direction);
        }

        throw new RuntimeException(); //can't reach here
    }

    //recursive
    private void move(char[][] grid, Position current, char direction) {
        Position next = MOVEMENT_MAP.get(direction)
                .apply(current);
        if (grid[next.x()][next.y()] == '.' && grid[next.x()][next.y() + 1] == '.') {
            moveOneBoxUp(grid, current, next);
        } else if (grid[next.x()][next.y()] == '[' && grid[next.x()][next.y() + 1] == ']') { //align
            move(grid, next, direction);
            moveOneBoxUp(grid, current, next);
        } else if (grid[next.x()][next.y()] == '.' && grid[next.x()][next.y() + 1] == '[') { //one on right
            move(grid, new Position(next.x(), next.y() + 1), direction);
            moveOneBoxUp(grid, current, next);

        } else if (grid[next.x()][next.y()] == ']' && grid[next.x()][next.y() + 1] == '.') { //one on left
            move(grid, new Position(next.x(), next.y() - 1), direction);
            moveOneBoxUp(grid, current, next);
        } else if (grid[next.x()][next.y()] == ']' && grid[next.x()][next.y() + 1] == '[') { //2 mis-align
            move(grid, new Position(next.x(), next.y() - 1), direction);
            move(grid, new Position(next.x(), next.y() + 1), direction);
            moveOneBoxUp(grid, current, next);
        }
    }

    private void moveOneBoxUp(char[][] grid, Position current, Position next) {
        grid[current.x()][current.y()] = '.';
        grid[current.x()][current.y() + 1] = '.';

        grid[next.x()][next.y()] = '[';
        grid[next.x()][next.y() + 1] = ']';
    }
}
