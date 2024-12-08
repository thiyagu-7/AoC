package com.thiyagu_7.adventofcode.year2024.day8;

import com.thiyagu_7.adventofcode.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolutionDay8 {
    public long part1(List<String> input) {
        Map<Character, List<Location>> antennaLocations = parseAntennaLocation(input);
        int m = input.size();
        int n = input.get(0).length();
        Set<Location> antinodeLocations = new HashSet<>();

        for (Map.Entry<Character, List<Location>> entry : antennaLocations.entrySet()) {
            List<Location> locations = entry.getValue();
            //for every pair of antenna
            for (int i = 0; i < locations.size(); i++) {
                for (int j = i + 1; j < locations.size(); j++) {
                    Pair<Location, Location> antinodesForAntenna =
                            findAntiNodeLocation(locations.get(i), locations.get(j));

                    if (isLocationInsideGrid(antinodesForAntenna.getKey(), m, n)) {
                        antinodeLocations.add(antinodesForAntenna.getKey());
                    }
                    if (isLocationInsideGrid(antinodesForAntenna.getValue(), m, n)) {
                        antinodeLocations.add(antinodesForAntenna.getValue());
                    }
                }
            }
        }
        return antinodeLocations.size();
    }

    private Map<Character, List<Location>> parseAntennaLocation(List<String> input) {
        Map<Character, List<Location>> antennaLocations = new HashMap<>();

        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) != '.') {
                    antennaLocations.computeIfAbsent(line.charAt(j), ignoreMe -> new ArrayList<>())
                            .add(new Location(i, j));
                }
            }
        }
        return antennaLocations;

    }

    //find 2 anti-node location for a given pair of antennas
    private Pair<Location, Location> findAntiNodeLocation(Location firstAntenna, Location secondAntenna) {
        int xDiff = Math.abs(firstAntenna.x - secondAntenna.x);
        int yDiff = Math.abs(firstAntenna.y - secondAntenna.y);
        Location firstAntinode;
        Location secondAntinode;
        if (firstAntenna.y < secondAntenna.y) {
            firstAntinode = new Location(firstAntenna.x - xDiff, firstAntenna.y - yDiff);
            secondAntinode = new Location(secondAntenna.x + xDiff, secondAntenna.y + yDiff);
        } else {
            firstAntinode = new Location(firstAntenna.x - xDiff, firstAntenna.y + yDiff);
            secondAntinode = new Location(secondAntenna.x + xDiff, secondAntenna.y - yDiff);
        }
        return new Pair<>(firstAntinode, secondAntinode);
    }

    private boolean isLocationInsideGrid(Location location, int m, int n) {
        return location.x >= 0 && location.x < m && location.y >= 0 && location.y < n;
    }

    record Location(int x, int y) {

    }

    private Set<Location> findAllAntiNodeLocations(Location firstAntenna, Location secondAntenna,
                                                   int m, int n) {
        int xDiff = Math.abs(firstAntenna.x - secondAntenna.x);
        int yDiff = Math.abs(firstAntenna.y - secondAntenna.y);
        Location first = firstAntenna;
        Location second = secondAntenna;

        Set<Location> antinodeLocations = new HashSet<>();
        // antenna location is also an anti-node location
        antinodeLocations.add(first);
        antinodeLocations.add(second);

        boolean isFirstLocationInsideGrid = true;
        boolean isSecondLocationInsideGrid = true;
        do {
            if (firstAntenna.y < secondAntenna.y) {
                first = isFirstLocationInsideGrid ? new Location(first.x - xDiff, first.y - yDiff) : first;
                second = isSecondLocationInsideGrid ? new Location(second.x + xDiff, second.y + yDiff) : second;
            } else {
                first = isFirstLocationInsideGrid ? new Location(first.x - xDiff, first.y + yDiff) : first;
                second = isSecondLocationInsideGrid ? new Location(second.x + xDiff, second.y - yDiff) : second;
            }
            if (isLocationInsideGrid(first, m, n)) {
                antinodeLocations.add(first);
            } else {
                isFirstLocationInsideGrid = false;
            }
            if (isLocationInsideGrid(second, m, n)) {
                antinodeLocations.add(second);
            } else {
                isSecondLocationInsideGrid = false;
            }
        } while (isFirstLocationInsideGrid || isSecondLocationInsideGrid);

        return antinodeLocations;
    }

    public long part2(List<String> input) {
        Map<Character, List<Location>> antennaLocations = parseAntennaLocation(input);
        int m = input.size();
        int n = input.get(0).length();
        Set<Location> antinodeLocations = new HashSet<>();

        for (Map.Entry<Character, List<Location>> entry : antennaLocations.entrySet()) {
            List<Location> locations = entry.getValue();
            //for every pair of antenna
            for (int i = 0; i < locations.size(); i++) {
                for (int j = i + 1; j < locations.size(); j++) {
                    antinodeLocations.addAll(findAllAntiNodeLocations(locations.get(i), locations.get(j), m, n));
                }
            }
        }
        return antinodeLocations.size();
    }
}
