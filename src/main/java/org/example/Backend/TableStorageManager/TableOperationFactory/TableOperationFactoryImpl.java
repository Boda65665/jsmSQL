package org.example.Backend.TableStorageManager.TableOperationFactory;

import org.example.Backend.TableStorageManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableCreater.TableCraterImpl;
import org.example.Backend.TableStorageManager.TableDeleater.TableDeleter;
import org.example.Backend.TableStorageManager.TableDeleater.TableDeleterImpl;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TableReader.TableReader;
import org.example.Backend.TableStorageManager.TableReader.TableReaderImpl;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
import org.example.Backend.TableStorageManager.TableWriter.TableWriterImpl;

public class TableOperationFactoryImpl implements TableOperationFactory {
    private final TablePathProvider tablePathProvider;

    public TableOperationFactoryImpl(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    @Override
    public TableCrater getTableCrater() {
        return new TableCraterImpl(tablePathProvider, getTableWriter());
    }

    @Override
    public TableDeleter getTableDeleter() {
        return new TableDeleterImpl(tablePathProvider);
    }

    @Override
    public TableWriter getTableWriter() {
        return new TableWriterImpl(tablePathProvider);
    }

    @Override
    public TableReader getTableReader() {
        return new TableReaderImpl(tablePathProvider);
    }
}
