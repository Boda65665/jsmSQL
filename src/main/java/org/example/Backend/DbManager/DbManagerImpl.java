package org.example.Backend.DbManager;

public class DbManagerImpl<K, V> implements DbManager<K, V> {
    private final BtreeManager<K, V> btreeManager;

    public DbManagerImpl(String nameDb, String basePath) {
        btreeManager = new BtreeManager(nameDb, basePath);
    }

    @Override
    public V get(K key) {
        return btreeManager.get(key);
    }

    @Override
    public void put(K key, V value) {
        btreeManager.insert(key, value);
    }

    @Override
    public void delete(K key) {
        btreeManager.delete(key);
    }

    @Override
    public K higherKey(K key) {
        return btreeManager.higherKey(key);
    }

    @Override
    public K maxKey() {
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

    @Override
    public int size() {
        return btreeManager.size();
    }
}
