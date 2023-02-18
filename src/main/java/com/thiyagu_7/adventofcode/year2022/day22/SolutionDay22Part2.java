package com.thiyagu_7.adventofcode.year2022.day22;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

// part2Sample and part2 are tailored as per the input
// final answer derived based on the last position
public class SolutionDay22Part2 {

    public Location part2Sample(List<String> input) {
        char[][] grid = CommonUtil.parseAsGrid(input);
        String instructions = input.get(input.size() - 1);

        // tailored to the input
        char[][] one = getSubGrid(grid, 0, 8, 4);
        char[][] two = getSubGrid(grid, 4, 0, 4);
        char[][] three = getSubGrid(grid, 4, 4, 4);
        char[][] four = getSubGrid(grid, 4, 8, 4);
        char[][] five = getSubGrid(grid, 8, 8, 4);
        char[][] six = getSubGrid(grid, 8, 12, 4);

        // tailored to the input
        Map<CubeSide, char[][]> c = new HashMap<>();
        c.put(CubeSide.TOP, four);
        c.put(CubeSide.LEFT, three);
        c.put(CubeSide.RIGHT, rotateAnti((six)));
        c.put(CubeSide.BOTTOM, two);
        c.put(CubeSide.FRONT, five);
        c.put(CubeSide.BACK, reverseCol(reverseRow(one)));

        return tracePath(c, CommonUtil.parseInstructions(instructions),
                CubeSide.BACK, new Position(3, 3), Direction.LEFT, 4);
    }

    public Location part2(List<String> input) {
        char[][] grid = CommonUtil.parseAsGrid(input);
        String instructions = input.get(input.size() - 1);
        int faceSize = 50;

        // tailored to the input
        char[][] one = getSubGrid(grid, 0, 50, faceSize);
        char[][] two = getSubGrid(grid, 0, 100, faceSize);
        char[][] three = getSubGrid(grid, 50, 50, faceSize);
        char[][] four = getSubGrid(grid, 100, 0, faceSize);
        char[][] five = getSubGrid(grid, 100, 50, faceSize);
        char[][] six = getSubGrid(grid, 150, 0, faceSize);

        // tailored to the input
        Map<CubeSide, char[][]> c = new HashMap<>();
        c.put(CubeSide.TOP, three);
        c.put(CubeSide.LEFT, rotate(four));
        c.put(CubeSide.RIGHT, rotate((two)));
        c.put(CubeSide.BOTTOM, rotate(six));
        c.put(CubeSide.FRONT, five);
        c.put(CubeSide.BACK, reverseCol(reverseRow(one)));


        return tracePath(c, CommonUtil.parseInstructions(instructions),
                CubeSide.BACK, new Position(faceSize - 1, faceSize - 1), Direction.LEFT, faceSize);
    }

    record Position(int x, int y) {
    }

    record Location(CubeSide cubeSide, Position position, Direction direction) {
    }

    public Location tracePath(Map<CubeSide, char[][]> cubeSideMap,
                              List<Instruction> instructions,
                              CubeSide cubeSide,
                              Position position,
                              Direction direction,
                              int faceSize) {
        CubeSide currentCubeSide = cubeSide;
        Direction currentDirection = direction;
        Position currentPosition = position;

        for (Instruction instruction : instructions) {
            if (instruction.getType() == InstructionType.MOVE) {
                Move move = (Move) instruction;
                int amount = move.amount();
                for (int t = 0; t < amount; t++) {
                    Location currentLocation = new Location(currentCubeSide, currentPosition, currentDirection);
                    Location newLocation = move(cubeSideMap, currentLocation, faceSize);
                    if (newLocation == null) {
                        break;
                    }
                    currentCubeSide = newLocation.cubeSide;
                    currentPosition = newLocation.position;
                    currentDirection = newLocation.direction;
                }
            } else {
                Turn turn = (Turn) instruction;
                if (turn.clockwise()) {
                    currentDirection = switch (currentDirection) {
                        case RIGHT -> Direction.DOWN;
                        case DOWN -> Direction.LEFT;
                        case LEFT -> Direction.UP;
                        case UP -> Direction.RIGHT;
                    };
                } else {
                    currentDirection = switch (currentDirection) {
                        case RIGHT -> Direction.UP;
                        case UP -> Direction.LEFT;
                        case LEFT -> Direction.DOWN;
                        case DOWN -> Direction.RIGHT;
                    };
                }

            }
        }
        /*
        System.out.println("Cube " + currentCubeSide);
        System.out.println(currentPosition);
        System.out.println(currentDirection);
        */
        return new Location(currentCubeSide, currentPosition, currentDirection);
    }

