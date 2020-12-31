package com.thiyagu_7.adventofcode.year2020.day21;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay21 {
    public long part1(List<String> input) {
        Map<String, Map<String, Integer>> allergenToIngredientsCount = new HashMap<>();
        Map<String, Integer> ingredientsCount = new HashMap<>();

        input.forEach(l -> processLine(l, allergenToIngredientsCount, ingredientsCount));
        allergenToIngredientsCount.forEach((k, v) -> System.out.println(k + " " + v));

        Set<String> eliminated = new HashSet<>();
        //for each allergen, find the ingredients that appear max num times and eliminate that ingredient.
        for (Map.Entry<String, Map<String, Integer>> e : allergenToIngredientsCount.entrySet()) {
            int max = e.getValue().values()
                    .stream()
                    .max(Comparator.naturalOrder())
                    .orElse(0);

            e.getValue().entrySet()
                    .stream()
                    .filter(ingCount -> ingCount.getValue().equals(max))
                    .forEach(ingCount -> eliminated.add(ingCount.getKey()));
        }

        return ingredientsCount.entrySet().stream()
                .filter(ingredient -> !eliminated.contains(ingredient.getKey()))
                .mapToLong(Map.Entry::getValue)
                .sum();
    }

    private void processLine(String line, Map<String, Map<String, Integer>> allergenToIngredientsCount,
                             Map<String, Integer> ingredientsCount) {
        String[] p = line.split(" \\(contains ");
        String ingredientsStr = p[0].trim();
        String allergensStr = p[1].substring(0, p[1].length() - 1).trim();

        Set<String> ingredients = Arrays.stream(ingredientsStr.split(" "))
                .map(String::trim)
                .collect(Collectors.toSet());
        Set<String> allergens = Arrays.stream(allergensStr.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        for (String ingredient : ingredients) {
            ingredientsCount.merge(ingredient, 1, (o, n) -> o + 1);
        }
        for (String a : allergens) {
            if (allergenToIngredientsCount.containsKey(a)) {
                Map<String, Integer> ingToCount = allergenToIngredientsCount.get(a);
                for (String in : ingredients) {
                    ingToCount.merge(in, 1, (o, n) -> o + 1);
                }
            } else {
                allergenToIngredientsCount.put(a, ingredients.stream()
                        .collect(Collectors.toMap(in -> in, in -> 1)));
            }
        }
    }

    public String part2(List<String> input) {
        Map<String, Map<String, Integer>> allergenToIngredientsCount = new HashMap<>();
        Map<String, Integer> ingredientsCount = new HashMap<>();

        input.forEach(l -> processLine(l, allergenToIngredientsCount, ingredientsCount));
        allergenToIngredientsCount.forEach((k, v) -> System.out.println(k + " " + v));


        //sort such that there are min num of ingredients with max count first.
        /*
        dairy {nhms=1, kfcds=1, sbzzf=1, trh=1, mxmxvkd=2, sqjhc=1, fvjkl=1}
        fish {nhms=1, kfcds=1, sbzzf=1, mxmxvkd=2, sqjhc=2}
        soy {sqjhc=1, fvjkl=1}
         */
        Map<String, Map<String, Integer>> sortedAllergenToIngredientsCount =
                allergenToIngredientsCount.entrySet()
                        .stream()
                        .sorted(Comparator.comparingInt((Map.Entry<String, Map<String, Integer>> e) -> {
                            int max = e.getValue().values()
                                    .stream()
                                    .max(Comparator.naturalOrder())
                                    .orElse(0);
                            return max;
                        }).reversed()
                                .thenComparingLong(e -> {
                                    int max = e.getValue().values()
                                            .stream()
                                            .max(Comparator.naturalOrder())
                                            .orElse(0);
                                    long numOfIngWithMaxValue = e.getValue().values()
                                            .stream()
                                            .filter(ingCount -> ingCount.equals(max))
                                            .count();
                                    return numOfIngWithMaxValue;
                                }))
                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(),
                                (a, b) -> a, LinkedHashMap::new));

        Set<String> allocatedIngredients = new HashSet<>();
        Map<String, String> allergenToIngredient = new HashMap<>();

        while (true) {
            int skipped = 0;
            for (Map.Entry<String, Map<String, Integer>> entry : sortedAllergenToIngredientsCount.entrySet()) {
                int max = entry.getValue().values()
                        .stream()
                        .max(Comparator.naturalOrder())
                        .orElse(0);

                Set<String> candidateIngredients = entry.getValue().entrySet()
                        .stream()
                        .filter(ingCount -> ingCount.getValue().equals(max))
                        .filter(ingCount -> !allocatedIngredients.contains(ingCount.getKey()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());
                if (candidateIngredients.size() > 1) {
                    System.out.println("Skipping " + entry.getKey() + " for " + candidateIngredients);
                    skipped++;
                    continue;
                }
                if (!candidateIngredients.isEmpty()) {
                    allocatedIngredients.add(candidateIngredients.iterator().next());
                    allergenToIngredient.put(entry.getKey(), candidateIngredients.iterator().next());
                }
            }

            if (skipped == 0) {
                break;
            }
        }

        return allergenToIngredient.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey)) //sort by allergen
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(","));
    }
}
