package org.example.Backend.TableStorageManager.FreeSpaceManager.Factory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerImpl;

public class FreeSpaceManagerFactoryImpl implements FreeSpaceManagerFactory {
    private final String baseDbPath;
    private final String prefixName = "freespace_";
    private final DbManagerFactory dbManagerFactory;

    public FreeSpaceManagerFactoryImpl(String baseDbPath, DbManagerFactory dbManagerFactory) {
        this.baseDbPath = baseDbPath;
        this.dbManagerFactory = dbManagerFactory;
    }

    @Override
    public FreeSpaceManager getFreeSpaceManager(String nameTable) {
        DbManager dbManager = dbManagerFactory.getDbManager(baseDbPath, prefixName + nameTable);
        return new FreeSpaceManagerImpl(dbManager);
    }
}
