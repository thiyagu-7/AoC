package com.thiyagu_7.adventofcode.year2023.day15;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class SolutionDay15 {
    public int part1(List<String> input) {
        String onlyLine = input.get(0);
        return Arrays.stream(onlyLine.split(","))
                .mapToInt(this::hash)
                .sum();
    }

    private int hash(String s) {
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash += c;
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }

    public int part2(List<String> input) {
        String onlyLine = input.get(0);
        Map<Integer, List<Lens>> map = new HashMap<>();
        IntStream.rangeClosed(0, 255)
                .forEach(i -> map.put(i, new ArrayList<>()));

        for (String step : onlyLine.split(",")) {
            if (step.contains("=")) {
                String[] parts = step.split("=");
                String lensLabel = parts[0];
                int focalLength = Integer.parseInt(parts[1]);

                int boxId = hash(lensLabel);
                List<Lens> lens = map.get(boxId);
                //update lens' focalLength or add if not present
                int idx = lens.indexOf(new Lens(lensLabel, focalLength)); //focalLength value not involved in 'equals'
                if (idx != -1) {
                    lens.get(idx)
                            .setFocalLength(focalLength);
                } else {
                    lens.add(new Lens(lensLabel, focalLength));
                }

            } else {
                String lensLabel = step.substring(0, step.length() - 1);
                int boxId = hash(lensLabel);
                List<Lens> lens = map.get(boxId);
                //remove if present
                lens.remove(new Lens(lensLabel, 0)); //using dummy value for focalLength
            }
        }
        //find focusing power of all the lenses
        return map.entrySet().stream()
                .mapToInt(e -> IntStream.range(0, e.getValue().size())
                        .map(lensIndex -> (1 + e.getKey()) * (1 + lensIndex) * e.getValue().get(lensIndex).focalLength)
                        .sum()
                )
                .sum();
    }

    public int part2a(List<String> input) {
        String onlyLine = input.get(0);
        //using a different data structure
        Map<Integer, Map<String, Integer>> map = new HashMap<>();
        IntStream.rangeClosed(0, 255)
                .forEach(i -> map.put(i, new LinkedHashMap<>())); //important - to preserve insertion order

        for (String step : onlyLine.split(",")) {
            if (step.contains("=")) {
                String[] parts = step.split("=");
                String lensLabel = parts[0];
                int focalLength = Integer.parseInt(parts[1]);

                int boxId = hash(lensLabel);
                Map<String, Integer> lens = map.get(boxId);
                //update lens' focalLength or add if not present
                lens.put(lensLabel, focalLength);

            } else {
                String lensLabel = step.substring(0, step.length() - 1);
                int boxId = hash(lensLabel);
                Map<String, Integer> lens = map.get(boxId);
                //remove if present
                lens.remove(lensLabel);
            }
        }
        //find focusing power of all the lenses
        int sum = 0;
        for(Map.Entry<Integer, Map<String, Integer>> outer: map.entrySet()) {
            int idx = 0;
            for(Map.Entry<String, Integer> inner: outer.getValue().entrySet()) {
                sum += (1 + outer.getKey()) * (1 + idx++) * (inner.getValue());
            }
         }
        return sum;
    }

    @Getter
    @Setter
    @ToString
    private static class Lens {
        private final String label;
        private int focalLength;

        private Lens(String label, int focalLength) {
            this.label = label;
            this.focalLength = focalLength;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lens lens = (Lens) o;
            return Objects.equals(label, lens.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }
    }
}
