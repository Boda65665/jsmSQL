package org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager;

public interface PositionTableMetadataManager {
    Integer getEndPosition(String tableName);
    void setEndPosition(String tableName, int endPosition);

    Integer getPositionStartLastMetadataFragment(String tableName);
    void setPositionStartLastMetadataFragment(String tableName, int positionStartLastMetadataFragment);
}
