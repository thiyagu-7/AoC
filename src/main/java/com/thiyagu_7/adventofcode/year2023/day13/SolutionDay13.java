package com.thiyagu_7.adventofcode.year2023.day13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SolutionDay13 {
    public int part1(List<String> input) {
        List<List<String>> grids = parseInput(input);
        int cols = 0;
        int rows = 0;
        for (List<String> grid : grids) {
            List<String> invertedGrid = IntStream.range(0, grid.get(0).length())
                    .mapToObj(idx -> getColumn(grid, idx))
                    .toList();

            int colIndex = findReflection(invertedGrid);
            if (colIndex != -1) {
                cols += colIndex + 1; //1-based index
            } else {
                int rowIndex = findReflection(grid);
                rows += rowIndex + 1; //1-based index
            }
        }

        return cols + (100 * rows);
    }

    private List<List<String>> parseInput(List<String> input) {
        List<List<String>> grids = new ArrayList<>();
        List<String> currentGrid = new ArrayList<>();

        for (String line : input) {
            if (line.isEmpty()) {
                grids.add(currentGrid);
                currentGrid = new ArrayList<>();
            } else {
                currentGrid.add(line);
            }
        }
        grids.add(currentGrid);
        return grids;
    }

    private String getColumn(List<String> input, int j) {
        StringBuilder col = new StringBuilder();
        for (String row : input) {
            col.append(row.charAt(j));
        }
        return col.toString();
    }

    private int findReflection(List<String> grid) {
        int numMatch = 0;
        int idx = -1;
        for (int row = 0; row < grid.size() - 1; row++) {
            int i = row;
            int j = i + 1;
            boolean match = true;
            int c = 0;
            while (i >= 0 && j < grid.size()) {
                if (!grid.get(i).equals(grid.get(j))) {
                    match = false;
                    break;
                }
                c++;
                i--;
                j++;
            }

            if (match && c > numMatch) {
                numMatch = c;
                idx = row;
                //'return row' will also work for part 1
                // for part 1 there aren't multiple matches
            }
        }
        return idx;
    }

    private int findReflectionPart2(List<String> grid, int prevMatchIndex) {
        int numMatch = 0;
        int idx = -1;
        for (int row = 0; row < grid.size() - 1; row++) {
            int i = row;
            int j = i + 1;
            boolean match = true;
            int c = 0;
            while (i >= 0 && j < grid.size()) {
                if (!grid.get(i).equals(grid.get(j))) {
                    match = false;
                    break;
                }
                c++;
                i--;
                j++;
            }

            if (match && c > numMatch && prevMatchIndex != row) {
                numMatch = c;
                idx = row;
            }
        }
        return idx;
    }

    public int part2(List<String> input) {
        List<List<String>> grids = parseInput(input);
        Map<Integer, Reflection> map = new HashMap<>();

        //find reflection details as per original (same as part 1)
        for (int k = 0; k < grids.size(); k++) {
            List<String> grid = grids.get(k);
            List<String> invertedGrid = IntStream.range(0, grid.get(0).length())
                    .mapToObj(idx -> getColumn(grid, idx))
                    .toList();

            int colIndex = findReflection(invertedGrid);
            if (colIndex != -1) {
                map.put(k, new Reflection(ReflectionAxis.COL, colIndex));
            } else {
                int rowIndex = findReflection(grid);
                map.put(k, new Reflection(ReflectionAxis.ROW, rowIndex));
            }
        }

        int cols = 0;
        int rows = 0;
        //apply smudge for each cell
        for (int k = 0; k < grids.size(); k++) {
            List<String> grid = grids.get(k);
            List<String> invertedGrid = IntStream.range(0, grid.get(0).length())
                    .mapToObj(idx -> getColumn(grid, idx))
                    .toList();
            List<String> updatedGrid;
            boolean newReflectionFound = false;

            // check cols (vertical line reflection)
            for (int i = 0; i < invertedGrid.size(); i++) {
                for (int j = 0; j < invertedGrid.get(0).length(); j++) {
                    updatedGrid = flipCell(invertedGrid, i, j);
                    int colIndex = findReflectionPart2(updatedGrid,
                            map.get(k).reflectionAxis == ReflectionAxis.COL ? map.get(k).index : -1);
                    if (colIndex != -1) {
                        Reflection currentReflection = new Reflection(ReflectionAxis.COL, colIndex);
                        //if we get a different reflection
                        if (!currentReflection.equals(map.get(k))) {
                            cols += colIndex + 1; //1-based index
                            newReflectionFound = true;
                            break;
                        }
                    }
                }
                if (newReflectionFound) {
                    break;
                }
            }
            // check rows (horizontal line reflection)
            if (!newReflectionFound) {
                for (int i = 0; i < grid.size(); i++) {
                    for (int j = 0; j < grid.get(0).length(); j++) {
                        updatedGrid = flipCell(grid, i, j);
                        int rowIndex = findReflectionPart2(updatedGrid,
                                map.get(k).reflectionAxis == ReflectionAxis.ROW ? map.get(k).index : -1);
                        if (rowIndex != -1) {
                            Reflection currentReflection = new Reflection(ReflectionAxis.ROW, rowIndex);
                            //if we get a different reflection
                            if (!currentReflection.equals(map.get(k))) {
                                rows += rowIndex + 1; //1-based index
                                newReflectionFound = true;
                                break;
                            }
                        }
                    }
                    if (newReflectionFound) {
                        break;
                    }
                }
            }
            if (!newReflectionFound) {
                Reflection reflection = map.get(k);
                if (reflection.reflectionAxis == ReflectionAxis.COL) {
                    cols += reflection.index + 1;
                } else {
                    rows += reflection.index + 1;
                }
            }
        }
        return cols + (100 * rows);
    }

    private List<String> flipCell(List<String> grid, int x, int y) {
        char[][] arr = new char[grid.size()][grid.get(0).length()];
        for (int i = 0; i < grid.size(); i++) {
            arr[i] = grid.get(i).toCharArray();
        }
        arr[x][y] = arr[x][y] == '.' ? '#' : '.';

        List<String> updatedGrid = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < arr[0].length; j++) {
                row.append(arr[i][j]);
            }
            updatedGrid.add(row.toString());
        }
        return updatedGrid;
    }

    record Reflection(ReflectionAxis reflectionAxis, int index) {

    }

    enum ReflectionAxis {
        ROW,
        COL,
    }
}
