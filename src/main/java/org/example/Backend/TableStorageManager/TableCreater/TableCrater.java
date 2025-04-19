package org.example.Backend.TableStorageManager.TableCreater;

import org.example.Backend.Models.TableMetaData;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

public abstract class TableCrater {
    protected final TablePathProvider pathProvider;

    protected TableCrater(TablePathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    public abstract void create(String tableName, TableMetaData tableMetaData);
}
