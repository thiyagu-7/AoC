package com.thiyagu_7.adventofcode.year2022.day11;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class InputProvider {
    static Map<Integer, SolutionDay11.Monkey> buildSampleInput() {
        return Map.of(
                0, SolutionDay11.Monkey.builder()
                        .id(0)
                        .items(buildQueue(List.of(79L, 98L)))
                        .operation(o -> o * 19)
                        .divideBy(23)
                        .monkeyToThrowToIfTrue(2)
                        .monkeyToThrowToIfFalse(3)
                        .build(),
                1, SolutionDay11.Monkey.builder()
                        .id(1)
                        .items(buildQueue(List.of(54L, 65L, 75L, 74L)))
                        .operation(o -> o + 6)
                        .divideBy(19)
                        .monkeyToThrowToIfTrue(2)
                        .monkeyToThrowToIfFalse(0)
                        .build(),
                2, SolutionDay11.Monkey.builder()
                        .id(2)
                        .items(buildQueue(List.of(79L, 60L, 97L)))
                        .operation(o -> o * o)
                        .divideBy(13)
                        .monkeyToThrowToIfTrue(1)
                        .monkeyToThrowToIfFalse(3)
                        .build(),
                3, SolutionDay11.Monkey.builder()
                        .id(3)
                        .items(buildQueue(List.of(74L)))
                        .operation(o -> o + 3)
                        .divideBy(17)
                        .monkeyToThrowToIfTrue(0)
                        .monkeyToThrowToIfFalse(1)
                        .build()
        );
    }

    static Map<Integer, SolutionDay11.Monkey> buildInput() {
        return Map.of(
                0, SolutionDay11.Monkey.builder()
                        .id(0)
                        .items(buildQueue(List.of(71L, 56L, 50L, 73L)))
                        .operation(o -> o * 11)
                        .divideBy(13)
                        .monkeyToThrowToIfTrue(1)
                        .monkeyToThrowToIfFalse(7)
                        .build(),
                1, SolutionDay11.Monkey.builder()
                        .id(1)
                        .items(buildQueue(List.of(70L, 89L, 82L)))
                        .operation(o -> o + 1)
                        .divideBy(7)
                        .monkeyToThrowToIfTrue(3)
                        .monkeyToThrowToIfFalse(6)
                        .build(),
                2, SolutionDay11.Monkey.builder()
                        .id(2)
                        .items(buildQueue(List.of(52L, 95L)))
                        .operation(o -> o * o)
                        .divideBy(3)
                        .monkeyToThrowToIfTrue(5)
                        .monkeyToThrowToIfFalse(4)
                        .build(),
                3, SolutionDay11.Monkey.builder()
                        .id(3)
                        .items(buildQueue(List.of(94L, 64L, 69L, 87L, 70L)))
                        .operation(o -> o + 2)
                        .divideBy(19)
                        .monkeyToThrowToIfTrue(2)
                        .monkeyToThrowToIfFalse(6)
                        .build(),
                4, SolutionDay11.Monkey.builder()
                        .id(4)
                        .items(buildQueue(List.of(98L, 72L, 98L, 53L, 97L, 51L)))
                        .operation(o -> o + 6)
                        .divideBy(5)
                        .monkeyToThrowToIfTrue(0)
                        .monkeyToThrowToIfFalse(5)
                        .build(),
                5, SolutionDay11.Monkey.builder()
                        .id(5)
                        .items(buildQueue(List.of(79L)))
                        .operation(o -> o + 7)
                        .divideBy(2)
                        .monkeyToThrowToIfTrue(7)
                        .monkeyToThrowToIfFalse(0)
                        .build(),
                6, SolutionDay11.Monkey.builder()
                        .id(6)
                        .items(buildQueue(List.of(77L, 55L, 63L, 93L, 66L, 90L, 88L, 71L)))
                        .operation(o -> o * 7)
                        .divideBy(11)
                        .monkeyToThrowToIfTrue(2)
                        .monkeyToThrowToIfFalse(4)
                        .build(),
                7, SolutionDay11.Monkey.builder()
                        .id(7)
                        .items(buildQueue(List.of(54L, 97L, 87L, 70L, 59L, 82L, 59L)))
                        .operation(o -> o + 8)
                        .divideBy(17)
                        .monkeyToThrowToIfTrue(1)
                        .monkeyToThrowToIfFalse(3)
                        .build()
        );
    }

    private static Queue<Long> buildQueue(List<Long> list) {
        return new LinkedList<>(list);
    }
}
