package org.example.Backend.TableStorageManager.TableMetadataManager.TableMetadataOperationFactory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManager;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManagerImpl;

public class TableMetadataOperationFactoryImpl implements TableMetadataOperationFactory {
    private final DbManagerFactory dbManagerFactory;
    private final String basePathDb;
    private final String PREFIX_NAME_DB_MANAGER_END_POSITION = "endPosition_";
    private final String PREFIX_NAME_DB_MANAGER_START_POSITION_LAST_METADATA = "positionStartLastMetadata_";
    private final String nameDbManagerEndPosition;
    private final String nameDbManagerStartPositionLastMetadata;

    public TableMetadataOperationFactoryImpl(DbManagerFactory dbManagerFactory, String basePathDb, String nameDb) {
        this.dbManagerFactory = dbManagerFactory;
        this.basePathDb = basePathDb;
        nameDbManagerEndPosition = PREFIX_NAME_DB_MANAGER_END_POSITION + nameDb;
        nameDbManagerStartPositionLastMetadata = PREFIX_NAME_DB_MANAGER_START_POSITION_LAST_METADATA + nameDb;
    }

    @Override
    public PositionTableMetadataManager getPositionTableMetadataManager() {
        DbManager managerEndPosition = dbManagerFactory.getDbManager(basePathDb, nameDbManagerEndPosition);
        DbManager managerPositionStartLastMetadata = dbManagerFactory.getDbManager(basePathDb, nameDbManagerStartPositionLastMetadata);
        return new PositionTableMetadataManagerImpl(managerEndPosition, managerPositionStartLastMetadata);
    }
}
