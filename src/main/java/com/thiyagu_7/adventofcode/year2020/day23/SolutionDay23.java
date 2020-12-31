package com.thiyagu_7.adventofcode.year2020.day23;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SolutionDay23 {
    public String part1(List<Integer> input, int numMoves) {
        List<Integer> list = new LinkedList<>(input);
        int len = input.size();
        int currentCupIndex = 0;
        for (int i = 0; i < numMoves; i++) {
            int currentCup = list.get(currentCupIndex);

            System.out.println(list);
            int nextCupIndex = (currentCupIndex + 4) % len; //considering next 3 to be removed
            int nextCupValue = list.get(nextCupIndex);

            List<Integer> removed = removeAndGetNext3Cups(list, currentCupIndex);
            int destCupIndex = getDestinationIndex(list, removed, currentCup);
            list.addAll(destCupIndex, removed);
            System.out.println(removed + " " + destCupIndex);

            //such that next cup must be current cup index + 1 (note: removal has taken place)
            list = reorder(list, (currentCupIndex + 1) % len, nextCupValue);
            currentCupIndex = (currentCupIndex + 1) % len;

            if (i == 9) {
                System.out.println("---" + list);
            }
        }
        System.out.println(list);

        StringBuilder res = new StringBuilder();
        int index = (list.indexOf(1) + 1) % len;
        for (int i = 0; i < len - 1; i++) {
            res.append(list.get(index));
            index = (index + 1) % len;
        }
        return res.toString();
    }

    //pivot such that destCupValue is at destCupIndex
    //eg, see b/w moves 3 and 4
    private List<Integer> reorder(List<Integer> list, int nextCupIndex, int nextCupValue) {
        int len = list.size();
        List<Integer> newList = new LinkedList<>(list);

        int currListIndex = list.indexOf(nextCupValue);
        int newListIndex = nextCupIndex;
        for (int i = 0; i < len; i++) {
            newList.set(newListIndex, list.get(currListIndex));
            currListIndex = (currListIndex + 1) % len;
            newListIndex = (newListIndex + 1) % len;
        }
        return newList;
    }

    private List<Integer> removeAndGetNext3Cups(List<Integer> list, int currentIndex) {
        int len = list.size();
        currentIndex = (currentIndex + 1) % len;
        List<Integer> removed = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            removed.add(list.get(currentIndex));
            currentIndex = (currentIndex + 1) % len;
        }
        list.removeAll(removed); //each num appears once in input
        return removed;
    }

    private int getDestinationIndex(List<Integer> list, List<Integer> removed, int currentCup) {
        int destination = currentCup - 1;
        while (destination == 0 || removed.contains(destination)) {
            if (destination == 0) {
                destination = 9;
            } else {
                destination--;
            }
        }
        return list.indexOf(destination) + 1;
    }
}
