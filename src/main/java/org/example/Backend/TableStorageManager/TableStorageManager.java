package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.Models.*;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.TableManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableManager.TableWriter.TableWriter;

public class TableStorageManager {
    private final DbManagerFactory dbManagerFactory;
    private final String baseDbPath;
    private final TableOperationFactory tableOperationFactory;
    private final TableWriter tableWriter;
    private final FragmentOperationFactory fragmentOperationFactory;
    private final String PREFIX_NAME_FREE_SPACE = "freespace_";
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

    public void save(String tableName, TabularData data){
        validSave(tableName, data);

        FragmentSaver fragmentSaver = fragmentOperationFactory.getFragmentSaver(tableWriter);
        FreeSpaceManager freeSpaceManager = getFreeSpaceManager(tableName);

        fragmentSaver.save(tableName, data, freeSpaceManager);
    }

    private FreeSpaceManager getFreeSpaceManager(String tableName) {
        DbManager dbManager = dbManagerFactory.getDbManager(baseDbPath, PREFIX_NAME_FREE_SPACE + tableName);
        return tableOperationFactory.getFreeSpaceManager(dbManager);
    }

    private void validSave(String tableName, TabularData data) {
        if (tableName == null) throw new NullPointerException("Table name cannot be null");
        if (tableName.isEmpty()) throw new IllegalArgumentException("Table name cannot be empty");
        if (data == null) throw new NullPointerException("Table data cannot be null");
    }
}
