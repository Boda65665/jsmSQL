package org.example.Backend.DbManager;

public class DbManagerImpl implements DbManager<Integer, Integer> {
    private final BtreeManager btreeManager;

    public DbManagerImpl(String nameDb, String basePath) {
        btreeManager = new BtreeManager(nameDb, basePath);
    }

    @Override
    public Integer get(Integer key) {
        return btreeManager.get(key);
    }

    @Override
    public void put(Integer key, Integer value) {
        btreeManager.insert(key, value);
    }

    @Override
    public void delete(Integer key) {
        btreeManager.delete(key);
    }

    @Override
    public Integer higherKey(Integer key) {
        return btreeManager.higherKey(key);
    }

    @Override
    public Integer maxKey() {
        return btreeManager.maxKey();
    }

    @Override
    public void close() {
        btreeManager.close();
    }

    @Override
    public void clear() {
        btreeManager.clear();
    }
}
