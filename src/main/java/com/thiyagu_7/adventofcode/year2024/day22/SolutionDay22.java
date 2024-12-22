package com.thiyagu_7.adventofcode.year2024.day22;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolutionDay22 {
    private static final int ITERATIONS = 2000;

    public long part1(List<String> input) {
        long sum = 0;
        for (String line : input) {
            long secretNum = Long.parseLong(line);
            for (int i = 0; i < ITERATIONS; i++) {
                secretNum = getNextSecretNumber(secretNum);
            }
            sum += secretNum;
        }
        return sum;
    }

    public int part2(List<String> input) {
        List<List<Integer>> pricesForAllBuyers = new ArrayList<>();
        for (String line : input) {
            List<Integer> prices = buildPriceSequenceForBuyer(Long.parseLong(line));
            pricesForAllBuyers.add(prices);
        }

        // Key is the 4 consecutive price changes list (list's toString)
        // Value is the sum of prices (each at the end of 4th price difference) across buyers
        Map<String, Integer> priceDifferenceSeqToPriceSum = new HashMap<>();
        for (List<Integer> prices : pricesForAllBuyers) {
            findPriceChangeSequences(prices, priceDifferenceSeqToPriceSum);
        }

        return priceDifferenceSeqToPriceSum.values()
                .stream()
                .max(Comparator.naturalOrder())
                .orElse(0);
    }


    private List<Integer> buildPriceSequenceForBuyer(long secretNum) {
        List<Integer> prices = new ArrayList<>();
        prices.add((int) (secretNum % 10));

        for (int i = 0; i < ITERATIONS; i++) {
            secretNum = getNextSecretNumber(secretNum);
            prices.add((int) (secretNum % 10));
        }
        return prices;
    }

    private void findPriceChangeSequences(List<Integer> prices, Map<String, Integer> priceDifferenceSeqToPriceSum) {
        List<Integer> window = new LinkedList<>();
        Set<String> currentChanges = new HashSet<>();

        for (int i = 1; i < prices.size(); i++) {
            window.add(prices.get(i) - prices.get(i - 1));
            if (window.size() == 4) {
                if (currentChanges.add(window.toString())) {
                    priceDifferenceSeqToPriceSum.merge(window.toString(), prices.get(i), Integer::sum);
                }
                window.remove(0);
            }
        }
    }

    private long getNextSecretNumber(long secretNum) {
        secretNum = prune(mix(secretNum, secretNum * 64));
        secretNum = prune(mix(secretNum, secretNum / 32));
        secretNum = prune(mix(secretNum, secretNum * 2048));
        return secretNum;
    }

    private long mix(long secretNumber, long number) {
        return secretNumber ^ number;
    }

    private long prune(long secretNumber) {
        return secretNumber % 16777216;
    }
}
