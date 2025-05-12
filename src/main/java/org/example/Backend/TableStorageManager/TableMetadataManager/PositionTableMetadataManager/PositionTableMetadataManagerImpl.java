package org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager;

import org.example.Backend.DbManager.DbManager;

public class PositionTableMetadataManagerImpl implements PositionTableMetadataManager {
    private final DbManager<String, Integer> dbManager;

    public PositionTableMetadataManagerImpl(DbManager<String, Integer> dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public int getEndPosition(String tableName) {
        return dbManager.get(tableName);
    }

    @Override
    public void setEndPosition(String tableName, int endPosition) {
        dbManager.put(tableName, endPosition);
    }
}
