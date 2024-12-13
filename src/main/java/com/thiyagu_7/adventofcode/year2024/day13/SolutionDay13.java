package com.thiyagu_7.adventofcode.year2024.day13;

import com.thiyagu_7.adventofcode.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolutionDay13 {
    public int part1(List<String> input) {
        List<ButtonAndPrizeLocation> buttonAndPrizeLocations = parseInput(input);
        int allTokensSum = 0;

        for (ButtonAndPrizeLocation buttonAndPrizeLocation : buttonAndPrizeLocations) {
            int minTokens = Integer.MAX_VALUE;
            int x, y;
            for (int i = 0; i <= 100; i++) {
                int tokens;
                for (int j = 0; j <= 100; j++) {
                    Movement buttonAMovement = buttonAndPrizeLocation.buttonAMovement;
                    Movement buttonBMovement = buttonAndPrizeLocation.buttonBMovement;
                    x = buttonAMovement.x() * i + buttonBMovement.x() * j;
                    y = buttonAMovement.y() * i + buttonBMovement.y() * j;
                    tokens = i * 3 + j;

                    if (x == buttonAndPrizeLocation.prizePosition.x() && y == buttonAndPrizeLocation.prizePosition.y()) {
                        minTokens = Math.min(minTokens, tokens);
                    }
                }
            }
            if (minTokens != Integer.MAX_VALUE) { //if we can reach the prize
                allTokensSum += minTokens;
            }
        }
        return allTokensSum;
    }

    private List<ButtonAndPrizeLocation> parseInput(List<String> input) {
        List<ButtonAndPrizeLocation> buttonAndPrizeLocations = new ArrayList<>();
        Movement buttonAMovement = null;
        Movement buttonBMovement = null;
        Position prizePosition = null;

        for (String line : input) {
            if (!line.isEmpty()) {
                if (line.startsWith("Button A")) {
                    Pattern pattern = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
                    Matcher matcher = pattern.matcher(line);
                    matcher.find();
                    buttonAMovement = new Movement(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));

                } else if (line.startsWith("Button B")) {
                    Pattern pattern = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
                    Matcher matcher = pattern.matcher(line);
                    matcher.find();
                    buttonBMovement = new Movement(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
                } else {
                    Pattern pattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");
                    Matcher matcher = pattern.matcher(line);
                    matcher.find();
                    prizePosition = new Position(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
                }
            }
            if (line.startsWith("Prize:")) {
                buttonAndPrizeLocations.add(new ButtonAndPrizeLocation(
                        buttonAMovement, buttonBMovement, prizePosition
                ));
            }
        }
        return buttonAndPrizeLocations;
    }


    record ButtonAndPrizeLocation(Movement buttonAMovement, Movement buttonBMovement, Position prizePosition) {

    }

    record Movement(int x, int y) {

    }


    public long part2(List<String> input) {
        List<ButtonAndPrizeLocation> buttonAndPrizeLocations = parseInput(input);
        long allTokensSum = 0;
        for (ButtonAndPrizeLocation buttonAndPrizeLocation : buttonAndPrizeLocations) {
            long targetX = buttonAndPrizeLocation.prizePosition.x() + 10000000000000L;
            long targetY = buttonAndPrizeLocation.prizePosition.y() + 10000000000000L;

            //solving linear equations in 2 variables (using Cramer's rule)
            long[][] matrix = new long[][]{
                    {buttonAndPrizeLocation.buttonAMovement.x(), buttonAndPrizeLocation.buttonBMovement.x()},
                    {buttonAndPrizeLocation.buttonAMovement.y(), buttonAndPrizeLocation.buttonBMovement.y()}
            };
            long det = determinant(matrix);

            long[][] matrixCopy = copy(matrix);
            matrixCopy[0][0] = targetX;
            matrixCopy[1][0] = targetY;
            long det1 = determinant(matrixCopy);

            matrixCopy = copy(matrix);
            matrixCopy[0][1] = targetX;
            matrixCopy[1][1] = targetY;
            long det2 = determinant(matrixCopy);

            if (det1 % det == 0 && det2 % det == 0) {
                long a = det1 / det;
                long b = det2 / det;
                allTokensSum += a * 3 + b;
            }
        }
        return allTokensSum;
    }

    private long determinant(long[][] matrix) {
        return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
    }

    private long[][] copy(long[][] matrix) {
        long[][] copy = new long[2][2];
        for (int i = 0; i < 2; i++) {
            copy[i] = Arrays.copyOf(matrix[i], 2);
        }
        return copy;
    }
}
