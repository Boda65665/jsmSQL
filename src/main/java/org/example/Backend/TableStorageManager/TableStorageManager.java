package org.example.Backend.TableStorageManager;

import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.factory.TablePartTypeBytesConverterFactory;
import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerFactory;
import org.example.Backend.Models.*;
import org.example.Backend.TableStorageManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class TableStorageManager {
    private final DbManagerFactory dbManagerFactory = DbManagerFactory.getDbManagerFactory();
    private final String basePath = System.getProperty("user.dir") + File.separator + "db";
    private final TableOperationFactory tableOperationFactory;
    private final TableWriter tableWriter;
    private final DbManager<Integer, Integer> freeSpace;

    public TableStorageManager(TableOperationFactory tableOperationFactory) {
        this.tableOperationFactory = tableOperationFactory;
        tableWriter = tableOperationFactory.getTableWriter();
        freeSpace = dbManagerFactory.getDbManager(basePath, "freeSpace");
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
//    public void save(String tableName, TabularData data) {
//        validSave(tableName, data);
//
//        TablePartTypeConverter<TabularData> tabularDataConverter = TablePartTypeBytesConverterFactory.getTablePartTypeConverter(TablePartType.TABULAR_DATA);
//        ArrayList<Byte> bytesData = tabularDataConverter.toBytes(data);
//        int offSet = save(tableName, bytesData);
//
//        DbManager dbIndexManager = dbManagerFactory.getDbManager(basePath, tableName);
//        dbIndexManager.put(1, offSet);
//    }
//
//    private void validSave(String tableName, TabularData data) {
//        if (tableName == null || tableName.isEmpty())
//            throw new IllegalArgumentException("Table name cannot be null or empty");
//        if (data == null) throw new NullPointerException("Table data cannot be null");
//    }
//
//    private int save(String tableName, ArrayList<Byte> bytesData) {
//        int len = bytesData.size();
//        Integer countFreeBytes = getMoreSuitablePlace(len);
//        byte[] bytesDataForSave = bytesData;
//        int offset;
//
//        if (countFreeBytes == null) offset = -1;
//        else offset = getOffset(countFreeBytes, len);
//        if (countFreeBytes != null && countFreeBytes < len)
//            bytesDataForSave = Arrays.copyOf(bytesDataForSave, countFreeBytes);
//        tableWriter.write(tableName, bytesDataForSave, offset);
//        return offset;
//    }
//
//    private Integer getMoreSuitablePlace(int length) {
//        Integer countFreeBytes = freeSpace.get(length);
//        if (countFreeBytes != null) return length;
//
//        countFreeBytes = freeSpace.higherKey(length);
//        if (countFreeBytes == null) countFreeBytes = freeSpace.maxKey();
//        return countFreeBytes;
//    }
//
//    private Integer getOffset(int countFreeBytes, int length) {
//        int offset = freeSpace.get(countFreeBytes);
//        editFreeSpace(length, countFreeBytes);
//        return offset;
//    }
//
//    private void editFreeSpace(int length, Integer countFreeBytes) {
//        int newCountFreeBytes = countFreeBytes - length;
//        if (newCountFreeBytes < 10) return;
//
//        int offset = freeSpace.get(countFreeBytes);
//        freeSpace.delete(countFreeBytes);
//        freeSpace.put(newCountFreeBytes, offset + length);
//    }
//}
