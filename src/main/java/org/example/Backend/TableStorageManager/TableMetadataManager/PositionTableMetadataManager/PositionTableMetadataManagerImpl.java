package org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager;

import org.example.Backend.DbManager.DbManager;

public class PositionTableMetadataManagerImpl implements PositionTableMetadataManager {
    private final DbManager<String, Integer> managerEndPosition;
    private final DbManager<String, Integer> managerPositionStartLastMetadata;

    public PositionTableMetadataManagerImpl(DbManager<String, Integer> managerEndPosition, DbManager<String, Integer> managerPositionStartLastMetadata) {
        this.managerEndPosition = managerEndPosition;
        this.managerPositionStartLastMetadata = managerPositionStartLastMetadata;
    }

    @Override
    public Integer getEndPosition(String tableName) {
        return managerEndPosition.get(tableName);
    }

    @Override
    public void setEndPosition(String tableName, int endPosition) {
        managerEndPosition.put(tableName, endPosition);
    }

    @Override
    public Integer getPositionStartLastMetadataFragment(String tableName) {
        return managerPositionStartLastMetadata.get(tableName);
    }

    @Override
    public void setPositionStartLastMetadataFragment(String tableName, int positionStartLastMetadataFragment) {
        managerPositionStartLastMetadata.put(tableName, positionStartLastMetadataFragment);
    }
}
