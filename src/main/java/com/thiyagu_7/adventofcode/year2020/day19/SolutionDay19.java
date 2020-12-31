package com.thiyagu_7.adventofcode.year2020.day19;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay19 {
    public long part1(Map<String, String> rules, List<String> messages) {
        Map<String, Set<String>> ruleToValidMessages = new HashMap<>();

        for (Map.Entry<String, String> rule : rules.entrySet()) {
            if (!ruleToValidMessages.containsKey(rule.getKey())) {
                Set<String> validMessages = process(rule.getKey(), rules, ruleToValidMessages);
                ruleToValidMessages.put(rule.getKey(), validMessages);
            }
        }
        System.out.println(ruleToValidMessages);
        Set<String> rule0ValidMessages = ruleToValidMessages.get("0");

        return messages.stream()
                .filter(rule0ValidMessages::contains)
                .count();
    }

    private Set<String> process(String ruleNum, Map<String, String> rules, Map<String, Set<String>> ruleToValidMessages) {
        if (ruleToValidMessages.containsKey(ruleNum)) {
            return ruleToValidMessages.get(ruleNum);
        }
        String rhs = rules.get(ruleNum);
        if (rhs.startsWith("\"")) {
            return Collections.singleton(rhs.replaceAll("\"", ""));
        } else {
            Set<String> validMessages = new HashSet<>();
            String[] sections = rhs.split(" \\| "); //eg,  2 3 | 3 2

            Set<String> messages;
            for (String section : sections) {
                messages = new HashSet<>();
                String[] p = section.split(" ");
                for (String ruleNumber : p) {
                    Set<String> res = process(ruleNumber, rules, ruleToValidMessages);
                    ruleToValidMessages.put(ruleNumber, res);
                    messages = combine(messages, res);
                }
                validMessages.addAll(messages);
            }
            return validMessages;
        }
    }

    private Set<String> combine(Set<String> list1, Set<String> list2) {
        if (list1.isEmpty()) {
            return list2;
        }
        if (list2.isEmpty()) {
            return list1;
        }

        return list1.stream()
                .flatMap(l1 -> list2.stream()
                        .map(l2 -> l1 + l2))
                .collect(Collectors.toSet());
    }

    public long part2(Map<String, String> rules, List<String> messages) {
        Map<String, Set<String>> ruleToValidMessages = new HashMap<>();

        for (Map.Entry<String, String> rule : rules.entrySet()) {
            if (!ruleToValidMessages.containsKey(rule.getKey())) {
                Set<String> validMessages = process(rule.getKey(), rules, ruleToValidMessages);
                ruleToValidMessages.put(rule.getKey(), validMessages);
            }
        }
        //8: 42 -> 8: 42 | 8
        //11: 42 31 -> 11: 42 31 | 42 11 31

        List<String> valsForRule42List = new ArrayList<>(ruleToValidMessages.get("42"));
        List<String> valsForRule31List = new ArrayList<>(ruleToValidMessages.get("31"));

        //rule 0 : 8 11 -> 42 42 42 42 42........ 42 42 42 31 31 31
        int c = 0;
        for (String m : messages) {
            for (String valFrom31 : valsForRule31List) {
                String message = m;
                if (message.endsWith(valFrom31)) {

                    int num31 = 0;
                    int num42 = 0;
                    for (int i = 0; i < valsForRule31List.size(); i++) {
                        String valFrom31_1 = valsForRule31List.get(i);
                        if (message.endsWith(valFrom31_1)) {
                            message = message.substring(0, message.length() - valFrom31.length());
                            num31++;
                            i = -1;
                        }
                    }

                    String prefix = message;
                    for (int i = 0; i < valsForRule42List.size(); i++) {
                        String valFrom42 = valsForRule42List.get(i);
                        if (prefix.startsWith(valFrom42)) {
                            prefix = prefix.substring(valFrom42.length());
                            num42++;
                            i = -1;
                        }
                    }

                    //for rule 11, there must be equal number of 42 and 31
                    //also there must be at least one rule 8 (42).. hence >
                    if (prefix.isEmpty() && num42 > num31) {
                        c++;
                    }
                    break;
                }
            }
        }
        return c;
    }
}
