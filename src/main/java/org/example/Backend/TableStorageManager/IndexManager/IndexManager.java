package org.example.Backend.TableStorageManager.IndexManager;

import org.example.Backend.DbManager.DbManager;

public abstract class IndexManager<K, V> {
    protected final DbManager<K, V> indexes;

    protected IndexManager(DbManager<K, V> indexes) {
        this.indexes = indexes;
    }

    public abstract void addIndex(K key, V value);
    public abstract void removeIndex(K key);
    public abstract V getIndex(K key);
    public abstract V getLast();
    public abstract int size();
}
