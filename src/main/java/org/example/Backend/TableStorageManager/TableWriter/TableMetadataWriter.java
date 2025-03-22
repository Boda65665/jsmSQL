package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TableWriter.PrimitiveType.TableWriter;

public class TableMetadataWriter extends TableWriter<TableMetaData> {
    private final WriterAbstractFactory writerAbstractFactory;

    public TableMetadataWriter(TablePathProvider tablePathProvider, WriterAbstractFactory writerAbstractFactory) {
        super(tablePathProvider);
        this.writerAbstractFactory = writerAbstractFactory;
    }

    @Override
    public void write(String tableName, TableMetaData tableMetaData,int offset) {
        int sizeColumn = tableMetaData.getColumnStructList().size();

        TableWriter<Integer> tableIntWriter = writerAbstractFactory.getTableWriter(TypeData.INT);
        tableIntWriter.write(tableName, sizeColumn, 0);

        for (ColumnStruct columnStruct : tableMetaData.getColumnStructList()) {

        }
    }
}
