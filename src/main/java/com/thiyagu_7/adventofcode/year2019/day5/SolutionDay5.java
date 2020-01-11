package com.thiyagu_7.adventofcode.year2019.day5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * https://adventofcode.com/2019/day/5
 */
public class SolutionDay5 {
    private static int INPUT = 1;

    public List<Integer> part1(int[] inputs) {
        List<Integer> resultOutputs = new ArrayList<>();
        int index = 0;

        while (inputs[index] != 99) {
            List<Integer> digits = parseOpcodeAndParameter(inputs[index]);
            int len = digits.size();
            int opcode = digits.get(len - 1);

            int parameterModeIndex = len - 3; //since last 2 digits are opcode
            int result;
            switch (opcode) {
                case 1:
                    int num1 = resolve(inputs, inputs[index + 1], parameterModeIndex >= 0 ? digits.get(parameterModeIndex--) : 0);
                    int num2 = resolve(inputs, inputs[index + 2], parameterModeIndex >= 0 ? digits.get(parameterModeIndex) : 0);
                    result = num1 + num2;

                    int targetIndex = inputs[index + 3];
                    inputs[targetIndex] = result;
                    index += 4;
                    break;
                case 2:
                    num1 = resolve(inputs, inputs[index + 1], parameterModeIndex >= 0 ? digits.get(parameterModeIndex--) : 0);
                    num2 = resolve(inputs, inputs[index + 2], parameterModeIndex >= 0 ? digits.get(parameterModeIndex) : 0);
                    result = num1 * num2;

                    targetIndex = inputs[index + 3];
                    inputs[targetIndex] = result;
                    index += 4;
                    break;
                case 3:
                    //Input
                    System.out.println("READING");
                    targetIndex = inputs[index + 1];
                    inputs[targetIndex] = INPUT;
                    index += 2;
                    break;
                case 4:
                    //Output
                    int target = resolve(inputs, inputs[index + 1], parameterModeIndex >= 0 ? digits.get(parameterModeIndex) : 0);
                    System.out.println(target);
                    resultOutputs.add(target);
                    index += 2;
                    break;
            }
        }
        return resultOutputs;
    }

    private List<Integer> parseOpcodeAndParameter(int opcodeAndParameter) {
        List<Integer> digits = new ArrayList<>();
        while (opcodeAndParameter > 0) {
            digits.add(opcodeAndParameter % 10);
            opcodeAndParameter /= 10;
        }
        Collections.reverse(digits);
        return digits;
    }

    private int resolve(int[] inputs, int index, int parameterModeIndex) {
        if (parameterModeIndex == 0) {
            return inputs[index]; //position mode
        } else {
            return index; //immediate mode
        }
    }
}
