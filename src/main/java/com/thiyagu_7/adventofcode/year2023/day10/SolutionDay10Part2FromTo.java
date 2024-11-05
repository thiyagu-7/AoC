package com.thiyagu_7.adventofcode.year2023.day10;

import com.thiyagu_7.adventofcode.util.Pair;
import com.thiyagu_7.adventofcode.year2023.day10.model.Position;
import com.thiyagu_7.adventofcode.year2023.day10.model.PositionAndConnectablePipes;
import com.thiyagu_7.adventofcode.year2023.day10.model.PossibleNextPositionsFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * Uses From...To and To...From
 */
public class SolutionDay10Part2FromTo {
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
        //try each of the pipes for the position 'S' (start position)
        List<Character> pipes = List.of('|', '-', 'L', 'J', '7', 'F');
        Set<Position> pipePositions = new HashSet<>();
        TreeMap<Integer, List<FromTo>> fromToMap = new TreeMap<>();

        for (char pipe : pipes) {
            grid[startPosition.x()][startPosition.y()] = pipe;
            fromToMap = new TreeMap<>();
            pipePositions = new HashSet<>();
            if (traverse(grid, startPosition, pipePositions, fromToMap)) {
                // have found the right pipe for 'S' to make a loop
                break;
            }
        }

        Set<Position> tilePositionsInsideLoop = new HashSet<>();
        while (!fromToMap.isEmpty()) {
            Map.Entry<Integer, List<FromTo>> firstEntry = fromToMap.firstEntry();
            for (FromTo fromTo : firstEntry.getValue()) {
                FromTo toFrom = new FromTo(fromTo.y, fromTo.x);
                int k = -1;
                //find the first row in the TreeMap which has to...from
                for (Map.Entry<Integer, List<FromTo>> entry : fromToMap.entrySet()) {
                    if (firstEntry.getKey().equals(entry.getKey())) {
                        continue;
                    }
                    if (entry.getValue().contains(toFrom)) {
                        k = entry.getKey();
                        break;
                    }
                }
                if (k != -1) {
                    int start = firstEntry.getKey();
                    for (int i = start; i <= k; i++) {
                        if (!pipePositions.contains(new Position(i, fromTo.y()))) { //fromTo.x also works
                            tilePositionsInsideLoop.add(new Position(i, fromTo.y));
                        }
                    }
                    fromToMap.get(k)
                            .remove(toFrom);
                }
            }
            fromToMap.remove(firstEntry.getKey());
        }
        return tilePositionsInsideLoop.size();
    }

    record FromTo(int x, int y) {

    }


    private char[][] parseInput(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private boolean traverse(char[][] grid, Position currentPosition,
                             Set<Position> pipePositions,
                             TreeMap<Integer, List<FromTo>> fromToMap) {
        int m = grid.length;
        int n = grid[0].length;
        Position startPosition = currentPosition;
        Map<Character, PossibleNextPositionsFunction> possibleNextPositionsFunctionMap =
                NextPositionsMapBuilder.buildNextPositionsMap();
        Position previousPosition = currentPosition;

        do {
            //add to set of pipe positions
            pipePositions.add(currentPosition);

            char currentPipe = grid[currentPosition.x()][currentPosition.y()];
            Pair<PositionAndConnectablePipes, PositionAndConnectablePipes> twoPossiblePositions =
                    possibleNextPositionsFunctionMap.get(currentPipe)
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
            } else if (
                    isValid(position2.position(), m, n)
                            &&
                            position2.allowedPipes().contains(grid[position2.position().x()][position2.position().y()])
                            && !previousPosition.equals(position2.position())) {
                previousPosition = currentPosition;
                currentPosition = position2.position();
            } else {
                // no valid connecting pipe
                return false;
            }
            //update from...to in the map
            if (previousPosition.x() == currentPosition.x()) {
                fromToMap.computeIfAbsent(previousPosition.x(), x -> new ArrayList<>())
                        .add(new FromTo(previousPosition.y(), currentPosition.y()));
            }
        } while (!currentPosition.equals(startPosition));
        //found a loop
        return true;
    }

    private boolean isValid(Position position, int m, int n) {
        return position.x() >= 0 && position.x() < m && position.y() >= 0 && position.y() < n;
    }
}
