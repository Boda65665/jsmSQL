package org.example.Backend.TableStorageManager.TableDeleater;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

public abstract class TableDeleter {
    protected final TablePathProvider pathProvider;

    protected TableDeleter(TablePathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    public abstract void delete(String tableName);
}
