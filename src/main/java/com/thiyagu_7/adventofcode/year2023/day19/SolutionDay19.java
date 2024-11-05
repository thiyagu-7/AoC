package com.thiyagu_7.adventofcode.year2023.day19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        ParsedInput parsedInput = parseInput(input);
        Result result = new Result();
        dfs(parsedInput.workflows, "in", new ArrayList<>(), result);
        return result.res;
    }

    record Range(int low, int high) {
        static final Range DEFAULT_RANGE = new Range(1, 4000);

    }

    private void dfs(Map<String, List<Rule>> workflows,
                     String currentWorkflow,
                     List<Condition> conditions,
                     Result result) {
        if (currentWorkflow.equals("A")) {
            Map<PartRating, Range> partRatingRangeMap = new HashMap<>(Map.of(
                    PartRating.X, Range.DEFAULT_RANGE,
                    PartRating.M, Range.DEFAULT_RANGE,
                    PartRating.A, Range.DEFAULT_RANGE,
                    PartRating.S, Range.DEFAULT_RANGE
            ));
            //X {A=Range[low=1, high=4000], X=Range[low=1, high=4000], S=Range[low=1, high=1350], M=Range[low=2091, high=4000]}
            // A > 2005
            for (Condition condition : conditions) {
                Range range = partRatingRangeMap.get(condition.partRating);
                Range newRange;
                switch (condition.operator) {
                    case LESS_THAN -> {
                        int newHigh = Math.min(range.high, condition.value - 1);
                        if (range.low > newHigh) {
                            return;
                        }
                        newRange = new Range(range.low, newHigh);
                    }
                    case GREATER_THAN -> {
                        int newLow = Math.max(range.low, condition.value + 1);
                        if (newLow > range.high) {
                            return;
                        }
                        newRange = new Range(newLow, range.high);
                    }
                    default -> throw new RuntimeException();
                }
                partRatingRangeMap.put(condition.partRating, newRange);
            }
            result.res += partRatingRangeMap.values()
                    .stream()
                    .mapToLong(r -> r.high - r.low + 1)
                    .reduce(1L, (a, b) -> a * b);

        } else if (!currentWorkflow.equals("R")) {
            List<Rule> rules = workflows.get(currentWorkflow);
            List<Condition> allConditionsForThisWorkflow = new ArrayList<>();

            for (Rule rule : rules) {
                String condition = rule.condition;
                if (rule.condition == null) {
                    //get inverse of all rules' conditions for this workflow
                    /*
                    a>5
                    a<6
                    --
                    a<6
                    a>5
                     */
                    List<Condition> allConditionsForThisWorkflowFlipped = allConditionsForThisWorkflow.stream()
                            .map(c -> new Condition(
                                    c.partRating,
                                    c.operator == Operator.LESS_THAN ? Operator.GREATER_THAN : Operator.LESS_THAN, //flip condition
                                    c.operator == Operator.LESS_THAN ? c.value - 1: c.value + 1
                            ))
                            .toList();

                    List<Condition> conditionsCopy = new ArrayList<>(conditions);
                    conditionsCopy.addAll(allConditionsForThisWorkflowFlipped);

                    dfs(workflows, rule.sendTo, conditionsCopy, result);
                    return;
                }
                PartRating partRating = PartRating.getPartRating(condition.charAt(0));
                Operator operator = condition.charAt(1) == '<' ? Operator.LESS_THAN : Operator.GREATER_THAN;
                int num = Integer.parseInt(condition.substring(2));

                List<Condition> conditionsCopy = new ArrayList<>(conditions);
                Condition ruleCondition = new Condition(partRating, operator, num);
                conditionsCopy.add(ruleCondition);

                List<Condition> allConditionsForThisWorkflowFlipped = allConditionsForThisWorkflow.stream()
                        .map(c -> new Condition(
                                c.partRating,
                                c.operator == Operator.LESS_THAN ? Operator.GREATER_THAN : Operator.LESS_THAN, //flip condition
                                c.operator == Operator.LESS_THAN ? c.value - 1: c.value + 1
                        ))
                        .toList();
                conditionsCopy.addAll(allConditionsForThisWorkflowFlipped);


                allConditionsForThisWorkflow.add(ruleCondition);

                dfs(workflows, rule.sendTo, conditionsCopy, result);
            }
        }
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

    enum PartRating {
        X('x'), M('m'), A('a'), S('s');
        private final char partRating;
        private static final Map<Character, PartRating> PART_RATING_MAP;

        static {
            PART_RATING_MAP = Arrays.stream(PartRating.values())
                    .collect(Collectors.toMap(e -> e.partRating, Function.identity()));
        }

        PartRating(char partRating) {
            this.partRating = partRating;
        }

        public static PartRating getPartRating(char partRating) {
            return PART_RATING_MAP.get(partRating);
        }
    }

    enum Operator {
        LESS_THAN,
        GREATER_THAN;

    }

    record Condition(PartRating partRating, Operator operator, int value) {

    }

    record ParsedInput(Map<String, List<Rule>> workflows, List<Part> parts) {

    }

    record Part(int x, int m, int a, int s) {
        int sumOfRatings() {
            return x + m + a + s;
        }
    }

    private static class Result {
        long res = 0;
    }

}
