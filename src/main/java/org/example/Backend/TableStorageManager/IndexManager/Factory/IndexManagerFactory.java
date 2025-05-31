package org.example.Backend.TableStorageManager.IndexManager.Factory;

import org.example.Backend.TableStorageManager.IndexManager.IndexManager;

public interface IndexManagerFactory {
    IndexManager getIndexManager(String tableName);
}
