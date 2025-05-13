package org.example.Backend.TableStorageManager;

import org.example.Backend.Models.Record;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCrater;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.RecordManager.RecordOperationFactory.RecordOperationFactory;
import org.example.Backend.TableStorageManager.RecordManager.RecordSaver.RecordSaver;

public class TableStorageManager {
    private final FileOperationFactory fileOperationFactory;
    private final FileWriter fileWriter;
    private final FragmentOperationFactory fragmentOperationFactory;
    private final RecordOperationFactory recordOperationFactory;

    public TableStorageManager(FileOperationFactory fileOperationFactory, FragmentOperationFactory fragmentOperationFactory, RecordOperationFactory recordOperationFactory) {
        this.fileOperationFactory = fileOperationFactory;
        fileWriter = fileOperationFactory.getFileWriter();
        this.fragmentOperationFactory = fragmentOperationFactory;
        this.recordOperationFactory = recordOperationFactory;
    }

    public void createTable(String tableName, TableMetaData tableMetaData) {
        validCreateTable(tableName, tableMetaData);

        FileCrater fileCrater = fileOperationFactory.getFileCrater();
        fileCrater.create(tableName, tableMetaData);
    }

    private void validCreateTable(String tableName, TableMetaData tableMetaData) {
        if (tableName == null || tableName.isEmpty())
            throw new IllegalArgumentException("Table name cannot be null or empty");
        if (tableMetaData == null) throw new NullPointerException("TableMetaData cannot be null");
    }

    public void save(String tableName, Record data){
        validSave(tableName, data);

        RecordSaver recordSaver = getRecordSaver();

        int offsetIndex = recordSaver.save(tableName, data);

    }

    private RecordSaver getRecordSaver() {
        FragmentSaver fragmentSaver = fragmentOperationFactory.getFragmentRecordSaver(fileWriter);
        return recordOperationFactory.getRecordSaver(fragmentSaver);
    }

    private void validSave(String tableName, Record data) {
        if (tableName == null) throw new NullPointerException("Table name cannot be null");
        if (tableName.isEmpty()) throw new IllegalArgumentException("Table name cannot be empty");
        if (data == null) throw new NullPointerException("Table data cannot be null");
    }
}
