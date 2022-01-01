package com.thiyagu_7.adventofcode.year2021.day19;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionDay19 {
    public int part1(List<String> input) {
        Result result = process(input);
        return result.allUniqBeacons.size();
    }

    public int part2(List<String> input) {
        Result result = process(input);
        Map<Integer, Point> scannerPositions = result.scannerPositions;
        scannerPositions.put(0, new Point(0, 0, 0));
        int numScanners = scannerPositions.size();
        int max = -1;
        for (int i = 0; i < numScanners - 1; i++) {
            for (int j = i + 1; j < numScanners; j++) {
                Point p1 = scannerPositions.get(i);
                Point p2 = scannerPositions.get(j);
                int dist = Math.abs(p1.x - p2.x)
                        + Math.abs(p1.y - p2.y)
                        + Math.abs(p1.z - p2.z);
                max = Math.max(max, dist);
            }
        }
        return max;
    }

    private Result process(List<String> input) {
        Map<Integer, List<Point>> scannerToPoints = parseInput(input);
        int numScanners = scannerToPoints.size();

        Map<Integer, Point> scannerPositions = new HashMap<>();
        Set<Point> allUniqBeacons = new HashSet<>();

        List<Integer> scannersRemaining = IntStream.range(1, numScanners)
                .boxed().collect(Collectors.toList());
        Queue<Integer> fixedScanners = new LinkedList<>(List.of(0));

        while (!scannersRemaining.isEmpty()) {
            int scannerI = fixedScanners.poll();
            List<Integer> processed = new ArrayList<>();

            for (int k = 0; k < scannersRemaining.size(); k++) {
                int scannerJ = scannersRemaining.get(k);
                List<Point> scanner1Points = Collections.unmodifiableList(scannerToPoints.get(scannerI));
                List<Point> scanner2Points = Collections.unmodifiableList(scannerToPoints.get(scannerJ));

                for (int m = 0; m < 24; m++) {
                    int finalM = m;
                    List<Point> scanner2PointsTransformed = scanner2Points.stream()
                            .map(this::generateOrientations)
                            .map(list -> list.get(finalM))
                            .collect(Collectors.toList());

                    PointMatchResult pointMatchResult = match(scanner1Points, scanner2PointsTransformed);
                    if (pointMatchResult != null && pointMatchResult.matches.size() >= 12) {
                        // shift
                        for (Point p : scanner2PointsTransformed) {
                            p.x += pointMatchResult.x;
                            p.y += pointMatchResult.y;
                            p.z += pointMatchResult.z;
                        }
                        // update input with this orientation and updated points.
                        scannerToPoints.put(scannerJ, scanner2PointsTransformed);

                        allUniqBeacons.addAll(scanner1Points);
                        allUniqBeacons.addAll(scanner2PointsTransformed);

                        // found scannerJ position
                        scannerPositions.put(scannerJ,
                                new Point(pointMatchResult.x, pointMatchResult.y, pointMatchResult.z));

                        processed.add(scannerJ);
                        break;
                    }
                }
            }
            scannersRemaining.removeAll(processed);
            fixedScanners.addAll(processed);
        }
        return new Result(allUniqBeacons, scannerPositions);
    }

    private Map<Integer, List<Point>> parseInput(List<String> input) {
        List<Point> points = new ArrayList<>();
        Map<Integer, List<Point>> scannerToPoints = new HashMap<>();
        int scannerNum = -1;
        for (String line : input) {
            if (line.startsWith("---")) {
                scannerNum++;
                points = new ArrayList<>();
            } else if (line.isEmpty()) {
                scannerToPoints.put(scannerNum, points);
            } else {
                String[] p = line.split(",");
                points.add(new Point(p[0], p[1], p[2]));
            }
        }
        scannerToPoints.put(scannerNum, points);
        return scannerToPoints;
    }

    private List<Point> generateOrientations(Point point) {
        int x = point.x;
        int y = point.y;
        int z = point.z;
        return List.of(
                new Point(x, y, z),
                new Point(x, z, -y),
                new Point(x, -y, -z),
                new Point(x, -z, y),

                new Point(-x, -y, z),
                new Point(-x, z, y),
                new Point(-x, y, -z),
                new Point(-x, -z, -y),

                new Point(y, -x, z),
                new Point(y, -z, -x),
                new Point(y, x, -z),
                new Point(y, z, x),

                new Point(-y, x, z),
                new Point(-y, -z, x),
                new Point(-y, -x, -z),
                new Point(-y, z, -x),

                new Point(z, y, -x),
                new Point(z, -x, -y),
                new Point(z, -y, x),
                new Point(z, x, y),

                new Point(-z, y, x),
                new Point(-z, x, -y),
                new Point(-z, -y, -x),
                new Point(-z, -x, y)
        );
    }


    private static class PointMatchResult {
        private final Set<Point> matches;
        private final int x;
        private final int y;
        private final int z;

        private PointMatchResult(Set<Point> matches, int x, int y, int z) {
            this.matches = matches;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private PointMatchResult match(List<Point> points1, List<Point> points2) {
        for (int i = 0; i < points1.size(); i++) {
            for (int j = 0; j < points2.size(); j++) {
                // if i and j are same point
                // dist b/w two scanners
                Point p1 = points1.get(i);
                Point p2 = points2.get(j);
                int x = p1.x - p2.x;
                int y = p1.y - p2.y;
                int z = p1.z - p2.z;

                Set<Point> matches = match2(points1, points2, x, y, z);
                if (!matches.isEmpty()) {
                    return new PointMatchResult(matches, x, y, z);
                }
            }
        }
        return null;
    }

    private Set<Point> match2(List<Point> points1, List<Point> points2, int x, int y, int z) {
        int match = 0;
        Set<Point> matches = new HashSet<>();
        for (Point p2 : points2) {
            int x1 = x + p2.x;
            int y1 = y + p2.y;
            int z1 = z + p2.z;

            if (points1.contains(new Point(x1, y1, z1))) {
                match++;
                matches.add(p2);
            }
        }
        return match >= 12 ? matches : Set.of();
    }

    private static class Result {
        private final Set<Point> allUniqBeacons;
        private final Map<Integer, Point> scannerPositions;

        private Result(Set<Point> allUniqBeacons, Map<Integer, Point> scannerPositions) {
            this.allUniqBeacons = allUniqBeacons;
            this.scannerPositions = scannerPositions;
        }
    }

    public static class Point {
        private int x;
        private int y;
        private int z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point(String x, String y, String z) {
            this.x = Integer.parseInt(x);
            this.y = Integer.parseInt(y);
            this.z = Integer.parseInt(z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            if (y != point.y) return false;
            return z == point.z;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        @Override
        public String toString() {
            return x + "," + y + "," + z;
        }
    }
}
