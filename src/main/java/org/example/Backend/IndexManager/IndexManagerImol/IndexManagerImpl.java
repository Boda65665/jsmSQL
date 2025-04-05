package org.example.Backend.IndexManager.IndexManagerImol;

import org.example.Backend.IndexManager.IndexManager;
import org.example.Backend.IndexManager.IndexManagerImol.Btree.BTree;

public class IndexManagerImpl implements IndexManager<Integer, Integer> {
    private final BTree bTree = new BTree();

    @Override
    public Integer get(Integer key) {
        return 0;
    }

    @Override
    public void insert(Integer key, Integer value) {
        bTree.insert(key, value);
    }

    @Override
    public void delete(Integer key) {

    }
}
