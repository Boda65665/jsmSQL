package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerFactory;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.TabularData;
import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.BytesConverters.BytesConverterFactory;
import org.example.Backend.TableStorageManager.BytesConverters.BytesConverters;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
import org.example.Backend.TableStorageManager.TableWriter.TableWriterFactory;
import java.io.File;
import java.util.Arrays;

public class TableStorageManager {
    private final DbManagerFactory dbManagerFactory = DbManagerFactory.getDbManagerFactory();
    private final TableWriter tableWriter = TableWriterFactory.getTableWriter();
    private final DbManager<Integer, Integer> freeSpace;

    public TableStorageManager() {
        String basePath = System.getProperty("user.dir") + File.separator + "db";
        freeSpace = dbManagerFactory.getDbManager(basePath, "freeSpace");
    }

    public void save(String tableName, TabularData data) {
        for (Column column : data.getColumns()) {
            save(tableName, column);
        }
    }

    private void save(String tableName, Column data) {
        BytesConverters<Object> bytesConverters = (BytesConverters<Object>) BytesConverterFactory.getBytesConverters(TypeData.COLUMN);
        byte[] bytesData = bytesConverters.toBytes(data);
        save(tableName, bytesData);
    }

    private void save(String tableName, byte[] bytesData) {
        int len = bytesData.length;
        Integer countFreeBytes = getMoreSuitablePlace(len);
        byte[] bytesDataForSave = bytesData;
        int offset;

        if (countFreeBytes == null) offset = -1;
        else offset = getOffset(countFreeBytes, len);
        if (countFreeBytes != null && countFreeBytes < len) bytesDataForSave = Arrays.copyOf(bytesDataForSave, countFreeBytes);
        tableWriter.write(tableName, bytesDataForSave, offset);
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
