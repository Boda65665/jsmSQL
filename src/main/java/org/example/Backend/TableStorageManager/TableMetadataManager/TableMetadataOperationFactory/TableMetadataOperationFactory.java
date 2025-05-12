package org.example.Backend.TableStorageManager.TableMetadataManager.TableMetadataOperationFactory;

import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManager;

public interface TableMetadataOperationFactory {
    PositionTableMetadataManager getPositionTableMetadataManager();
}
