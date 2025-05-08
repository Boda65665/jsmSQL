package org.example.Backend.TableStorageManager.TableManager.TableIndexManager;

import org.example.Backend.DbManager.DbManager;

public class TableIndexManagerImpl extends TableIndexManager {

    protected TableIndexManagerImpl(DbManager indexes) {
        super(indexes);
    }

    @Override
    public void addIndex(Object key, Object value) {
        indexes.put(key, value);
    }

    @Override
    public void removeIndex(Object key) {
        indexes.delete(key);
    }

    @Override
    public Object getIndex(Object key) {
        return indexes.get(key);
    }
}
