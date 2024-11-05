package com.thiyagu_7.adventofcode.year2023.day10;

import com.thiyagu_7.adventofcode.util.Pair;
import com.thiyagu_7.adventofcode.util.Shoelace;
import com.thiyagu_7.adventofcode.year2023.day10.model.Position;
import com.thiyagu_7.adventofcode.year2023.day10.model.PositionAndConnectablePipes;
import com.thiyagu_7.adventofcode.year2023.day10.model.PossibleNextPositionsFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.IntStream;

public class SolutionDay10 {
    public int part1(List<String> input) {
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
        //try each of the pipes for the position 'S' (start position)
        List<Character> pipes = List.of('|', '-', 'L', 'J', '7', 'F');
        for (char pipe : pipes) {
            grid[startPosition.x()][startPosition.y()] = pipe;
            if (traverseToFindLoop(grid, startPosition, null)) {
                // have found the right pipe for 'S' to make a loop - so break
                break;
            }
        }
        return bfs(grid, startPosition);
    }

    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private boolean traverseToFindLoop(char[][] grid, Position currentPosition,
                                       List<Position> pipePositions) { //used for part 2
        int m = grid.length;
        int n = grid[0].length;
        Position startPosition = currentPosition;
        Map<Character, PossibleNextPositionsFunction> possibleNextPositionsFunctionMap =
                NextPositionsMapBuilder.buildNextPositionsMap();
        Position previousPosition = currentPosition;

        do {
            if (pipePositions != null) {
                pipePositions.add(currentPosition);
            }
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

    private int bfs(char[][] grid, Position currentPosition) {
        Queue<PositionAndDistance> queue = new LinkedList<>();
        int distance = 0;
        queue.add(new PositionAndDistance(currentPosition, distance));

        Set<Position> visited = new HashSet<>();
        Map<Character, PossibleNextPositionsFunction> possibleNextPositionsFunctionMap =
                NextPositionsMapBuilder.buildNextPositionsMap();

        while (!queue.isEmpty()) {
            PositionAndDistance positionAndDistance = queue.remove();
            currentPosition = positionAndDistance.position;

            char current = grid[currentPosition.x()][currentPosition.y()];
            Pair<PositionAndConnectablePipes, PositionAndConnectablePipes> twoPossiblePositions =
                    possibleNextPositionsFunctionMap.get(current)
                            .function()
                            .apply(currentPosition);

            PositionAndConnectablePipes position1 = twoPossiblePositions.getKey();
            PositionAndConnectablePipes position2 = twoPossiblePositions.getValue();

            distance = positionAndDistance.distance + 1;
            // we are following loop, so no need to check for connectivity
            // for 'S' two pipes will be added (two directions)
            if (!visited.contains(position1.position())) {
                queue.add(new PositionAndDistance(position1.position(), distance));
                visited.add(position1.position());
            }
            if (!visited.contains(position2.position())) {
                queue.add(new PositionAndDistance(position2.position(), distance));
                visited.add(position2.position());
            }
        }
        return distance - 1;
    }

    record PositionAndDistance(Position position, int distance) {

    }

    //Shoelace and Pick
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
        List<Position> pipePositions = new ArrayList<>();

        //try each of the pipes for the position 'S' (start position)
        List<Character> pipes = List.of('|', '-', 'L', 'J', '7', 'F');
        for (char pipe : pipes) {
            pipePositions = new ArrayList<>();
            grid[startPosition.x()][startPosition.y()] = pipe;
            if (traverseToFindLoop(grid, startPosition, pipePositions)) {
                // have found the right pipe for 'S' to make a loop - so break
                break;
            }
        }
        long area = Math.abs(Shoelace.shoeLace(pipePositions.stream()
                .map(p -> new com.thiyagu_7.adventofcode.util.Position(p.x(), p.y()))
                .toList()));
        // Pick theorem
        return (int) (area - (pipePositions.size() / 2) + 1);
    }
}
