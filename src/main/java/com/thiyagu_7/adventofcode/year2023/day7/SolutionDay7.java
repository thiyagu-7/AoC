package com.thiyagu_7.adventofcode.year2023.day7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class SolutionDay7 {
    public int part1(List<String> input) {
        return process(input, new HandAndBidComparatorForPart1());
    }

    public int part2(List<String> input) {
       return process(input, new HandAndBidComparatorForPart2());
    }
    private int process(List<String> input, Comparator<HandAndBid> comparator) {
        List<HandAndBid> handAndBids = parseInput(input);
        List<HandAndBid> sortedHandAndBids = handAndBids.stream()
                .sorted(comparator)
                .toList();
        return IntStream.range(0, sortedHandAndBids.size())
                .map(i -> (i + 1) * sortedHandAndBids.get(i).bid)
                .sum();
    }

    private static class HandAndBidComparatorForPart1 implements Comparator<HandAndBid> {
        @Override
        public int compare(HandAndBid o1, HandAndBid o2) {
            // from weakest to strongest
            List<List<Integer>> typeBasedOrder = List.of(
                    List.of(1, 1, 1, 1, 1),
                    List.of(2, 1, 1, 1),
                    List.of(2, 2, 1),
                    List.of(3, 1, 1),
                    List.of(3, 2),
                    List.of(4, 1),
                    List.of(5)
            );
            List<Character> cardLabelBasedOrder = new ArrayList<>(
                    List.of('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'));
            //reverse to get from weakest to strongest
            Collections.reverse(cardLabelBasedOrder);

            List<Integer> card1TypeInfo = getTypeInfoForPart1(o1.hand);
            List<Integer> card2TypeInfo = getTypeInfoForPart1(o2.hand);
            // compare by type
            int r = Integer.compare(
                    typeBasedOrder.indexOf(card1TypeInfo),
                    typeBasedOrder.indexOf(card2TypeInfo)
            );
            if (r != 0) {
                return r;
            }
            // compare each card to break ties
            for (int i = 0, j = 0; i < o1.hand.length(); i++, j++) {
                char c1 = o1.hand.charAt(i);
                char c2 = o2.hand.charAt(j);
                if (c1 != c2) {
                    return Integer.compare(
                            cardLabelBasedOrder.indexOf(c1),
                            cardLabelBasedOrder.indexOf(c2)
                    );
                }
            }
            // both hands are equal
            return 0;
        }
    }

    private static List<Integer> getTypeInfoForPart1(String cardHand) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : cardHand.toCharArray()) {
            map.merge(c, 1, (o, n) -> o + 1);
        }
        List<Integer> cardinality = new ArrayList<>(map.values());
        cardinality.sort(Comparator.reverseOrder());
        return cardinality;
    }

    private List<HandAndBid> parseInput(List<String> input) {
        return input.stream()
                .map(line -> line.split(" "))
                .map(p -> new HandAndBid(p[0], Integer.parseInt(p[1])))
                .toList();
    }

    private static class HandAndBidComparatorForPart2 implements Comparator<HandAndBid> {

        @Override
        public int compare(HandAndBid o1, HandAndBid o2) {
            List<List<Integer>> typeBasedOrder = List.of(
                    List.of(1, 1, 1, 1, 1),
                    List.of(2, 1, 1, 1),
                    List.of(2, 2, 1),
                    List.of(3, 1, 1),
                    List.of(3, 2),
                    List.of(4, 1),
                    List.of(5)
            );
            //'J' moved to the end
            List<Character> cardLabelBasedOrder = new ArrayList<>(
                    List.of('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'));
            Collections.reverse(cardLabelBasedOrder);

            List<Integer> card1TypeInfo = getTypeInfoForPart2(o1.hand);
            List<Integer> card2TypeInfo = getTypeInfoForPart2(o2.hand);

            int r = Integer.compare(
                    typeBasedOrder.indexOf(card1TypeInfo),
                    typeBasedOrder.indexOf(card2TypeInfo)
            );
            if (r != 0) {
                return r;
            }
            for (int i = 0, j = 0; i < o1.hand.length(); i++, j++) {
                char c1 = o1.hand.charAt(i);
                char c2 = o2.hand.charAt(j);
                if (c1 != c2) {
                    return Integer.compare(
                            cardLabelBasedOrder.indexOf(c1),
                            cardLabelBasedOrder.indexOf(c2)
                    );
                }
            }
            return 0;
        }
    }
    private static List<Integer> getTypeInfoForPart2(String cardHand) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : cardHand.toCharArray()) {
            map.merge(c, 1, (o, n) -> o + 1);
        }
        if (map.containsKey('J')) {
            int noOfJoker = map.get('J');
            //find the highest non-joker card
            Optional<Map.Entry<Character, Integer>> optionalEntry = map.entrySet()
                    .stream()
                    .filter(e -> e.getKey() != 'J')
                    .max(Map.Entry.comparingByValue());
            if (optionalEntry.isPresent()) {
                //make the joker card as the highest non-joker card to make the hand the strongest type possible
                map.compute(optionalEntry.get().getKey(), (k, v) -> v + noOfJoker);
                //remove J card
                map.remove('J');
            }
        }
        List<Integer> cardinality = new ArrayList<>(map.values());
        cardinality.sort(Comparator.reverseOrder());
        return cardinality;
    }

    record HandAndBid(String hand, int bid) {

    }
}
