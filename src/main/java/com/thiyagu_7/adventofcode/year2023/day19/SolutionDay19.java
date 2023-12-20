package com.thiyagu_7.adventofcode.year2023.day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolutionDay19 {
    public int part1(List<String> input) {
        ParsedInput parsedInput = parseInput(input);
        return parsedInput.parts.stream()
                .filter(part -> isAccepted(parsedInput.workflows, part))
                .mapToInt(Part::sumOfRatings)
                .sum();
    }

    private ParsedInput parseInput(List<String> input) {
        Map<String, List<Rule>> workflows = new HashMap<>();
        List<Part> parts = new ArrayList<>();
        int i;
        //parse workflow info
        for (i = 0; i < input.size(); i++) {
            String line = input.get(i);
            String workflowName;
            List<Rule> rules = new ArrayList<>();

            if (!line.isEmpty()) {
                workflowName = line.substring(0, line.indexOf("{"));
                String rulesString = line.substring(line.indexOf("{") + 1, line.length() - 1);
                String[] rulesArray = rulesString.split(",");

                int j;
                for (j = 0; j < rulesArray.length - 1; j++) {
                    String[] ruleParts = rulesArray[j].split(":");
                    rules.add(new Rule(ruleParts[0], ruleParts[1]));
                }
                rules.add(new Rule(null, rulesArray[j])); //last rule has no condition
            } else {
                break;
            }
            workflows.put(workflowName, rules);
        }
        // parse parts info
        Pattern pattern = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}");

        for (; i < input.size(); i++) {
            String line = input.get(i);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                parts.add(new Part(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4))
                ));
            }
        }
        return new ParsedInput(workflows, parts);
    }

    private boolean isAccepted(Map<String, List<Rule>> workflows, Part part) {
        String currentWorkflow = "in";
        while (true) {
            List<Rule> rules = workflows.get(currentWorkflow);
            for (Rule rule : rules) {
                if (rule.match(part)) {
                    String sendTo = rule.sendTo;
                    if (sendTo.equals("A")) {
                        return true;
                    } else if (sendTo.equals("R")) {
                        return false;
                    } else {
                        currentWorkflow = sendTo;
                        break;
                    }
                }
            }
        }
    }

    public long part2(List<String> input) {
        return 1;
    }

    record ParsedInput(Map<String, List<Rule>> workflows, List<Part> parts) {

    }

    record Rule(String condition, String sendTo) {
        boolean match(Part part) {
            if (condition == null) { //last rule in the workflow
                return true;
            }
            char category = condition.charAt(0);
            char operator = condition.charAt(1);
            boolean lessThan = operator == '<';
            int num = Integer.parseInt(condition.substring(2));
            return switch (category) {
                case 'x' -> lessThan ? part.x < num : part.x > num;
                case 'm' -> lessThan ? part.m < num : part.m > num;
                case 'a' -> lessThan ? part.a < num : part.a > num;
                case 's' -> lessThan ? part.s < num : part.s > num;
                default -> throw new RuntimeException();
            };
        }
    }

    record Part(int x, int m, int a, int s) {
        int sumOfRatings() {
            return x + m + a + s;
        }
    }

}
