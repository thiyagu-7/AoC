package com.thiyagu_7.adventofcode.year2022.day18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolutionDay18 {
    public int part1(List<String> input) {
        Map<Cube, List<Side>> sidesMap = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split(",");
            Cube current = new Cube(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

            List<Side> currentSides = new ArrayList<>(List.of(Side.values()));
            Cube other;
            //x left
            other = new Cube(current.x - 1, current.y, current.z);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.LEFT);
                sidesMap.get(other).remove(Side.RIGHT);
            }
            //x right
            other = new Cube(current.x + 1, current.y, current.z);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.RIGHT);
                sidesMap.get(other).remove(Side.LEFT);
            }
            //y up
            other = new Cube(current.x, current.y + 1, current.z);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.UP);
                sidesMap.get(other).remove(Side.DOWN);
            }
            //y down
            other = new Cube(current.x, current.y - 1, current.z);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.DOWN);
                sidesMap.get(other).remove(Side.UP);
            }
            //z left
            other = new Cube(current.x, current.y, current.z + 1);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.TOP);
                sidesMap.get(other).remove(Side.BOTTOM);
            }
            //z right
            other = new Cube(current.x, current.y, current.z - 1);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.BOTTOM);
                sidesMap.get(other).remove(Side.TOP);
            }
            sidesMap.put(current, currentSides);
        }
        return sidesMap.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    public int part2(List<String> input) {
        Map<Cube, List<Side>> sidesMap = new HashMap<>();
        int maxX = 0, maxY = 0, maxZ = 0;
        for (String line : input) {
            String[] parts = line.split(",");
            Cube current = new Cube(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            maxX = Math.max(current.x, maxX);
            maxY = Math.max(current.y, maxY);
            maxZ = Math.max(current.z, maxZ);

            List<Side> currentSides = new ArrayList<>(List.of(Side.values()));
            Cube other;
            //x left
            other = new Cube(current.x - 1, current.y, current.z);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.LEFT);
                sidesMap.get(other).remove(Side.RIGHT);
            }
            //x right
            other = new Cube(current.x + 1, current.y, current.z);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.RIGHT);
                sidesMap.get(other).remove(Side.LEFT);
            }
            //y up
            other = new Cube(current.x, current.y + 1, current.z);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.UP);
                sidesMap.get(other).remove(Side.DOWN);
            }
            //y down
            other = new Cube(current.x, current.y - 1, current.z);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.DOWN);
                sidesMap.get(other).remove(Side.UP);
            }
            //z left
            other = new Cube(current.x, current.y, current.z + 1);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.TOP);
                sidesMap.get(other).remove(Side.BOTTOM);
            }
            //z right
            other = new Cube(current.x, current.y, current.z - 1);
            if (sidesMap.containsKey(other)) {
                currentSides.remove(Side.BOTTOM);
                sidesMap.get(other).remove(Side.TOP);
            }
            sidesMap.put(current, currentSides);
        }
        for (Map.Entry<Cube, List<Side>> entry : sidesMap.entrySet()) {
            Cube curr = entry.getKey();
            List<Side> sides = entry.getValue();
            Iterator<Side> itr = sides.iterator();
            while (itr.hasNext()) {
                Side side = itr.next();
                boolean res;
                Set<Cube> visited = new HashSet<>();
                if (side == Side.LEFT) {
                    res = dfs(sidesMap.keySet(), new Cube(curr.x - 1, curr.y, curr.z),
                            maxX, maxY, maxZ, visited);
                } else if (side == Side.RIGHT) {
                    res = dfs(sidesMap.keySet(), new Cube(curr.x + 1, curr.y, curr.z),
                            maxX, maxY, maxZ, visited);
                } else if (side == Side.UP) {
                    res = dfs(sidesMap.keySet(), new Cube(curr.x, curr.y + 1, curr.z),
                            maxX, maxY, maxZ, visited);
                } else if (side == Side.DOWN) {
                    res = dfs(sidesMap.keySet(), new Cube(curr.x, curr.y - 1, curr.z),
                            maxX, maxY, maxZ, visited);
                } else if (side == Side.TOP) {
                    res = dfs(sidesMap.keySet(), new Cube(curr.x, curr.y, curr.z + 1),
                            maxX, maxY, maxZ, visited);
                } else {
                    res = dfs(sidesMap.keySet(), new Cube(curr.x, curr.y, curr.z - 1),
                            maxX, maxY, maxZ, visited);
                }

                if (!res) {
                    // System.out.println("Removing " + side + " for cube " + curr);
                    itr.remove();
                }
            }
        }
        return sidesMap.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    Map<Cube, Boolean> cache = new HashMap<>();

    private boolean dfs(Set<Cube> cubes, Cube curr, int maxX, int maxY, int maxZ,
                        Set<Cube> visited) {
        if (curr.x > maxX || curr.y > maxY || curr.z > maxZ ||
                curr.x < 0 || curr.y < 0 || curr.z < 0) {
            return true;
        }
        if (cubes.contains(curr)) {
            return false;
        }
       /* if (cache.containsKey(curr)) {
            return cache.get(curr);
        }*/
        if (visited.contains(curr)) {
            return false;
        }

        visited.add(curr);
        boolean r = dfs(cubes, new Cube(curr.x + 1, curr.y, curr.z), maxX, maxY, maxZ, visited)
                || dfs(cubes, new Cube(curr.x - 1, curr.y, curr.z), maxX, maxY, maxZ, visited)
                || dfs(cubes, new Cube(curr.x, curr.y + 1, curr.z), maxX, maxY, maxZ, visited)
                || dfs(cubes, new Cube(curr.x, curr.y - 1, curr.z), maxX, maxY, maxZ, visited)
                || dfs(cubes, new Cube(curr.x, curr.y, curr.z + 1), maxX, maxY, maxZ, visited)
                || dfs(cubes, new Cube(curr.x, curr.y, curr.z - 1), maxX, maxY, maxZ, visited);

        //no use in visiting that location in a different route. So, no need to remove from visited
        //visited.remove(curr); //keeps running if we have it

        /*if (r) {
            cache.put(curr, r); //!!!! wrong answer if we cache all results (2493). Works if we cache only true
        }*/
        return r;
    }

    private record Cube(int x, int y, int z) {
    }

    private enum Side {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        TOP,
        BOTTOM;
    }
}
