package com.thiyagu_7.adventofcode.year2024.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SolutionDay11 {
    private static final int BLINK_COUNT_PART1 = 25;
    private static final int BLINK_COUNT_PART2 = 75;

    public int part1(String input) {
        List<Long> stones = Arrays.stream(input.split(" "))
                .map(Long::valueOf)
                .toList();
        for (int i = 0; i < BLINK_COUNT_PART1; i++) {
            stones = blinkBrute(stones);
        }
        return stones.size();
    }

    private List<Long> blinkBrute(List<Long> stones) {
        List<Long> result = new ArrayList<>();
        for (Long stone : stones) {
            result.addAll(blink(stone));
        }
        return result;
    }

    private List<Long> blink(Long stone) {
        if (stone == 0L) {
            return List.of(1L);
        } else if (stone.toString().length() % 2 == 1) {
            return List.of(stone * 2024);
        } else {
            String num = stone.toString();
            String firstHalf = num.substring(0, num.length() / 2);
            String secondHalf = num.substring(num.length() / 2);

            return List.of(
                    Long.valueOf(firstHalf),
                    Long.valueOf(secondHalf)
            );
        }
    }

    public long part2(String input) {
        List<Long> stones = Arrays.stream(input.split(" "))
                .map(Long::valueOf)
                .toList();
        Map<Long, Long> stoneToFrequency = stones.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (int i = 0; i < BLINK_COUNT_PART2; i++) {
            Map<Long, Long> newStoneToFrequency = new HashMap<>();

            for (Map.Entry<Long, Long> entry : stoneToFrequency.entrySet()) {
                List<Long> result = blink(entry.getKey());
                //entry.getValue() is the parent/original stone's frequency
                result.forEach(r -> newStoneToFrequency.merge(r, entry.getValue(), Long::sum));
            }
            stoneToFrequency = newStoneToFrequency;
        }

        return stoneToFrequency.values()
                .stream()
                .mapToLong(v -> v)
                .sum();
    }

    //Approach 2 - Takes one stone processes it till level 75 and goes to next stone
    public long part2a(String input) {
        List<Long> stones = Arrays.stream(input.split(" "))
                .map(Long::valueOf)
                .toList();

        long result = 0;
        //For each level - maps stone to frequency
        Map<Integer, Map<Long, Long>> levelToStoneFrequencyCache = new HashMap<>();
        for (Long stone : stones) {
            result += compute(stone, 1, levelToStoneFrequencyCache);
        }
        return result;
    }


    private long compute(Long currentStone, int blinkNum, Map<Integer, Map<Long, Long>> levelToStoneFrequencyCache) {
        if (!levelToStoneFrequencyCache.containsKey(blinkNum)) {
            levelToStoneFrequencyCache.put(blinkNum, new HashMap<>());
        }

        if (levelToStoneFrequencyCache.get(blinkNum).containsKey(currentStone)) {
            return levelToStoneFrequencyCache.get(blinkNum).get(currentStone);
        }

        if (blinkNum <= BLINK_COUNT_PART2) {
            List<Long> stones = blink(currentStone);
            long sum = 0;
            for (Long stone : stones) {
                sum += compute(stone, blinkNum + 1, levelToStoneFrequencyCache);
            }
            levelToStoneFrequencyCache.get(blinkNum)
                    .put(currentStone, sum);
            return sum;
        }
        return 1;
    }
}
