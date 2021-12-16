package com.thiyagu_7.adventofcode.year2021.day16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SolutionDay16Final {
    private static final Map<Character, String> HEX_BIN = new HashMap<>();

    static {
        HEX_BIN.put('0', "0000");
        HEX_BIN.put('1', "0001");
        HEX_BIN.put('2', "0010");
        HEX_BIN.put('3', "0011");
        HEX_BIN.put('4', "0100");
        HEX_BIN.put('5', "0101");
        HEX_BIN.put('6', "0110");
        HEX_BIN.put('7', "0111");
        HEX_BIN.put('8', "1000");
        HEX_BIN.put('9', "1001");
        HEX_BIN.put('A', "1010");
        HEX_BIN.put('B', "1011");
        HEX_BIN.put('C', "1100");
        HEX_BIN.put('D', "1101");
        HEX_BIN.put('E', "1110");
        HEX_BIN.put('F', "1111");
    }

    public long part1(String inputHex) {
        String binaryInput = hexToBinary(inputHex);
        Part1Result part1Result = new Part1Result();
        // ProcessedResult not needed for part 1
        process(binaryInput, part1Result);
        return part1Result.result;
    }

    public long part2(String inputHex) {
        String binaryInput = hexToBinary(inputHex);
        ProcessedResult processedResult = process(binaryInput, new Part1Result());
        //only one value will be there at the end
        return processedResult.values.get(0);
    }

    private ProcessedResult process(String binary, Part1Result part1Result) {
        int version = binaryToDecimalInt(binary.substring(0, 3));
        int typeId = binaryToDecimalInt(binary.substring(3, 6));

        part1Result.result += version;

        ProcessedResult processedResult;
        if (typeId == 4) {
            processedResult = parseLiteralValuePacket(binary.substring(6));
            return new ProcessedResult(1, processedResult.lengthOfPackets + 6,
                    processedResult.values);
        } else {
            processedResult = parseOperatorPacket(binary.substring(6), part1Result);
            long result;
            if (typeId == 0) {
                result = processedResult.values.stream()
                        .mapToLong(i -> i)
                        .sum();
            } else if (typeId == 1) {
                result = processedResult.values.stream()
                        .reduce(1L, (a, b) -> a * b);
            } else if (typeId == 2) {
                result = processedResult.values.stream()
                        .min(Comparator.naturalOrder())
                        .get();
            } else if (typeId == 3) {
                result = processedResult.values.stream()
                        .max(Comparator.naturalOrder())
                        .get();
            } else if (typeId == 5) {
                result = processedResult.values.get(0) > processedResult.values.get(1) ? 1 : 0;
            } else if (typeId == 6) {
                result = processedResult.values.get(0) < processedResult.values.get(1) ? 1 : 0;
            } else if (typeId == 7) {
                result = processedResult.values.get(0).equals(processedResult.values.get(1)) ? 1 : 0;
            } else {
                throw new RuntimeException("Cannot reach here!");
            }
            return new ProcessedResult(
                    1,
                    processedResult.lengthOfPackets + 6,
                    List.of(result));
        }
    }

    private ProcessedResult parseOperatorPacket(String binary, Part1Result part1Result) {
        char lengthTypeId = binary.charAt(0);
        ProcessedResult processedResult;
        int numberOfPacketsProcessed = 0;
        int lengthOfPacketsProcessed = 0;
        List<Long> values = new ArrayList<>();

        if (lengthTypeId == '0') {
            int totalLength = binaryToDecimalInt(binary.substring(1, 16));

            while (lengthOfPacketsProcessed < totalLength) {
                processedResult = process(binary.substring(lengthOfPacketsProcessed + 16), part1Result);
                numberOfPacketsProcessed += processedResult.numberOfPackets;
                lengthOfPacketsProcessed += processedResult.lengthOfPackets;
                values.addAll(processedResult.values);
            }
            return new ProcessedResult(numberOfPacketsProcessed,
                    lengthOfPacketsProcessed + 1 + 15, values);
        } else {
            int numOfSubPackets = binaryToDecimalInt(binary.substring(1, 12));

            while (numberOfPacketsProcessed < numOfSubPackets) {
                processedResult = process(binary.substring(lengthOfPacketsProcessed + 12), part1Result);
                numberOfPacketsProcessed += processedResult.numberOfPackets;
                lengthOfPacketsProcessed += processedResult.lengthOfPackets;
                values.addAll(processedResult.values);
            }
            return new ProcessedResult(numberOfPacketsProcessed,
                    lengthOfPacketsProcessed + 1 + 11, values);
        }
    }

    private ProcessedResult parseLiteralValuePacket(String binary) {
        int i = 0;
        int len = 0;
        StringBuilder valueBuilder = new StringBuilder();
        while (i < binary.length()) {
            String block = binary.substring(i, i + 5);
            len += 5;
            valueBuilder.append(block.substring(1));
            if (block.startsWith("0")) {
                return new ProcessedResult(1, len,
                        List.of(binaryToDecimalLong(valueBuilder.toString()))
                );
            }
            i += 5;
        }
        throw new RuntimeException("Cannot reach here!");
    }

    private static class Part1Result {
        private int result;
    }

    private static class ProcessedResult {
        private final int numberOfPackets;
        private final int lengthOfPackets;
        private final List<Long> values;

        private ProcessedResult(int numberOfPackets, int lengthOfPackets, List<Long> values) {
            this.numberOfPackets = numberOfPackets;
            this.lengthOfPackets = lengthOfPackets;
            this.values = values;
        }
    }

    private String hexToBinary(String hex) {
        return Arrays.stream(hex.split(""))
                .map(s -> HEX_BIN.get(s.charAt(0)))
                .collect(Collectors.joining());
    }

    private int binaryToDecimalInt(String bin) {
        return Integer.parseInt(bin, 2);
    }

    private long binaryToDecimalLong(String bin) {
        return Long.parseLong(bin, 2);
    }
}