    public Location move(Map<CubeSide, char[][]> cubeSideMap, Location currentLocation, int faceSize) {
        return switch (currentLocation.cubeSide) {
            case TOP -> handleTop(cubeSideMap, currentLocation, faceSize);
            case LEFT -> handleLeft(cubeSideMap, currentLocation, faceSize);
            case RIGHT -> handleRight(cubeSideMap, currentLocation, faceSize);
            case BOTTOM -> handleBottom(cubeSideMap, currentLocation, faceSize);
            case FRONT -> handleFront(cubeSideMap, currentLocation, faceSize);
            case BACK -> handleBack(cubeSideMap, currentLocation, faceSize);
        };
    }

    //1
    private Location handleTop(Map<CubeSide, char[][]> cubeSideMap, Location currentLocation, int n) {
        CubeSide curentCubeSide = currentLocation.cubeSide;
        Direction currentDirection = currentLocation.direction;
        Position position = currentLocation.position;
        int x = position.x;
        int y = position.y;
        int newX, newY;
        CubeSide newCubeSide;
        char[][] grid = cubeSideMap.get(currentLocation.cubeSide);

        if (currentDirection == Direction.RIGHT) {
            if (y + 1 == n) {
                newCubeSide = CubeSide.RIGHT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = x;
                newY = 0;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            currentDirection);
                } else {
                    return null;
                }
            } else if (grid[x][y + 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y + 1),
                        currentDirection);
            } else if (grid[x][y + 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.LEFT) {
            if (y - 1 == -1) {
                newCubeSide = CubeSide.LEFT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = x;
                newY = n - 1;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            currentDirection);
                } else {
                    return null;
                }
            } else if (grid[x][y - 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y - 1),
                        currentDirection);
            } else if (grid[x][y - 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.DOWN) {
            if (x + 1 == n) {
                newCubeSide = CubeSide.FRONT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = 0;
                newY = y;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            currentDirection);
                } else {
                    return null;
                }
            } else if (grid[x + 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x + 1, y),
                        currentDirection);
            } else if (grid[x + 1][y] == '#') {
                return null;
            }
        } else { //UP
            if (x - 1 == -1) {
                newCubeSide = CubeSide.BACK;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = 0; //SEE
                newY = n - y - 1; //SEE
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.DOWN); //see
                } else {
                    return null;
                }
            } else if (grid[x - 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x - 1, y),
                        currentDirection);
            } else if (grid[x - 1][y] == '#') {
                return null;
            }
        }
        throw new RuntimeException();
    }

    //2
    private Location handleLeft(Map<CubeSide, char[][]> cubeSideMap, Location currentLocation, int n) {
        CubeSide curentCubeSide = currentLocation.cubeSide;
        Direction currentDirection = currentLocation.direction;
        Position position = currentLocation.position;
        int x = position.x;
        int y = position.y;
        int newX, newY;
        CubeSide newCubeSide;
        char[][] grid = cubeSideMap.get(currentLocation.cubeSide);

        if (currentDirection == Direction.RIGHT) {
            if (y + 1 == n) {
                newCubeSide = CubeSide.TOP;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = x;
                newY = 0;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            currentDirection);
                } else {
                    return null;
                }
            } else if (grid[x][y + 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y + 1),
                        currentDirection);
            } else if (grid[x][y + 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.LEFT) {
            if (y - 1 == -1) {
                newCubeSide = CubeSide.BOTTOM;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = x;
                newY = n - 1;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            currentDirection);
                } else {
                    return null;
                }
            } else if (grid[x][y - 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y - 1),
                        currentDirection);
            } else if (grid[x][y - 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.DOWN) {
            if (x + 1 == n) {
                newCubeSide = CubeSide.FRONT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);

                newX = n - y - 1; //SEE - using y
                newY = 0; //SEE
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.RIGHT); //see
                } else {
                    return null;
                }
            } else if (grid[x + 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x + 1, y),
                        currentDirection);
            } else if (grid[x + 1][y] == '#') {
                return null;
            }
        } else { //UP
            if (x - 1 == -1) {
                newCubeSide = CubeSide.BACK;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = n - y - 1; //SEE
                newY = n - 1; //see
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.LEFT); //see
                } else {
                    return null;
                }
            } else if (grid[x - 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x - 1, y),
                        currentDirection);
            } else if (grid[x - 1][y] == '#') {
                return null;
            }
        }
        throw new RuntimeException();
    }

    //3
    private Location handleRight(Map<CubeSide, char[][]> cubeSideMap, Location currentLocation, int n) {
        CubeSide curentCubeSide = currentLocation.cubeSide;
        Direction currentDirection = currentLocation.direction;
        Position position = currentLocation.position;
        int x = position.x;
        int y = position.y;
        int newX, newY;
        CubeSide newCubeSide;
        char[][] grid = cubeSideMap.get(currentLocation.cubeSide);

        if (currentDirection == Direction.RIGHT) {
            if (y + 1 == n) {
                newCubeSide = CubeSide.BOTTOM;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = x;
                newY = 0;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            currentDirection);
                } else {
                    return null;
                }
            } else if (grid[x][y + 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y + 1),
                        currentDirection);
            } else if (grid[x][y + 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.LEFT) {
            if (y - 1 == -1) {
                newCubeSide = CubeSide.TOP;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = x;
                newY = n - 1;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            currentDirection);
                } else {
                    return null;
                }
            } else if (grid[x][y - 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y - 1),
                        currentDirection);
            } else if (grid[x][y - 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.DOWN) {
            if (x + 1 == n) {
                newCubeSide = CubeSide.FRONT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = y; //SEE using y
                newY = n - 1;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.LEFT); //see
                } else {
                    return null;
                }
            } else if (grid[x + 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x + 1, y),
                        currentDirection);
            } else if (grid[x + 1][y] == '#') {
                return null;
            }
        } else { //UP
            if (x - 1 == -1) {
                newCubeSide = CubeSide.BACK;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = y;
                newY = 0;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.RIGHT); //see
                } else {
                    return null;
                }
            } else if (grid[x - 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x - 1, y),
                        currentDirection);
            } else if (grid[x - 1][y] == '#') {
                return null;
            }
        }
        throw new RuntimeException();
    }

    //4
    private Location handleBottom(Map<CubeSide, char[][]> cubeSideMap, Location currentLocation, int n) {
        CubeSide curentCubeSide = currentLocation.cubeSide;
        Direction currentDirection = currentLocation.direction;
        Position position = currentLocation.position;
        int x = position.x;
        int y = position.y;
        int newX, newY;
        CubeSide newCubeSide;
        char[][] grid = cubeSideMap.get(currentLocation.cubeSide);

        if (currentDirection == Direction.RIGHT) {
            if (y + 1 == n) {
                newCubeSide = CubeSide.LEFT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = x;
                newY = 0;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            currentDirection);
                } else {
                    return null;
                }
            } else if (grid[x][y + 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y + 1),
                        currentDirection);
            } else if (grid[x][y + 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.LEFT) {
            if (y - 1 == -1) {
                newCubeSide = CubeSide.RIGHT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = x;
                newY = n - 1;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            currentDirection);
                } else {
                    return null;
                }
            } else if (grid[x][y - 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y - 1),
                        currentDirection);
            } else if (grid[x][y - 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.DOWN) {
            if (x + 1 == n) {
                newCubeSide = CubeSide.FRONT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = n - 1; //SEE
                newY = n - y - 1; //SEE
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.UP); //see
                } else {
                    return null;
                }
            } else if (grid[x + 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x + 1, y),
                        currentDirection);
            } else if (grid[x + 1][y] == '#') {
                return null;
            }
        } else { //UP
            if (x - 1 == -1) {
                newCubeSide = CubeSide.BACK;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = n - 1;
                newY = y; //see
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.UP); //see
                } else {
                    return null;
                }
            } else if (grid[x - 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x - 1, y),
                        currentDirection);
            } else if (grid[x - 1][y] == '#') {
                return null;
            }
        }
        throw new RuntimeException();
    }

    //5
    private Location handleFront(Map<CubeSide, char[][]> cubeSideMap, Location currentLocation, int n) {
        CubeSide curentCubeSide = currentLocation.cubeSide;
        Direction currentDirection = currentLocation.direction;
        Position position = currentLocation.position;
        int x = position.x;
        int y = position.y;
        int newX, newY;
        CubeSide newCubeSide;
        char[][] grid = cubeSideMap.get(currentLocation.cubeSide);

        if (currentDirection == Direction.RIGHT) {
            if (y + 1 == n) {
                newCubeSide = CubeSide.RIGHT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = n - 1;
                newY = x; //SEE
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.UP); //see
                } else {
                    return null;
                }
            } else if (grid[x][y + 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y + 1),
                        currentDirection);
            } else if (grid[x][y + 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.LEFT) {
            if (y - 1 == -1) {
                newCubeSide = CubeSide.LEFT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = n - 1; //SEE
                newY = n - x - 1;//SEE using x
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.UP); //see
                } else {
                    return null;
                }
            } else if (grid[x][y - 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y - 1),
                        currentDirection);
            } else if (grid[x][y - 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.DOWN) {
            if (x + 1 == n) {
                newCubeSide = CubeSide.BOTTOM;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = n - 1;
                newY = n - y - 1; //SEE
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.UP); //see
                } else {
                    return null;
                }
            } else if (grid[x + 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x + 1, y),
                        currentDirection);
            } else if (grid[x + 1][y] == '#') {
                return null;
            }
        } else { //UP
            if (x - 1 == -1) {
                newCubeSide = CubeSide.TOP;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = n - 1;
                newY = y;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.UP); //see
                } else {
                    return null;
                }
            } else if (grid[x - 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x - 1, y),
                        currentDirection);
            } else if (grid[x - 1][y] == '#') {
                return null;
            }
        }
        throw new RuntimeException();
    }

    //6
    private Location handleBack(Map<CubeSide, char[][]> cubeSideMap, Location currentLocation, int n) {
        CubeSide curentCubeSide = currentLocation.cubeSide;
        Direction currentDirection = currentLocation.direction;
        Position position = currentLocation.position;
        int x = position.x;
        int y = position.y;
        int newX, newY;
        CubeSide newCubeSide;
        char[][] grid = cubeSideMap.get(currentLocation.cubeSide);

        if (currentDirection == Direction.RIGHT) {
            if (y + 1 == n) {
                newCubeSide = CubeSide.LEFT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = 0;
                newY = n - x - 1;//SEE
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.DOWN); //see
                } else {
                    return null;
                }
            } else if (grid[x][y + 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y + 1),
                        currentDirection);
            } else if (grid[x][y + 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.LEFT) {
            if (y - 1 == -1) {
                newCubeSide = CubeSide.RIGHT;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = 0;
                newY = x;
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.DOWN); //see
                } else {
                    return null;
                }
            } else if (grid[x][y - 1] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x, y - 1),
                        currentDirection);
            } else if (grid[x][y - 1] == '#') {
                return null;
            }
        } else if (currentDirection == Direction.DOWN) {
            if (x + 1 == n) {
                newCubeSide = CubeSide.BOTTOM;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = 0;
                newY = y;//see
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.DOWN); //see
                } else {
                    return null;
                }
            } else if (grid[x + 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x + 1, y),
                        currentDirection);
            } else if (grid[x + 1][y] == '#') {
                return null;
            }
        } else { //UP
            if (x - 1 == -1) {
                newCubeSide = CubeSide.TOP;
                char[][] newGrid = cubeSideMap.get(newCubeSide);
                newX = 0; //SEE
                newY = n - y - 1; //SEE
                if (newGrid[newX][newY] != '#') {
                    return new Location(
                            newCubeSide,
                            new Position(newX, newY),
                            Direction.DOWN); //see
                } else {
                    return null;
                }
            } else if (grid[x - 1][y] == '.') {
                return new Location(
                        curentCubeSide,
                        new Position(x - 1, y),
                        currentDirection);
            } else if (grid[x - 1][y] == '#') {
                return null;
            }
        }
        throw new RuntimeException();
    }

    enum CubeSide {
        TOP, RIGHT, BOTTOM, LEFT,
        FRONT, BACK;
    }

    private char[][] getSubGrid(char[][] grid, int startRow, int startCol, int faceSize) {
        char[][] finalGrid = new char[faceSize][faceSize];
        for (int i = 0, x = startRow; i < faceSize; i++, x++) {
            for (int j = 0, y = startCol; j < faceSize; j++, y++) {
                finalGrid[i][j] = grid[x][y];
            }
        }
        return finalGrid;
    }

    //clockwise
    private char[][] rotate(char[][] grid) {
        int n = grid.length;
        char[][] rotatedGrid = new char[n][n];

        int k = 0, l;
        for (int j = 0; j < n; j++) {
            l = 0;
            for (int i = n - 1; i >= 0; i--) {
                rotatedGrid[k][l++] = grid[i][j];
            }
            k++;
        }
        return rotatedGrid;
    }

    private char[][] rotateAnti(char[][] grid) {
        int n = grid.length;
        char[][] rotatedGrid = new char[n][n];

        int k = 0, l;
        for (int j = n - 1; j >= 0; j--) {
            l = 0;
            for (int i = 0; i < n; i++) {
                rotatedGrid[k][l++] = grid[i][j];
            }
            k++;
        }
        return rotatedGrid;
    }

    private char[][] reverseRow(char[][] grid) {
        int n = grid.length;
        char[][] rotatedGrid = new char[n][n];
        for (int i = 0; i < n; i++) {
            rotatedGrid[i] = grid[n - i - 1];
        }
        return rotatedGrid;
    }

    private char[][] reverseCol(char[][] grid) {
        int n = grid.length;
        char[][] rotatedGrid = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotatedGrid[i][n - j - 1] = grid[i][j];
            }
        }
        return rotatedGrid;
    }
}
