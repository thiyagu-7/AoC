package com.thiyagu_7.adventofcode.year2022.day3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionDay3 {
    public int part1(List<String> input) {
        return input.stream()
                .mapToInt(this::findCommonItemTypePriority)
                .sum();
    }

    private int findCommonItemTypePriority(String rucksack) {
        int len = rucksack.length();
        String firstRucksack = rucksack.substring(0, len / 2);
        String secondRucksack = rucksack.substring(len / 2, len);

        char badgeItemType = findOnlyCommonElement(asSet(firstRucksack), asSet(secondRucksack));
        return computePriority(badgeItemType);
    }

    private Set<Character> asSet(String str) {
        return str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toSet());
    }

    @SafeVarargs
    private char findOnlyCommonElement(Set<Character>... sets) {
        int length = sets.length;
        Set<Character> common = new HashSet<>(sets[0]);
        IntStream.range(1, length)
                .forEach(i -> common.retainAll(sets[i]));

        return common.iterator().next();
    }

    private int computePriority(char itemType) {
        return Character.isLowerCase(itemType) ? itemType - 96
                : 26 + (itemType - 64);
    }

    public int part2(List<String> input) {
        int sum = 0;
        for (int i = 0; i < input.size(); i += 3) {
            sum += findBadgePriority(input.get(i), input.get(i + 1), input.get(i + 2));
        }
        return sum;
    }

    //The item type that corresponds to the badge will be in all 3 group's rucksack
    private int findBadgePriority(String group1Rucksack, String group2Rucksack, String group3Rucksack) {
        char badgeItemType = findOnlyCommonElement(asSet(group1Rucksack), asSet(group2Rucksack),
                asSet(group3Rucksack));
        return computePriority(badgeItemType);
    }
}
