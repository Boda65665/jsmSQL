package org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager;

public interface PositionTableMetadataManager {
    int getEndPosition(String tableName);
    void setEndPosition(String tableName, int endPosition);
}
