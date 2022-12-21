package com.thiyagu_7.adventofcode.year2022.day20;

import java.util.LinkedList;
import java.util.List;

public class SolutionDay20 {
    public int part1(List<Integer> input) {
        List<ValueNode> originalList = input.stream()
                .map(ValueNode::new)
                .toList();
        return (int) mix(originalList, new LinkedList<>(originalList));
    }

    public long part2(List<Integer> input) {
        List<ValueNode> originalList = input.stream()
                .map(e -> e * 811589153L)
                .map(ValueNode::new)
                .toList();
        List<ValueNode> currentList = new LinkedList<>(originalList);
        long result = 0;
        for (int i = 0; i < 10; i++) {
            //currentList will be modified inside mix. Use it for subsequent runs
            result = mix(originalList, currentList);
        }
        return result;
    }

    //This class cannot be made into a Record as we need reference equality for dealing with duplicate numbers
    private static final class ValueNode {
        private final long value;

        private ValueNode(long value) {
            this.value = value;
        }

        public long value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private long mix(List<ValueNode> originalList, List<ValueNode> currentList) {
        int idx, indexToAdd;
        ValueNode zeroNode = null;
        for (ValueNode curr : originalList) {
            idx = currentList.indexOf(curr);
            if (curr.value == 0) {
                zeroNode = curr;
                continue;
            }
            currentList.remove(idx);

            if (curr.value > 0) {
                indexToAdd = (int) ((idx + curr.value) % currentList.size());
                currentList.add(indexToAdd, curr);
            } else {
                indexToAdd = (int) (Math.abs(curr.value) % currentList.size());
                indexToAdd = (currentList.size() + idx - indexToAdd) % currentList.size();
                if (indexToAdd == 0) {
                    indexToAdd = currentList.size();
                }
                currentList.add(indexToAdd, curr);
            }
        }
        idx = currentList.indexOf(zeroNode);
        int size = currentList.size();
        return currentList.get((idx + 1000) % size).value
                + currentList.get((idx + 2000) % size).value
                + currentList.get((idx + 3000) % size).value;
    }
}
