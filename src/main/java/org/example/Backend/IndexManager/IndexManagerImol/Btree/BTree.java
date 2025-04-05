package org.example.Backend.IndexManager.IndexManagerImol.Btree;

public class BTree {
    private final NodeInserter nodeInserter = new NodeInserter(4);
    private final Node root = new Node();

    public void insert(Integer key, Integer value) {
        nodeInserter.insertNode(root, key, value);
    }
}
