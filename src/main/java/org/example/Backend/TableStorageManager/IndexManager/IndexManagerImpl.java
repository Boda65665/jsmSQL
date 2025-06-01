package org.example.Backend.TableStorageManager.IndexManager;

import org.example.Backend.DbManager.DbManager;

public class IndexManagerImpl<K, V> extends IndexManager<K, V> {

    public IndexManagerImpl(DbManager<K, V> indexes) {
        super(indexes);
    }

    @Override
    public void addIndex(K key, V value) {
        indexes.put(key, value);
    }

    @Override
    public void removeIndex(K key) {
        indexes.delete(key);
    }

    @Override
    public V getIndex(K key) {
        return indexes.get(key);
    }

    @Override
    public V getLast() {
        if(indexes.maxKey() == null) return null;
        return getIndex(indexes.maxKey());
    }

    @Override
    public int size() {
        return indexes.size();
    }
}
