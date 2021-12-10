package com.thiyagu_7.adventofcode.year2021.day10;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class SolutionDay10 {
    private final Map<Character, Character> OPEN_CLOSE = new HashMap<>();
    private final Map<Character, Character> CLOSE_OPEN;
    private final Map<Character, Integer> SCORES_PART1 = new HashMap<>();
    private final Map<Character, Integer> SCORES_PART2 = new HashMap<>();

    public SolutionDay10() {
        OPEN_CLOSE.put('(', ')');
        OPEN_CLOSE.put('[', ']');
        OPEN_CLOSE.put('{', '}');
        OPEN_CLOSE.put('<', '>');

        CLOSE_OPEN = OPEN_CLOSE.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        SCORES_PART1.put(')', 3);
        SCORES_PART1.put(']', 57);
        SCORES_PART1.put('}', 1197);
        SCORES_PART1.put('>', 25137);

        SCORES_PART2.put(')', 1);
        SCORES_PART2.put(']', 2);
        SCORES_PART2.put('}', 3);
        SCORES_PART2.put('>', 4);
    }

    public int part1(List<String> input) {
        int syntaxErrorScore = 0;
        for (String line : input) {
            Stack<Character> stack = new Stack<>();
            for (Character c : line.toCharArray()) {
                if (OPEN_CLOSE.containsKey(c)) {
                    // open char
                    stack.push(c);
                } else {
                    Character top = stack.pop();
                    if (CLOSE_OPEN.get(c) != top) {
                        syntaxErrorScore += SCORES_PART1.get(c);
                        break;
                    }
                }
            }
        }
        return syntaxErrorScore;
    }

    public long part2(List<String> input) {
        List<Long> completionScores = new ArrayList<>();
        for (String line : input) {
            Stack<Character> stack = new Stack<>();
            boolean corrupted = false;
            for (Character c : line.toCharArray()) {
                if (OPEN_CLOSE.containsKey(c)) {
                    stack.push(c);
                } else {
                    Character top = stack.pop();
                    if (CLOSE_OPEN.get(c) != top) {
                        corrupted = true;
                        break;
                    }
                }
            }

            StringBuilder builder = new StringBuilder();
            if (!corrupted && !stack.isEmpty()) {
                // incomplete line
                while (!stack.isEmpty()) {
                    builder.append(OPEN_CLOSE.get(stack.pop()));
                }
                long score = 0;
                for (char c : builder.toString().toCharArray()) {
                    score = score * 5 + SCORES_PART2.get(c);
                }
                completionScores.add(score);
            }
        }
        completionScores.sort(Comparator.naturalOrder());
        //always size is odd as per problem description
        return completionScores.get(completionScores.size() / 2);
    }
}
