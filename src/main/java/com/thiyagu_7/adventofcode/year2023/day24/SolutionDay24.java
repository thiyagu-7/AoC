package com.thiyagu_7.adventofcode.year2023.day24;

import java.util.ArrayList;
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
}
