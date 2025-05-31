package org.example.Backend.TableStorageManager.IndexManager.Factory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.TableStorageManager.IndexManager.IndexManager;
import org.example.Backend.TableStorageManager.IndexManager.IndexManagerImpl;

public class IndexManagerFactoryImpl implements IndexManagerFactory {
    private final DbManagerFactory dbManagerFactory;
    private final String basePath;
    private final String PREFIX_NAME_INDEX = "index_";

    public IndexManagerFactoryImpl(DbManagerFactory dbManagerFactory, String basePath) {
        this.dbManagerFactory = dbManagerFactory;
        this.basePath = basePath;
    }

    @Override
    public IndexManager getIndexManager(String tableName) {
        String nameIndex = PREFIX_NAME_INDEX + tableName;
        DbManager dbManager = dbManagerFactory.getDbManager(basePath, nameIndex);
        return new IndexManagerImpl(dbManager);
    }
}
