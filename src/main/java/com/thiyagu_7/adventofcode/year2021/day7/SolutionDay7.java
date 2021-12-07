package com.thiyagu_7.adventofcode.year2021.day7;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class SolutionDay7 {

    public int part1(List<Integer> input) {
        input.sort(Comparator.naturalOrder());
        int size = input.size();
        // median
        int position = size % 2 == 1 ? input.get(size / 2)
                : (input.get(size / 2) + input.get((size - 1) / 2)) / 2;
        return findDistance(input, position, Function.identity());
    }

    public int part2(List<Integer> input) {
        double average = input.stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0);
        Function<Integer, Integer> cost = dist -> (dist * (dist + 1)) / 2;

        // for sample data set - 'ceil' worked and for actual data set 'floor' worked
        return Math.min(
                findDistance(input, (int) Math.floor(average), cost),
                findDistance(input, (int) Math.ceil(average), cost)
        );
    }

    private int findDistance(List<Integer> input, int targetPosition, Function<Integer, Integer> computeCost) {
        int fuel = 0;
        for (int val : input) {
            int dist = Math.abs(val - targetPosition);
            fuel += computeCost.apply(dist);
        }
        return fuel;
    }
    /*
    Discussions on part 2:
    The minimum is actually guaranteed to lie within +-0.5 of the mean.
    https://www.reddit.com/r/adventofcode/comments/rav728/2021_day7_can_part2_be_done_in_a_smart_way/
    https://www.reddit.com/r/adventofcode/comments/rawxad/2021_day_7_part_2_i_wrote_a_paper_on_todays/
    https://www.reddit.com/r/adventofcode/comments/rars4g/2021_day_7_why_do_these_values_work_spoilers/
     */
}
