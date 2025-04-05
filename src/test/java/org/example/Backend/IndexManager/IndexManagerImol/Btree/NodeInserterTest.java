package org.example.Backend.IndexManager.IndexManagerImol.Btree;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NodeInserterTest {
    private final NodeInserter nodeInserter = new NodeInserter(4);

    @Test
    void insert() {
        Node node = generateTestData();
        String exceptedResult = getExceptedResult();

        assertEquals(exceptedResult, node.printTree());
    }

    private Node generateTestData() {
        Node node = new Node();
        nodeInserter.insertNode(node, 4, 4);
        nodeInserter.insertNode(node, 5, 5);
        nodeInserter.insertNode(node, 6, 6);
        nodeInserter.insertNode(node, 7, 7);
        nodeInserter.insertNode(node, 8, 8);
        nodeInserter.insertNode(node, 9, 9);
        nodeInserter.insertNode(node, 1, 1);
        nodeInserter.insertNode(node, 2, 2);
        nodeInserter.insertNode(node, 3, 3);
        nodeInserter.insertNode(node, 15, 15);
        nodeInserter.insertNode(node, 22, 22);
        nodeInserter.insertNode(node, 10, 10);
        nodeInserter.insertNode(node, 11, 11);
        nodeInserter.insertNode(node, 14, 14);
        nodeInserter.insertNode(node, 33, 33);
        nodeInserter.insertNode(node, 77, 77);
        nodeInserter.insertNode(node, 66, 66);
        return node;
    }

    private String getExceptedResult() {
        return """
                Level 0: 5 9\s
                Level 1: 2\s
                Level 2: 1\s
                Level 2: 3 4\s
                Level 1: 7\s
                Level 2: 6\s
                Level 2: 8\s
                Level 1: 11 15 33\s
                Level 2: 10\s
                Level 2: 14\s
                Level 2: 22\s
                Level 2: 66 77\s
                """;
    }

    @Test
    void validInsert() {
        Node node = new Node();
        assertThrows(NullPointerException.class, () -> nodeInserter.insertNode(null, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> nodeInserter.insertNode(node, -1, 0));
    }
}