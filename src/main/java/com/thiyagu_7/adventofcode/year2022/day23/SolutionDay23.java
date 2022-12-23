package com.thiyagu_7.adventofcode.year2022.day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class SolutionDay23 {
    private static final int[] X_COOR = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] Y_COOR = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};
    private static final List<Direction> DIRECTIONS = List.of(
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST);

    public int part1(List<String> input) {
        Set<Location> allElvesLocations = new HashSet<>();

        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    allElvesLocations.add(new Location(i, j));
                }
            }
        }
        List<Direction> directions = new ArrayList<>(DIRECTIONS);
        for (int round = 0; round < 10; round++) {
            //first half of round
            Set<Location> toMoveElves = findElvesToMove(allElvesLocations);
            Map<Location, Direction> proposedMovements = proposeMovement(allElvesLocations, toMoveElves, directions);
            //second half of round
            move(allElvesLocations, proposedMovements);

            Direction head = directions.remove(0);
            directions.add(head);
        }
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE,
                maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Location location : allElvesLocations) {
            minX = Math.min(minX, location.x);
            maxX = Math.max(maxX, location.x);
            minY = Math.min(minY, location.y);
            maxY = Math.max(maxY, location.y);
        }
        // find area and subtract the elves location to find empty ground tiles
        return (maxX - minX + 1) * (maxY - minY + 1) - allElvesLocations.size();
    }

    private Set<Location> findElvesToMove(Set<Location> allElvesLocations) {
        return allElvesLocations.stream()
                .filter(location -> hasNeighbours(allElvesLocations, location))
                .collect(Collectors.toSet());
    }

    private boolean hasNeighbours(Set<Location> allElvesLocations, Location current) {
        for (int k = 0; k < 8; k++) {
            int newX = current.x + X_COOR[k];
            int newY = current.y + Y_COOR[k];
            if (allElvesLocations.contains(new Location(newX, newY))) {
                return true;
            }
        }
        return false;
    }

    private Map<Location, Direction> proposeMovement(Set<Location> allElvesLocations,
                                                     Set<Location> elvesToMove,
                                                     List<Direction> directions) {
        Map<Location, Direction> proposedMovements = new HashMap<>();
        for (Location currentElfLocation : elvesToMove) {
            Direction destination = getProposal(allElvesLocations, currentElfLocation, directions);
            if (destination != null) {
                proposedMovements.put(currentElfLocation, destination);
            }
        }
        return proposedMovements;
    }

    private Direction getProposal(Set<Location> allElvesLocations, Location currentElfLocation,
                                  List<Direction> directions) {
        for (int i = 0; i < 4; i++) {
            Direction direction = directions.get(i);
            List<Location> possibleLocationsToMove = direction.getLocationsToCheckFunc()
                    .apply(currentElfLocation);
            boolean canMove = possibleLocationsToMove.stream()
                    .noneMatch(allElvesLocations::contains);
            if (canMove) {
                return direction;
            }
        }
        return null;
    }

    private void move(Set<Location> allElvesLocations,
                      Map<Location, Direction> proposedMovements) {
        record Movement(Location source, Location destination) {
        }
        Map<Location, List<Location>> destinationToSourceMapping = proposedMovements.entrySet()
                .stream()
                .map(e -> new Movement(e.getKey(),
                        e.getValue().getMoveFunc()
                                .apply(e.getKey())))
                .collect(Collectors.groupingBy(Movement::destination,
                        Collectors.mapping(Movement::source, Collectors.toList())));

        destinationToSourceMapping.entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1) // filter out if more than one elf wants to end up on that location
                .forEach(e -> {
                    allElvesLocations.remove(e.getValue().get(0)); //Remove old location
                    allElvesLocations.add(e.getKey()); //Move to new location
                });
    }

    private enum Direction {
        NORTH(l -> List.of(
                new Location(l.x - 1, l.y - 1),
                new Location(l.x - 1, l.y),
                new Location(l.x - 1, l.y + 1)
        ),
                l -> new Location(l.x - 1, l.y)),
        SOUTH(l -> List.of(
                new Location(l.x + 1, l.y - 1),
                new Location(l.x + 1, l.y),
                new Location(l.x + 1, l.y + 1)
        ),
                l -> new Location(l.x + 1, l.y)),
        WEST(l -> List.of(
                new Location(l.x - 1, l.y - 1),
                new Location(l.x, l.y - 1),
                new Location(l.x + 1, l.y - 1)
        ),
                l -> new Location(l.x, l.y - 1)),
        EAST(l -> List.of(
                new Location(l.x - 1, l.y + 1),
                new Location(l.x, l.y + 1),
                new Location(l.x + 1, l.y + 1)
        ),
                l -> new Location(l.x, l.y + 1));

        private final Function<Location, List<Location>> locationsToCheckFunc;
        private final UnaryOperator<Location> moveFunc;

        Direction(Function<Location, List<Location>> locationsToCheckFunc,
                  UnaryOperator<Location> moveFunc) {
            this.locationsToCheckFunc = locationsToCheckFunc;
            this.moveFunc = moveFunc;
        }

        public Function<Location, List<Location>> getLocationsToCheckFunc() {
            return locationsToCheckFunc;
        }

        public UnaryOperator<Location> getMoveFunc() {
            return moveFunc;
        }
    }

    private record Location(int x, int y) {

    }

    public int part2(List<String> input) {
        Set<Location> allElvesLocations = new HashSet<>();

        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    allElvesLocations.add(new Location(i, j));
                }
            }
        }
        List<Direction> directions = new ArrayList<>(DIRECTIONS);
        for (int round = 0; ; round++) {
            //first half of round
            Set<Location> toMoveElves = findElvesToMove(allElvesLocations);
            if (toMoveElves.isEmpty()) {
                return round + 1;
            }
            Map<Location, Direction> proposedMovements = proposeMovement(allElvesLocations, toMoveElves, directions);
            //second half of round
            move(allElvesLocations, proposedMovements);

            Direction head = directions.remove(0);
            directions.add(head);
        }
    }
}
