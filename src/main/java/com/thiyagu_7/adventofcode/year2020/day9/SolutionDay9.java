package com.thiyagu_7.adventofcode.year2020.day9;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SolutionDay9 {
    public long part1(List<String> input, int preambleLength) {
        int start = 0;
        List<Long> ints = input.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        //for each num after the preamble
        for (int i = preambleLength; i < ints.size(); i++) {
            if (!twoSum(ints.subList(start++, i), ints.get(i))) {
                return ints.get(i);
            }
        }
        return 0;
    }

    private boolean twoSum(List<Long> ints, long target) {
        Set<Long> set = new HashSet<>();
        for (Long num : ints) {
            long complement = target - num;
            if (set.contains(complement)) {
                return true;
            }
            set.add(num);
        }
        return false;
    }

    public long part2(List<String> input, int preambleLength) {
        long inValidNumber = part1(input, preambleLength);

        List<Long> ints = input.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        for (int i = 0; i < ints.size(); i++) {
            long sum = ints.get(i);
            TreeSet<Long> contiguousNums = new TreeSet<>();
            contiguousNums.add(sum);
            for (int j = i + 1; j < ints.size() && sum <= inValidNumber; j++) {
                contiguousNums.add(ints.get(j));
                sum += ints.get(j);

                if (sum == inValidNumber) {
                    return contiguousNums.first() + contiguousNums.last();
                }
            }
        }
        return -1; //should not happen
    }
}
