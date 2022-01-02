package com.thiyagu_7.adventofcode.year2021.day21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionDay21 {
    public int part1(int player1Position, int player2Position) {
        int player1Score = 0;
        int player2Score = 0;
        int nextDiceValue = 1;
        int numDiceRolls = 0;
        boolean player1Playing = true;

        while (player1Score < 1000 && player2Score < 1000) {
            // sum of nextDiceValue, nextDiceValue + 1, nextDiceValue + 2
            int currentMoveScore;
            if (nextDiceValue == 99) {
                currentMoveScore = 99 + 100 + 1;
                nextDiceValue = 2;
            } else {
                currentMoveScore = (nextDiceValue * 3) + 3;
                nextDiceValue += 3;
            }

            if (player1Playing) {
                player1Position = move(player1Position, currentMoveScore);
                player1Score += player1Position;
            } else {
                player2Position = move(player2Position, currentMoveScore);
                player2Score += player2Position;
            }
            numDiceRolls += 3;
            player1Playing = !player1Playing;
        }
        if (player1Score >= 1000) {
            return numDiceRolls * player2Score;
        }
        return numDiceRolls * player1Score;

    }

    public int part1Recur(int player1Position, int player2Position) {
        return part1Recur(player1Position, player2Position, 0, 0,
                true, 1, 0);
    }

    private int part1Recur(int player1Position, int player2Position, int player1Score, int player2Score,
                           boolean player1Playing,
                           int nextDiceValue, int numDiceRolls) {
        if (player1Score >= 1000) {
            return numDiceRolls * player2Score;
        } else if (player2Score >= 1000) {
            return numDiceRolls * player1Score;
        }
        int currentMoveScore;
        if (nextDiceValue == 99) {
            currentMoveScore = 99 + 100 + 1;
            nextDiceValue = 2;
        } else {
            currentMoveScore = (nextDiceValue * 3) + 3;
            nextDiceValue += 3;
        }
        if (player1Playing) {
            player1Position = move(player1Position, currentMoveScore);
            player1Score += player1Position;
        } else {
            player2Position = move(player2Position, currentMoveScore);
            player2Score += player2Position;
        }
        return part1Recur(player1Position, player2Position, player1Score, player2Score, !player1Playing,
                nextDiceValue, numDiceRolls + 3);
    }

    private int move(int playerPosition, int currentMoveScore) {
        int newPlayerPosition = (playerPosition + currentMoveScore) % 10;
        return newPlayerPosition == 0 ? 10 : newPlayerPosition;
    }

    public long part2(int player1Position, int player2Position) {
        //when using allToCount
        //List<Integer> combinations = new ArrayList<>(new HashSet<>(getAllCombinations()));
        List<Integer> combinations = getAllCombinations();

        Map<String, Result> cache = new HashMap<>();
        Result res = part2Recur(player1Position, player2Position, 0, 0, true,
                combinations, cache);
        System.out.println(res.aWins);
        System.out.println(res.bWins);
        return Math.max(res.aWins, res.bWins);
    }

    /*static Map<Integer, Long> allToCount;
    static {
        allToCount = getAllCombinations().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }*/

    public Result part2Recur(int player1Position, int player2Position, int player1Score, int player2Score,
                             boolean player1Playing,
                             List<Integer> combinations,
                             Map<String, Result> cache) {
        String cacheKey = player1Position + "|" + player2Position
                + "|" + player1Score + "|" + player2Score
                + "|" + player1Playing;
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }
        if (player1Score >= 21) {
            return new Result(1L, 0L);
        } else if (player2Score >= 21) {
            return new Result(0L, 1L);
        }
        long pl1 = 0;
        long pl2 = 0;
        // works with allToCount too - works without cache too (takes ~10 secs)
        // say 6 -> 100 wins
        // if there are three 6's then 300
        for (int i = 0; i < combinations.size(); i++) {
            int val = combinations.get(i);
            int p1Pos = player1Position;
            int p2Pos = player2Position;
            int p1Score = player1Score;
            int p2Score = player2Score;
            if (player1Playing) {
                p1Pos = (p1Pos + val) % 10;
                if (p1Pos == 0) {
                    p1Pos = 10;
                }
                p1Score += p1Pos;
            } else {
                p2Pos = (p2Pos + val) % 10;
                if (p2Pos == 0) {
                    p2Pos = 10;
                }
                p2Score += p2Pos;
            }
            Result r = part2Recur(p1Pos, p2Pos, p1Score, p2Score, !player1Playing, combinations, cache);
           /* long c = allToCount.get(val);
            pl1 += r.aWins * c;
            pl2 += r.bWins * c;*/
            pl1 += r.aWins;
            pl2 += r.bWins;
        }
        cache.put(cacheKey, new Result(pl1, pl2));
        return cache.get(cacheKey);
    }

    private static List<Integer> getAllCombinations() {
        List<Integer> all = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                for (int k = 1; k <= 3; k++) {
                    all.add(i + j + k);
                }
            }
        }
        return all;
    }

    private static class Result {
        private final long aWins;
        private final long bWins;


        private Result(long aWins, long bWins) {
            this.aWins = aWins;
            this.bWins = bWins;
        }
    }
}
