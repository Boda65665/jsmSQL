package org.example.Backend.TableStorageManager.TableCreater;

import org.example.Backend.Models.TableStruct;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

public abstract class TableCrate {
    private final TablePathProvider pathProvider;

    protected TableCrate(TablePathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    public abstract void create(String tableName, TableStruct struct);
}
