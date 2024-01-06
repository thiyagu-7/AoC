package com.thiyagu_7.adventofcode.util;

import java.util.List;

public class LCMHelper {
    private static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static long findLCM(List<Integer> list) {
        long lcm = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            long curr = list.get(i);
            lcm = (lcm * curr) / gcd(lcm, curr);
        }
        return lcm;
    }
}
