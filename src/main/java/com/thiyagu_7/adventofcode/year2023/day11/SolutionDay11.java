package com.thiyagu_7.adventofcode.year2023.day11;

import java.util.ArrayList;
import java.util.List;

public class SolutionDay11 {
    public int part1(List<String> universe) {
        List<Integer> noGalaxiesRows = findRowsWithNoGalaxies(universe);
        List<Integer> noGalaxiesColumns = findColumnsWithNoGalaxies(universe);
        List<String> expandedUniverse = new ArrayList<>();

        //expand the universe
        for (int i = 0; i < universe.size(); i++) {
            StringBuilder currRow = new StringBuilder();
            for (int j = 0; j < universe.get(0).length(); j++) {
                char c = universe.get(i)
                        .charAt(j);
                currRow.append(c);
                if (noGalaxiesColumns.contains(j)) {
                    // duplicate
                    currRow.append(c);
                }
            }
            expandedUniverse.add(currRow.toString());
            if (noGalaxiesRows.contains(i)) {
                //duplicate
                expandedUniverse.add(currRow.toString());
            }
        }

        List<Position> galaxiesPosition = new ArrayList<>();
        //find all galaxies position
        for (int i = 0; i < expandedUniverse.size(); i++) {
            for (int j = 0; j < expandedUniverse.get(0).length(); j++) {
                if (expandedUniverse.get(i).charAt(j) == '#') {
                    galaxiesPosition.add(new Position(i, j));
                }
            }
        }
        //find the shortest path between every pair of galaxies
        int sum = 0;
        for (int i = 0; i < galaxiesPosition.size(); i++) {
            for (int j = i + 1; j < galaxiesPosition.size(); j++) {
                Position p1 = galaxiesPosition.get(i);
                Position p2 = galaxiesPosition.get(j);
                int dist = Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
                sum += dist;
            }
        }
        return sum;
    }

    private List<Integer> findRowsWithNoGalaxies(List<String> universe) {
        List<Integer> rows = new ArrayList<>();
        for (int i = 0; i < universe.size(); i++) {
            String row = universe.get(i);
            boolean galaxyFound = false;
            for (char c : row.toCharArray()) {
                if (c == '#') {
                    galaxyFound = true;
                    break;
                }
            }
            if (!galaxyFound) {
                rows.add(i);
            }
        }
        return rows;
    }

    private List<Integer> findColumnsWithNoGalaxies(List<String> universe) {
        List<Integer> cols = new ArrayList<>();
        for (int j = 0; j < universe.get(0).length(); j++) {
            boolean galaxyFound = false;
            for (int i = 0; i < universe.size(); i++) {
                if (universe.get(i).charAt(j) == '#') {
                    galaxyFound = true;
                    break;
                }
            }
            if (!galaxyFound) {
                cols.add(j);
            }
        }
        return cols;
    }

    public long part2(List<String> universe, int noOfCopies) {
        noOfCopies = noOfCopies - 1;
        List<Integer> noGalaxiesRows = findRowsWithNoGalaxies(universe);
        List<Integer> noGalaxiesColumns = findColumnsWithNoGalaxies(universe);

        List<Position> galaxiesPosition = new ArrayList<>();
        //find all galaxies position
        for (int i = 0; i < universe.size(); i++) {
            for (int j = 0; j < universe.get(0).length(); j++) {
                if (universe.get(i).charAt(j) == '#') {
                    galaxiesPosition.add(new Position(i, j));
                }
            }
        }
        //find the shortest path between every pair of galaxies
        long sum = 0;
        for (int i = 0; i < galaxiesPosition.size(); i++) {
            for (int j = i + 1; j < galaxiesPosition.size(); j++) {
                Position p1 = galaxiesPosition.get(i);
                Position p2 = galaxiesPosition.get(j);
                int dist = Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);

                //consider the empty rows that are in between p1 and p2
                for (int r : noGalaxiesRows) {
                    if (r > p1.x && r < p2.x) {
                        dist += noOfCopies;
                    }
                }
                //p1 should be left of p2
                if (p1.y > p2.y) {
                    Position t = p1;
                    p1 = p2;
                    p2 = t;
                }
                //consider the empty cols that are in between p1 and p2
                for (int c : noGalaxiesColumns) {
                    if (c > p1.y && c < p2.y) {
                        dist += noOfCopies;
                    }
                }
                sum += dist;
            }
        }
        return sum;
    }

    record Position(int x, int y) {

    }
}
