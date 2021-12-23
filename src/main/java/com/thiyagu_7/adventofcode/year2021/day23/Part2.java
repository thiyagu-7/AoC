package com.thiyagu_7.adventofcode.year2021.day23;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Part2 {
    public static int res = Integer.MAX_VALUE;

    public void getPossibleMovements(Burrow burrow, int cost) {
        if (isEnd(burrow)) {
            res = Math.min(res, cost);
            return;
        }
        if (cost > res) {
            return;
        }
        char[][] rooms = burrow.rooms;
        char[] hallway = burrow.hallway;
        for (int i = 0; i < hallway.length; i++) {
            if (hallway[i] != '.') {
                Map<Burrow, Integer> hallwayMovements = getPossibleMovementsForHallway(burrow, i);
                hallwayMovements.forEach((k, v) -> getPossibleMovements(k, cost + v));
            }
        }

        //row 1
        for (int j = 0; j < 4; j++) {
            char c = getDestination(j);
            if (rooms[0][j] != '.') {
                // all must be in destination to be skipped
                if (rooms[0][j] == c && rooms[1][j] == c && rooms[2][j] == c && rooms[3][j] == c) {
                    continue;
                }
                Map<Burrow, Integer> hallwayMovements = getPossibleMovements(burrow, 0, j);
                hallwayMovements.forEach((k, v) -> getPossibleMovements(k, cost + v));
            }
        }
        // row 2
        for (int j = 0; j < 4; j++) {
            char c = getDestination(j);
            if (rooms[1][j] != '.'
                    && rooms[0][j] == '.') { //all above must be '.' to move this
                // all must be in destination to be skipped
                if (rooms[1][j] == c && rooms[2][j] == c && rooms[3][j] == c) {
                    continue;
                }

                Map<Burrow, Integer> hallwayMovements = getPossibleMovements(burrow, 1, j);
                hallwayMovements.forEach((k, v) -> getPossibleMovements(k, cost + v));
            }
        }
        // row 3
        for (int j = 0; j < 4; j++) {
            char c = getDestination(j);
            if (rooms[2][j] != '.'
                    && rooms[0][j] == '.' && rooms[1][j] == '.') { //all above
                // all must be in destination to be skipped
                if (rooms[2][j] == c && rooms[3][j] == c) {
                    continue;
                }
                Map<Burrow, Integer> hallwayMovements = getPossibleMovements(burrow, 2, j);
                hallwayMovements.forEach((k, v) -> getPossibleMovements(k, cost + v));
            }
        }
        // row 4
        for (int j = 0; j < 4; j++) {
            char c = getDestination(j);
            if (rooms[3][j] != '.'
                    && rooms[0][j] == '.' && rooms[1][j] == '.' && rooms[2][j] == '.') { //all above
                if (rooms[3][j] == c) {
                    continue;
                }
                Map<Burrow, Integer> hallwayMovements = getPossibleMovements(burrow, 3, j);
                hallwayMovements.forEach((k, v) -> getPossibleMovements(k, cost + v));
            }
        }
    }

    private String stringValue(Burrow burrow) {
        return new String(burrow.hallway) + Arrays.toString(burrow.rooms[0])
                + Arrays.toString(burrow.rooms[1])
                + Arrays.toString(burrow.rooms[2])
                + Arrays.toString(burrow.rooms[3]);
    }

    public Map<Burrow, Integer> getPossibleMovementsForHallway(Burrow burrow, int hallwayIdx) {
        Map<Burrow, Integer> burrows = new HashMap<>();
        char[][] rooms = burrow.rooms;
        char[] hallway = burrow.hallway;
        char c = hallway[hallwayIdx];
        int destRoomIdx = getDestinationRoomIndex(c);
        int destHallwayIdx = (destRoomIdx + 1) * 2;
        //left
        if (destHallwayIdx < hallwayIdx) {
            for (int i = destHallwayIdx; i < hallwayIdx; i++) {
                if (hallway[i] != '.') {  //cannot skip over
                    return burrows;
                }
            }

            int cost = Math.abs(hallwayIdx - destHallwayIdx) * getCost(c);
            if (rooms[0][destRoomIdx] == '.') {
                if ((rooms[1][destRoomIdx] == '.' || rooms[1][destRoomIdx] == c)
                        && (rooms[2][destRoomIdx] == '.' || rooms[2][destRoomIdx] == c)
                        && (rooms[3][destRoomIdx] == '.' || rooms[3][destRoomIdx] == c)) {
                    char[][] newRooms = copy(rooms);
                    Burrow newBurrow;
                    char[] newHallway = copy(hallway);
                    newHallway[hallwayIdx] = '.';
                    if (rooms[3][destRoomIdx] == '.') {
                        newRooms[3][destRoomIdx] = c;
                        newBurrow = new Burrow(newHallway, newRooms);
                        cost += 4 * getCost(c);
                    } else if (rooms[2][destRoomIdx] == '.') {
                        newRooms[2][destRoomIdx] = c;
                        newBurrow = new Burrow(newHallway, newRooms);
                        cost += 3 * getCost(c);
                    } else if (rooms[1][destRoomIdx] == '.') {
                        newRooms[1][destRoomIdx] = c;
                        newBurrow = new Burrow(newHallway, newRooms);
                        cost += 2 * getCost(c);
                    } else {
                        newRooms[0][destRoomIdx] = c;
                        newBurrow = new Burrow(newHallway, newRooms);
                        cost += 1 * getCost(c);
                    }
                    burrows.put(newBurrow, cost);
                }
            }
        } else { //right
            for (int i = hallwayIdx + 1; i <= destHallwayIdx; i++) {
                if (hallway[i] != '.') {  //cannot skip over
                    return burrows;
                }
            }

            int cost = Math.abs(hallwayIdx - destHallwayIdx) * getCost(c);
            if (rooms[0][destRoomIdx] == '.') {
                if ((rooms[1][destRoomIdx] == '.' || rooms[1][destRoomIdx] == c)
                        && (rooms[2][destRoomIdx] == '.' || rooms[2][destRoomIdx] == c)
                        && (rooms[3][destRoomIdx] == '.' || rooms[3][destRoomIdx] == c)) {
                    char[][] newRooms = copy(rooms);
                    Burrow newBurrow;
                    char[] newHallway = copy(hallway);
                    newHallway[hallwayIdx] = '.';
                    if (rooms[3][destRoomIdx] == '.') {
                        newRooms[3][destRoomIdx] = c;
                        newBurrow = new Burrow(newHallway, newRooms);
                        cost += 4 * getCost(c);
                    } else if (rooms[2][destRoomIdx] == '.') {
                        newRooms[2][destRoomIdx] = c;
                        newBurrow = new Burrow(newHallway, newRooms);
                        cost += 3 * getCost(c);
                    } else if (rooms[1][destRoomIdx] == '.') {
                        newRooms[1][destRoomIdx] = c;
                        newBurrow = new Burrow(newHallway, newRooms);
                        cost += 2 * getCost(c);
                    } else {
                        newRooms[0][destRoomIdx] = c;
                        newBurrow = new Burrow(newHallway, newRooms);
                        cost += 1 * getCost(c);
                    }
                    burrows.put(newBurrow, cost);
                }
            }
        }
        return burrows;
    }

    // x can be 0, 1, 2, or 3
    public Map<Burrow, Integer> getPossibleMovements(Burrow burrow, int x, int y) {
        Map<Burrow, Integer> burrows = new HashMap<>();
        char[][] rooms = burrow.rooms;
        char[] hallway = burrow.hallway;
        char c = burrow.rooms[x][y];

        int hallwayIdx = (y + 1) * 2;
        int cost;
        int initCost = 0;
        // check all
        if (x >= 1) {
            for (int i = x -1; i >= 0; i--) {
                if (burrow.rooms[i][y] != '.') {
                    return burrows;
                }
            }
            initCost = getCost(c) * x;
        }

        // left
        cost = initCost;
        hallwayIdx--;
        while (hallwayIdx >= 0) {
            if (hallway[hallwayIdx] == '.') {
                if (hallwayIdx == 0) {
                    // came from index 1 and hence 1 * getCost()
                    cost += getCost(c);
                } else {
                    cost += 2 * getCost(c);
                }
                char[] newHallway = copy(hallway);
                char[][] newRooms = copy(rooms);
                newHallway[hallwayIdx] = c;
                newRooms[x][y] = '.';
                Burrow newBurrow = new Burrow(newHallway, newRooms);
                burrows.put(newBurrow, cost);
            } else {
                break;
            }
            // check if hallwayIdx - 1 is the destination
            if (hallwayIdx >= 3) {
                int l = hallwayIdx - 1;
                int roomIdx = (l / 2) - 1;
                if (getDestination(roomIdx) == c && rooms[0][roomIdx] == '.') {
                    if ((rooms[1][roomIdx] == '.' || rooms[1][roomIdx] == c)
                            && (rooms[2][roomIdx] == '.' || rooms[2][roomIdx] == c)
                            && (rooms[3][roomIdx] == '.' || rooms[3][roomIdx] == c)) {
                        char[][] newRooms = copy(rooms);
                        newRooms[x][y] = '.';
                        Burrow newBurrow;
                        if (rooms[3][roomIdx] == '.') {
                            newRooms[3][roomIdx] = c;
                            newBurrow = new Burrow(hallway, newRooms);
                            cost += 5 * getCost(c);
                        } else if (rooms[2][roomIdx] == '.') {
                            newRooms[2][roomIdx] = c;
                            newBurrow = new Burrow(hallway, newRooms);
                            cost += 4 * getCost(c);
                        } else if (rooms[1][roomIdx] == '.') {
                            newRooms[1][roomIdx] = c;
                            newBurrow = new Burrow(hallway, newRooms);
                            cost += 3 * getCost(c);
                        } else {
                            newRooms[0][roomIdx] = c;
                            newBurrow = new Burrow(hallway, newRooms);
                            cost += 2 * getCost(c);
                        }
                        burrows = new HashMap<>(); //
                        burrows.put(newBurrow, cost);
                        return burrows; //
                    }
                }
            }
            hallwayIdx = hallwayIdx == 1 ? 0 : (hallwayIdx - 2);
        }

        hallwayIdx = (y + 1) * 2;

        // right
        cost = initCost;
        hallwayIdx++;
        while (hallwayIdx <= 10) {
            if (hallway[hallwayIdx] == '.') {
                if (hallwayIdx == 10) {
                    // came from index 0 and hence 1 * getCost()
                    cost += getCost(c);
                } else {
                    cost += 2 * getCost(c);
                }
                char[] newHallway = copy(hallway);
                char[][] newRooms = copy(rooms);
                newHallway[hallwayIdx] = c;
                newRooms[x][y] = '.';
                Burrow newBurrow = new Burrow(newHallway, newRooms);
                burrows.put(newBurrow, cost);
            } else {
                break;
            }
            // check if hallwayIdx + 1 is the destination
            if (hallwayIdx <= 7) {
                int l = hallwayIdx + 1;
                int roomIdx = (l / 2) - 1;
                if (getDestination(roomIdx) == c && rooms[0][roomIdx] == '.') {
                    if ((rooms[1][roomIdx] == '.' || rooms[1][roomIdx] == c)
                            && (rooms[2][roomIdx] == '.' || rooms[2][roomIdx] == c)
                            && (rooms[3][roomIdx] == '.' || rooms[3][roomIdx] == c)) {
                        char[][] newRooms = copy(rooms);
                        newRooms[x][y] = '.';
                        Burrow newBurrow;
                        if (rooms[3][roomIdx] == '.') {
                            newRooms[3][roomIdx] = c;
                            newBurrow = new Burrow(hallway, newRooms);
                            cost += 5 * getCost(c);
                        } else if (rooms[2][roomIdx] == '.') {
                            newRooms[2][roomIdx] = c;
                            newBurrow = new Burrow(hallway, newRooms);
                            cost += 4 * getCost(c);
                        } else if (rooms[1][roomIdx] == '.') {
                            newRooms[1][roomIdx] = c;
                            newBurrow = new Burrow(hallway, newRooms);
                            cost += 3 * getCost(c);
                        } else {
                            newRooms[0][roomIdx] = c;
                            newBurrow = new Burrow(hallway, newRooms);
                            cost += 2 * getCost(c);
                        }
                        burrows = new HashMap<>();
                        burrows.put(newBurrow, cost);
                        return burrows;
                    }
                }
            }
            hallwayIdx = hallwayIdx == 9 ? 10 : (hallwayIdx + 2);
        }
        return burrows;
    }

    private char getDestination(int roomIdx) {
        if (roomIdx == 0) {
            return 'A';
        } else if (roomIdx == 1) {
            return 'B';
        } else if (roomIdx == 2) {
            return 'C';
        }else if (roomIdx == 3) {
            return 'D';
        }
       throw new RuntimeException();
    }

    private int getDestinationRoomIndex(char c) {
        if (c == 'A') {
            return 0;
        } else if (c == 'B') {
            return 1;
        } else if (c == 'C') {
            return 2;
        }  else if (c == 'D') {
            return 3;
        }
        throw new RuntimeException();
    }

    public int getCost(char c) {
        if (c == 'A') {
            return 1;
        } else if (c == 'B') {
            return 10;
        } else if (c == 'C') {
            return 100;
        } else if (c == 'D') {
            return 1000;
        }
        throw new RuntimeException();
    }

    private char[] copy(char[] c) {
        return Arrays.copyOf(c, c.length);
    }

    private char[][] copy(char[][] c) {
        char[][] r = new char[c.length][c[0].length];
        for (int i = 0; i < c.length; i++) {
            r[i] = Arrays.copyOf(c[i], c[i].length);
        }
        return r;
    }

    private boolean isEnd(Burrow burrow) {
        char[][] rooms = burrow.rooms;
        return rooms[0][0] == 'A' && rooms[1][0] == 'A' && rooms[2][0] == 'A' && rooms[3][0] == 'A'
                && rooms[0][1] == 'B' && rooms[1][1] == 'B' && rooms[2][1] == 'B' && rooms[3][1] == 'B'
                && rooms[0][2] == 'C' && rooms[1][2] == 'C' & rooms[2][2] == 'C' && rooms[3][2] == 'C'
                && rooms[0][3] == 'D' && rooms[1][3] == 'D' && rooms[2][3] == 'D' && rooms[3][3] == 'D';
    }

    public static class Burrow {
        private final char[] hallway;
        private final char[][] rooms;

        public Burrow(char[] hallway, char[][] rooms) {
            this.hallway = hallway;
            this.rooms = rooms;
        }

        public Burrow(char[][] rooms) {
            hallway = new char[11];
            Arrays.fill(hallway, '.');
            this.rooms = rooms;
        }

    }
}
