package com.thiyagu_7.adventofcode.year2022.day15;

import com.thiyagu_7.adventofcode.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

// x, y are reversed - x is horizontal and y is vertical
public class SolutionDay15 {
    public int part1(List<String> input, int rowNum) {
        Map<Integer, List<Range>> rangesByRow = new HashMap<>();
        for (String line : input) {
            Pair<Position, Position> positions = parseInputLine(line);
            process(positions.getKey(), positions.getValue(), rangesByRow);
        }
        List<Range> ranges = rangesByRow.get(rowNum);
        int res = 0;
        for (Range range : ranges) {
            res += range.endX - range.startX + 1;
        }
        return res;
    }

    private Pair<Position, Position> parseInputLine(String line) {
        int start, end, x, y;
        start = line.indexOf("x=");
        end = line.indexOf(",");
        x = Integer.parseInt(line.substring(start + 2, end));
        start = line.indexOf("y=");
        end = line.indexOf(":");
        y = Integer.parseInt(line.substring(start + 2, end));
        Position sensorPosition = new Position(x, y);

        start = line.lastIndexOf("x=");
        end = line.lastIndexOf(",");
        x = Integer.parseInt(line.substring(start + 2, end));
        start = line.lastIndexOf("y=");
        end = line.length();
        y = Integer.parseInt(line.substring(start + 2, end));
        Position beaconPosition = new Position(x, y);

        return new Pair<>(sensorPosition, beaconPosition);
    }

    private void process(Position sensor, Position beacon, Map<Integer, List<Range>> rangesByRow) {
        int dist = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
        //bottom of diamond
        for (int i = dist; i >= 0; i--) {
            int y = sensor.y + (dist - i);
            int startX = sensor.x - i;
            int endX = sensor.x + i;

            //Don't include the beacon in the range
            if (startX == beacon.x && y == beacon.y) {
                startX++;
            } else if (endX == beacon.x && y == beacon.y) {
                endX--;
            }
            if (startX > endX) {
                continue;
            }

            Range range = new Range(startX, endX);
            List<Range> ranges = merge(rangesByRow.getOrDefault(y, new ArrayList<>()), range);
            rangesByRow.put(y, ranges);
        }
        //top of diamond
        for (int i = dist - 1; i >= 0; i--) {
            int y = sensor.y - (dist - i);
            int startX = sensor.x - i;
            int endX = sensor.x + i;

            //Don't include the beacon in the range
            if (startX == beacon.x && y == beacon.y) {
                startX++;
            } else if (endX == beacon.x && y == beacon.y) {
                endX--;
            }
            if (startX > endX) {
                continue;
            }

            Range range = new Range(startX, endX);
            List<Range> ranges = merge(rangesByRow.getOrDefault(y, new ArrayList<>()), range);
            rangesByRow.put(y, ranges);
        }
    }

    private List<Range> merge(List<Range> ranges, Range range) {
        if (ranges.isEmpty()) {
            return new ArrayList<>(List.of(range));
        }
        ranges.add(range);
        ranges.sort(Comparator.<Range>comparingInt(r -> r.startX)
                .thenComparingInt(r -> r.endX));

        Stack<Range> stack = new Stack<>();
        stack.add(ranges.get(0));
        for (int i = 1; i < ranges.size(); i++) {
            Range r1 = stack.peek();
            Range r2 = ranges.get(i);
            if (r2.startX > r1.endX) {
                stack.push(r2);
            } else if (r2.endX > r1.endX) {
                stack.pop();
                stack.push(new Range(r1.startX, r2.endX));
            }
        }
        List<Range> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        Collections.reverse(result);
        return result;
    }

    private record Position(int x, int y) {

    }

    private record Range(int startX, int endX) {

    }

    public long part2(List<String> input, int maxCoordinate) {
        Map<Integer, List<Range>> rangesByRow = new HashMap<>();
        List<Position> allBeaconPositions = new ArrayList<>();
        for (String line : input) {
            Pair<Position, Position> positions = parseInputLine(line);
            allBeaconPositions.add(positions.getValue());
            process(positions.getKey(), positions.getValue(), rangesByRow);
        }
        for (int row = 0; row <= maxCoordinate; row++) {
            List<Range> ranges = rangesByRow.get(row);
            for (int i = 1; i < ranges.size(); i++) {
                Range r1 = ranges.get(i - 1);
                Range r2 = ranges.get(i);
                int potentialBeaconX = r1.endX + 1;
                //check if range has one gap
                if (r1.endX + 2 == r2.startX
                        && !allBeaconPositions.contains(new Position(r1.endX + 1, row))
                        && potentialBeaconX >= 0 && potentialBeaconX <= maxCoordinate) {
                    return (potentialBeaconX * 4000000L) + row;
                }
            }
        }
        throw new RuntimeException();
    }
}
