package org.example.Backend.TableStorageManager.TableManager.TableIndexManager;

import org.example.Backend.DbManager.DbManager;

public abstract class TableIndexManager<K, V> {
    protected final DbManager indexes;

    protected TableIndexManager(DbManager indexes) {
        this.indexes = indexes;
    }

    public abstract void addIndex(K key, V value);
    public abstract void removeIndex(K key);
    public abstract V getIndex(K key);
}
