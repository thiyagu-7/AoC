package com.thiyagu_7.adventofcode.year2021.day18;

import java.util.ArrayList;
import java.util.List;

public class SnailfishNumber {
    private final TreeNode root = new TreeNode();

    public SnailfishNumber(String number) {
        insert(root, number.toCharArray(), 0);
        setRegularNodePointers(root);
    }

    private int insert(TreeNode node, char[] arr, int i) {
        if (Character.isDigit(arr[i])) {
            node.value = arr[i] - '0';
            return i + 1;
        }
        if (arr[i] == '[') {
            node.leftChild = new TreeNode();
            i = insert(node.leftChild, arr, i + 1);
        }
        // arr[i] will be ','
        node.rightChild = new TreeNode();
        i = insert(node.rightChild, arr, i + 1);
        // arr[i] will be ]
        return i + 1;
    }

    private void setRegularNodePointers(TreeNode node) {
        List<TreeNode> nodes = new ArrayList<>();
        traverseInOrder(node, nodes);

        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).leftRegularNode = i - 1 >= 0 ? nodes.get(i - 1) : null;
            nodes.get(i).rightRegularNode = i + 1 < nodes.size() ? nodes.get(i + 1) : null;
        }
    }

    private void traverseInOrder(TreeNode node, List<TreeNode> nodes) {
        if (node == null) {
            return;
        }
        if (node.value != null) {
            nodes.add(node);
            return;
        }
        traverseInOrder(node.leftChild, nodes);
        traverseInOrder(node.rightChild, nodes);
    }

    public void reduce() {
        boolean splitHappened;
        do {
            explode(root, 1);
            SplitResult splitResult = split(root);
            splitHappened = splitResult.splitHappened;
        } while (splitHappened);
    }

    private TreeNode explode(TreeNode node, int level) {
        if (node == null) {
            return null;
        }
        // explode happens for pairs with primitive values
        if (level == 5 && node.leftChild != null && node.leftChild.value != null
                && node.rightChild != null && node.rightChild.value != null) {
            if (node.leftChild.leftRegularNode != null) {
                node.leftChild.leftRegularNode.value += node.leftChild.value;
            }
            if (node.rightChild.rightRegularNode != null) {
                node.rightChild.rightRegularNode.value += node.rightChild.value;
            }
            TreeNode newNode = new TreeNode();
            newNode.value = 0;
            newNode.leftRegularNode = node.leftChild.leftRegularNode;
            newNode.rightRegularNode = node.rightChild.rightRegularNode;

            if (node.leftChild.leftRegularNode != null) {
                node.leftChild.leftRegularNode.rightRegularNode = newNode;
            }
            if (node.rightChild.rightRegularNode != null) {
                node.rightChild.rightRegularNode.leftRegularNode = newNode;
            }
            return newNode;
        }
        node.leftChild = explode(node.leftChild, level + 1);
        node.rightChild = explode(node.rightChild, level + 1);

        return node;
    }

    private SplitResult split(TreeNode node) {
        if (node == null) {
            return new SplitResult(null, false);
        }
        if (node.value != null && node.value >= 10) {
            TreeNode newNode = new TreeNode();
            newNode.value = null; // not a leaf (could have left this as null is the default)

            TreeNode left = new TreeNode();
            left.value = node.value / 2;

            TreeNode right = new TreeNode();
            right.value = (node.value + 1) / 2;

            //set left and right for new children
            left.leftRegularNode = node.leftRegularNode;
            left.rightRegularNode = right;
            if (left.leftRegularNode != null) {
                left.leftRegularNode.rightRegularNode = left;
            }

            right.leftRegularNode = left;
            right.rightRegularNode = node.rightRegularNode;
            if (right.rightRegularNode != null) {
                node.rightRegularNode.leftRegularNode = right;
            }

            newNode.leftChild = left;
            newNode.rightChild = right;
            return new SplitResult(newNode, true);
        }

        SplitResult leftSplitResult = split(node.leftChild);
        node.leftChild = leftSplitResult.node;
        if (leftSplitResult.splitHappened) {
            return new SplitResult(node, leftSplitResult.splitHappened); //true
        }
        SplitResult rightSplitResult = split(node.rightChild);
        node.rightChild = rightSplitResult.node;
        return new SplitResult(node, rightSplitResult.splitHappened);
    }

    public String getSnailfishNumber() {
        return inorder(root);
    }

    private String inorder(TreeNode node) {
        if (node == null) {
            return "";
        }
        if (node.value != null) {
            return String.valueOf(node.value);
        }
        return "[" + inorder(node.leftChild) + "," + inorder(node.rightChild) + "]";
    }

    public long findMagnitude() {
        return findMagnitude(root);
    }

    private long findMagnitude(TreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node.value != null) {
            return node.value;
        }
        return 3 * findMagnitude(node.leftChild) +
                2 * findMagnitude(node.rightChild);
    }

    public static class TreeNode {
        private Integer value;
        private TreeNode leftChild;
        private TreeNode rightChild;
        // set only for primitive nodes
        private TreeNode leftRegularNode;
        private TreeNode rightRegularNode;
    }

    private static class SplitResult {
        private final TreeNode node;
        private final boolean splitHappened;

        private SplitResult(TreeNode node, boolean splitHappened) {
            this.node = node;
            this.splitHappened = splitHappened;
        }
    }
}
