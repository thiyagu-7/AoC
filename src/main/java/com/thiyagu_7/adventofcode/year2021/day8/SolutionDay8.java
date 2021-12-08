package com.thiyagu_7.adventofcode.year2021.day8;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay8 {
    public int part1(List<String> input) {
        Set<Integer> neededLength = new HashSet<>(Arrays.asList(2, 3, 4, 7));
        List<String> allOutputValues = input.stream()
                .map(line -> line.split(" \\| ")[1])
                .collect(Collectors.toList());

        return (int) allOutputValues.stream()
                .flatMap(outputValues -> Arrays.stream(outputValues.split(" ")))
                .filter(outputValue -> neededLength.contains(outputValue.length()))
                .count();
    }

    public int part2(List<String> input) {
        int sum = 0;
        for (String line : input) {
            String[] parts = line.split(" \\| ");
            Map<String, Integer> patternToDigit = getPatternToDigit(parts[0]);
            int decodedValue = Integer.parseInt(Arrays.stream(parts[1].split(" "))
                    .map(outputValue -> {
                        char[] outputValueCharArray = outputValue.toCharArray();
                        Arrays.sort(outputValueCharArray);
                        return new String(outputValueCharArray);
                    })
                    .map(patternToDigit::get)
                    .reduce("", (s, c) -> s += c, (s1, s2) -> s1 + s2));
            sum += decodedValue;
        }
        return sum;
    }

    private Map<String, Integer> getPatternToDigit(String signalPatterns) {
        Map<Integer, List<Set<Character>>> lengthToSignalPatterns = Arrays.stream(signalPatterns.split(" "))
                .collect(Collectors.groupingBy(String::length,
                        Collectors.mapping(signalPattern -> signalPattern.chars()
                                .mapToObj(c -> (char) c)
                                .collect(Collectors.toSet()), Collectors.toList())));

        Map<Integer, Set<Character>> identifiedDigitToSignalPattern = new HashMap<>();
        identifiedDigitToSignalPattern.put(1, lengthToSignalPatterns.get(2).get(0));
        identifiedDigitToSignalPattern.put(4, lengthToSignalPatterns.get(4).get(0));
        identifiedDigitToSignalPattern.put(7, lengthToSignalPatterns.get(3).get(0));
        identifiedDigitToSignalPattern.put(8, lengthToSignalPatterns.get(7).get(0));

        Map<Character, Character> identifiedMappings = new HashMap<>();

        // 7 - 1 -> A
        Set<Character> onePattern = new HashSet<>(identifiedDigitToSignalPattern.get(1));
        Set<Character> sevenPattern = new HashSet<>(identifiedDigitToSignalPattern.get(7));
        sevenPattern.removeAll(onePattern);
        Character characterA = sevenPattern.iterator().next();
        identifiedMappings.put('A', characterA);

        /*
        1 compare with 0, 6, 9:
          For one digit - one char will be missing - (6, c) - lock C and F position
         */
        List<Set<Character>> zeroSixAndNine = lengthToSignalPatterns.get(6).stream()
                .map(HashSet::new)
                .collect(Collectors.toList());
        for (Set<Character> pattern : zeroSixAndNine) {
            onePattern = new HashSet<>(identifiedDigitToSignalPattern.get(1));
            onePattern.removeAll(pattern);
            if (!onePattern.isEmpty()) {
                identifiedMappings.put('C', onePattern.iterator().next());
                // get the other ('F')
                onePattern = new HashSet<>(identifiedDigitToSignalPattern.get(1));
                onePattern.remove(identifiedMappings.get('C'));
                identifiedMappings.put('F', onePattern.iterator().next());
                identifiedDigitToSignalPattern.put(6, pattern);
                break;
            }
        }
        /*
        Compare C and F with 2, 3, 5
          C, F both present - 3
          C present - 2
          F present - 5
         */
        List<Set<Character>> twoThreeAndFive = lengthToSignalPatterns.get(5).stream()
                .map(HashSet::new)
                .collect(Collectors.toList());
        for (Set<Character> pattern : twoThreeAndFive) {
            Character characterC = identifiedMappings.get('C');
            Character characterF = identifiedMappings.get('F');
            if (pattern.contains(characterC) && pattern.contains(characterF)) {
                identifiedDigitToSignalPattern.put(3, pattern);
            } else if (pattern.contains(characterC)) {
                identifiedDigitToSignalPattern.put(2, pattern);
            } else {
                identifiedDigitToSignalPattern.put(5, pattern);
            }
        }
        //2, 3 diff - E, F
        Set<Character> twoPattern = new HashSet<>(identifiedDigitToSignalPattern.get(2));
        Set<Character> threePattern = new HashSet<>(identifiedDigitToSignalPattern.get(3));
        twoPattern.removeAll(threePattern); //E
        threePattern.removeAll(identifiedDigitToSignalPattern.get(2)); //F
        identifiedMappings.put('E', twoPattern.iterator().next());
        identifiedMappings.put('F', threePattern.iterator().next());

        //3, 5 diff - B
        threePattern = new HashSet<>(identifiedDigitToSignalPattern.get(3));
        Set<Character> fivePattern = new HashSet<>(identifiedDigitToSignalPattern.get(5));
        fivePattern.removeAll(threePattern);
        identifiedMappings.put('B', fivePattern.iterator().next());

        //5, 6 diff - E
        fivePattern = new HashSet<>(identifiedDigitToSignalPattern.get(5));
        Set<Character> sixPattern = new HashSet<>(identifiedDigitToSignalPattern.get(6));
        sixPattern.removeAll(fivePattern);
        identifiedMappings.put('E', sixPattern.iterator().next());

        /*
        Compare 6 with remaining two in length 6 bucket (0 and 9)
          Diff is E - 9
          Diff is D - 0
         */
        List<Set<Character>> zeroAndNine = lengthToSignalPatterns.get(6).stream()
                .filter(set -> !set.equals(identifiedDigitToSignalPattern.get(6)))
                .map(HashSet::new)
                .collect(Collectors.toList());

        for (Set<Character> pattern : zeroAndNine) {
            sixPattern = new HashSet<>(identifiedDigitToSignalPattern.get(6));
            sixPattern.removeAll(pattern);
            if (sixPattern.iterator().next().equals(identifiedMappings.get('E'))) {
                identifiedDigitToSignalPattern.put(9, pattern);
            } else { //E
                identifiedMappings.put('D', sixPattern.iterator().next());
                identifiedDigitToSignalPattern.put(0, pattern);
            }
        }

        // find last ('G')
        identifiedMappings.put('G', lengthToSignalPatterns.get(7).get(0) //8
                .stream()
                .filter(character -> !identifiedMappings.containsValue(character))
                .findFirst()
                .get());

        //patternToDigit (sorted by pattern)
        return identifiedDigitToSignalPattern.entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(),
                        entry.getValue().stream()
                                .sorted()
                                .reduce("", (s, c) -> s += c, (s1, s2) -> s1 + s2)
                ))
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
