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

        balanceIfRequired(desiredNode);
        desiredNode.deleteKeyValuePairByKey(key);
    }

    private void valid(Node node, int key) {
        if(node == null) throw new NullPointerException("node is null");
        if(key < 0) throw new IllegalArgumentException("key is negative");
    }

    private void balanceIfRequired(Node node) {
        if (!isRequiredBalancing(node)) return;

        if (node.isLeaf()) {
            balanceLeaf(node);
        }
    }

    private boolean isRequiredBalancing(Node node) {
        int sizeKeyAfterDelete = node.sizeKeyValuePair() - 1;
        return sizeKeyAfterDelete < getMinimSizeKey() || node.sizeChild() > sizeKeyAfterDelete;
    }

    public int getMinimSizeKey(){
        return (treeOrder/2)-1;
    }

    private void balanceLeaf(Node node) {
        if (canBorrowFromNeighbor(node)) borrowFromNeighbor(node);
    }

    private boolean canBorrowFromNeighbor(Node node) {
        Node leftNeighbor = getLeftNeighbor(node);
        Node rightNeighbor = getRightNeighbor(node);

        return canBorrow(leftNeighbor) || canBorrow(rightNeighbor);
    }

    private Node getLeftNeighbor(Node node) {
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

    private Node getRightNeighbor(Node node) {
        Node parent = node.getParent();
        int indexDeletedChild = getIndexNodeInParent(node);

        if (indexDeletedChild + 1 < parent.sizeChild()) return parent.getChild(indexDeletedChild + 1);
        return null;
    }

    private boolean canBorrow(Node node) {
        return node!=null && node.sizeKeyValuePair() > getMinimSizeKey();
    }

    private void borrowFromNeighbor(Node node) {
        int indexDeletedChild = getIndexNodeInParent(node);
        Node parent = node.getParent();
        Node donorBrother = getDonorBrotherForBorrow(node);
        KeyValuePair brotherDonorKeyValuePair = getBrotherDonorKeyValuePairForBorrow(node);
        Integer indexParentKeyReplacement = getIndexParentKeyReplacementForBorrow(node, indexDeletedChild);
        KeyValuePair parentDonorKeyValuePair = parent.getKeyValuePair(indexParentKeyReplacement);

        borrow(donorBrother, brotherDonorKeyValuePair, parent, indexParentKeyReplacement, node, parentDonorKeyValuePair);
    }

    private Node getDonorBrotherForBorrow(Node node) {
        if (isBorrowFromLeftNeighbor(node)) return getLeftNeighbor(node);
        return getRightNeighbor(node);
    }

    private boolean isBorrowFromLeftNeighbor(Node node) {
        Node leftNeighbor = getLeftNeighbor(node);
        return canBorrow(leftNeighbor);
    }

    private KeyValuePair getBrotherDonorKeyValuePairForBorrow(Node node) {
        Node donnorNode = getDonorBrotherForBorrow(node);
        if (isBorrowFromLeftNeighbor(node)) return donnorNode.getKeyValuePair(donnorNode.sizeKeyValuePair()-1);
        else return donnorNode.getKeyValuePair(0);
    }

    private Integer getIndexParentKeyReplacementForBorrow(Node node, int indexDeletedChild) {
        if (isBorrowFromLeftNeighbor(node)) return indexDeletedChild - 1;
        return indexDeletedChild;
    }

    private void borrow(Node donorBrother, KeyValuePair brotherDonorKeyValuePair, Node parent, Integer indexParentKeyReplacement, Node node, KeyValuePair parentDonorKeyValuePair) {
        donorBrother.deleteKeyValuePairByKey(brotherDonorKeyValuePair.getKey());
        parent.setKeyValuePair(indexParentKeyReplacement, brotherDonorKeyValuePair);
        node.addKeyValuePair(parentDonorKeyValuePair);
    }
}
