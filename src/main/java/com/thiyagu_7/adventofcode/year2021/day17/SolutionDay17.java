package com.thiyagu_7.adventofcode.year2021.day17;

public class SolutionDay17 {
    public int part1(int x1, int x2, int y1, int y2) {
        Result result = bruteForce(x1, x2, y1, y2);
        return result.maxYAttained;
    }

    public int part2(int x1, int x2, int y1, int y2) {
        Result result = bruteForce(x1, x2, y1, y2);
        return result.numberOfTimesWithinTargetArea;
    }

    private Result bruteForce(int x1, int x2, int y1, int y2) {
        int currX, currY;
        // max y for each try - max y in tries when we don't hit target shouldn't be counted
        int maxY;
        int maxYAttained = Integer.MIN_VALUE;
        int x, y;
        int numberOfTimesWithinTargetArea = 0;
        for (int i = 0; i <= 500; i++) {
            for (int j = -500; j <= 500; j++) {
                x = i;
                y = j;
                currX = 0;
                currY = 0;
                maxY = Integer.MIN_VALUE;
                while (!shouldStop(currY, y1)) {
                    currX += x;
                    currY += y;
                    maxY = Math.max(maxY, currY);
                    if (isWithinTarget(currX, currY, x1, x2, y1, y2)) {
                        maxYAttained = Math.max(maxYAttained, maxY); //part 1
                        numberOfTimesWithinTargetArea++; //part 2
                        break;
                    }
                    if (x > 0) {
                        x--;
                    } else if (x < 0) {
                        x++;
                    }
                    y--;
                }
            }
        }
        return new Result(maxYAttained, numberOfTimesWithinTargetArea);
    }

    private boolean isWithinTarget(int currX, int currY, int x1, int x2, int y1, int y2) {
        return currX >= x1 && currX <= x2 && currY >= y1 && currY <= y2;
    }

    private boolean shouldStop(int currY, int y1) {
        return currY < y1;
    }

    private record Result(int maxYAttained, int numberOfTimesWithinTargetArea) {

    }
}
