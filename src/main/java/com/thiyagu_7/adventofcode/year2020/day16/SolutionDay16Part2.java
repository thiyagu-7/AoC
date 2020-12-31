package com.thiyagu_7.adventofcode.year2020.day16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionDay16Part2 {
    public long part2(List<String> rules, String yourTicket, List<String> nearbyTickets) {
        //fieldName to set of valid values
        Map<String, TreeSet<Integer>> map = new HashMap<>();

        rules.forEach(rule -> processRule(rule, map));

        //discard tickets with invalid value
        List<String> validTickets = discardInvalidTickets(map, nearbyTickets);
        Integer[][] tickets = new Integer[validTickets.size() + 1][yourTicket.length()];

        tickets[0] = Arrays.stream(yourTicket.split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
        for (int i = 0; i < validTickets.size(); i++) {
            String[] ticketNums = validTickets.get(i).split(",");
            tickets[i + 1] = Arrays.stream(ticketNums)
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new);
        }

        Map<Integer, String> colToFieldName = new HashMap<>();
        Map<Integer, Set<String>> colToPotentialFieldName = new HashMap<>();
        //iterate by col
        for (int i = 0; i < tickets[0].length; i++) {
            Set<String> potential = new HashSet<>();
            for (int j = 0; j < tickets.length; j++) {
                Integer ticketVal = tickets[j][i];
                if (potential.isEmpty()) {
                    potential = map.entrySet().stream()
                            .filter(entry -> entry.getValue().contains(ticketVal))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());
                } else {
                    //intersection
                    Set<String> finalPotential = potential;
                    potential = map.entrySet().stream()
                            .filter(entry -> entry.getValue().contains(ticketVal))
                            .filter(entry -> finalPotential.contains(entry.getKey()))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());
                }
            }
            colToPotentialFieldName.put(i, potential);
        }
        System.out.println(colToFieldName);

        Map<Integer, Set<String>> sortedColToPotentialFieldName = colToPotentialFieldName.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getValue().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new));


        for (Map.Entry<Integer, Set<String>> entry : sortedColToPotentialFieldName.entrySet()) {
            Set<String> current = entry.getValue();
            Set<String> finalV = current.stream()
                    .filter(s -> !colToFieldName.values().contains(s))
                    .collect(Collectors.toSet());
            if (finalV.size() != 1) {
                System.out.println(finalV);
                throw new RuntimeException("Size was not one");
            }
            colToFieldName.put(entry.getKey(), finalV.iterator().next());
        }

        String[] myTicketParts = yourTicket.split(",");
        return IntStream.range(0, myTicketParts.length)
                .filter(i -> colToFieldName.get(i).startsWith("departure"))
                .mapToLong(i -> Long.parseLong(myTicketParts[i]))
                .reduce(1L, (a, b) -> a * b);
    }

    private List<String> discardInvalidTickets(Map<String, TreeSet<Integer>> map, List<String> nearbyTickets) {
        List<String> validTickets = new ArrayList<>();

        Map<String, TreeSet<Integer>> clonedMap = new HashMap<>(map);
        for (String line : nearbyTickets) { //one line - 3,9,18
            String[] ticketNums = line.split(",");
            boolean valid = false;

            for (String numString : ticketNums) {
                int num = Integer.parseInt(numString);
                valid = false;
                for (Map.Entry<String, TreeSet<Integer>> entry : clonedMap.entrySet()) {
                    if (entry.getValue().contains(num)) {
                        //  entry.getValue().remove(num);
                        valid = true;
                    }
                }
                if (!valid) {
                    break; //don't process other ticket values in same line if found an invalid
                }
            }
            if (valid) {
                validTickets.add(line);
            }
        }
        return validTickets;
    }

    //e.g., row: 6-11 or 33-44
    private void processRule(String rule, Map<String, TreeSet<Integer>> map) {
        String[] parts = rule.split(":");
        String fieldName = parts[0];
        String range = parts[1].trim();
        for (String oneRange : range.split("or")) {
            String[] nums = oneRange.trim().split("-");

            IntStream.rangeClosed(Integer.parseInt(nums[0].trim()), Integer.parseInt(nums[1].trim()))
                    .forEach(i -> map.computeIfAbsent(fieldName, ignoreMe -> new TreeSet<>())
                            .add(i));
        }

    }
}

