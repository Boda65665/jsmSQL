package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.Models.*;
import org.example.Backend.Models.Record;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.RecordManager.RecordOperationFactory.RecordOperationFactory;
import org.example.Backend.TableStorageManager.RecordManager.RecordSaver.RecordSaver;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCrater;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;

public class TableStorageManager {
    private final DbManagerFactory dbManagerFactory;
    private final String baseDbPath;
    private final FileOperationFactory fileOperationFactory;
    private final FileWriter fileWriter;
    private final FragmentOperationFactory fragmentOperationFactory;
    private final RecordOperationFactory recordOperationFactory;
    private final String PREFIX_NAME_FREE_SPACE = "freespace_";

    public TableStorageManager(String baseDbPath, DbManagerFactory dbManagerFactory, FileOperationFactory fileOperationFactory, FragmentOperationFactory fragmentOperationFactory, RecordOperationFactory recordOperationFactory) {
        this.dbManagerFactory = dbManagerFactory;
        this.baseDbPath = baseDbPath;
        this.fileOperationFactory = fileOperationFactory;
        fileWriter = fileOperationFactory.getTableWriter();
        this.fragmentOperationFactory = fragmentOperationFactory;
        this.recordOperationFactory = recordOperationFactory;
    }

    public void createTable(String tableName, TableMetaData tableMetaData) {
        validCreateTable(tableName, tableMetaData);

        FileCrater fileCrater = fileOperationFactory.getTableCrater();
        fileCrater.create(tableName, tableMetaData);
    }

    private void validCreateTable(String tableName, TableMetaData tableMetaData) {
        if (tableName == null || tableName.isEmpty())
            throw new IllegalArgumentException("Table name cannot be null or empty");
        if (tableMetaData == null) throw new NullPointerException("TableMetaData cannot be null");
    }

    public void save(String tableName, Record data){
        validSave(tableName, data);

        FreeSpaceManager freeSpaceManager = getFreeSpaceManager(tableName);
        RecordSaver recordSaver = getRecordSaver();

        int offsetIndex = recordSaver.save(tableName, data);

    }

    private RecordSaver getRecordSaver() {
        FragmentSaver fragmentSaver = fragmentOperationFactory.getFragmentRecordSaver(fileWriter);
        return recordOperationFactory.getRecordSaver(fragmentSaver);
    }

    private FreeSpaceManager getFreeSpaceManager(String tableName) {
        DbManager dbManager = dbManagerFactory.getDbManager(baseDbPath, PREFIX_NAME_FREE_SPACE + tableName);
        return fileOperationFactory.getFreeSpaceManager(dbManager);
    }

    private void validSave(String tableName, Record data) {
        if (tableName == null) throw new NullPointerException("Table name cannot be null");
        if (tableName.isEmpty()) throw new IllegalArgumentException("Table name cannot be empty");
        if (data == null) throw new NullPointerException("Table data cannot be null");
    }
}
