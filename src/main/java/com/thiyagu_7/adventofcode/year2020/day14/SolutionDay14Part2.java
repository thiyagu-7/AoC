package com.thiyagu_7.adventofcode.year2020.day14;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionDay14Part2 {
    private Map<Long, Integer> memoryMap = new HashMap<>();

    public long part2(List<String> input) {
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
            String valAsBinaryString = Integer.toBinaryString(Integer.parseInt(memory));

            for(int i = valAsBinaryString.length() - 1, j = mask.length() - 1; j >=0; i--, j--) {
                char m = mask.charAt(j);
                if (m == '0') {//unchanged if '0'
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
            writeValues(0, "", res.toString(), Integer.parseInt(value));
    }

    private void writeValues(int i, String address, String originalAddress, int val) {
        if (i == originalAddress.length()) {
            memoryMap.put(Long.parseLong(address, 2), val);
        } else {
            char curr = originalAddress.charAt(i);
            if (curr == 'X') {
                writeValues(i + 1, address + '1', originalAddress, val);
                writeValues(i + 1, address + '0', originalAddress, val);
            } else {
                writeValues(i + 1, address + curr, originalAddress, val);
            }
        }

    }
}
