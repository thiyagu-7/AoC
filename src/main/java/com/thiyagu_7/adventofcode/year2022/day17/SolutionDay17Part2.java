package com.thiyagu_7.adventofcode.year2022.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SolutionDay17Part2 {
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
}
