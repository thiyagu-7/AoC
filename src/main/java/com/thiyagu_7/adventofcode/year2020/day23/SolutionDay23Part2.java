package com.thiyagu_7.adventofcode.year2020.day23;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionDay23Part2 {

    public long part2(List<Integer> input, int numMoves, int max) {
        LinkedListHelper.Node current;
        LinkedListHelper linkedListHelper = new LinkedListHelper();
        input.forEach(linkedListHelper::insert);
        current = linkedListHelper.head;

        Map<Integer, LinkedListHelper.Node> valToNodeMap = new HashMap<>();
        LinkedListHelper.Node t = current;
        do {
            valToNodeMap.put(t.val, t);
            t = t.next;

        } while (t != linkedListHelper.head);

        for (int i = 0; i < numMoves; i++) {
            List<Integer> removed = Arrays.asList(current.next.val,
                    current.next.next.val,
                    current.next.next.next.val);
            LinkedListHelper.Node newNext = current.next.next.next.next;
            LinkedListHelper.Node destNode = getDestinationIndex(valToNodeMap, removed, current.val, max);

            //insert at dest
            LinkedListHelper.Node destNext = destNode.next;
            destNode.next = current.next;
            current.next.next.next.next = destNext;

            //update current
            current.next = newNext;
            current = newNext;
        }

        LinkedListHelper.Node one = valToNodeMap.get(1);
        System.out.println(one.next.val);
        System.out.println(one.next.next.val);
        return (long) one.next.val * one.next.next.val;
    }

    private LinkedListHelper.Node getDestinationIndex(Map<Integer, LinkedListHelper.Node> valToNodeMap,
                                                      List<Integer> removed,
                                                      int currentCup,
                                                      int max) {
        int destination = currentCup - 1;
        while (destination == 0 || removed.contains(destination)) {
            if (destination == 0) {
                destination = max;
            } else {
                destination--;
            }
        }
        return valToNodeMap.get(destination);
    }

    public static class LinkedListHelper {
        private Node head;
        private Node tail;


        void insert(int val) {
            if (head == null) {
                head = tail = new Node(val);
                tail.next = head;
            } else {
                Node node = new Node(val);
                tail.next = node;
                tail = node;
                tail.next = head;
            }
        }

        private static class Node {
            private int val;
            private Node next;

            private Node(int val) {
                this.val = val;
            }
        }
    }
}
