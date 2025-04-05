package org.example.Backend.IndexManager.IndexManagerImol.Btree;

import lombok.Getter;
import lombok.Setter;
import java.util.LinkedList;
import java.util.List;

public class Node {
    @Getter
    @Setter
    private Node parent;
    private LinkedList<KeyValuePair> keyValuePairs = new LinkedList<>();
    private LinkedList<Node> children = new LinkedList<>();

    public Node() {
    }

    public Node(List<KeyValuePair> keyValuePairs, List<Node> childrenFirstChild, Node parent) {
        addAllKeyValuePairs(keyValuePairs);
        addAllChildren(childrenFirstChild);
        this.parent = parent;
    }

    public void addAllKeyValuePairs(List<KeyValuePair> keyValuePairs) {
        for (KeyValuePair keyValuePair : keyValuePairs) {
            addKeyValuePair(keyValuePair);
        }
    }

    public void addKeyValuePair(KeyValuePair pair) {
        int insertIndex = getKeyValueInsertIndex(pair.getKey());
        keyValuePairs.add(insertIndex, pair);
    }

    private int getKeyValueInsertIndex(int smallerKey) {
        for (int i = 0; i < keyValuePairs.size(); i++) {
            if (keyValuePairs.get(i).getKey() > smallerKey) return i;
        }
        return keyValuePairs.size();
    }

    public void addAllChildren(List<Node> childrenFirstChild) {
        for (Node child : childrenFirstChild) {
            addChild(child);
        }
    }

    public void addChild(Node child) {
        int smallerKey = child.getKey(0);
        int insertIndex = getChildInsertIndex(smallerKey);
        children.add(insertIndex, child);
    }

    private int getChildInsertIndex(int smallerKey) {
        for (int i = 0; i < children.size() && i < keyValuePairs.size(); i++) {
            if (keyValuePairs.get(i).getKey() > smallerKey) return i;
        }
        return children.size();
    }

    public void deleteChild(int smallerKey) {
        int deleteIndex = getChildDeleteIndex(smallerKey);
        if (deleteIndex == -1) return;

        children.remove(deleteIndex);
    }

    private int getChildDeleteIndex(int smallerKey) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getKey(0) == smallerKey) return i;
        }
        return -1;
    }

    public int getKey(int index) {
        return keyValuePairs.get(index).getKey();
    }

    public int getValue(int index) {
        return keyValuePairs.get(index).getValue();
    }

    public KeyValuePair getKeyValuePair(int index) {
        return keyValuePairs.get(index);
    }

    public Node getChild(int index) {
        return children.get(index);
    }

    public List<Node> sliceChildren(int fromIndex, int toIndex) {
        return children.subList(fromIndex, toIndex);
    }

    public List<KeyValuePair> sliceKeyValuePairs(int fromIndex, int toIndex) {
        return keyValuePairs.subList(fromIndex, toIndex);
    }

    public int sizeKeyValuePair() {
        return keyValuePairs.size();
    }

    public int sizeChild() {
        return children.size();
    }

    public boolean isEmptyChild() {
        return children.isEmpty();
    }

    public boolean isLeaf(){
        return isEmptyChild();
    }

    public void clear() {
        keyValuePairs.clear();
        children.clear();
        parent = null;
    }

    public String printTree() {
        StringBuilder builder = new StringBuilder();

        return printTree(this, 0, builder);
    }

    private String printTree(Node node, int level, StringBuilder builder) {
        builder.append("Level ").append(level).append(": ");

        for (KeyValuePair pair : node.keyValuePairs) {
            builder.append(pair.getKey()).append(" ");
        }
        builder.append("\n");
        for (Node child : node.children) {
            printTree(child, level + 1, builder);
        }

        return builder.toString();
    }

    public void deleteKeyValuePairByKey(int key) {
        for (int i = 0; i < keyValuePairs.size(); i++) {
            if (keyValuePairs.get(i).getKey() == key) {
                keyValuePairs.remove(i);
            }
        }
    }

    public void deleteKeyValuePairByIndex(int index) {
        keyValuePairs.remove(index);
    }

    public void setKeyValuePair(int index, KeyValuePair keyPredecessor) {
        keyValuePairs.set(index, keyPredecessor);
    }
}