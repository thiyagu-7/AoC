package com.thiyagu_7.adventofcode.year2023.day10;

import com.thiyagu_7.adventofcode.util.Pair;
import com.thiyagu_7.adventofcode.year2023.day10.model.Position;
import com.thiyagu_7.adventofcode.year2023.day10.model.PositionAndConnectablePipes;
import com.thiyagu_7.adventofcode.year2023.day10.model.PossibleNextPositionsFunction;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Using 3x3 pipe
 * https://www.reddit.com/r/adventofcode/comments/18fuxcu/2023_day_10_part_2_anyone_else_used_3x3_pipes/
 */
public class SolutionDay10Part2_3x3Pipe {
    public int part2(List<String> input) {
        char[][] grid = parseInput(input);

        Position startPosition = IntStream.range(0, grid.length)
                .mapToObj(i -> IntStream.range(0, grid[i].length)
                        .filter(j -> grid[i][j] == 'S')
                        .mapToObj(j -> new Position(i, j))
                        .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .get();
        char[][] expandedGrid = null;
        //to keep track of original cell which got expanded
        int[][] cellNums = new int[grid.length * 3][grid[0].length * 3];

        //try each of the pipes for the position 'S' (start position)
        List<Character> pipes = List.of('|', '-', 'L', 'J', '7', 'F');
        Set<Position> pipePositions = new HashSet<>();

        for (char pipe : pipes) {
            grid[startPosition.x()][startPosition.y()] = pipe;
            expandedGrid = expandGrid(grid, cellNums);
            //update S position
            Position newStartPosition = new Position(startPosition.x() * 3 + 1, startPosition.y() * 3 + 1);
            pipePositions = new HashSet<>();
            if (traverseToFindLoop(expandedGrid, newStartPosition, pipePositions)) {
                // have found the right pipe for 'S' to make a loop - so break
                break;
            }
        }
        //(0, 0) is a new/expanded cell
        bfsAndMark(expandedGrid, new Position(0, 0), pipePositions, new HashSet<>());

        Set<Integer> vals = new HashSet<>();
        for (int i = 0; i < expandedGrid.length; i++) {
            for (int j = 0; j < expandedGrid[0].length; j++) {
                if (expandedGrid[i][j] != 'X') {
                    int num = cellNums[i][j];
                    //not part of pipe edge or pipe edge expansion
                    boolean f = true;
                    for (Position p : pipePositions) {
                        if (cellNums[p.x()][p.y()] == num) {
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        vals.add(cellNums[i][j]);
                    }
                }
            }
        }
        return vals.size();
    }

    private boolean traverseToFindLoop(char[][] grid, Position currentPosition,
                                       Set<Position> pipePositions) {
        int m = grid.length;
        int n = grid[0].length;
        Position startPosition = currentPosition;
        Map<Character, PossibleNextPositionsFunction> possibleNextPositionsFunctionMap =
                NextPositionsMapBuilder.buildNextPositionsMap();
        Position previousPosition = currentPosition;

        do {
            pipePositions.add(currentPosition);

            char current = grid[currentPosition.x()][currentPosition.y()];
            Pair<PositionAndConnectablePipes, PositionAndConnectablePipes> twoPossiblePositions =
                    possibleNextPositionsFunctionMap.get(current)
                            .function()
                            .apply(currentPosition);

            PositionAndConnectablePipes position1 = twoPossiblePositions.getKey();
            PositionAndConnectablePipes position2 = twoPossiblePositions.getValue();

            // if position[1|2].position is connected && position[1|2].position is not the previous position
            if (isValid(position1.position(), m, n)
                    && position1.allowedPipes().contains(grid[position1.position().x()][position1.position().y()])
                    && !previousPosition.equals(position1.position())) {
                previousPosition = currentPosition;
                currentPosition = position1.position();
            } else if (isValid(position2.position(), m, n)
                    && position2.allowedPipes().contains(grid[position2.position().x()][position2.position().y()])
                    && !previousPosition.equals(position2.position())) {
                previousPosition = currentPosition;
                currentPosition = position2.position();
            } else {
                // no valid connecting pipe
                return false;
            }
        } while (!currentPosition.equals(startPosition));
        //found a loop
        return true;
    }

    private boolean isValid(Position position, int m, int n) {
        return position.x() >= 0 && position.x() < m && position.y() >= 0 && position.y() < n;
    }

    private char[][] expandGrid(char[][] grid, int[][] cellNums) {
        int num = 1;
        char[][] expandedGrid = new char[grid.length * 3][grid[0].length * 3];
        int k = 0;

        for (int i = 0; i < grid.length; i++, k += 3) {
            int l = 0;
            for (int j = 0; j < grid[0].length; j++) {
                char[][] expandedCell = expandCell(grid[i][j]);
                int previousL = l;

                for (int ii = 0; ii < expandedCell.length; ii++) {
                    l = previousL;
                    for (int jj = 0; jj < expandedCell[0].length; jj++, l++) {
                        expandedGrid[k + ii][l] = expandedCell[ii][jj];
                        cellNums[k + ii][l] = num;
                    }
                }
                num++;
            }
        }
        return expandedGrid;
    }

    private char[][] expandCell(char cell) {
        return switch (cell) {
            case '.' -> new char[][]{
                    {'.', '.', '.'},
                    {'.', '.', '.'},
                    {'.', '.', '.'}
            };
            case '|' -> new char[][]{
                    {'.', '|', '.'},
                    {'.', '|', '.'},
                    {'.', '|', '.'}
            };
            case '-' -> new char[][]{
                    {'.', '.', '.'},
                    {'-', '-', '-'},
                    {'.', '.', '.'}
            };

            case 'L' -> new char[][]{
                    {'.', '|', '.'},
                    {'.', 'L', '-'},
                    {'.', '.', '.'}
            };
            case 'J' -> new char[][]{
                    {'.', '|', '.'},
                    {'-', 'J', '.'},
                    {'.', '.', '.'}
            };
            case '7' -> new char[][]{
                    {'.', '.', '.'},
                    {'-', '7', '.'},
                    {'.', '|', '.'}
            };
            case 'F' -> new char[][]{
                    {'.', '.', '.'},
                    {'.', 'F', '-'},
                    {'.', '|', '.'}
            };
            default -> throw new IllegalStateException("Unexpected value: " + cell);
        };
    }


    private void bfsAndMark(char[][] grid, Position currentPosition,
                     Set<Position> pipeEdges,
                     Set<Position> visited) {
        int m = grid.length;
        int n = grid[0].length;

        Queue<Position> queue = new LinkedList<>();
        queue.add(currentPosition);

        int[] xCoordinates = new int[]{0, 0, -1, 1};
        int[] yCoordinates = new int[]{-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            currentPosition = queue.remove();
            // mark cell
            grid[currentPosition.x()][currentPosition.y()] = 'X';

            for (int k = 0; k < 4; k++) {
                Position newPosition = new Position(
                        currentPosition.x() + xCoordinates[k],
                        currentPosition.y() + yCoordinates[k]);
                if (isValid(newPosition, m, n)
                        && !visited.contains(newPosition)
                        & !pipeEdges.contains(newPosition)) {
                    visited.add(newPosition);
                    queue.add(newPosition);
                }
            }
        }
    }

    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }
}
