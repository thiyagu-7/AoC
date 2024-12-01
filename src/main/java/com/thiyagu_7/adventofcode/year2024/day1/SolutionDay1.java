package com.thiyagu_7.adventofcode.year2024.day1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SolutionDay1 {
    public long part1(List<String> input) {
        InputList inputList = parseInput(input);

        inputList.list1.sort(Comparator.naturalOrder());
        inputList.list2.sort(Comparator.naturalOrder());

        long sumOfDistances = 0;
        for (int i = 0; i < inputList.list1.size(); i++) {
            sumOfDistances += Math.abs(inputList.list1.get(i) - inputList.list2.get(i));
        }
        return sumOfDistances;
    }

    public long part2(List<String> input) {
        InputList inputList = parseInput(input);
        Map<Long, Long> locationIdToFrequency = inputList.list2.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long similarityScore = 0;
        for (Long locationId : inputList.list1) {
            similarityScore += locationId * locationIdToFrequency.getOrDefault(locationId, 0L);
        }
        return similarityScore;
    }

    private InputList parseInput(List<String> input) {
        List<Long> list1 = new ArrayList<>();
        List<Long> list2 = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split("\\s+");
            list1.add(Long.parseLong(parts[0]));
            list2.add(Long.parseLong(parts[1]));
        }
        return new InputList(list1, list2);
    }

    record InputList(List<Long> list1, List<Long> list2) {

    }
}
