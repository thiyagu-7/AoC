package com.thiyagu_7.adventofcode.year2020.day16;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class SolutionDay16 {
    public int part1(List<String> rules, List<String> nearbyTickets) {
        Set<Integer> allTickets = new HashSet<>();

        rules.forEach(rule -> processRule(rule, allTickets));

        int invalidTicketValues = 0;
        for(String line: nearbyTickets) {
            String[] p = line.split(",");
            for(String numString : p) {
                int num = Integer.parseInt(numString);
                if (!allTickets.contains(num)) {
                    invalidTicketValues += num;
                }
            }
        }
        return invalidTicketValues;
    }

    //e.g., row: 6-11 or 33-44
    private void processRule(String rule, Set<Integer> allTickets) {
        String range = rule.split(":")[1];
        for (String oneRange : range.split("or")) {
            String[] nums = oneRange.trim().split("-");

            IntStream.rangeClosed(Integer.parseInt(nums[0].trim()), Integer.parseInt(nums[1].trim()))
                    .forEach(allTickets::add);
        }
    }
}
