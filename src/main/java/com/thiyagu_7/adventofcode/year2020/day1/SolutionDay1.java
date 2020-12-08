package com.thiyagu_7.adventofcode.year2020.day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay1 {
    public int part1(List<String> input) {
        List<Integer> ints = listOfStringToListOfInt(input);
        return twoSum(ints, 2020);
    }

    public int part2(List<String> input) {
        List<Integer> ints = listOfStringToListOfInt(input);
        for (int i = 0; i < ints.size(); i++) {
            int target = 2020 - ints.get(i);
            int twoSum = twoSum(ints.subList(i + 1, ints.size()), target);
            if (twoSum != -1) {
                return twoSum * ints.get(i);
            }
        }
        return -1; //cannot happen
    }

    private int twoSum(List<Integer> ints, int target) {
        Set<Integer> set = new HashSet<>();

        for (Integer integer : ints) {
            int complement = target - integer;
            if (set.contains(complement)) {
                return integer * complement;
            }
            set.add(integer);
        }
        return -1;
    }
    private List<Integer> listOfStringToListOfInt(List<String> input) {
        return input.stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
