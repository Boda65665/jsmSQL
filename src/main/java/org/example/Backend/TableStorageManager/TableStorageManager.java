package org.example.Backend.TableStorageManager;

import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DmManagerFactory;
import org.example.Backend.Models.*;
import org.example.Backend.TableStorageManager.InsertionPointManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
import java.util.ArrayList;
import java.util.List;

public class TableStorageManager {
    private final DmManagerFactory dmManagerFactory;
    private final String baseDbPath;
    private final TableOperationFactory tableOperationFactory;
    private final TableWriter tableWriter;
    private final int LENGTH_LINK_INDICATOR_BYTE_COUNT = 1;
    private final int MAX_LENGTH_LINK_INDICATOR_BYTE_COUNT = 4;

    public TableStorageManager(String baseDbPath, DmManagerFactory dmManagerFactory, TableOperationFactory tableOperationFactory) {
        this.dmManagerFactory = dmManagerFactory;
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
        DbManager dbManager = dmManagerFactory.getDbManager(baseDbPath, "freeSpace_" + tableName);
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
        if(freeSpaceManager.freeSpaceIsOver()){
            tableWriter.write(tableName, bytesData, -1);
            tableWriter.write(tableName, new byte[]{0}, -1);
            position = -1;
        }
        else {
            FreeMemoryInfo freeMemoryInfo = freeSpaceManager.getInsertionPoint(len + LENGTH_LINK_INDICATOR_BYTE_COUNT + MAX_LENGTH_LINK_INDICATOR_BYTE_COUNT);

            write(tableName, bytesData, freeMemoryInfo);
            freeSpaceManager.editFreeSpace(len, freeMemoryInfo.getCountFreeBytes());
            if (freeMemoryInfo.getCountFreeBytes() < len) {
                List<Byte> sublist = bytesData.subList(freeMemoryInfo.getCountFreeBytes(), len);
                save(tableName, new ArrayList<>(sublist), freeSpaceManager);
            }
            position = freeMemoryInfo.getPosition();
        }

        return position;
    }

    private int numberToByteCount(int number) {
        return (int) Math.ceil(Math.log(Math.abs(number) + 1) / Math.log(256));
    }


    private void write(String tableName, ArrayList<Byte> bytesData, FreeMemoryInfo freeMemoryInfo) {

        int endIndex = Math.min(freeMemoryInfo.getCountFreeBytes(), bytesData.size());
        List<Byte> sublist = bytesData.subList(0, endIndex);
        ArrayList<Byte> bytesDataForSave = new ArrayList<>(sublist);
        tableWriter.write(tableName, bytesDataForSave, freeMemoryInfo.getPosition());
    }
}
