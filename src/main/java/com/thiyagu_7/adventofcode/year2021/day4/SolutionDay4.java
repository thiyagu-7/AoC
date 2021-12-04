package com.thiyagu_7.adventofcode.year2021.day4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionDay4 {
    private static final int SIZE = 5; // row and column length

    //part 1 - first board to win
    public int part1(List<String> ip) {
        return playBingo(ip, WINNER_STRATEGY.FIRST_BOARD_WINS);
    }

    //part 2 - last board to win
    public int part2(List<String> ip) {
        return playBingo(ip, WINNER_STRATEGY.LAST_BOARD_WINS);
    }

    private enum WINNER_STRATEGY {
        FIRST_BOARD_WINS,
        LAST_BOARD_WINS
    }

    private int playBingo(List<String> ip, WINNER_STRATEGY strategy) {
        Input input = parseInput(ip);
        List<Integer> numbers = Arrays.stream(input.numbers.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<int[][]> boards = input.boards;

        int numOfBoardsShouldHaveWon = strategy == WINNER_STRATEGY.FIRST_BOARD_WINS
                ? 1
                : boards.size();

        // for each board, map number -> index
        List<Map<Integer, int[]>> numToIndexes = new ArrayList<>();
        for (int[][] currentBoard : boards) {
            Map<Integer, int[]> indexesMap = new HashMap<>();
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    indexesMap.put(currentBoard[i][j], new int[]{i, j});
                }
            }
            numToIndexes.add(indexesMap);
        }

        // for each board, keep track of number of drawn (marked) numbers per row and col
        List<MarkedEntries> allMarkedEntries = IntStream.range(0, boards.size())
                .mapToObj(ignoreMe -> new MarkedEntries())
                .collect(Collectors.toList());

        // won boards by index
        Set<Integer> wonBoards = new HashSet<>();

        for (Integer num : numbers) {
            for (int boardNum = 0; boardNum < boards.size(); boardNum++) {
                // Note: Numbers may not appear on all boards
                int[] index = numToIndexes.get(boardNum)
                        .get(num);
                if (index != null) {
                    int row = index[0];
                    int col = index[1];
                    int[][] currentBoard = boards.get(boardNum);
                    currentBoard[row][col] = -1;
                    // update markedEntries
                    MarkedEntries markedEntries = allMarkedEntries.get(boardNum);
                    markedEntries.numberOfMarkedEntriesByRow[row]++;
                    markedEntries.numberOfMarkedEntriesByCol[col]++;

                    if (markedEntries.numberOfMarkedEntriesByRow[row] == SIZE
                            || markedEntries.numberOfMarkedEntriesByCol[col] == SIZE) {
                        wonBoards.add(boardNum);
                        if (wonBoards.size() == numOfBoardsShouldHaveWon) {
                            int sum = 0;
                            for (int[] oneRow : currentBoard) {
                                for (int val : oneRow) {
                                    if (val != -1) {
                                        sum += val;
                                    }
                                }
                            }
                            return sum * num;
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Oops.. Something is wrong.. No winner found");
    }


    public Input parseInput(List<String> input) {
        String numbers = input.get(0);
        List<int[][]> boards = new ArrayList<>();

        List<String> currentRows = new ArrayList<>();
        for (int i = 2; i < input.size(); i++) {
            String row = input.get(i);
            if (row.isEmpty()) {
                boards.add(convertToIntArray(currentRows));
                currentRows = new ArrayList<>();
            } else {
                currentRows.add(row);
            }
        }
        boards.add(convertToIntArray(currentRows));
        return new Input(numbers, boards);
    }

    private int[][] convertToIntArray(List<String> currentRows) {
        return currentRows.stream()
                // input has leading spaces and multiple spaces
                .map(oneRow -> oneRow.trim().split("\\s+"))
                .map(strArr -> Arrays.stream(strArr)
                        .mapToInt(Integer::parseInt)
                        .toArray()) // Stream<int[]>
                .toArray(int[][]::new);
    }

    private static class Input {
        private final String numbers;
        private final List<int[][]> boards;

        private Input(String numbers, List<int[][]> boards) {
            this.numbers = numbers;
            this.boards = boards;
        }
    }

    private static class MarkedEntries {
        // how many entries are marked in a given row
        private final int[] numberOfMarkedEntriesByRow = new int[SIZE];
        // how many entries are marked in a given col
        private final int[] numberOfMarkedEntriesByCol = new int[SIZE];
    }
}
