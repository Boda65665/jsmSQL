package org.example.Backend.TableStorageManager.TableCreater;

import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.BytesConverters.BytesConverterFactory;
import org.example.Backend.TableStorageManager.BytesConverters.BytesConverters;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
import org.example.Backend.TableStorageManager.TableWriter.TableWriterFactory;
import java.io.File;
import java.io.IOException;

public class TableCrateImpl extends TableCrate{

    public TableCrateImpl(TablePathProvider pathProvider) {
        super(pathProvider);
    }

    @Override
    public void create(String tableName, TableMetaData tableMetaData) {
        String pathToTable = pathProvider.getTablePath(tableName);
        if (crateTable(pathToTable)) {
            addMetaDataTable(tableName, tableMetaData);
        }
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
        byte[] byteTableMetaData = getBytesFromMetaData(tableMetaData);
        write(tableName, byteTableMetaData);
    }

    private byte[] getBytesFromMetaData(TableMetaData tableMetaData) {
        BytesConverters<TableMetaData> tableMetaDataBytesConverters
                = (BytesConverters<TableMetaData>) BytesConverterFactory.getBytesConverters(TypeData.TABLE_METADATA);
        return tableMetaDataBytesConverters.toBytes(tableMetaData);
    }

    private void write(String tableName, byte[] byteTableMetaData) {
        TableWriter tableWriter = TableWriterFactory.getTableWriter();
        tableWriter.write(tableName, byteTableMetaData, 0);
    }
}
