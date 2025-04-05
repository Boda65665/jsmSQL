package org.example.Backend.IndexManager.IndexManagerImol.Btree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NodeGetterTest {
    private final NodeInserter nodeInserter = new NodeInserter(4);
    private final NodeGetter nodeGetter = new NodeGetter();

    @ParameterizedTest
    @MethodSource("caseForGet")
    void get(int key, Integer exceptedValue) {
        Node node = generateTestData();
        assertEquals(exceptedValue, nodeGetter.get(node, key));
    }

    public static Stream<Arguments> caseForGet() {
        return Stream.of(
                Arguments.of(4,1),
                Arguments.of(5,2),
                Arguments.of(6,3),
                Arguments.of(7,4),
                Arguments.of(8,5),
                Arguments.of(9,6),
                Arguments.of(1,7),
                Arguments.of(2,8),
                Arguments.of(3,9),
                Arguments.of(123, null)
        );
    }

    private Node generateTestData() {
        Node node = new Node();
        nodeInserter.insertNode(node, 4, 1);
        nodeInserter.insertNode(node, 5, 2);
        nodeInserter.insertNode(node, 6, 3);
        nodeInserter.insertNode(node, 7, 4);
        nodeInserter.insertNode(node, 8, 5);
        nodeInserter.insertNode(node, 9, 6);
        nodeInserter.insertNode(node, 1, 7);
        nodeInserter.insertNode(node, 2, 8);
        nodeInserter.insertNode(node, 3, 9);
        return node;
    }

    @Test
    void getFromNullNode() {
        assertEquals(null, nodeGetter.get(null, 0));
    }
}