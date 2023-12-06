package com.thiyagu_7.adventofcode.year2023.day4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionDay4 {
    public int part1(List<String> input) {
        return input.stream()
                .map(this::parseInputLine)
                .map(this::noOfMatches)
                .map(n -> (int) Math.pow(2, n - 1)) //first winning card = 1 pt; doubles thereafter
                .reduce(0, Integer::sum);
    }

    private InputLine parseInputLine(String line) {
        String[] numbers = line.split(": ")[1]
                .split(" \\| ");
        return new InputLine(
                convertStringArrayToSet(numbers[0].trim().split("\\s+")),
                convertStringArrayToSet(numbers[1].trim().split("\\s+")));
    }

    private Set<Integer> convertStringArrayToSet(String[] s) {
        return Arrays.stream(s)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    private int noOfMatches(InputLine inputLine) {
        return (int) inputLine.winningNumbers
                .stream()
                .filter(inputLine.ourNumbers::contains)
                .count();
    }

    public int part2(List<String> input) {
        Map<Integer, Integer> cardNumberToNumberOfMatches = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            cardNumberToNumberOfMatches.put(i + 1,
                    noOfMatches(parseInputLine(input.get(i))));
        }

        Map<Integer, Integer> cardNumberToNumOfInstances = IntStream.rangeClosed(1, input.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), ignoreMe -> 1));

        // you win copies of the scratchcards below the winning card equal to the number of matches.
        for (Map.Entry<Integer, Integer> entry : cardNumberToNumberOfMatches.entrySet()) {
            int currentCardNum = entry.getKey();
            int noOfMatches = entry.getValue();
            int noOfInstancesOfCurrentCard = cardNumberToNumOfInstances.get(currentCardNum);

            //for each of the cards from [currentCardNum + 1 ... currentCardNum + noOfMatches], we win
            //new cards equal to the noOfInstancesOfCurrentCard
            for (int i = 0; i < noOfMatches; i++) {
                cardNumberToNumOfInstances.compute(++currentCardNum, (k, v) -> v + noOfInstancesOfCurrentCard);
            }
        }
        return cardNumberToNumOfInstances.values()
                .stream()
                .mapToInt(v -> v)
                .sum();
    }

    record InputLine(Set<Integer> winningNumbers, Set<Integer> ourNumbers) {

    }


}
