package com.thiyagu_7.adventofcode.year2020.day13;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionDay13 {
    public int part1(List<String> input) {
        int earliestToLeave = Integer.parseInt(input.get(0));
        List<Integer> busIds = Arrays.stream(input.get(1).split(","))
                .filter(s -> !s.equals("x"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int resultTime = Integer.MAX_VALUE;
        int chosenBusId = 0;
        for (int busId: busIds) {
            int time = earliestToLeave;
            while (true) {
                if (time % busId == 0) {
                    if (resultTime > time) {
                        resultTime = time;
                        chosenBusId = busId;
                    }
                    break;
                }
                time++;
            }
        }
        return (resultTime - earliestToLeave) * chosenBusId;
    }

    public long part2Brute(List<String> input) {
        List<String> busIdInput = Arrays.stream(input.get(1).split(","))
                .collect(Collectors.toList());
        int maxBusId = busIdInput.stream()
                .filter(s -> !s.equals("x"))
                .map(Integer::parseInt)
                .max(Comparator.naturalOrder())
                .get();
        int indexOfMax = busIdInput.indexOf(maxBusId + "");

        long num = maxBusId;
        while (true) {
            long startingTime = num - indexOfMax;
            long res = startingTime;

            boolean f = true;
            for (String busId : busIdInput) {
                if (!busId.equals("x")) {
                    if (startingTime % Integer.parseInt(busId) != 0) {
                        f = false;
                        break;
                    }
                }
                startingTime++;
            }
            if (f) {
                return res;
            }
            num = num + maxBusId; //next multiple
        }
    }
    public long part2(List<String> input) {
        List<String> busIdInput = Arrays.stream(input.get(1).split(","))
                .collect(Collectors.toList());
        long current = 0;
        long lcm = 1;

        int i = 1;
        for (String busId : busIdInput) {
            if (!busId.equals("x")) {
                while (true) {
                    current += lcm;
                    if (checkAlignment(busIdInput.subList(0, i), current)) {
                        break;
                    }
                }
                lcm = lcm * Integer.parseInt(busId);
            }
            i++;
        }
        return current;
    }

    private boolean checkAlignment(List<String> busIdInput, long timestamp) {
        for (String busId : busIdInput) {
            if (!busId.equals("x")) {
                if (timestamp % Integer.parseInt(busId) != 0) {
                   return false;

                }
            }
            timestamp++;
        }
        return true;
    }
}
