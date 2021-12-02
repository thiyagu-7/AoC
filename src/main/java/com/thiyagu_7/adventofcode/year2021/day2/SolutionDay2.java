package com.thiyagu_7.adventofcode.year2021.day2;

import java.util.List;

public class SolutionDay2 {
    public int part1(List<String> input) {
        /*
         x - horizontal position
         y - depth
         */
        int x = 0, y = 0;
        for (String command : input) {
            String[] parts = command.split(" ");
            int amount = Integer.parseInt(parts[1]);
            switch (parts[0]) {
                case "forward":
                    x += amount;
                    break;
                case "down":
                    y += amount;
                    break;
                case "up":
                    y -= amount;
                    break;
            }
        }
        return x * y;
    }

    public int part2(List<String> input) {
        //horizontal position and depth
        int x = 0, y = 0;
        int aim = 0;
        for (String command : input) {
            String[] parts = command.split(" ");
            int amount = Integer.parseInt(parts[1]);
            switch (parts[0]) {
                case "forward":
                    x += amount;
                    y += (aim * amount);
                    break;
                case "down":
                    aim += amount;
                    break;
                case "up":
                    aim -= amount;
                    break;
            }
        }
        return x * y;
    }
}
