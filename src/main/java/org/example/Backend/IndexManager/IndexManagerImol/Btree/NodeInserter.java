package org.example.Backend.IndexManager.IndexManagerImol.Btree;

import java.util.ArrayList;
import java.util.List;

public class NodeInserter {
    private final int treeOrder;
    private Node root;

    public NodeInserter(int treeOrder) {
        this.treeOrder = treeOrder;
    }

    public void insertNode(Node root, int key, int value) {
        valid(root, key);
        this.root = root;

        Node leaf = getLeaf(root, key);
        insert(leaf, key, value);

        balanceIfRequired(key);

    }

    private void valid(Node root, int key) {
        if (root == null) throw new NullPointerException("root is null");
        if(key < 0) throw new IllegalArgumentException("key is negative");
    }

    public Node getLeaf(Node node, int key) {
        Node current = node;
        if (current.isEmptyChild()) return current;

        while (!current.isLeaf()) {
            boolean found = false;
            for (int i = 0; i < current.sizeKeyValuePair(); i++) {
                if (current.getKey(i) > key) {
                    current = current.getChild(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                current = current.getChild(current.sizeKeyValuePair());
            }
        }
        return current;
    }

    private void insert(Node leaf, int key, int value) {
        leaf.addKeyValuePair(new KeyValuePair(key, value));
    }

    private void balanceIfRequired(int key) {
        Node leaf = getLeaf(root, key);

        if (isRequiredBalancing(leaf)) balance(leaf);
    }

    private boolean isRequiredBalancing(Node node) {
        return node.sizeKeyValuePair() == treeOrder;
    }

    private void balance(Node node) {
        liftUp(node);
    }

    private void liftUp(Node node) {
        if (isRoot(node)) liftUpRoot(node);
        else {
            liftUpNotRoot(node);

            Node parent = node.getParent();
            if (isRequiredBalancing(parent)) {
                liftUp(parent);
            }
        }
    }

    private boolean isRoot(Node node) {
        return node.getParent() == null;
    }

    private void liftUpRoot(Node node) {
        List<Node> children = getNewChildren(node, root);
        KeyValuePair keyValuePair = node.getKeyValuePair(getMiddleIndexNode(node.sizeKeyValuePair()));

        updateParentInChildren(node, children);
        root.clear();
        root.addKeyValuePair(keyValuePair);
        root.addAllChildren(children);
    }

    private List<Node> getNewChildren(Node node, Node parent) {
        List<Node> children = new ArrayList<>();
        children.add(getFirstChild(node, parent));
        children.add(getSecondChild(node, parent));
        return children;
    }

    private Node getFirstChild(Node node, Node parent) {
        List<KeyValuePair> keyValuePairsFirstChildren = node.sliceKeyValuePairs(0, getMiddleIndexNode(node.sizeKeyValuePair()));

        List<Node> childrenFirstChild = new ArrayList<>();
        if (node.sizeChild() > 0) {
            childrenFirstChild = node.sliceChildren(0, getMiddleIndexNode(node.sizeChild()) + 1);
        }

        return new Node(keyValuePairsFirstChildren, childrenFirstChild, parent);
    }

    private int getMiddleIndexNode(int size) {
        return (int) Math.floor((double) size / 2) - 1;
    }

    private Node getSecondChild(Node node, Node parent) {
        List<KeyValuePair> keyValuePairsSecondChildren = node.sliceKeyValuePairs(getMiddleIndexNode(node.sizeKeyValuePair()) + 1,
                node.sizeKeyValuePair());
        List<Node> childrenSecondChild = new ArrayList<>();
        if (node.sizeChild() > 1) {
            childrenSecondChild = node.sliceChildren(getMiddleIndexNode(node.sizeChild()) + 1, node.sizeChild());
        }
        return new Node(keyValuePairsSecondChildren, childrenSecondChild, parent);
    }

    private void updateParentInChildren(Node node, List<Node> children) {
        Node childrenFirst = children.get(0);
        Node childrenSecond = children.get(1);
        for (int i = 0; i < node.sizeChild(); i++) {
            Node child = node.getChild(i);
            if (i <= getMiddleIndexNode(node.sizeChild())){
                child.setParent(childrenFirst);
            }
            else {
                child.setParent(childrenSecond);
            }
        }
    }

    private void liftUpNotRoot(Node node) {
        Node parent = node.getParent();
        KeyValuePair keyValuePair = node.getKeyValuePair(getMiddleIndexNode(node.sizeKeyValuePair()));
        List<Node> children = getNewChildren(node, parent);

        updateParentInChildren(node, children);
        parent.addKeyValuePair(keyValuePair);
        parent.deleteChild(node.getKey(0));
        parent.addAllChildren(children);
    }
}