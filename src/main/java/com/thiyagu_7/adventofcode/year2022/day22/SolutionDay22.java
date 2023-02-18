package com.thiyagu_7.adventofcode.year2022.day22;

import java.util.List;

public class SolutionDay22 {
    private static final char UNINITIALIZED_LEFT = ' ';
    private static final char UNINITIALIZED_RIGHT = '\u0000';

    public int part1(List<String> input) {
        char[][] grid = CommonUtil.parseAsGrid(input);
        String instructions = input.get(input.size() - 1);
        return tracePath(grid, CommonUtil.parseInstructions(instructions));
    }

    private int tracePath(char[][] grid, List<Instruction> instructions) {
        int m = grid.length;
        int n = grid[0].length;

        int x = 0, y = getFirstValidColumn(grid, 0);
        int t;
        Direction direction = Direction.RIGHT;

        for (Instruction instruction : instructions) {
            if (instruction.getType() == InstructionType.MOVE) {
                Move move = (Move) instruction;
                int amount = move.amount();
                if (direction == Direction.RIGHT) {
                    for (t = 0; t < amount; t++) {
                        if (y + 1 == n || grid[x][y + 1] == UNINITIALIZED_RIGHT) {
                            int col = getFirstValidColumn(grid, x);
                            if (grid[x][col] != '#') {
                                y = col;
                            } else {
                                break;
                            }
                        } else if (grid[x][y + 1] == '.') {
                            y++;
                        } else if (grid[x][y + 1] == '#') {
                            break;
                        }
                    }
                } else if (direction == Direction.LEFT) {
                    for (t = 0; t < amount; t++) {
                        if (y - 1 == -1 || grid[x][y - 1] == UNINITIALIZED_LEFT) {
                            int col = getLastValidColumn(grid, x);
                            if (grid[x][col] != '#') {
                                y = col;
                            } else {
                                break;
                            }
                        } else if (grid[x][y - 1] == '.') {
                            y--;
                        } else if (grid[x][y - 1] == '#') {
                            break;
                        }
                    }
                } else if (direction == Direction.DOWN) {
                    for (t = 0; t < amount; t++) {
                        if (x + 1 == m
                                || grid[x + 1][y] == UNINITIALIZED_LEFT
                                || grid[x + 1][y] == UNINITIALIZED_RIGHT) {
                            int row = getFirstValidRow(grid, y);
                            if (grid[row][y] != '#') {
                                x = row;
                            } else {
                                break;
                            }
                        } else if (grid[x + 1][y] == '.') {
                            x++;
                        } else if (grid[x + 1][y] == '#') {
                            break;
                        }
                    }
                } else { //UP
                    for (t = 0; t < amount; t++) {
                        if (x - 1 == -1
                                || grid[x - 1][y] == UNINITIALIZED_LEFT
                                || grid[x - 1][y] == UNINITIALIZED_RIGHT) {
                            int row = getLastValidRow(grid, y);
                            if (grid[row][y] != '#') {
                                x = row;
                            } else {
                                break;
                            }
                        } else if (grid[x - 1][y] == '.') {
                            x--;
                        } else if (grid[x - 1][y] == '#') {
                            break;
                        }
                    }
                }
            } else {
                Turn turn = (Turn) instruction;
                if (turn.clockwise()) {
                    direction = switch (direction) {
                        case RIGHT -> Direction.DOWN;
                        case DOWN -> Direction.LEFT;
                        case LEFT -> Direction.UP;
                        case UP -> Direction.RIGHT;
                    };
                } else {
                    direction = switch (direction) {
                        case RIGHT -> Direction.UP;
                        case UP -> Direction.LEFT;
                        case LEFT -> Direction.DOWN;
                        case DOWN -> Direction.RIGHT;
                    };
                }
            }
        }

        return (1000 * (x + 1))
                + (4 * (y + 1))
                + switch (direction) {
            case RIGHT -> 0;
            case DOWN -> 1;
            case LEFT -> 2;
            case UP -> 3;
        };
    }

    private int getFirstValidColumn(char[][] grid, int row) {
        for (int j = 0; j < grid[row].length; j++) {
            if (grid[row][j] != UNINITIALIZED_LEFT) {
                return j;
            }
        }
        throw new RuntimeException();
    }

    private int getLastValidColumn(char[][] grid, int row) {
        for (int j = grid[row].length - 1; j >= 0; j--) {
            if (grid[row][j] != UNINITIALIZED_RIGHT) {
                return j;
            }
        }
        throw new RuntimeException();
    }

    private int getFirstValidRow(char[][] grid, int col) {
        for (int i = 0; i < grid.length; i++) {
            if (grid[i][col] != UNINITIALIZED_LEFT && grid[i][col] != UNINITIALIZED_RIGHT) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    private int getLastValidRow(char[][] grid, int col) {
        for (int i = grid.length - 1; i >= 0; i--) {
            if (grid[i][col] != UNINITIALIZED_LEFT && grid[i][col] != UNINITIALIZED_RIGHT) {
                return i;
            }
        }
        throw new RuntimeException();
    }
}
