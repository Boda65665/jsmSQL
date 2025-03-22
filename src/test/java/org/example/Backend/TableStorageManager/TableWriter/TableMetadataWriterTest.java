package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.Models.TableMetaData;
import org.example.Backend.TableStorageManager.TablePathProvider.DatabaseTablePathProvider;
import org.junit.jupiter.api.Test;

class TableMetadataWriterTest {
    private final DatabaseTablePathProvider pathProvider = new DatabaseTablePathProvider();
    private final WriterAbstractFactory writerAbstractFactory = new WriterAbstractFactory(pathProvider);
    private final TableMetadataWriter tableMetadataWriter = new TableMetadataWriter(pathProvider, writerAbstractFactory);
    private final String TABLE_NAME = "test_table";

    @Test
    void write() {
        TableMetaData tableMetaData = new TableMetaData();

        tableMetadataWriter.write(TABLE_NAME, tableMetaData, 0);
    }
}