package com.thiyagu_7.adventofcode.year2021.day22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Idea from https://github.com/eddmann/advent-of-code/blob/master/2021/python/src/day22/solution-1.py
public class SolutionDay22Part2 {
    public long part2(List<String> input) {
        // true - on; false - off
        Map<Boolean, List<Cuboid>> map = new HashMap<>();
        map.put(true, new ArrayList<>());
        map.put(false, new ArrayList<>());

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
            int[] xRange = parseRange(parts[0]);
            int[] yRange = parseRange(parts[1]);
            int[] zRange = parseRange(parts[2]);

            Cuboid c = new Cuboid(xRange[0], xRange[1], yRange[0], yRange[1],
                    zRange[0], zRange[1]);

            /*
             cuboid (c)|current instruction(s) -> result
             on|on -> off
             on|off -> off
             off|on -> on
             off|off -> on (tricky one - this is to generate a compensating instruction)
             */
            List<Cuboid> cuboids = map.get(true);
            List<Cuboid> newC1 = new ArrayList<>();
            for (int i = 0; i < cuboids.size(); i++) {
                Cuboid s = cuboids.get(i);
                Cuboid overlap = overlapRegion(c, s);
                if (overlap != null) {
                    newC1.add(overlap);
                }
            }
            cuboids = map.get(false);
            List<Cuboid> newC2 = new ArrayList<>();
            for (int i = 0; i < cuboids.size(); i++) {
                Cuboid s = cuboids.get(i);
                Cuboid overlap = overlapRegion(c, s);
                if (overlap != null) {
                    newC2.add(overlap);
                }
            }
            map.get(false).addAll(newC1);
            map.get(true).addAll(newC2);

            // don't add if curr is off
            if (on) {
                map.get(true).add(c);
            }
        }

        return map.get(true).stream()
                .mapToLong(Cuboid::getVolume)
                .sum() - map.get(false).stream()
                .mapToLong(Cuboid::getVolume)
                .sum();
    }

    private int[] parseRange(String s) {
        String p = s.split("=")[1];
        String[] range = p.split("\\.\\.");
        return new int[]{Integer.parseInt(range[0]), Integer.parseInt(range[1])};
    }

    private static class Cuboid {
        private final int x1;
        private final int x2;
        private final int y1;
        private final int y2;
        private final int z1;
        private final int z2;

        private Cuboid(int x1, int x2, int y1, int y2, int z1, int z2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.z1 = z1;
            this.z2 = z2;
        }

        long getVolume() {
            return (long) (x2 - x1 + 1) * (y2 - y1 + 1) * (z2 - z1 + 1);
        }
    }

    private Cuboid overlapRegion(Cuboid c1, Cuboid c2) {
        int x1 = Math.max(c1.x1, c2.x1);
        int x2 = Math.min(c1.x2, c2.x2);
        int y1 = Math.max(c1.y1, c2.y1);
        int y2 = Math.min(c1.y2, c2.y2);
        int z1 = Math.max(c1.z1, c2.z1);
        int z2 = Math.min(c1.z2, c2.z2);

        if (x1 > x2 || y1 > y2 || z1 > z2) {
            return null;
        }
        return new Cuboid(x1, x2, y1, y2, z1, z2);
    }
}
