package org.example.Backend.TableStorageManager.TableCreater;

import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TablePartType;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TableCraterImpl extends TableCrater {
    private final TableWriter tableWriter;

    public TableCraterImpl(TablePathProvider pathProvider, TableWriter tableWriter) {
        super(pathProvider);
        this.tableWriter = tableWriter;
    }

    @Override
    public void create(String tableName, TableMetaData tableMetaData) {
        valid(tableName, tableMetaData);

        String pathToTable = pathProvider.getTablePath(tableName);
        if (crateTable(pathToTable)) {
            addMetaDataTable(tableName, tableMetaData);
        }
    }

    private void valid(String tableName, TableMetaData tableMetaData) {
        if (tableName == null) throw new NullPointerException("tableName is null");
        if (tableName.trim().isEmpty()) throw new IllegalArgumentException("tableName is empty");
        if (tableMetaData == null) throw new NullPointerException("tableMetaData is null");
    }

    private boolean crateTable(String pathToTable) {
        File tableFile = new File(pathToTable);
        if(!tableFile.exists()) {
            try {
                tableFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    private void addMetaDataTable(String tableName, TableMetaData tableMetaData) {
        ArrayList<Byte> byteTableMetaData = getBytesFromMetaData(tableMetaData);
        write(tableName, byteTableMetaData);
    }

    private ArrayList<Byte> getBytesFromMetaData(TableMetaData tableMetaData) {
        TablePartTypeConverter<TableMetaData> tableMetaDataBytesConverters
                = (TablePartTypeConverter<TableMetaData>) BytesConverterFactory.getTablePartTypeConverter(TablePartType.TABLE_METADATA);
        return tableMetaDataBytesConverters.toBytes(tableMetaData);
    }

    private void write(String tableName, ArrayList<Byte> byteTableMetaData) {
        tableWriter.write(tableName, byteTableMetaData, 0);
    }
}
