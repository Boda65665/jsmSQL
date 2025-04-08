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
        Node nodeWithEnoughKeysOnLeftBrother = getNodeWithEnoughKeysOnLeftBrother();

        Node nodeWithEnoughKeysOnRightBrother = getNodeWithEnoughKeysOnRightBrother();

        int deleteKey = 70;
        return Stream.of(
                Arguments.of(nodeWithEnoughKeysOnLeftBrother, deleteKey,
                        getExpectedResultWhenDeletingAndBorrowingFromLeftBrother()),
                Arguments.of(nodeWithEnoughKeysOnRightBrother,deleteKey,
                        getExpectedResultWhenDeletingAndBorrowingFromRightBrother())
        );
    }

    private static Node getNodeWithEnoughKeysOnLeftBrother() {
        Node node = testHelper.generateNodeForDelete();
        insertKeyInLeftBrother(node);
        return node;
    }

    private static void insertKeyInLeftBrother(Node node) {
        inserter.insertNode(node, 51, -1);
    }

    private static Node getNodeWithEnoughKeysOnRightBrother() {
        Node node = testHelper.generateNodeForDelete();
        insertKeyInRightBrother(node);
        return node;
    }

    private static void insertKeyInRightBrother(Node node) {
        inserter.insertNode(node, 91, -1);
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

    private static String getExpectedResultWhenDeletingAndBorrowingFromRightBrother(){
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

    @ParameterizedTest
    @MethodSource("caseForRemoveFromLeafWithCombine")
    @DisplayName("remove key with balancing due to combination")
    void removeFromLeafWithCombine(Node node, int deleteKey, String expected) {
        nodeRemover.remove(node, deleteKey);
        assertEquals(expected, node.printTree());
    }

    public static Stream<Arguments> caseForRemoveFromLeafWithCombine() {
        return Stream.of(
            Arguments.of(testHelper.generateNodeForDelete(), 70, getExpectedResultFromCombineParentWithLeftBrother()),
            Arguments.of(testHelper.generateNodeForDelete(), 50, getExpectedResultFromCombineParentWithRightBrother())
        );
    }

    private static String getExpectedResultFromCombineParentWithLeftBrother() {
        return """
                Level 0: 40\s
                Level 1: 20\s
                Level 2: 10\s
                Level 2: 30\s
                Level 1: 80 100\s
                Level 2: 50 60\s
                Level 2: 90\s
                Level 2: 110 120\s
                """;
    }

    private static String getExpectedResultFromCombineParentWithRightBrother() {
        return """
                Level 0: 40\s
                Level 1: 20\s
                Level 2: 10\s
                Level 2: 30\s
                Level 1: 80 100\s
                Level 2: 60 70\s
                Level 2: 90\s
                Level 2: 110 120\s
                """;
    }

    @ParameterizedTest
    @MethodSource("caseForRemoveFromNodeInternalWithReplacePredecessor")
    @DisplayName("remove key from node internal with balance with the help replace predecessor")
    public void removeFromNodeInternalWithReplacePredecessor(Node node, int deleteKey, String expected) {
        nodeRemover.remove(node, deleteKey);

        assertEquals(expected, node.printTree());
    }


    public static Stream<Arguments> caseForRemoveFromNodeInternalWithReplacePredecessor() {
        int deleteKey = 20;

        return Stream.of(
            Arguments.of(getNodeForReplaceOnPredecessor(), deleteKey, getExceptedResultForReplaceOnPredecessor()),
            Arguments.of(getNodeForReplaceOnSuccessor(), deleteKey, getExceptedResultForReplaceOnSuccessor())
        );
    }

    private static Node getNodeForReplaceOnPredecessor() {
        Node node = testHelper.generateNodeForDelete();
        inserter.insertNode(node, 15, -1);
        return node;
    }

    private static String getExceptedResultForReplaceOnPredecessor() {
        return """
                Level 0: 40\s
                Level 1: 15\s
                Level 2: 10\s
                Level 2: 30\s
                Level 1: 60 80 100\s
                Level 2: 50\s
                Level 2: 70\s
                Level 2: 90\s
                Level 2: 110 120\s
                """;
    }

    private static Object getNodeForReplaceOnSuccessor() {
        Node node = testHelper.generateNodeForDelete();
        inserter.insertNode(node, 31, -1);
        return node;
    }

    private static String getExceptedResultForReplaceOnSuccessor() {
        return """
                Level 0: 40\s
                Level 1: 30\s
                Level 2: 10\s
                Level 2: 31\s
                Level 1: 60 80 100\s
                Level 2: 50\s
                Level 2: 70\s
                Level 2: 90\s
                Level 2: 110 120\s
                """;
    }

    @Test
    @DisplayName("remove key from node internal with balance with the help combine child")
    public void removeFromNodeInternalWithCombineChild() {
        Node node = generateTestDataForRemoveFromNodeInternalWithCombineChild();
        nodeRemover.remove(node, 20);
        assertEquals(getExceptedResultForCombinePredecessor(), node.printTree());
    }

    private Node generateTestDataForRemoveFromNodeInternalWithCombineChild() {
        Node node = testHelper.generateNodeForDelete();
        testHelper.insertToNode(20, 3,1, node);
        return node;
    }

    private static String getExceptedResultForCombinePredecessor() {
        return """
                Level 0: 40\s
                Level 1: 22\s
                Level 2: 10 21\s
                Level 2: 23 30\s
                Level 1: 60 80 100\s
                Level 2: 50\s
                Level 2: 70\s
                Level 2: 90\s
                Level 2: 110 120\s
                """;
    }
}