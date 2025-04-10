package org.example.Backend.IndexManager;

public class IndexDbManagerImpl implements IndexDbManager<Integer, Integer> {
    private final BtreeManager btreeManager;

    public IndexDbManagerImpl(String tableName) {
        btreeManager = new BtreeManager(tableName);
    }

    @Override
    public Integer get(Integer key) {
        return btreeManager.get(key);
    }

    @Override
    public void insert(Integer key, Integer value) {
        btreeManager.insert(key, value);
    }

    @Override
    public void delete(Integer key) {
        btreeManager.delete(key);
    }

    @Override
    public void close() {
        btreeManager.close();
    }
}
