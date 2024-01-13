package com.thiyagu_7.adventofcode.year2023.day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//uses memoization
public class SolutionDay12Part2 {

    public long part2(List<String> input) {
        List<SpringDetail> springDetails = input.stream()
                .map(InputParser::parseLine)
                .map(this::unfold)
                .toList();
        long arrangements = 0;
        for (SpringDetail springDetail : springDetails) {
            arrangements += processPart2(springDetail.spring(), springDetail.damagedSpringsGroupCount(),
                    0, new HashMap<>());
        }
        return arrangements;
    }

    // does prefix check - if groups mismatch for prefix, return early
    private long processPart2(String spring, List<Integer> damagedSpringsGroupCount, int i,
                              Map<String, Long> cache) {
        int damaged = 0;
        List<Integer> groups = new ArrayList<>();

        for (int j = 0; j < i; j++) {
            if (spring.charAt(j) == '#') {
                damaged++;
            } else if (spring.charAt(j) == '.' && damaged > 0) {
                groups.add(damaged);
                damaged = 0;
            }
        }
        if (i == spring.length() && damaged > 0) {
            groups.add(damaged);
        }

        //check for invalid groups and return
        if (groups.size() > damagedSpringsGroupCount.size()) {
            return 0;
        }
        for (int k = 0; k < groups.size(); k++) {
            if (!groups.get(k).equals(damagedSpringsGroupCount.get(k))) {
                return 0;
            }
        }


        //base case
        if (i == spring.length()) {
            if (groups.size() == damagedSpringsGroupCount.size()) {
                return 1;
            }
            return 0;
        }

        int numGroupsSoFar = groups.size();
        String key = i + "|" + numGroupsSoFar;
        if (i - 1 >= 0 && spring.charAt(i - 1) == '.' && cache.containsKey(key)) {
            return cache.get(key);
        }
        long arrangements;
        if (spring.charAt(i) == '?') {
            char[] arr = spring.toCharArray();
            arr[i] = '.';
            arrangements = processPart2(new String(arr), damagedSpringsGroupCount, i + 1, cache);
            arr[i] = '#';
            arrangements += processPart2(new String(arr), damagedSpringsGroupCount, i + 1, cache);
        } else {
            arrangements = processPart2(spring, damagedSpringsGroupCount, i + 1, cache);
        }

        if (i - 1 >= 0 && spring.charAt(i - 1) == '.') {
            cache.put(key, arrangements);
        }
        return arrangements;
    }

    private SpringDetail unfold(SpringDetail springDetail) {
        int times = 5;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            builder.append(springDetail.spring());
            if (i != times - 1) {
                builder.append("?");
            }
        }
        List<Integer> d = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            d.addAll(springDetail.damagedSpringsGroupCount());
        }
        return new SpringDetail(builder.toString(), d);
    }
}
