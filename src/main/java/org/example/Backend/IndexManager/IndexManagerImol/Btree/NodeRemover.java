package org.example.Backend.IndexManager.IndexManagerImol.Btree;

public class NodeRemover {
    private final NodeGetter nodeGetter = new NodeGetter();
    private final int treeOrder;

    public NodeRemover(int treeOrder) {
        this.treeOrder = treeOrder;
    }

    public void remove(Node node, int key) {
        valid(node, key);

        Node desiredNode = nodeGetter.searchNode(node, key);
        if (desiredNode == null) return;

        balanceIfRequired(desiredNode, key);
        desiredNode.deleteKeyValuePairByKey(key);
    }

    private void valid(Node node, int key) {
        if(node == null) throw new NullPointerException("node is null");
        if(key < 0) throw new IllegalArgumentException("key is negative");
    }

    private void balanceIfRequired(Node node, int key) {
        if (!isRequiredBalancing(node)) return;

        if (node.isLeaf()) balanceLeaf(node);
        else balanceNodeInternal(node, key);
    }

    private boolean isRequiredBalancing(Node node) {
        int sizeKeyAfterDelete = node.sizeKeyValuePair() - 1;
        return sizeKeyAfterDelete < getMinimSizeKey() || node.sizeChild() > sizeKeyAfterDelete;
    }

    public int getMinimSizeKey(){
        return (treeOrder/2)-1;
    }

    private void balanceLeaf(Node node) {
        if (canBorrowFromBrother(node)) borrowFromBrother(node);
        else if (canCombineParentWithChild(node))combineChildWithParent(node);
    }

    private boolean canBorrowFromBrother(Node node) {
        Node leftBrother = getLeftBrother(node);
        Node rightBrother = getRightBrother(node);

        return canBorrow(leftBrother) || canBorrow(rightBrother);
    }

    private Node getLeftBrother(Node node) {
        Node parent = node.getParent();
        int indexDeletedChild = getIndexNodeInParent(node);

        if (indexDeletedChild - 1 >= 0) return parent.getChild(indexDeletedChild - 1);
        return null;
    }

    private int getIndexNodeInParent(Node node) {
        Node parent = node.getParent();
        int key = node.getKey(0);

        for (int i = 0; i < parent.sizeChild(); i++) {
            if (parent.getChild(i).getKey(0) == key) return i;
        }
        return -1;
    }

    private Node getRightBrother(Node node) {
        Node parent = node.getParent();
        int indexDeletedChild = getIndexNodeInParent(node);

        if (indexDeletedChild + 1 < parent.sizeChild()) return parent.getChild(indexDeletedChild + 1);
        return null;
    }

    private boolean canBorrow(Node node) {
        return node!=null && node.sizeKeyValuePair() > getMinimSizeKey();
    }

    private void borrowFromBrother(Node node) {
        int indexDeletedChild = getIndexNodeInParent(node);
        Node parent = node.getParent();
        Node donorBrother = getDonorBrotherForBorrow(node);
        KeyValuePair brotherDonorKeyValuePair = getBrotherDonorKeyValuePairForBorrow(node);
        Integer indexParentKeyReplacement = getIndexParentKeyReplacementForBorrow(node, indexDeletedChild);
        KeyValuePair parentDonorKeyValuePair = parent.getKeyValuePair(indexParentKeyReplacement);

        borrow(donorBrother, brotherDonorKeyValuePair, parent, indexParentKeyReplacement, node, parentDonorKeyValuePair);
    }

    private Node getDonorBrotherForBorrow(Node node) {
        if (isCanBorrowFromLeftBrother(node)) return getLeftBrother(node);
        return getRightBrother(node);
    }

    private boolean isCanBorrowFromLeftBrother(Node node) {
        Node leftBrother = getLeftBrother(node);
        return canBorrow(leftBrother);
    }

    private KeyValuePair getBrotherDonorKeyValuePairForBorrow(Node node) {
        Node donnorNode = getDonorBrotherForBorrow(node);
        if (isCanBorrowFromLeftBrother(node)) return donnorNode.getKeyValuePair(donnorNode.sizeKeyValuePair()-1);
        else return donnorNode.getKeyValuePair(0);
    }

    private Integer getIndexParentKeyReplacementForBorrow(Node node, int indexDeletedChild) {
        if (isCanBorrowFromLeftBrother(node)) return indexDeletedChild - 1;
        return indexDeletedChild;
    }

