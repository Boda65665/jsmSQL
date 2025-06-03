package org.example.Backend.TableStorageManager.FileManager.FileCreater;

import org.example.Backend.DataToBytesConverter.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TablePartType;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileCraterImpl extends FileCrater {
    private final FileWriter fileWriter;

    public FileCraterImpl(FilePathProvider pathProvider, FileWriter fileWriter) {
        super(pathProvider);
        this.fileWriter = fileWriter;
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
                = (TablePartTypeConverter<TableMetaData>) BytesConverterFactory.getTablePartTypeConverter(TablePartType.METADATA);
        return tableMetaDataBytesConverters.toBytes(tableMetaData);
    }

    private void write(String tableName, ArrayList<Byte> byteTableMetaData) {
        fileWriter.write(tableName, byteTableMetaData, 0);
    }
}
