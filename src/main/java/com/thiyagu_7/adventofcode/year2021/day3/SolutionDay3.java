package com.thiyagu_7.adventofcode.year2021.day3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionDay3 {
    public int part1(List<String> input) {
        int n = input.size();
        StringBuilder gammaRate = new StringBuilder();
        StringBuilder epsilonRate = new StringBuilder();
        int numberOfOnes;
        for (int j = 0; j < input.get(0).length(); j++) {
            numberOfOnes = 0;
            for (String s : input) {
                if (s.charAt(j) == '1') {
                    numberOfOnes++;
                }
            }
            if (numberOfOnes > n / 2) {
                gammaRate.append("1");
                epsilonRate.append("0");
            } else {
                gammaRate.append("0");
                epsilonRate.append("1");
            }
        }
        return Integer.parseInt(gammaRate.toString(), 2)
                * Integer.parseInt(epsilonRate.toString(), 2);
    }

    public int part2(List<String> input) {
        List<String> oxygenGenerator = new ArrayList<>(input);
        List<String> co2Scrubber = new ArrayList<>(input);

        String oxygenGeneratorRating = process(oxygenGenerator, RatingType.OXYGEN_GENERATOR);
        String co2ScrubberRating = process(co2Scrubber, RatingType.CO2_SCRUBBER);

        return Integer.parseInt(oxygenGeneratorRating, 2)
                * Integer.parseInt(co2ScrubberRating, 2);
    }

    private enum RatingType {
        OXYGEN_GENERATOR,
        CO2_SCRUBBER
    }

    private String process(List<String> list, RatingType ratingType) {
        for (int j = 0; j < list.get(0).length(); j++) {
            int n = list.size();
            if (n == 1) {
                return list.get(0);
            }
            int numberOfOnes = 0;
            for (String s : list) {
                if (s.charAt(j) == '1') {
                    numberOfOnes++;
                }
            }
            char c;
            /*
            oxygen generator rating - most common value
            CO2 scrubber rating - least common value
             */
            if (numberOfOnes >= (n + 1) / 2) {
                c = ratingType == RatingType.OXYGEN_GENERATOR ? '1' : '0';
            } else {
                c = ratingType == RatingType.OXYGEN_GENERATOR ? '0' : '1';
            }
            int col = j;
            list = list.stream()
                    .filter(s -> s.charAt(col) == c)
                    .collect(Collectors.toList());
        }
        return list.get(0);
    }
}
