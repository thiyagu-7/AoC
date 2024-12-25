package com.thiyagu_7.adventofcode.year2024.day25;

import java.util.ArrayList;
import java.util.List;

public class SolutionDay25 {
    public int part1(List<String> input) {
        PinHeights pinHeights = parseInput(input);
        return findUniqueLockKeyCombinations(pinHeights);
    }

    private PinHeights parseInput(List<String> input) {
        int idx = 0;
        int pins;
        List<List<Integer>> pinHeightsOfLocks = new ArrayList<>();
        List<List<Integer>> pinHeightsOfKeys = new ArrayList<>();
        List<Integer> currentHeight;

        while (idx < input.size()) {
            if (isLock(input.get(idx))) {
                currentHeight = new ArrayList<>();
                for (int j = 0; j < 5; j++) {
                    pins = 0;
                    for (int i = idx + 1; i < idx + 6; i++) {
                        if (input.get(i).charAt(j) == '#') {
                            pins++;
                        } else {
                            break;
                        }
                    }
                    currentHeight.add(pins);
                }
                pinHeightsOfLocks.add(currentHeight);
            } else {
                currentHeight = new ArrayList<>();
                for (int j = 0; j < 5; j++) {
                    pins = 0;
                    for (int i = idx + 5; i > idx; i--) {
                        if (input.get(i).charAt(j) == '#') {
                            pins++;
                        } else {
                            break;
                        }
                    }
                    currentHeight.add(pins);
                }
                pinHeightsOfKeys.add(currentHeight);
            }
            idx += 8;
        }
        return new PinHeights(pinHeightsOfLocks, pinHeightsOfKeys);
    }

    private int findUniqueLockKeyCombinations(PinHeights pinHeights) {
        int match = 0;
        List<List<Integer>> pinHeightsOfLocks = pinHeights.pinHeightsOfLocks;
        List<List<Integer>> pinHeightsOfKeys = pinHeights.pinHeightsOfKeys;

        for (List<Integer> lockHeights : pinHeightsOfLocks) {
            for (List<Integer> keyHeights : pinHeightsOfKeys) {
                boolean fit = true;
                for (int k = 0; k < 5; k++) {
                    if (lockHeights.get(k) + keyHeights.get(k) > 5) {
                        fit = false;
                        break;
                    }
                }
                if (fit) {
                    match++;
                }
            }
        }
        return match;
    }

    record PinHeights(List<List<Integer>> pinHeightsOfLocks, List<List<Integer>> pinHeightsOfKeys) {

    }

    private boolean isLock(String topLine) {
        return topLine.equals("#####");
    }
}
