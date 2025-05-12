package org.example.Backend.TableStorageManager.TableMetadataManager.TableMetadataOperationFactory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManager;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManagerImpl;

public class TableMetadataOperationFactoryImpl implements TableMetadataOperationFactory {
    private final DbManagerFactory dbManagerFactory;
    private final String basePathDb;
    private final String nameDb;

    public TableMetadataOperationFactoryImpl(DbManagerFactory dbManagerFactory, String basePathDb, String nameDb) {
        this.dbManagerFactory = dbManagerFactory;
        this.basePathDb = basePathDb;
        this.nameDb = nameDb;
    }

    @Override
    public PositionTableMetadataManager getPositionTableMetadataManager() {
        DbManager dbManager = dbManagerFactory.getDbManager(basePathDb, nameDb);
        return new PositionTableMetadataManagerImpl(dbManager);
    }
}
