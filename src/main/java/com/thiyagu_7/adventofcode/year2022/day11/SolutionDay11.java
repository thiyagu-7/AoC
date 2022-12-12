package com.thiyagu_7.adventofcode.year2022.day11;

import lombok.Builder;
import lombok.ToString;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class SolutionDay11 {
    private static final int PART_1_NUM_ROUNDS = 20;
    private static final int PART_2_NUM_ROUNDS = 10000;

    public int part1(Map<Integer, Monkey> input) {
        return (int) process(input, PART_1_NUM_ROUNDS, l -> l / 3L);
    }

    public long part2(Map<Integer, Monkey> input) {
        List<Integer> divisionChecks = input.values()
                .stream()
                .map(m -> m.divideBy)
                .toList();

        return process(input, PART_2_NUM_ROUNDS, l -> applyCRT(l, divisionChecks));
    }

    private long process(Map<Integer, Monkey> input, int num, UnaryOperator<Long> additionalMapping) {
        //ordered by Monkey id
        TreeMap<Integer, Monkey> monkeys = new TreeMap<>(input);
        Map<Integer, Integer> numTimesProcessed = new HashMap<>();

        for (int round = 0; round < num; round++) {
            for (Map.Entry<Integer, Monkey> entry : monkeys.entrySet()) {
                Integer monkeyId = entry.getKey();
                Monkey monkey = entry.getValue();

                numTimesProcessed.compute(monkeyId,
                        (k, curr) -> (curr == null ? 0 : curr) + monkey.items.size());

                Queue<Long> items = monkey.items;

                while (!items.isEmpty()) {
                    Long worryLevel = items.poll();
                    Long newWorryLevel = monkey.operation.apply(worryLevel);
                    newWorryLevel = additionalMapping.apply(newWorryLevel);

                    int monkeyToThrowTo = newWorryLevel % monkey.divideBy == 0 ? monkey.monkeyToThrowToIfTrue :
                            monkey.monkeyToThrowToIfFalse;
                    monkeys.get(monkeyToThrowTo).items.add(newWorryLevel);
                }
            }
        }

        TreeMap<Integer, Integer> result = numTimesProcessed
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey,
                        (o, n) -> o,
                        () -> new TreeMap<>(Comparator.<Integer>reverseOrder())));
        return result.keySet()
                .stream()
                .limit(2)
                .reduce(1L, (a, b) -> a * b, Long::sum);
    }

    static long applyCRT(long num, List<Integer> divisionChecks) {
        int[] arr = divisionChecks.stream()
                .mapToInt(d -> ((int) (num % d)))
                .toArray();
        int p = divisionChecks.stream()
                .reduce(1, (a, b) -> a * b);
        return crt(arr, divisionChecks.stream()
                        .mapToInt(a -> a)
                        .toArray(),
                arr.length, p);
    }

    static long crt(int[] a, int[] m, int n, int p) {
        int x = 0;

        for (int i = 0; i < n; i++) {
            int M = p / m[i], y = 0;
            for (int j = 0; j < m[i]; j++) {
                if ((M * j) % m[i] == 1) {
                    y = j;
                    break;
                }
            }
            x = x + a[i] * M * y;
        }
        return x % p;
    }

    @Builder
    @ToString(of = "items")
    static class Monkey {
        private int id;
        private Queue<Long> items; //worry level
        private UnaryOperator<Long> operation;
        private int divideBy;
        private int monkeyToThrowToIfTrue;
        private int monkeyToThrowToIfFalse;
    }
}
