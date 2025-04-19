package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerFactory;
import org.example.Backend.Models.*;
import org.example.Backend.TableStorageManager.BytesConverters.BytesConverterFactory;
import org.example.Backend.TableStorageManager.BytesConverters.BytesConverters;
import org.example.Backend.TableStorageManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactoryImpl;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;

import java.io.File;
import java.util.Arrays;

public class TableStorageManager {
    private final DbManagerFactory dbManagerFactory = DbManagerFactory.getDbManagerFactory();
    private final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();
    private final TableWriter tableWriter = tableOperationFactory.getTableWriter();
    private final DbManager<Integer, Integer> freeSpace;
    private final String basePath = System.getProperty("user.dir") + File.separator + "db";

    public TableStorageManager() {
        freeSpace = dbManagerFactory.getDbManager(basePath, "freeSpace");
    }

    public void createTable(String tableName, TableMetaData tableMetaData) {
        TableCrater tableCrater = tableOperationFactory.getTableCrater();
    }

    public void save(String tableName, TabularData data) {
        for (Column column : data.getColumns()) {
            int offset = save(tableName, column);
            DbManager dbIndexManager = dbManagerFactory.getDbManager(basePath, tableName);
        }
    }

    private int save(String tableName, Column data) {
        BytesConverters<Object> bytesConverters = (BytesConverters<Object>) BytesConverterFactory.getBytesConverters(TypeData.COLUMN);
        byte[] bytesData = bytesConverters.toBytes(data);
        return save(tableName, bytesData);
    }

    private int save(String tableName, byte[] bytesData) {
        int len = bytesData.length;
        Integer countFreeBytes = getMoreSuitablePlace(len);
        byte[] bytesDataForSave = bytesData;
        int offset;

        if (countFreeBytes == null) offset = -1;
        else offset = getOffset(countFreeBytes, len);
        if (countFreeBytes != null && countFreeBytes < len) bytesDataForSave = Arrays.copyOf(bytesDataForSave, countFreeBytes);
        tableWriter.write(tableName, bytesDataForSave, offset);
        return offset;
    }

    private Integer getMoreSuitablePlace(int length) {
        Integer countFreeBytes = freeSpace.get(length);
        if (countFreeBytes != null) return length;

        countFreeBytes = freeSpace.higherKey(length);
        if (countFreeBytes == null) countFreeBytes = freeSpace.maxKey();
        return countFreeBytes;
    }

    private Integer getOffset(int countFreeBytes, int length) {
        int offset = freeSpace.get(countFreeBytes);
        editFreeSpace(length, countFreeBytes);
        return offset;
    }

    private void editFreeSpace(int length, Integer countFreeBytes) {
        int newCountFreeBytes = countFreeBytes - length;
        if (newCountFreeBytes < 10) return;

        int offset = freeSpace.get(countFreeBytes);
        freeSpace.delete(countFreeBytes);
        freeSpace.put(newCountFreeBytes, offset + length);
    }
}
