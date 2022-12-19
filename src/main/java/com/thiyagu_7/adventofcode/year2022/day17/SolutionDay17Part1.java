package com.thiyagu_7.adventofcode.year2022.day17;


import java.util.Arrays;
import java.util.function.Supplier;

public class SolutionDay17Part1 {
    private static final int FLOOR_ROW = 5000; //meh..

    public int part1(String jetPattern) {
        char[][] chamber = new char[FLOOR_ROW][7];
        for (int i = 0; i < FLOOR_ROW; i++) {
            Arrays.fill(chamber[i], '.');
        }
        RockSupplier rockSupplier = new RockSupplier();
        JetPatternSupplier jetPatternSupplier = new JetPatternSupplier(jetPattern);

        int highestRock = FLOOR_ROW;

        //Simulate 2022 rock fall
        for (int i = 0; i < 2022; i++) {
            int highestCoordinateForThisRock = simulateRockFall(chamber, rockSupplier, jetPatternSupplier, highestRock);
            highestRock = Math.min(highestRock, highestCoordinateForThisRock);
        }
        return FLOOR_ROW - highestRock;
    }


    private record Pair(Class<?> rockClass, int jetPatternIndex) {

    }
    //LinkedHashMap<Pair, Integer> resultCache = new LinkedHashMap<>();

    private int simulateRockFall(char[][] chamber,
                                 Supplier<Rock> rockSupplier, JetPatternSupplier jetPatternSupplier,
                                 int highestRock) {
        Rock rock = rockSupplier.get();
        rock.beginRock(chamber, highestRock);
        /*Pair pair = new Pair(rock.getClass(), jetPatternSupplier.getIndex());
         if (resultCache.containsKey(pair)) {
            System.out.println("Found " + pair + " " + Math.min(rock.getRow(), highestRock));
        }*/

        while (true) {
            Character movement = jetPatternSupplier.get();

            if (movement == '<') {
                rock.moveLeft(chamber);
            } else {
                rock.moveRight(chamber);
            }
            if (!rock.moveDown(chamber)) {
               /* if (!resultCache.containsKey(pair)) {
                    System.out.println("Processing " + pair + " " + Math.min(rock.getRow(), highestRock));
                    resultCache.put(pair, Math.min(rock.getRow(), highestRock));
                }*/
                return rock.getRow(); //current rock's highest row
            }
        }
    }
}
