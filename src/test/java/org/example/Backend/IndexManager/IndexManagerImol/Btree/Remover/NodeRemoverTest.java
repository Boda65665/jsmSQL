package org.example.Backend.IndexManager.IndexManagerImol.Btree.Remover;

import org.example.Backend.IndexManager.IndexManagerImol.Btree.Node;
import org.example.Backend.IndexManager.IndexManagerImol.Btree.NodeInserter;
import org.example.Backend.IndexManager.IndexManagerImol.Btree.NodeRemover;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeRemoverTest {
    private final NodeRemover nodeRemover = new NodeRemover(4);
    private static final NodeInserter inserter = new NodeInserter(4);
    private static final RemoversTH testHelper = new RemoversTH();

    @ParameterizedTest
    @MethodSource("caseForRemoveFromLeafWithBrothersBorrow")
    @DisplayName("removing a key from a list with the condition of being able to borrow keys from a brother or sister")
    void removeFromLeafWithBrothersBorrow(Node node, int deleteKey, String expected) {
        nodeRemover.remove(node, deleteKey);
        assertEquals(expected, node.printTree());
    }

    public static Stream<Arguments> caseForRemoveFromLeafWithBrothersBorrow() {
        Node nodeWithEnoughKeysOnLeftBrother = testHelper.generateNodeForDeleteFromLeaf();
        insertKeyInLeftBrother(nodeWithEnoughKeysOnLeftBrother);

        Node nodeWithEnoughKeysOnRightBrother = testHelper.generateNodeForDeleteFromLeaf();
        insertKeyInRightBrother(nodeWithEnoughKeysOnRightBrother);

        int deleteKey = 70;
        return Stream.of(
                Arguments.of(nodeWithEnoughKeysOnLeftBrother, deleteKey,
                        getExpectedResultWhenDeletingAndBorrowingFromLeftBrother()),
                Arguments.of(nodeWithEnoughKeysOnRightBrother,deleteKey,
                        getExpectedResultWhenDeletingAndBorrowingFromRightBrother())
        );
    }

    private static void insertKeyInLeftBrother(Node leftBrother) {
        inserter.insertNode(leftBrother, 51, -1);
    }

    private static void insertKeyInRightBrother(Node rightBrother) {
        inserter.insertNode(rightBrother, 91, -1);
    }

    private static String getExpectedResultWhenDeletingAndBorrowingFromLeftBrother() {
        return """
                Level 0: 40\s
                Level 1: 20\s
                Level 2: 10\s
                Level 2: 30\s
                Level 1: 51 80 100\s
                Level 2: 50\s
                Level 2: 60\s
                Level 2: 90\s
                Level 2: 110 120\s
                """;
    }

    private static String getExpectedResultWhenDeletingAndBorrowingFromRightBrother() {
        return """
                Level 0: 40\s
                Level 1: 20\s
                Level 2: 10\s
                Level 2: 30\s
                Level 1: 60 90 100\s
                Level 2: 50\s
                Level 2: 80\s
                Level 2: 91\s
                Level 2: 110 120\s
                """;
    }
}