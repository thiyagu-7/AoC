package com.thiyagu_7.adventofcode.year2020.day22;

import java.util.ArrayList;
import java.util.List;

public class SolutionDay22 {
    public long part1(List<Integer> player1, List<Integer> player2) {
        int i = 0, j = 0;
        while (!player1.isEmpty() && !player2.isEmpty()) {
            int c1 = player1.get(i);
            int c2 = player2.get(j);
            if (c1 > c2) {
                player1.add(c1);
                player1.add(c2);
            } else {
                player2.add(c2);
                player2.add(c1);
            }
            player1.remove(i);
            player2.remove(j);
        }

        return computeScore(player1, player2);
    }

    private int computeScore(List<Integer> player1, List<Integer> player2) {
        List<Integer> winnerCards = player1.isEmpty() ? player2 : player1;
        int mult = 1;
        int score = 0;
        for (int i = winnerCards.size() - 1; i >= 0; i--) {
            score += winnerCards.get(i) * mult;
            mult++;
        }
        return score;
    }

    public int part2(List<Integer> player1, List<Integer> player2) {
        recurPlay(player1, player2);
        return computeScore(player1, player2);
    }

    private int recurPlay(List<Integer> player1, List<Integer> player2) {
        int i = 0, j = 0;
        List<State> states = new ArrayList<>();
        while (!player1.isEmpty() && !player2.isEmpty()) {
            if (sameState(states, player1, player2)) {
                return 1; //player 1
            }

            states.add(new State(new ArrayList<>(player1), new ArrayList<>(player2)));
            int c1 = player1.get(i);
            int c2 = player2.get(j);

            //if both have at least as many cards remaining in their deck
            if (player1.size() - 1 >= c1 && player2.size() - 1 >= c2) {
                int subGameWinner = recurPlay(new ArrayList<>(player1.subList(1, c1 + 1)),
                        new ArrayList<>(player2.subList(1, c2 + 1)));

                if (subGameWinner == 1) {
                    player1.add(c1); //winner card add first
                    player1.add(c2);
                } else {
                    player2.add(c2);
                    player2.add(c1);
                }
            } else if (c1 > c2) {
                player1.add(c1);
                player1.add(c2);
            } else {
                player2.add(c2);
                player2.add(c1);
            }
            player1.remove(i);
            player2.remove(j);
        }
        return player1.isEmpty() ? 2 : 1;
    }

    private static class State {
        private List<Integer> player1;
        private List<Integer> player2;

        private State(List<Integer> player1, List<Integer> player2) {
            this.player1 = player1;
            this.player2 = player2;
        }
    }
    private boolean sameState(List<State> states, List<Integer> player1, List<Integer> player2) {
        return states.stream()
                .anyMatch(s -> s.player1.equals(player1) && s.player2.equals(player2));
    }
}
