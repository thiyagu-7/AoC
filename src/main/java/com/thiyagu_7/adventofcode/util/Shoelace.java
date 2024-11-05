package com.thiyagu_7.adventofcode.util;

import java.util.List;

public class Shoelace {
    public static long shoeLace(List<Position> positions) {
        long area = 0;
        for (int i = 0; i < positions.size() - 1; i++) {
            Position p1 = positions.get(i);
            Position p2 = positions.get(i + 1);
            area += (long) p1.x() * p2.y() - (long) p1.y() * p2.x();
        }
        Position p1 = positions.get(positions.size() - 1);
        Position p2 = positions.get(0);
        area += (long) p1.x() * p2.y() - (long) p1.y() * p2.x();
        return area / 2;
    }
}
