package org.example.Backend.TableStorageManager;

import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.Models.*;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
import java.util.ArrayList;
import java.util.List;

public class TableStorageManager {
    private final DbManagerFactory dbManagerFactory;
    private final String baseDbPath;
    private final TableOperationFactory tableOperationFactory;
    private final TableWriter tableWriter;
    private final int LENGTH_LINK_INDICATOR_BYTE_COUNT = 1;
    private final int MAX_LENGTH_LINK_INDICATOR_BYTE_COUNT = 4;

    public TableStorageManager(String baseDbPath, DbManagerFactory dbManagerFactory, TableOperationFactory tableOperationFactory) {
        this.dbManagerFactory = dbManagerFactory;
        this.baseDbPath = baseDbPath;
        this.tableOperationFactory = tableOperationFactory;
        tableWriter = tableOperationFactory.getTableWriter();
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


    public void save(String tableName, TabularData data) {
        validSave(tableName, data);

        TablePartTypeConverter<TabularData> tabularDataConverter =  BytesConverterFactory.getTablePartTypeConverter(TablePartType.TABULAR_DATA);
        ArrayList<Byte> bytesData = tabularDataConverter.toBytes(data);
        DbManager dbManager = dbManagerFactory.getDbManager(baseDbPath, "freeSpace_" + tableName);
        FreeSpaceManager freeSpaceManager = tableOperationFactory.getFreeSpaceManager(dbManager);
        int offset = save(tableName, bytesData, freeSpaceManager);

    }

    private void validSave(String tableName, TabularData data) {
        if (tableName == null || tableName.isEmpty())
            throw new IllegalArgumentException("Table name cannot be null or empty");
        if (data == null) throw new NullPointerException("Table data cannot be null");
    }

    private int save(String tableName, ArrayList<Byte> bytesData, FreeSpaceManager freeSpaceManager) {
        int len = bytesData.size();
        int position = 0;
        if (freeSpaceManager.freeSpaceIsOver()) {
            tableWriter.write(tableName, bytesData, -1);
            tableWriter.write(tableName, new byte[]{0}, -1);
            position = -1;
        } else {
            FreeMemoryInfo freeMemoryInfo = freeSpaceManager.getInsertionPoint(len + LENGTH_LINK_INDICATOR_BYTE_COUNT + MAX_LENGTH_LINK_INDICATOR_BYTE_COUNT);
            int lengthFragment = Math.min(freeMemoryInfo.getCountFreeBytes(), bytesData.size());
            write(tableName, bytesData, lengthFragment, freeMemoryInfo.getPosition());
            freeSpaceManager.adjustFreeSpace(len, freeMemoryInfo.getCountFreeBytes());
            if (freeMemoryInfo.getCountFreeBytes() < len) {
                List<Byte> sublist = bytesData.subList(freeMemoryInfo.getCountFreeBytes(), len);
                save(tableName, new ArrayList<>(sublist), freeSpaceManager);
            }
            position = freeMemoryInfo.getPosition();
        }

        return position;
    }

    private void write(String tableName, ArrayList<Byte> bytesData, int lengthFragment, int position) {
        List<Byte> sublist = bytesData.subList(0, lengthFragment);
        ArrayList<Byte> bytesDataForSave = new ArrayList<>(sublist);
        tableWriter.write(tableName, bytesDataForSave, position);
    }
}
