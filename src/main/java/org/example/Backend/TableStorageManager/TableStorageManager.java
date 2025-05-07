package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.Models.*;
import org.example.Backend.TableStorageManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;

public class TableStorageManager {
    private final DbManagerFactory dbManagerFactory;
    private final String baseDbPath;
    private final TableOperationFactory tableOperationFactory;
    private final TableWriter tableWriter;
    private final FragmentOperationFactory fragmentOperationFactory;

    public TableStorageManager(String baseDbPath, DbManagerFactory dbManagerFactory, TableOperationFactory tableOperationFactory, FragmentOperationFactory fragmentOperationFactory) {
        this.dbManagerFactory = dbManagerFactory;
        this.baseDbPath = baseDbPath;
        this.tableOperationFactory = tableOperationFactory;
        tableWriter = tableOperationFactory.getTableWriter();
        this.fragmentOperationFactory = fragmentOperationFactory;
    }

    public void createTable(String tableName, TableMetaData tableMetaData) {
        validCreateTable(tableName, tableMetaData);

        TableCrater tableCrater = tableOperationFactory.getTableCrater();
        tableCrater.create(tableName, tableMetaData);
    }

    private void validCreateTable(String tableName, TableMetaData tableMetaData) {
        if (tableName == null || tableName.isEmpty())
            throw new IllegalArgumentException("Table name cannot be null or empty");
        if (tableMetaData == null) throw new NullPointerException("TableMetaData cannot be null");
    }
}
