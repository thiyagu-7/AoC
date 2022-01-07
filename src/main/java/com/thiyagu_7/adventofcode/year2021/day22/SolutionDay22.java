package com.thiyagu_7.adventofcode.year2021.day22;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SolutionDay22 {
    // brute force
    public int part1BruteForce(List<String> input) {
        Set<Point> onPoints = new HashSet<>();
        for (String line : input) {
            String points;
            boolean on = false;
            if (line.startsWith("on")) {
                points = line.substring(3);
                on = true;
            } else {
                points = line.substring(4);
            }
            String[] parts = points.split(",");
            int[] xRange = trimToRange(parseRange(parts[0]));
            int[] yRange = trimToRange(parseRange(parts[1]));
            int[] zRange = trimToRange(parseRange(parts[2]));
            if (xRange != null && yRange != null && zRange != null) {
                for (int i = xRange[0]; i <= xRange[1]; i++) {
                    for (int j = yRange[0]; j <= yRange[1]; j++) {
                        for (int k = zRange[0]; k <= zRange[1]; k++) {
                            Point p = new Point(i, j, k);
                            if (on) {
                                onPoints.add(p);
                            } else {
                                onPoints.remove(p);
                            }
                        }
                    }
                }
            }
        }
        return onPoints.size();
    }

    // x=10..12
    private int[] parseRange(String s) {
        String p = s.split("=")[1];
        String[] range = p.split("\\.\\.");
        return new int[]{Integer.parseInt(range[0]), Integer.parseInt(range[1])};
    }

    //-50, 50
    private int[] trimToRange(int[] range) {
        return trimToRange(range, -50, 50);
    }

    private int[] trimToRange(int[] range, int min, int max) {
        int start = range[0];
        int end = range[1];
        if (start > max || end < min) {
            return null;
        }
        return new int[]{
                Math.max(start, min),
                Math.min(end, max)
        };
    }

    public static class Point {
        private final int x;
        private final int y;
        private final int z;

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
