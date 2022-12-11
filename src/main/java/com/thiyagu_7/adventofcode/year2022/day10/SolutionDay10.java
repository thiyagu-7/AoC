package com.thiyagu_7.adventofcode.year2022.day10;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SolutionDay10 {
    public int part1(List<String> input) {
        int cycle = 0;
        int signalStrengths = 0;
        Register register = new Register(1);
        List<Integer> cycleNums = List.of(20, 60, 100, 140, 180, 220);

        for (String instruction : input) {
            List<CycleAndRegisterValue> cycleAndRegisterValues = processInstruction(cycle, register, instruction);

            Map<Integer, Integer> cyclesToValue = cycleAndRegisterValues.stream()
                    .collect(Collectors.toMap(CycleAndRegisterValue::cycle, CycleAndRegisterValue::registerValue));
            //signal strength - the cycle number multiplied by the value of the register
            for (Integer cycleNum : cycleNums) {
                if (cyclesToValue.containsKey(cycleNum)) {
                    signalStrengths += cycleNum * cyclesToValue.get(cycleNum);
                    break;
                }
            }
            cycle = new TreeSet<>(cyclesToValue.keySet()).last(); //get max cycle num
        }
        return signalStrengths;
    }

    // returns register value 'during' a cycle
    private List<CycleAndRegisterValue> processInstruction(int previousCycle, Register register, String instruction) {
        String[] parts = instruction.split(" ");
        int oldRegisterValue = register.value;

        if (parts[0].equals("noop")) {
            return List.of(new CycleAndRegisterValue(previousCycle + 1, oldRegisterValue));
        }
        //end of cycle 2, register has,
        register.value += Integer.parseInt(parts[1]);
        return List.of(
                new CycleAndRegisterValue(previousCycle + 1, oldRegisterValue),
                new CycleAndRegisterValue(previousCycle + 2, oldRegisterValue));
    }

    public char[][] part2(List<String> input) {
        char[][] crt = new char[6][40];
        Register register = new Register(1);
        int cycle = 0;

        int row = 0;
        for (String instruction : input) {
            int currentRegisterValue = register.value;
            List<CycleAndRegisterValue> cycleAndRegisterValues = processInstruction(cycle, register, instruction);
           /*
            The CRT draws a single pixel during each cycle.
            If the sprite is positioned such that one of its three pixels is the pixel currently being drawn,
            the screen produces a lit pixel (#); otherwise, the screen leaves the pixel dark (.)
             */
            for (CycleAndRegisterValue cycleAndRegisterValue : cycleAndRegisterValues) {
                int processingCycle = cycleAndRegisterValue.cycle;
                int positionToDraw = (processingCycle - 1) % 40;

                if (positionToDraw == currentRegisterValue - 1 ||
                        positionToDraw == currentRegisterValue ||
                        positionToDraw == currentRegisterValue + 1) {
               /* if (processingCycle % 40 >= currentRegisterValue &&
                        processingCycle % 40 <= currentRegisterValue + 2) { //doesn't work */
                    crt[row][positionToDraw] = '#';
                } else {
                    crt[row][positionToDraw] = '.';
                }
                if (processingCycle % 40 == 0) {
                    row++;
                }
            }
            List<Integer> cycles = cycleAndRegisterValues.stream()
                    .map(CycleAndRegisterValue::cycle)
                    .collect(Collectors.toCollection(ArrayList::new));
            cycle = cycles.get(cycles.size() - 1); //get max cycle num which is the last one.
        }
        /*for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 40; j++) {
                System.out.print(crt[i][j]);
            }
            System.out.println();
        }*/
        return crt;
    }

    private static class Register {
        private int value;

        public Register(int value) {
            this.value = value;
        }
    }

    private record CycleAndRegisterValue(int cycle, int registerValue) {

    }
}
