package com.thiyagu_7.adventofcode.year2021.day24;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

// Real solve done by hand. See notes
public class SolutionDay24 {
    public Map<String, Long> process(List<String> input, List<Integer> digits) {
        Iterator<Integer> itr = digits.iterator();
        return process(input, itr::next);
    }

    private Map<String, Long> process(List<String> input, Supplier<Integer> inputDigitSupplier) {
        Map<String, Long> alu = new HashMap<>(Map.of("w", 0L, "x", 0L, "y", 0L, "z", 0L));
        for (String line : input) {
            String[] parts = line.split(" ", 2);
            if (parts[0].equals("inp")) {
                alu.put(parts[1], (long) inputDigitSupplier.get());
            } else {
                String[] operands = parts[1].split(" ");
                switch (parts[0]) {
                    case "add":
                        alu.put(operands[0],
                                alu.get(operands[0]) + resolve(alu, operands[1]));
                        break;
                    case "mul":
                        alu.put(operands[0],
                                alu.get(operands[0]) * resolve(alu, operands[1]));
                        break;
                    case "div":
                        alu.put(operands[0],
                                alu.get(operands[0]) / resolve(alu, operands[1]));
                        break;
                    case "mod":
                        alu.put(operands[0],
                                alu.get(operands[0]) % resolve(alu, operands[1]));
                        break;
                    case "eql":
                        alu.put(operands[0],
                                alu.get(operands[0]).equals(resolve(alu, operands[1]))
                                        ? 1L : 0L);
                        break;
                }
            }
        }
        return alu;
    }

    private Long resolve(Map<String, Long> alu, String operand) {
        return operand.length() == 1 && Character.isLetter(operand.charAt(0))
                ? alu.get(operand)
                : Long.parseLong(operand);
    }
}
