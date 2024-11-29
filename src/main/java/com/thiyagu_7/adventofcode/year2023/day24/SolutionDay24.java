package com.thiyagu_7.adventofcode.year2023.day24;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolutionDay24 {
    public int part1(List<String> input, double low, double high) {
        List<InputLine> inputLines = parseInput(input);
        int res = 0;
        for (int i = 0; i < inputLines.size(); i++) {
            Position position1 = inputLines.get(i).position;
            Velocity velocity1 = inputLines.get(i).velocity;
            PositionXY position1XY = new PositionXY(position1.x, position1.y);
            VelocityXY velocity1XY = new VelocityXY(velocity1.xVelocity, velocity1.yVelocity);

            for (int j = i + 1; j < inputLines.size(); j++) {
                Position position2 = inputLines.get(j).position;
                Velocity velocity2 = inputLines.get(j).velocity;
                PositionXY position2XY = new PositionXY(position2.x, position2.y);
                VelocityXY velocity2XY = new VelocityXY(velocity2.xVelocity, velocity2.yVelocity);

                SlopeIntercept slopeIntercept1 = findSlopeIntercept(position1XY, velocity1XY);
                SlopeIntercept slopeIntercept2 = findSlopeIntercept(position2XY, velocity2XY);

                PositionXY intersection = findIntersection(slopeIntercept1, slopeIntercept2);

                if (isWithinTestRange(intersection, low, high)
                        && isIntersectionInTheFuture(intersection, position1XY, velocity1XY, position2XY, velocity2XY)) {
                    res++;
                }

            }
        }
        return res;
    }

    private List<InputLine> parseInput(List<String> input) {
        List<InputLine> inputLines = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(" @ ");
            String[] position = parts[0].split(", ");
            String[] velocity = parts[1].split(", ");
            inputLines.add(
                    new InputLine(
                            new Position(
                                    Double.parseDouble(position[0].trim()),
                                    Double.parseDouble(position[1].trim()),
                                    Double.parseDouble(position[2].trim())
                            ),
                            new Velocity(
                                    Integer.parseInt(velocity[0].trim()),
                                    Integer.parseInt(velocity[1].trim()),
                                    Integer.parseInt(velocity[2].trim())
                            )
                    )
            );
        }
        return inputLines;
    }

    private SlopeIntercept findSlopeIntercept(PositionXY currentPositionXY, VelocityXY velocityXY) {
        PositionXY nextPositionXY = new PositionXY(
                currentPositionXY.x + velocityXY.x,
                currentPositionXY.y + velocityXY.y
        );
        //y = mx + c
        //m = (y₂ - y₁)/(x₂ - x₁)
        //c = y₁ - x₁(y₂ - y₁)/(x₂ - x₁)
        double slope = (nextPositionXY.y - currentPositionXY.y) / (double) (nextPositionXY.x - currentPositionXY.x);
        double c = currentPositionXY.y - currentPositionXY.x * slope;
        return new SlopeIntercept(slope, c);
    }

    private PositionXY findIntersection(SlopeIntercept slopeIntercept1, SlopeIntercept slopeIntercept2) {
        //y = mx + c
        //eq of line: -mx + y - c = 0 (a₁x + b₁y + c₁ = 0)
        double a1 = -slopeIntercept1.slope;
        double b1 = 1;
        double c1 = -slopeIntercept1.intercept;
        double a2 = -slopeIntercept2.slope;
        double b2 = 1;
        double c2 = -slopeIntercept2.intercept;

        double x = (b1 * c2 - b2 * c1) / (a1 * b2 - a2 * b1);
        double y = (a2 * c1 - a1 * c2) / (a1 * b2 - a2 * b1);
        return new PositionXY(x, y);
    }

    private boolean isWithinTestRange(PositionXY intersection, double low, double high) {
        return intersection.x >= low && intersection.x <= high
                && intersection.y >= low && intersection.y <= high;
    }

    private boolean isIntersectionInTheFuture(PositionXY intersection,
                                              PositionXY position1XY, VelocityXY velocity1XY,
                                              PositionXY position2XY, VelocityXY velocity2XY) {

        PositionXY nextPosition1XY = new PositionXY(
                position1XY.x + velocity1XY.x,
                position1XY.y + velocity1XY.y
        );
        PositionXY nextPosition2XY = new PositionXY(
                position2XY.x + velocity2XY.x,
                position2XY.y + velocity2XY.y
        );
        return (Math.signum(position1XY.x - nextPosition1XY.x) == Math.signum(position1XY.x - intersection.x))
                && (Math.signum(position1XY.y - nextPosition1XY.y) == Math.signum(position1XY.y - intersection.y))
                && (Math.signum(position2XY.x - nextPosition2XY.x) == Math.signum(position2XY.x - intersection.x))
                && (Math.signum(position2XY.y - nextPosition2XY.y) == Math.signum(position2XY.y - intersection.y));
    }

    record Position(double x, double y, double z) {

    }

    record PositionXY(double x, double y) {

    }

    record VelocityXY(int x, int y) {

    }

    record Velocity(int xVelocity, int yVelocity, int zVelocity) {

    }

    record SlopeIntercept(double slope, double intercept) {
    }

    record InputLine(Position position, Velocity velocity) {

    }

    public RockAndVelocity part2(List<String> input) {
        List<InputLine> inputLines = parseInput(input);
        Result result;

        long[][] matrix = new long[4][4];
        long[] cons = new long[4];

        result = eq1(inputLines.get(0).position, inputLines.get(0).velocity, inputLines.get(1).position, inputLines.get(1).velocity);
        fill(matrix, cons, 0, result);
        result = eq1(inputLines.get(1).position, inputLines.get(1).velocity, inputLines.get(2).position, inputLines.get(2).velocity);
        fill(matrix, cons, 1, result);
        result = eq1(inputLines.get(2).position, inputLines.get(2).velocity, inputLines.get(3).position, inputLines.get(3).velocity);
        fill(matrix, cons, 2, result);
        result = eq1(inputLines.get(3).position, inputLines.get(3).velocity, inputLines.get(4).position, inputLines.get(4).velocity);
        fill(matrix, cons, 3, result);

        BigDecimal d = determinant(matrix);
        long[][] copiedMatrix;
        copiedMatrix = copy(matrix);
        fillCons(copiedMatrix, cons, 0);
        BigDecimal d1 = determinant(copiedMatrix);

        copiedMatrix = copy(matrix);
        fillCons(copiedMatrix, cons, 1);
        BigDecimal d2 = determinant(copiedMatrix);

        copiedMatrix = copy(matrix);
        fillCons(copiedMatrix, cons, 2);
        BigDecimal d3 = determinant(copiedMatrix);

        copiedMatrix = copy(matrix);
        fillCons(copiedMatrix, cons, 3);
        BigDecimal d4 = determinant(copiedMatrix);

        BigDecimal x = d1.divide(d, 3, RoundingMode.HALF_UP);
        BigDecimal y = d2.divide(d, 3, RoundingMode.HALF_UP);
        BigDecimal dx = d3.divide(d, 3, RoundingMode.HALF_UP);
        BigDecimal dy = d4.divide(d, 3, RoundingMode.HALF_UP);
        //System.out.println(x +" " + y + " " + dx + " " + dy);

        result = eq2(inputLines.get(0).position, inputLines.get(0).velocity, inputLines.get(1).position, inputLines.get(1).velocity);
        fill(matrix, cons, 0, result);
        result = eq2(inputLines.get(1).position, inputLines.get(1).velocity, inputLines.get(2).position, inputLines.get(2).velocity);
        fill(matrix, cons, 1, result);
        result = eq2(inputLines.get(2).position, inputLines.get(2).velocity, inputLines.get(3).position, inputLines.get(3).velocity);
        fill(matrix, cons, 2, result);
        result = eq2(inputLines.get(3).position, inputLines.get(3).velocity, inputLines.get(4).position, inputLines.get(4).velocity);
        fill(matrix, cons, 3, result);

        d = determinant(matrix);
        copiedMatrix = copy(matrix);
        fillCons(copiedMatrix, cons, 0);
        d1 = determinant(copiedMatrix);

        copiedMatrix = copy(matrix);
        fillCons(copiedMatrix, cons, 1);
        d2 = determinant(copiedMatrix);

        copiedMatrix = copy(matrix);
        fillCons(copiedMatrix, cons, 2);
        d3 = determinant(copiedMatrix);

        copiedMatrix = copy(matrix);
        fillCons(copiedMatrix, cons, 3);
        d4 = determinant(copiedMatrix);

        //x = d1.divide(d, 3, RoundingMode.HALF_UP); // should be same as previous x, but diff in decimal
        BigDecimal z = d2.divide(d, 3, RoundingMode.HALF_UP);
        dx = d3.divide(d, 3, RoundingMode.HALF_UP); // same as previous dx
        BigDecimal dz = d4.divide(d, 3, RoundingMode.HALF_UP);

        //System.out.println(x +" " + z + " " + dx + " " + dz);

        //Part 2 answer is x.add(y).add(z)
        return new RockAndVelocity(x, y, z, dx, dy, dz);
    }

    private void fillCons(long[][] matrix, long[] cons, int j) {
        for (int i = 0; i < 4; i++) {
            matrix[i][j] = cons[i];
        }
    }

    private long[][] copy(long[][] matrix) {
        long[][] copy = new long[4][4];
        for (int i = 0; i < 4; i++) {
            copy[i] = Arrays.copyOf(matrix[i], 4);
        }
        return copy;
    }

    private void fill(long[][] row, long[] cons, int i, Result result) {
        row[i][0] = result.a;
        row[i][1] = result.b;
        row[i][2] = (long) result.c;
        row[i][3] = (long) result.d;
        cons[i] = (long) result.e;
    }

    private Result eq1(Position position1, Velocity velocity1,
                              Position position2, Velocity velocity2) {
        int x = velocity2.yVelocity - velocity1.yVelocity;
        int y = velocity1.xVelocity - velocity2.xVelocity;
        double dx = position1.y - position2.y;
        double dy = position2.x - position1.x;

        double cons = (position2.x * velocity2.yVelocity)
                + (position1.y * velocity1.xVelocity)
                - (position2.y * velocity2.xVelocity)
                - (position1.x * velocity1.yVelocity);

        return new Result(x, y, dx, dy, cons);
    }

    private Result eq2(Position position1, Velocity velocity1,
                              Position position2, Velocity velocity2) {
        int x = velocity2.zVelocity - velocity1.zVelocity;
        int z = velocity1.xVelocity - velocity2.xVelocity;
        double dx = position1.z - position2.z;
        double dz = position2.x - position1.x;

        double cons = (position2.x * velocity2.zVelocity)
                + (position1.z * velocity1.xVelocity)
                - (position2.z * velocity2.xVelocity)
                - (position1.x * velocity1.zVelocity);

        return new Result(x, z, dx, dz, cons);

    }

    record Result(int a, int b, double c, double d, double e) {

    }

    private BigDecimal determinant(long[][] matrix) {
        if (matrix.length == 2) {
            BigDecimal a = BigDecimal.valueOf(matrix[0][0]).multiply(BigDecimal.valueOf(matrix[1][1]));
            BigDecimal b = BigDecimal.valueOf(matrix[1][0]).multiply(BigDecimal.valueOf(matrix[0][1]));
            return a.subtract(b);
        }
        int n = matrix.length;
        boolean neg = false;
        BigDecimal res = BigDecimal.ZERO;
        for (int j = 0; j < n; j++) {
            long[][] subMatrix = subMatrix(matrix, 0, j);
            res = res.add(BigDecimal.valueOf(matrix[0][j]).multiply(determinant(subMatrix))
                    .multiply((neg ? BigDecimal.valueOf(-1) : BigDecimal.ONE)));
            neg = !neg;
        }
        return res;
    }

    private long[][] subMatrix(long[][] matrix, int x, int y) {
        int n = matrix.length;
        long[][] subMatrix = new long[n - 1][n - 1];
        int ii = 0;
        for (int i = 0; i < n; i++) {
            if (i == x) {
                continue;
            }
            int jj = 0;
            for (int j = 0; j < n; j++) {
                if (j == y) {
                    continue;
                }
                subMatrix[ii][jj++] = matrix[i][j];
            }
            ii++;
        }
        return subMatrix;
    }
}