    private void borrow(Node donorBrother, KeyValuePair brotherDonorKeyValuePair, Node parent, Integer indexParentKeyReplacement, Node node, KeyValuePair parentDonorKeyValuePair) {
        donorBrother.deleteKeyValuePairByKey(brotherDonorKeyValuePair.getKey());
        parent.setKeyValuePair(indexParentKeyReplacement, brotherDonorKeyValuePair);
        node.addKeyValuePair(parentDonorKeyValuePair);
    }


    private boolean canCombineParentWithChild(Node node) {
        Node leftChild = getLeftBrother(node);
        Node rightChild = getRightBrother(node);
        return leftChild != null || rightChild != null;
    }

    private void combineChildWithParent(Node node) {
        int indexDonorParentKeyForCombineWithChild = getIndexDonorParentKeyForCombineWithChild(node);
        int indexChildWithWhomFatherCombined = getIndexChildWithWhomFatherCombined(node);

        combine(node,indexDonorParentKeyForCombineWithChild, indexChildWithWhomFatherCombined);
    }

    private int getIndexDonorParentKeyForCombineWithChild(Node node) {
        int indexNodeInParent = getIndexNodeInParent(node);

        if (isCanCombineWithLeft(node)) return indexNodeInParent - 1;
        return indexNodeInParent;
    }

    private int getIndexChildWithWhomFatherCombined(Node node) {
        int indexNodeInParent = getIndexNodeInParent(node);
        if (isCanCombineWithLeft(node)) return indexNodeInParent - 1;
        else return indexNodeInParent + 1;
    }

    private boolean isCanCombineWithLeft(Node node) {
        return getLeftBrother(node) != null;
    }

    private void combine(Node node,int indexDonorParentKeyForCombineWithChild, int indexChildWithWhomFatherCombined) {
        Node parent = node.getParent();
        Node childCombine = parent.getChild(indexChildWithWhomFatherCombined);
        childCombine.addKeyValuePair(parent.getKeyValuePair(indexDonorParentKeyForCombineWithChild));
        parent.deleteKeyValuePairByIndex(indexDonorParentKeyForCombineWithChild);
        parent.deleteChild(node.getKey(0));
    }

    private void balanceNodeInternal(Node node, int key) {
        if (canReplacePredecessor(node, key)) replaceDeleteKeyValueOnPredecessorKeyValue(node, key);
        else combinePredecessor(node, key);
    }

    private boolean canReplacePredecessor(Node node, int key) {
        Node leftPredecessor = getLeftPredecessor(node, key);
        if (canReplaceOnPredecessor(leftPredecessor)) return true;

        Node rightPredecessor = getRightPredecessor(node, key);
        return canReplaceOnPredecessor(rightPredecessor);
    }

    private boolean canReplaceOnPredecessor(Node predecessor) {
        return predecessor.sizeKeyValuePair() > getMinimSizeKey();
    }

    private Node getLeftPredecessor(Node node, int key) {
        int leftPredecessorIndex = node.getIndexKey(key);
        return node.getChild(leftPredecessorIndex);
    }

    private Node getRightPredecessor(Node node, int key) {
        int rightPredecessorIndex = node.getIndexKey(key) + 1;
        return node.getChild(rightPredecessorIndex);
    }

    private void replaceDeleteKeyValueOnPredecessorKeyValue(Node node, int key) {
        Node predecessor = getPredecessor(node, key);
        int indexDeletKey = node.getIndexKey(key);

        int indexKeyPredecessor = getIndexKeyPredecessor(node, key);
        node.setKeyValuePair(indexDeletKey, predecessor.getKeyValuePair(indexKeyPredecessor));
        predecessor.deleteKeyValuePairByIndex(indexKeyPredecessor);
    }

    private Node getPredecessor(Node node, int key) {
        Node leftPredecessor = getLeftPredecessor(node, key);
        if (canReplaceOnPredecessor(leftPredecessor)) return leftPredecessor;
        return getRightPredecessor(node, key);
    }

    private int getIndexKeyPredecessor(Node node, int key) {
        Node leftPredecessor = getLeftPredecessor(node, key);
        if (canReplaceOnPredecessor(leftPredecessor)) return leftPredecessor.sizeKeyValuePair()-1;
        return 0;
    }

    private void combinePredecessor(Node node, int key) {
        Node leftPredecessor = getLeftPredecessor(node, key);
        Node rightPredecessor = getRightPredecessor(node, key);

        leftPredecessor.addKeyValuePair(rightPredecessor.getKeyValuePair(0));
        node.deleteChild(rightPredecessor.getKey(0));
    }
}
