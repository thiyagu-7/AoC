package com.thiyagu_7.adventofcode.year2022.day13;

import java.util.ArrayList;
import java.util.List;

public class SolutionDay13 {
    public int part1(List<String> input) {
        int result = 0;
        int idx = 1;
        for (int i = 0; i < input.size(); i += 3) {
            String packet1 = input.get(i);
            String packet2 = input.get(i + 1);

            List<Object> r1 = new ArrayList<>();
            List<Object> r2 = new ArrayList<>();

            buildList(packet1, 0, r1);
            buildList(packet2, 0, r2);

            int res = compare(r1, r2);
            if (res == -1) {
                result += idx;
            }
            idx++;
        }
        return result;
    }

    private int buildList(String packet, int i, List<Object> parsedPacket) {
        while (i < packet.length()) {
            if (packet.charAt(i) == ',') {
                i++;
                continue;
            }
            if (packet.charAt(i) == '[') {
                parsedPacket.add(new ArrayList<>());
                int idx = parsedPacket.size() - 1;
                i = buildList(packet, i + 1, (List<Object>) parsedPacket.get(idx));
                //i++
            } else if (packet.charAt(i) != ']') {
                int nextComma = packet.indexOf(',', i) == -1 ? Integer.MAX_VALUE : packet.indexOf(',', i);
                int nextClosingBrace = packet.indexOf(']', i);
                int end = Math.min(nextComma, nextClosingBrace);
                int value = Integer.parseInt(packet.substring(i, end));

                parsedPacket.add(value);

                if (packet.charAt(end) == ']') {
                    //return end + 2;
                    return end;
                }
                i = end + 1;
            } else {  // ']'
                return i + 1;
            }
        }
        return i; //?
    }

    private static int compare(Object left, Object right) {
        if (left instanceof Integer && right instanceof Integer) {
            int leftNum = (int) left;
            int rightNum = (int) right;
            return Integer.compare(leftNum, rightNum);
        } else if (left instanceof List<?> && right instanceof List<?>) {
            List<Object> leftList = (List<Object>) left;
            List<Object> rightList = (List<Object>) right;
            int ii = 0;
            int jj = 0;
            for (; ii < leftList.size() && jj < rightList.size(); ii++, jj++) {
                Object o1 = leftList.get(ii);
                Object o2 = rightList.get(jj);
                int res;
                if (o1 instanceof Integer && o2 instanceof List<?>) {
                    res = compare(List.of(o1), o2);
                } else if (o1 instanceof List<?> && o2 instanceof Integer) {
                    res = compare(o1, List.of(o2));
                } else {
                    res = compare(o1, o2);
                }
                if (res != 0) {
                    return res;
                }
            }
            if (ii < leftList.size()) {
                return 1;
            } else if (jj < rightList.size()) {
                return -1;
            } else {
                return 0;
            }
        } else {
            throw new RuntimeException();
        }
    }

    public int part2(List<String> input) {
        List<Object> all = new ArrayList<>();
        for (int i = 0; i < input.size(); i += 3) {
            String packet1 = input.get(i);
            String packet2 = input.get(i + 1);

            List<Object> r1 = new ArrayList<>();
            List<Object> r2 = new ArrayList<>();

            buildList(packet1, 0, r1);
            buildList(packet2, 0, r2);

            all.add(r1);
            all.add(r2);
        }
        List<Object> dividerPacket1 = new ArrayList<>();
        List<Object> dividerPacket2 = new ArrayList<>();
        buildList("[[2]]", 0, dividerPacket1);
        buildList("[[6]]", 0, dividerPacket2);

        all.add(dividerPacket1);
        all.add(dividerPacket2);

        all.sort((l, r) -> compare(l, r));

        /*all.stream()
                .forEach(e -> System.out.println(e));*/

        return (all.indexOf(dividerPacket1) + 1)
                * (all.indexOf(dividerPacket2) + 1);
    }
}
