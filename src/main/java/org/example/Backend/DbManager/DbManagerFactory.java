package org.example.Backend.DbManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DbManagerFactory {
    private final ConcurrentHashMap<String, DbManager> indexManagersMap = new ConcurrentHashMap<>();
    private static DbManagerFactory dbManagerFactory;

    private DbManagerFactory() {}

    public static DbManagerFactory getDbManagerFactory() {
        if (dbManagerFactory == null) dbManagerFactory = new DbManagerFactory();
        return dbManagerFactory;
    }

    public synchronized DbManager getDbManager(String basePath, String nameDb) {
        if (!indexManagersMap.containsKey(nameDb)) putDbManager(nameDb, basePath);
        return indexManagersMap.get(nameDb);
    }

    private synchronized void putDbManager(String nameDb, String basePath) {
        DbManager dbManager = new DbManagerImpl(nameDb, basePath);
        indexManagersMap.put(nameDb, dbManager);
    }

    public synchronized List<DbManager> getDbManagers() {
        return new ArrayList<>(indexManagersMap.values());
    }
}
