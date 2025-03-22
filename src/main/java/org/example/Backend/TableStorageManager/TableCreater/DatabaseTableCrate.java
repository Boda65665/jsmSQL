package org.example.Backend.TableStorageManager.TableCreater;

import org.example.Backend.Models.TableMetaData;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

import java.io.File;

public class DatabaseTableCrate extends TableCrate{
    public DatabaseTableCrate(TablePathProvider pathProvider, TablePathProvider tablePathProvider) {
        super(pathProvider);
    }

    @Override
    public void create(String tableName, TableMetaData tableMetaData) {
        String pathToTable = pathProvider.getTablePath(tableName);
        crateTable(pathToTable);
        addMetaDataTable(tableName, tableMetaData);
    }

    private void addMetaDataTable(String tableName, TableMetaData tableMetaData) {
    }

    private void crateTable(String pathToTable) {
        File tableFile = new File(pathToTable);
        tableFile.mkdirs();
    }
}
