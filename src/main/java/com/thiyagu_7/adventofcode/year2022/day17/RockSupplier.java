package com.thiyagu_7.adventofcode.year2022.day17;

import java.util.List;
import java.util.function.Supplier;

class RockSupplier implements Supplier<Rock> {
    private int i = 0;
    private final List<Supplier<Rock>> rocksSupplier = List.of(
            Rock1::new,
            Rock2::new,
            Rock3::new,
            Rock4::new,
            Rock5::new
    );

    @Override
    public Rock get() {
        Rock rock = rocksSupplier.get(i++)
                .get();
        if (i == 5) {
            i = 0;
        }
        return rock;
    }
}
