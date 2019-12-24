package com.thiyagu_7.adventofcode.year2019.day2;

import java.util.Arrays;

/**
 * https://adventofcode.com/2019/day/2
 */
public class SolutionDay2 {
    public int part1(int[] inputs) {
        int index = 0;
        while (inputs[index] != 99) {
            int result = process(inputs, inputs[index], inputs[index + 1], inputs[index + 2]);
            int targetIndex = inputs[index + 3];
            inputs[targetIndex] = result;
            index += 4;
        }
        return inputs[0];
    }

    public int part2(int[] inputs, int desiredResult) {
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                int[] currInput = Arrays.copyOf(inputs, inputs.length);
                currInput[1] = noun;
                currInput[2] = verb;
                if (part1(currInput) == desiredResult) {
                    return 100 * noun + verb;
                }
            }
        }
        throw new RuntimeException("No solution found");
    }

    private int process(int[] inputs, int opcode, int index1, int index2) {
        return opcode == 1 ? inputs[index1] + inputs[index2] : inputs[index1] * inputs[index2];
    }
}
