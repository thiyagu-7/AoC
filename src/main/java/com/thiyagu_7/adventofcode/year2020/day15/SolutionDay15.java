package com.thiyagu_7.adventofcode.year2020.day15;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SolutionDay15 {
    public int part1(List<Integer> startingNumbers) {
        return getNthSpokenWord(startingNumbers, 2020);
    }

    private int getNthSpokenWord(List<Integer> startingNumbers, int n) {
        {
            Map<Integer, Integer> numberToPreviousTurn = new HashMap<>();
            Map<Integer, Integer> numberToRecentTurn = new HashMap<>();
            IntStream.range(0, startingNumbers.size())
                    .forEach(i -> numberToRecentTurn.put(startingNumbers.get(i), i + 1));

            int lastSpoken = startingNumbers.get(startingNumbers.size() - 1);
            int turn = startingNumbers.size() + 1;

            while (turn <= n) {
                if (numberToRecentTurn.containsKey(lastSpoken) && numberToPreviousTurn.containsKey(lastSpoken)) {
                    lastSpoken = numberToRecentTurn.get(lastSpoken) - numberToPreviousTurn.get(lastSpoken);
                } else {
                    lastSpoken = 0;
                }

                if (numberToRecentTurn.containsKey(lastSpoken)) {
                    int recent = numberToRecentTurn.get(lastSpoken);
                    numberToPreviousTurn.put(lastSpoken, recent);
                }
                numberToRecentTurn.put(lastSpoken, turn);

                turn++;
            }
            return lastSpoken;
        }
    }
    public int part2(List<Integer> startingNumbers) {
        return getNthSpokenWord(startingNumbers, 30000000);
    }
}
