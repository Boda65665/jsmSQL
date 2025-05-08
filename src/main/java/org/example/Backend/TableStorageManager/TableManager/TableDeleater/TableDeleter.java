package org.example.Backend.TableStorageManager.TableManager.TableDeleater;

import org.example.Backend.TableStorageManager.TableManager.TablePathProvider.TablePathProvider;

public abstract class TableDeleter {
    protected final TablePathProvider pathProvider;

    protected TableDeleter(TablePathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    public abstract void delete(String tableName);
}
