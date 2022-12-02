package com.thiyagu_7.adventofcode.year2022.day2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thiyagu_7.adventofcode.year2022.day2.SolutionDay2.Shape.PAPER;
import static com.thiyagu_7.adventofcode.year2022.day2.SolutionDay2.Shape.ROCK;
import static com.thiyagu_7.adventofcode.year2022.day2.SolutionDay2.Shape.SCISSOR;

public class SolutionDay2 {
    private static final Map<Shape, Shape> WINNING_MOVES_MAP = new HashMap<>();
    private static final Map<Shape, Shape> LOSING_MOVES_MAP = new HashMap<>();

    static {
        WINNING_MOVES_MAP.put(ROCK, PAPER);
        WINNING_MOVES_MAP.put(PAPER, SCISSOR);
        WINNING_MOVES_MAP.put(SCISSOR, ROCK);

        LOSING_MOVES_MAP.put(ROCK, SCISSOR);
        LOSING_MOVES_MAP.put(PAPER, ROCK);
        LOSING_MOVES_MAP.put(SCISSOR, PAPER);
    }

    public int part1(List<String> input) {
        return input.stream()
                .mapToInt(line -> playRoundPart1Rules(line.charAt(0), line.charAt(2)))
                .sum();
    }

    // using shapes, finds the outcome
    private int playRoundPart1Rules(char opponentShape, char yourShape) {
        Shape opponentMove = Shape.getShapeForOpponentMove(opponentShape);
        Shape yourMove = Shape.getShapeForYourMove(yourShape);
        int score = yourMove.getScore();

        if (opponentMove == yourMove) { //draw
            return score + 3;
        }
        if (WINNING_MOVES_MAP.get(opponentMove) == yourMove) { //won
            return score + 6;
        }
        return score;
    }

    public int part2(List<String> input) {
        return input.stream()
                .mapToInt(line -> playRoundPart2Rules(line.charAt(0), line.charAt(2)))
                .sum();
    }

    // using outcome, finds the shape
    private int playRoundPart2Rules(char opponentShape, char yourShape) {
        Shape opponentMove = Shape.getShapeForOpponentMove(opponentShape);
        int score;

        if (yourShape == 'X') { //lose
            Shape yourMove = LOSING_MOVES_MAP.get(opponentMove);
            score = yourMove.getScore();
        } else if (yourShape == 'Y') { //draw
            Shape yourMove = opponentMove;
            score = yourMove.getScore() + 3;
        } else { //win
            Shape yourMove = WINNING_MOVES_MAP.get(opponentMove);
            score = yourMove.getScore() + 6;
        }
        return score;
    }

    enum Shape {
        ROCK(1),
        PAPER(2),
        SCISSOR(3);
        private final int score;

        Shape(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public static Shape getShapeForOpponentMove(char shape) {
            return shape == 'A' ? ROCK : (shape == 'B' ? PAPER : SCISSOR);
        }

        public static Shape getShapeForYourMove(char shape) {
            return shape == 'X' ? ROCK : (shape == 'Y' ? PAPER : SCISSOR);
        }
    }
}
