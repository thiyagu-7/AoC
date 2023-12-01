package com.thiyagu_7.adventofcode.year2023.day1;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SolutionDay1 {
    public int part1(List<String> input) {
        return input.stream()
                .mapToInt(this::parseNumForPart1)
                .sum();
    }

    private int parseNumForPart1(String line) {
        Integer firstNum = null, secondNum = null;
        for (char c : line.toCharArray()) {
            if (Character.isDigit(c)) {
                firstNum = c - '0';
                break;
            }
        }
        for (int i = line.length() - 1; i >= 0; i--) {
            if (Character.isDigit(line.charAt(i))) {
                secondNum = line.charAt(i) - '0';
                break;
            }
        }
        secondNum = secondNum == null ? firstNum : secondNum;
        return firstNum * 10 + secondNum;
    }

    public int part2(List<String> input) {
        return input.stream()
                .mapToInt(this::parseNumForPart2)
                .sum();
    }

    private int parseNumForPart2(String line) {
        Map<String, Integer> digitMapping = Map.of(
                "one", 1,
                "two", 2,
                "three", 3,
                "four", 4,
                "five", 5,
                "six", 6,
                "seven", 7,
                "eight", 8,
                "nine", 9,
                "ten", 10
        );
        // find first number
        int firstNumIndex = -1;
        for (int i = 0; i < line.length(); i++) {
            if (Character.isDigit(line.charAt(i))) {
                firstNumIndex = i;
                break;
            }
        }
        record IndexAndDigit(int index, int digit) {
        }
        IndexAndDigit indexAndDigit = digitMapping.keySet()
                .stream()
                .filter(line::contains)
                .map(d -> new IndexAndDigit(
                        line.indexOf(d),
                        digitMapping.get(d)))
                .min(Comparator.comparingInt(IndexAndDigit::index))
                .orElse(null);
        int firstNum;
        if (firstNumIndex == -1) {
            firstNum = indexAndDigit.digit;
        } else if (indexAndDigit == null) {
            firstNum = line.charAt(firstNumIndex) - '0';
        } else if (firstNumIndex < indexAndDigit.index) {
            firstNum = line.charAt(firstNumIndex) - '0';
        } else {
            firstNum = indexAndDigit.digit;
        }
        // find last number
        int secondNumIndex = -1;
        for (int i = line.length() - 1; i >= 0; i--) {
            if (Character.isDigit(line.charAt(i))) {
                secondNumIndex = i;
                break;
            }
        }
        indexAndDigit = digitMapping.keySet()
                .stream()
                .filter(line::contains)
                .map(d -> new IndexAndDigit(
                        line.lastIndexOf(d),
                        digitMapping.get(d)))
                .max(Comparator.comparingInt(IndexAndDigit::index))
                .orElse(null);
        int secondNum;
        if (secondNumIndex == -1) {
            secondNum = indexAndDigit.digit;
        } else if (indexAndDigit == null) {
            secondNum = line.charAt(secondNumIndex) - '0';
        } else if (secondNumIndex > indexAndDigit.index) {
            secondNum = line.charAt(secondNumIndex) - '0';
        } else {
            secondNum = indexAndDigit.digit;
        }

        return firstNum * 10 + secondNum;
    }
}
