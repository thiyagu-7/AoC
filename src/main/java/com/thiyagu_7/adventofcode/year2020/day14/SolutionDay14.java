package com.thiyagu_7.adventofcode.year2020.day14;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionDay14 {
    private Map<String, Long> memoryMap = new HashMap<>();

    public long part1(List<String> input) {
        String mask = "";
        for (String line : input) {
            if (line.startsWith("mask")) {
                mask = line.split("=")[1].trim(); //next batch
            } else {
                //eg, mem[60126] = 9674686
                String[] parts = line.split("=");
                String memVal = parts[0].split("mem")[1];
                String memory = memVal.substring(1, memVal.length() - 2);
                String value = parts[1].trim();

                applyMask(mask, memory, value);
            }
        }
       return memoryMap.values()
                .stream()
                .mapToLong(l -> l)
                .sum();

    }

    private void applyMask(String mask, String memory, String value) {
        StringBuilder res = new StringBuilder();
        String valAsBinaryString = Integer.toBinaryString(Integer.parseInt(value));

        for (int i = valAsBinaryString.length() - 1, j = mask.length() - 1; j >= 0; i--, j--) {
            char m = mask.charAt(j);
            if (m == 'X') {
                if (i >= 0) {
                    res.append(valAsBinaryString.charAt(i));
                } else {
                    res.append('0');
                }
            } else {
                res.append(m);
            }
        }
        res.reverse();
        memoryMap.put(memory, Long.parseLong(res.toString(), 2));
    }
}
