package org.example.Backend.IndexManager;

import java.util.concurrent.ConcurrentHashMap;

public class IndexDbManagerFactory {
    private final ConcurrentHashMap<String, IndexDbManager> indexManagersMap = new ConcurrentHashMap<>();

    public synchronized IndexDbManager getIndexManager(String tableName) {
        return indexManagersMap.get(tableName);
    }

    public synchronized void putIndexManager(String tableName, IndexDbManager manager) {
        indexManagersMap.put(tableName, manager);
    }
}
