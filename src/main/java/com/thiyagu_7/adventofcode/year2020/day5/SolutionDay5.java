package com.thiyagu_7.adventofcode.year2020.day5;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionDay5 {
    public int part1(List<String> seats) { //list of boarding passes
        return seats.stream()
                .map(this::findSeatId)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }

    private int findSeatId(String seat) {
        int row = binarySearch(seat.substring(0, 7), 'F', 127);
        int col = binarySearch(seat.substring(7), 'L', 7);
        return row * 8 + col;
    }

    private int binarySearch(String sequence, char lowChar, int high) {
        int low = 0;

        for (char c: sequence.toCharArray()) {
            int mid = (low + high) / 2;
            if (c == lowChar) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return sequence.charAt(sequence.length() - 1) == 'F' ? low : high;
    }

    public int part2(List<String> seats) {
        Map<Integer, List<Integer>> allSeats = seats.stream()
                .map(seat -> {
                    int row = binarySearch(seat.substring(0, 7), 'F', 127);
                    int col = binarySearch(seat.substring(7), 'L', 7);
                    return new int[] {row, col};
                })
                .collect(Collectors.groupingBy(rowCol -> rowCol[0],
                        Collectors.mapping(rowCol -> rowCol[1], Collectors.toList())));

        TreeSet<Integer> rowNums = new TreeSet<>(allSeats.keySet());
        int firstRow = rowNums.first(); //10
        int lastRow = rowNums.last(); //106

        //all rows with missing cols
        /*
        10=[3, 1, 6, 4, 5, 2, 7]
        69=[2, 4, 5, 3, 6, 7, 1]

         allSeats.entrySet().stream()
             .filter(e -> e.getValue().size() == 7)
             .forEach(System.out::println);
         */

        //all rows with missing cols but not at beginning or end
        Map<Integer, List<Integer>> seatsWithMissingColumns = allSeats.entrySet().stream()
                .filter(e -> e.getValue().size() == 7) //one col missing
                .filter(e -> e.getKey() != firstRow && e.getKey() != lastRow)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //System.out.println(seatsWithMissingColumns.size());//1

        int row = seatsWithMissingColumns.keySet().iterator().next();
        List<Integer> cols = seatsWithMissingColumns.values().iterator().next(); //get only element

        return IntStream.range(0, 7)
                .filter(i -> !cols.contains(i))
                .map(i -> row * 8 + i)
                .findFirst()
                .getAsInt();
    }
}
