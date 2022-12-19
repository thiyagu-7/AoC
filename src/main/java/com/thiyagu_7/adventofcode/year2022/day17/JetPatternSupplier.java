package com.thiyagu_7.adventofcode.year2022.day17;

import java.util.function.Supplier;

class JetPatternSupplier implements Supplier<Character> {
    private int i = 0;
    private final String jetPattern;

    JetPatternSupplier(String jetPattern) {
        this.jetPattern = jetPattern;
    }

    public int getIndex() {
        return i;
    }
    @Override
    public Character get() {
        Character c = jetPattern.charAt(i++);
        if (i == jetPattern.length()) {
            i = 0;
        }
        return c;
    }
}
