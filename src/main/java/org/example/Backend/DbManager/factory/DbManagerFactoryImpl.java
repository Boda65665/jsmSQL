package org.example.Backend.DbManager.factory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DbManagerFactoryImpl implements DbManagerFactory {
    private final ConcurrentHashMap<String, DbManager> indexManagersMap = new ConcurrentHashMap<>();
    private static DbManagerFactoryImpl dbManagerFactoryImpl;

    private DbManagerFactoryImpl() {}

    public static DbManagerFactoryImpl getDbManagerFactory() {
        if (dbManagerFactoryImpl == null) dbManagerFactoryImpl = new DbManagerFactoryImpl();
        return dbManagerFactoryImpl;
    }

    @Override
    public synchronized DbManager getDbManager(String basePath, String nameDb) {
        if (!indexManagersMap.containsKey(nameDb)) putDbManager(nameDb, basePath);
        return indexManagersMap.get(nameDb);
    }

    private synchronized void putDbManager(String nameDb, String basePath) {
        DbManager dbManager = new DbManagerImpl(nameDb, basePath);
        indexManagersMap.put(nameDb, dbManager);
    }

    @Override
    public synchronized List<DbManager> getDbManagers() {
        return new ArrayList<>(indexManagersMap.values());
    }
}
