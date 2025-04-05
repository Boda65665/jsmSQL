package org.example.Backend.IndexManager.IndexManagerImol.Btree;

public class NodeGetter {
    public Integer get(Node root, int key) {
        if (root == null) return null;
        Node desiredNode = searchNode(root,key);
        if (desiredNode == null) return null;

        return getValue(desiredNode, key);
    }

    public Node searchNode(Node node, int key) {
        int indexChild = node.sizeKeyValuePair();
        for (int i = 0; i < node.sizeKeyValuePair(); i++) {
            if (node.getKey(i) == key) return node;
            if (node.getKey(i) > key) {
                indexChild = i;
                break;
            }
        }

        if (node.isLeaf()) return null;
        return searchNode(node.getChild(indexChild), key);
    }

    private int getValue(Node desiredNode, int key) {
        int value = 0;
        for (int i = 0; i < desiredNode.sizeKeyValuePair(); i++) {
            if (desiredNode.getKey(i) == key) value = desiredNode.getValue(i);
        }
        return value;
    }


}
