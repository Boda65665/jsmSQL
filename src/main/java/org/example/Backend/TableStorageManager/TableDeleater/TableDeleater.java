package org.example.Backend.TableStorageManager.TableDeleater;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

public abstract class TableDeleater {
    private final TablePathProvider pathProvider;

    protected TableDeleater(TablePathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    public abstract void delete(String tableName);
}
