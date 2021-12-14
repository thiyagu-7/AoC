package com.thiyagu_7.adventofcode.year2021.day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionDay14 {
    // brute force
    public int part1(List<String> input, int numSteps) {
        InputData inputData = parseInput(input);
        String polymer = inputData.polymerTemplate;
        Map<String, String> pairInsertionRules = inputData.pairInsertionRules;

        for (int i = 0; i < numSteps; i++) {
            StringBuilder result = new StringBuilder();
            for (int j = 0; j < polymer.length() - 1; j++) {
                String subs = pairInsertionRules.getOrDefault(polymer.substring(j, j + 2), "");
                result.append(polymer.charAt(j))
                        .append(subs);
            }
            result.append(polymer.charAt(polymer.length() - 1)); //add last char
            polymer = result.toString();

        }
        // quantity of the most common element - the quantity of the least common element
        return (int) findDiffBetweenMaxAndMinElement(
                Arrays.stream(polymer.split(""))
                        .collect(Collectors.groupingBy(s -> s.charAt(0), Collectors.counting())));
    }

    private InputData parseInput(List<String> input) {
        return new InputData(input.get(0), IntStream.range(2, input.size())
                .mapToObj(input::get)
                .map(line -> line.split(" -> "))
                .collect(Collectors.toMap(p -> p[0], p -> p[1])));
    }

    private record InputData(String polymerTemplate, Map<String, String> pairInsertionRules) {

    }

    private long findDiffBetweenMaxAndMinElement(Map<Character, Long> map) {
        LinkedHashMap<Character, Long> elementByFrequency = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (v1, v2) -> {
                            throw new RuntimeException();
                        },
                        LinkedHashMap::new));

        List<Long> frequencies = new ArrayList<>(elementByFrequency.values());
        return frequencies.get(frequencies.size() - 1) - frequencies.get(0);
    }

    public long part2(List<String> input, int numSteps) {
        InputData inputData = parseInput(input);
        String polymer = inputData.polymerTemplate;
        Map<String, String> pairInsertionRules = inputData.pairInsertionRules;

        Map<String, Map<Character, Long>> cachedCharFreqForPairs = new HashMap<>();
        Map<Character, Long> allCharFreq = new HashMap<>();
        for (int i = 0; i < polymer.length() - 1; i++) {
            String pair = polymer.substring(i, i + 2);
            Map<Character, Long> charFreq = recur(pair, 0, numSteps, pairInsertionRules, cachedCharFreqForPairs);

            charFreq.forEach((k, v) -> allCharFreq.merge(k, v, Long::sum));
            if (!cachedCharFreqForPairs.containsKey(pair + "|" + 0)) {
                cachedCharFreqForPairs.put(pair, charFreq);
            }
        }
        // add last char
        allCharFreq.merge(polymer.charAt(polymer.length() - 1), 1L, (o, n) -> o + 1);
        return findDiffBetweenMaxAndMinElement(allCharFreq);
    }

    private Map<Character, Long> recur(String current, int step, int numSteps,
                                       Map<String, String> pairInsertionRules,
                                       Map<String, Map<Character, Long>> cachedCharFreqForPairs) {
        if (step == numSteps) {
            Map<Character, Long> res = new HashMap<>();
            res.put(current.charAt(0), 1L);
            return res;
        }
        Map<Character, Long> m1;
        Map<Character, Long> m2;
        String subs = pairInsertionRules.get(current);

        // call 1
        String pair = current.charAt(0) + subs;
        if (cachedCharFreqForPairs.containsKey(pair + "|" + step)) {
            m1 = cachedCharFreqForPairs.get(pair + "|" + step);
        } else {
            m1 = recur(pair, step + 1, numSteps, pairInsertionRules, cachedCharFreqForPairs);
            cachedCharFreqForPairs.put(pair + "|" + step, m1);

        }
        // call 2
        pair = subs + current.charAt(1);
        if (cachedCharFreqForPairs.containsKey(pair + "|" + step)) {
            m2 = cachedCharFreqForPairs.get(pair + "|" + step);
        } else {
            m2 = recur(pair, step + 1, numSteps, pairInsertionRules, cachedCharFreqForPairs);
            cachedCharFreqForPairs.put(pair + "|" + step, m2);
        }

        Map<Character, Long> res = new HashMap<>(m1);
        m2.forEach((key, value) -> res.merge(key, value, Long::sum));
        return res;
    }
}
