package com.thiyagu_7.adventofcode.year2020.day6;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionDay6 {
    //anyone answered yes
    public int part1(List<String> lines) {
        List<String> oneGroupData = new ArrayList<>();
        int result = 0;
        for (String line : lines) {
            if (line.isEmpty()) {
                result += findNumberOfQuestionsAnyoneAnsweredWithYes(oneGroupData);
                oneGroupData = new ArrayList<>();
            } else {
                oneGroupData.add(line);
            }
        }
        result += findNumberOfQuestionsAnyoneAnsweredWithYes(oneGroupData);
        return result;
    }

    private int findNumberOfQuestionsAnyoneAnsweredWithYes(List<String> groupData) {
        Set<Character> uniqueQues = new HashSet<>();
        groupData.forEach(line -> line.chars()
                .mapToObj(c -> (char) c)
                .forEach(uniqueQues::add));
        return uniqueQues.size();
    }

    //everyone answered yes
    public int part2(List<String> lines) {
        List<String> oneGroupData = new ArrayList<>();
        int result = 0;
        for (String line : lines) {
            if (line.isEmpty()) {
                result += findNumberOfQuestionsEveryoneAnsweredWithYes(oneGroupData);
                oneGroupData = new ArrayList<>();
            } else {
                oneGroupData.add(line);
            }
        }
        result += findNumberOfQuestionsEveryoneAnsweredWithYes(oneGroupData);
        return result;
    }

    private int findNumberOfQuestionsEveryoneAnsweredWithYes(List<String> groupData) {
        Set<Character> common = new HashSet<>();
        //add first person answers
        groupData.get(0).chars()
                .mapToObj(c -> (char) c)
                .forEach(common::add);
        for (int i = 1; i < groupData.size(); i++) {
            String onePersonResponse = groupData.get(i);
            common.retainAll(onePersonResponse.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toSet()));
        }
        return common.size();
    }
}
