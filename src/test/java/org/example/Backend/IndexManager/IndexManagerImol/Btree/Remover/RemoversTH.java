package org.example.Backend.IndexManager.IndexManagerImol.Btree.Remover;

import org.example.Backend.IndexManager.IndexManagerImol.Btree.Node;
import org.example.Backend.IndexManager.IndexManagerImol.Btree.NodeInserter;

public class RemoversTH {
    private final NodeInserter nodeInserter = new NodeInserter(4);

    public Node generateNodeForDeleteFromLeaf(){
        Node node = new Node();
        insertToNode(12, 10, node);
        return node;
    }

    private void insertToNode(int count, int step, Node node) {
        for (int i = 1; i <= count; i++) {
            nodeInserter.insertNode(node, i * step, -1);
        }
    }
}
