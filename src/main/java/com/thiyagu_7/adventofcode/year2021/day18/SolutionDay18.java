package com.thiyagu_7.adventofcode.year2021.day18;

import java.util.List;

public class SolutionDay18 {
    public long part1(List<String> input) {
        String result = input.get(0);
        SnailfishNumber snailfishNumber = null;
        for (int i = 1; i < input.size(); i++) {
            String number = join(result, input.get(i));
            snailfishNumber = new SnailfishNumber(number);
            snailfishNumber.reduce();
            result = snailfishNumber.getSnailfishNumber();
        }
        // last number is the final - get magnitude of that.
        assert snailfishNumber != null;
        return snailfishNumber.findMagnitude();
    }

    private String join(String number1, String number2) {
        return "[" + number1 + "," + number2 + "]";
    }

    public long part2(List<String> input) {
        long max = 0;
        for (int i = 0; i < input.size() - 1; i++) {
            for (int j = 1 + 1; j < input.size(); j++) {
                SnailfishNumber snailfishNumber1 = new SnailfishNumber(join(input.get(i), input.get(j)));
                SnailfishNumber snailfishNumber2 = new SnailfishNumber(join(input.get(j), input.get(i)));
                snailfishNumber1.reduce();
                snailfishNumber2.reduce();
                max = Math.max(max, Math.max(snailfishNumber1.findMagnitude(), snailfishNumber2.findMagnitude()));
            }
        }
        return max;
    }

}
