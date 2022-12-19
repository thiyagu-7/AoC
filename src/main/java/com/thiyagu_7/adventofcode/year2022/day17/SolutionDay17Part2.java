package com.thiyagu_7.adventofcode.year2022.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class SolutionDay17Part2 {
    private static final char EMPTY = '.';
    private static final int FLOOR_ROW = 50000; //meh..

    public long part2(String jetPattern) {
        return part2(jetPattern, 2022);
    }

    public long part2(String jetPattern, long n) {
        char[][] chamber = new char[FLOOR_ROW][7];
        for (int i = 0; i < FLOOR_ROW; i++) {
            Arrays.fill(chamber[i], '.');
        }
        RockSupplier rockSupplier = new RockSupplier();
        JetPatternSupplier jetPatternSupplier = new JetPatternSupplier(jetPattern);

        int highestRock = FLOOR_ROW;
        LinkedHashMap<Pair, Integer> resultCache = new LinkedHashMap<>();
        Set<Pair> duplicatePairs = new LinkedHashSet<>();

        Set<Long> dups = new LinkedHashSet<>();
        //Simulate 2022 rock fall
        for (long i = 1; i <= n; i++) {
            Rock rock = rockSupplier.get();
            Pair pair = new Pair(rock.getClass(), jetPatternSupplier.getIndex(), i);
            if (resultCache.containsKey(pair)) {
                duplicatePairs.add(pair);
                dups.add(i);

                int numDupsInBetween = 0;
                if (duplicatePairs.size() == 5) {
                    Pair firstPair = duplicatePairs.iterator().next();

                    //find first occurrence of it and extract the remaining entries
                    LinkedHashMap<Pair, Integer> initialResults = new LinkedHashMap<>();
                    LinkedHashMap<Pair, Integer> loopingResults = new LinkedHashMap<>();

                    boolean found = false;

                    for (Map.Entry<Pair, Integer> entry : resultCache.entrySet()) {
                        if (entry.getKey().equals(firstPair)) {
                            found = true;
                        }

                        if (found) {
                            if (loopingResults.size() > 1 && entry.getKey().equals(firstPair)) {
                                //to ignore last 5 elements
                                break;
                            }
                            if (dups.contains(entry.getKey().getRockNum() - 1)) {
                                numDupsInBetween++;
                            }
                            loopingResults.put(entry.getKey(), entry.getValue());
                        } else {
                            initialResults.put(entry.getKey(), entry.getValue());
                        }
                    }

                    int loopingResultsSize = loopingResults.size() + numDupsInBetween; //have to add dups found in between as well
                    List<Integer> loopingResultsAllValues = new ArrayList<>(loopingResults.values());
                    List<Integer> initialResultsAllValues = new ArrayList<>(initialResults.values());
                    //int firstResult = loopingResultsAllValues.get(0);

                    int firstResult = initialResultsAllValues.get(initialResultsAllValues.size() - 1);
                    int lastResult = loopingResultsAllValues.get(loopingResultsAllValues.size() - 1);
                    int loopResultDiff = firstResult - lastResult;

                    long rem = n - initialResults.size();
                    int mod = (int) (rem % loopingResultsSize);

                    return (rem / loopingResultsSize) * loopResultDiff
                            + (mod != 0 ? firstResult - loopingResultsAllValues.get(mod - 1) : 0)
                            + FLOOR_ROW - firstResult;
                }
            } else {
                duplicatePairs.clear();
            }
            int highestCoordinateForThisRock = simulateRockFall(chamber, rock, jetPatternSupplier, highestRock);
            highestRock = Math.min(highestRock, highestCoordinateForThisRock);
            if (!resultCache.containsKey(pair)) {
                resultCache.put(pair, highestRock);
            } else {
                dups.add(i);
            }

        }

        return FLOOR_ROW - highestRock;
    }

    private static final class Pair {
        private final Class<?> rockClass;
        private final int jetPatternIndex;
        private final long rockNum;

        private Pair(Class<?> rockClass, int jetPatternIndex, long rockNum) {
            this.rockClass = rockClass;
            this.jetPatternIndex = jetPatternIndex;
            this.rockNum = rockNum;
        }

        public Class<?> rockClass() {
            return rockClass;
        }

        public int jetPatternIndex() {
            return jetPatternIndex;
        }

        public long getRockNum() {
            return rockNum;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Pair) obj;
            return Objects.equals(this.rockClass, that.rockClass) &&
                    this.jetPatternIndex == that.jetPatternIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(rockClass, jetPatternIndex);
        }

        @Override
        public String toString() {
            return "Pair[" +
                    "rockClass=" + rockClass + ", " +
                    "jetPatternIndex=" + jetPatternIndex + ']';
        }


    }

    private int simulateRockFall(char[][] chamber,
                                 Rock rock, JetPatternSupplier jetPatternSupplier,
                                 int highestRock) {

        rock.beginRock(chamber, highestRock);

        while (true) {
            Character movement = jetPatternSupplier.get();

            if (movement == '<') {
                rock.moveLeft(chamber);
            } else {
                rock.moveRight(chamber);
            }
            if (!rock.moveDown(chamber)) {
                return rock.getRow(); //current rock's highest row
            }
        }
    }

    private static class RockSupplier implements Supplier<Rock> {
        private int i = 0;
        private final List<Supplier<Rock>> rocksSupplier = List.of(
                Rock1::new,
                Rock2::new,
                Rock3::new,
                Rock4::new,
                Rock5::new
        );

        @Override
        public Rock get() {
            Rock rock = rocksSupplier.get(i++)
                    .get();
            if (i == 5) {
                i = 0;
            }
            return rock;
        }
    }

    private static class JetPatternSupplier implements Supplier<Character> {
        private int i = 0;
        private final String jetPattern;

        private JetPatternSupplier(String jetPattern) {
            this.jetPattern = jetPattern;
        }

        @Override
        public Character get() {
            Character c = jetPattern.charAt(i++);
            if (i == jetPattern.length()) {
                i = 0;
            }
            return c;
        }

        public int getIndex() {
            return i;
        }
    }

    private static class Rock1 implements Rock {
        private final char[][] rockStructure;
        //row and column are the co-ordinates of top of this rock.
        private int row;
        private int column = 2;

        public Rock1() {
            this.rockStructure = new char[][]{
                    {'#', '#', '#', '#'}
            };
        }

        public int beginRock(char[][] chamber, int highestRock) {
            this.row = highestRock - 4;
            for (int j = 0; j < 4; j++) {
                chamber[row][column + j] = rockStructure[0][j];
            }
            return row;
        }

        public boolean moveRight(char[][] chamber) {
            int rightEndCol = column + 3;
            if (rightEndCol + 1 == chamber[0].length
                    || chamber[row][rightEndCol + 1] != EMPTY) {
                return false;
            }
            chamber[row][rightEndCol + 1] = '#';
            chamber[row][column] = EMPTY;
            column++;
            return true;
        }

        public boolean moveLeft(char[][] chamber) {
            int leftEndCol = column;
            int rightEndCol = column + 3;
            if (leftEndCol - 1 < 0
                    || chamber[row][leftEndCol - 1] != EMPTY) {
                return false;
            }
            chamber[row][leftEndCol - 1] = '#';
            chamber[row][rightEndCol] = EMPTY;
            column--;
            return true;
        }

        public boolean moveDown(char[][] chamber) {
            if (row + 1 == chamber.length) {
                return false;
            }
            for (int j = column; j < column + 4; j++) {
                if (chamber[row + 1][j] != EMPTY) {
                    return false;
                }
            }
            for (int j = column; j < column + 4; j++) {
                chamber[row][j] = EMPTY;
                chamber[row + 1][j] = '#';
            }
            row++;
            return true;
        }

        @Override
        public int getRow() {
            return row;
        }
    }

    private static class Rock2 implements Rock {
        private final char[][] rockStructure;
        private int row;
        private int column = 2;

        private Rock2() {
            this.rockStructure = new char[][]{
                    {'.', '#', '.'},
                    {'#', '#', '#'},
                    {'.', '#', '.'}
            };
        }

        public int beginRock(char[][] chamber, int highestRock) {
            this.row = highestRock - 4 - 2;
            int rowTmp = row;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    chamber[rowTmp][column + j] = rockStructure[i][j];
                }
                rowTmp++;
            }
            return row;
        }

        public boolean moveRight(char[][] chamber) {
            int rightEndCol = column + 2;
            if (rightEndCol + 1 == chamber[0].length
                    || chamber[row][rightEndCol] != EMPTY
                    || chamber[row + 1][rightEndCol + 1] != EMPTY
                    || chamber[row + 2][rightEndCol] != EMPTY) {
                return false;
            }
            chamber[row + 1][rightEndCol + 1] = '#';
            chamber[row][rightEndCol] = '#';
            chamber[row + 2][rightEndCol] = '#';

            chamber[row][column + 1] = EMPTY;
            chamber[row + 1][column] = EMPTY;
            chamber[row + 2][column + 1] = EMPTY;

            column++;
            return true;
        }

        public boolean moveLeft(char[][] chamber) {
            int leftEndCol = column;
            int rightEndCol = column + 2;
            if (leftEndCol - 1 < 0
                    || chamber[row][column] != EMPTY
                    || chamber[row + 1][leftEndCol - 1] != EMPTY
                    || chamber[row + 2][column] != EMPTY) {
                return false;
            }

            chamber[row + 1][leftEndCol - 1] = '#';
            chamber[row][column] = '#';
            chamber[row + 2][column] = '#';

            chamber[row][column + 1] = EMPTY;
            chamber[row + 1][rightEndCol] = EMPTY;
            chamber[row + 2][column + 1] = EMPTY;
            column--;
            return true;
        }

        public boolean moveDown(char[][] chamber) {
            int bottomRow = row + 2;
            if (bottomRow + 1 == chamber.length
                    || chamber[bottomRow][column] != EMPTY
                    || chamber[bottomRow + 1][column + 1] != EMPTY
                    || chamber[bottomRow][column + 2] != EMPTY) {
                return false;
            }

            chamber[bottomRow + 1][column + 1] = '#';
            chamber[bottomRow][column] = '#';
            chamber[bottomRow][column + 2] = '#';

            chamber[row + 1][column] = EMPTY;
            chamber[row + 1][column + 2] = EMPTY;
            chamber[row][column + 1] = EMPTY;
            row++;
            return true;
        }

        @Override
        public int getRow() {
            return row;
        }
    }

    private static class Rock3 implements Rock {
        private final char[][] rockStructure;
        private int row;
        private int column = 2;

        private Rock3() {
            this.rockStructure = new char[][]{
                    {'.', '.', '#'},
                    {'.', '.', '#'},
                    {'#', '#', '#'}
            };
        }

        public int beginRock(char[][] chamber, int highestRock) {
            this.row = highestRock - 4 - 2;
            int tmpRow = row;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    chamber[tmpRow][column + j] = rockStructure[i][j];
                }
                tmpRow++;
            }
            return row;
        }

        public boolean moveRight(char[][] chamber) {
            int rightEndCol = column + 2;
            if (rightEndCol + 1 == chamber[0].length
                    || chamber[row][rightEndCol + 1] != EMPTY
                    || chamber[row + 1][rightEndCol + 1] != EMPTY
                    || chamber[row + 2][rightEndCol + 1] != EMPTY) {
                return false;
            }
            chamber[row][rightEndCol + 1] = '#';
            chamber[row + 1][rightEndCol + 1] = '#';
            chamber[row + 2][rightEndCol + 1] = '#';

            chamber[row][rightEndCol] = EMPTY;
            chamber[row + 1][rightEndCol] = EMPTY;
            chamber[row + 2][column] = EMPTY;
            column++;
            return true;
        }

        public boolean moveLeft(char[][] chamber) {
            int leftEndCol = column;
            int rightEndCol = column + 2;
            if (leftEndCol - 1 < 0
                    || chamber[row][column + 1] != EMPTY
                    || chamber[row + 1][column + 1] != EMPTY
                    || chamber[row + 2][leftEndCol - 1] != EMPTY) {
                return false;
            }

            chamber[row][column + 1] = '#';
            chamber[row + 1][column + 1] = '#';
            chamber[row + 2][leftEndCol - 1] = '#';

            chamber[row][rightEndCol] = EMPTY;
            chamber[row + 1][rightEndCol] = EMPTY;
            chamber[row + 2][rightEndCol] = EMPTY;
            column--;
            return true;
        }

        public boolean moveDown(char[][] chamber) {
            int bottomRow = row + 2;
            int rightEndCol = column + 2;
            if (bottomRow + 1 == chamber.length
                    || chamber[bottomRow + 1][column] != EMPTY
                    || chamber[bottomRow + 1][column + 1] != EMPTY
                    || chamber[bottomRow + 1][column + 2] != EMPTY) {
                return false;
            }

            chamber[bottomRow + 1][column] = '#';
            chamber[bottomRow + 1][column + 1] = '#';
            chamber[bottomRow + 1][column + 2] = '#';

            chamber[row][rightEndCol] = EMPTY;
            chamber[row + 2][column] = EMPTY;
            chamber[row + 2][column + 1] = EMPTY;
            row++;
            return true;
        }

        @Override
        public int getRow() {
            return row;
        }
    }

    private static class Rock4 implements Rock {
        private final char[][] rockStructure;
        private int row;
        private int column = 2;

        private Rock4() {
            this.rockStructure = new char[][]{
                    {'#'},
                    {'#'},
                    {'#'},
                    {'#'}
            };
        }

        @Override
        public int beginRock(char[][] chamber, int highestRock) {
            this.row = highestRock - 4 - 3;
            int tmpRow = row;
            for (int i = 0; i < 4; i++) {
                chamber[tmpRow][column] = rockStructure[i][0];
                tmpRow++;
            }
            return row;
        }

        @Override
        public boolean moveRight(char[][] chamber) {
            if (column + 1 == chamber[0].length
                    || chamber[row][column + 1] != EMPTY
                    || chamber[row + 1][column + 1] != EMPTY
                    || chamber[row + 2][column + 1] != EMPTY
                    || chamber[row + 3][column + 1] != EMPTY) {
                return false;
            }
            for (int i = row; i < row + 4; i++) {
                chamber[i][column + 1] = chamber[i][column];
                chamber[i][column] = EMPTY;
            }
            column++;
            return true;
        }

        @Override
        public boolean moveLeft(char[][] chamber) {
            if (column - 1 < 0
                    || chamber[row][column - 1] != EMPTY
                    || chamber[row + 1][column - 1] != EMPTY
                    || chamber[row + 2][column - 1] != EMPTY
                    || chamber[row + 3][column - 1] != EMPTY) {
                return false;
            }
            for (int i = row; i < row + 4; i++) {
                chamber[i][column - 1] = chamber[i][column];
                chamber[i][column] = EMPTY;
            }
            column--;
            return true;
        }

        @Override
        public boolean moveDown(char[][] chamber) {
            int lowestRow = row + 3;
            if (lowestRow + 1 == chamber.length || chamber[lowestRow + 1][column] != EMPTY) {
                return false;
            }
            chamber[lowestRow + 1][column] = '#';
            chamber[row][column] = EMPTY;
            row++;
            return true;
        }

        @Override
        public int getRow() {
            return row;
        }
    }

    private static class Rock5 implements Rock {
        private final char[][] rockStructure;
        private int row;
        private int column = 2;

        private Rock5() {
            this.rockStructure = new char[][]{
                    {'#', '#'},
                    {'#', '#'}
            };
        }

        public int beginRock(char[][] chamber, int highestRock) {
            this.row = highestRock - 4 - 1;
            int tmpRow = row;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    chamber[tmpRow][column + j] = rockStructure[i][j];
                }
                tmpRow++;
            }
            return row;
        }

        public boolean moveRight(char[][] chamber) {
            int rightEndCol = column + 1;
            if (rightEndCol + 1 == chamber[0].length
                    || chamber[row][rightEndCol + 1] != EMPTY
                    || chamber[row + 1][rightEndCol + 1] != EMPTY) {
                return false;
            }
            for (int i = row; i < row + 2; i++) {
                chamber[i][rightEndCol + 1] = chamber[i][rightEndCol];
            }
            for (int i = row; i < row + 2; i++) {
                chamber[i][column] = EMPTY;
            }
            column++;
            return true;
        }

        public boolean moveLeft(char[][] chamber) {
            int rightMostCol = column + 1;
            if (column - 1 < 0
                    || chamber[row][column - 1] != EMPTY
                    || chamber[row + 1][column - 1] != EMPTY) {
                return false;
            }
            for (int i = row; i < row + 2; i++) {
                chamber[i][column - 1] = chamber[i][column];
            }
            for (int i = row; i < row + 2; i++) {
                chamber[i][rightMostCol] = EMPTY;
            }
            column--;
            return true;
        }

        public boolean moveDown(char[][] chamber) {
            int lowestRow = row + 1;
            if (lowestRow + 1 == chamber.length
                    || chamber[lowestRow + 1][column] != EMPTY
                    || chamber[lowestRow + 1][column + 1] != EMPTY) {
                return false;
            }
            for (int j = column; j < column + 2; j++) {
                chamber[lowestRow + 1][j] = chamber[lowestRow][j];
            }

            for (int j = column; j < column + 2; j++) {
                chamber[row][j] = EMPTY;
            }
            row++;
            return true;
        }

        @Override
        public int getRow() {
            return row;
        }
    }
}
