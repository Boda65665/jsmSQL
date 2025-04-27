package org.example.Backend.TableStorageManager.TableOperationFactory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.TableStorageManager.InsertionPointManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.InsertionPointManager.FreeSpaceManagerImpl;
import org.example.Backend.TableStorageManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableCreater.TableCraterImpl;
import org.example.Backend.TableStorageManager.TableDeleater.TableDeleter;
import org.example.Backend.TableStorageManager.TableDeleater.TableDeleterImpl;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProviderImpl;
import org.example.Backend.TableStorageManager.TableReader.TableReader;
import org.example.Backend.TableStorageManager.TableReader.TableReaderImpl;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
import org.example.Backend.TableStorageManager.TableWriter.TableWriterImpl;

public class TableOperationFactoryImpl implements TableOperationFactory {
    private final TablePathProvider tablePathProvider = new TablePathProviderImpl();
    private final TableWriter tableWriter = new TableWriterImpl(tablePathProvider);
    private final TableCrater tableCrater = new TableCraterImpl(tablePathProvider, tableWriter);
    private final TableReader tableReader = new TableReaderImpl(tablePathProvider);
    private final TableDeleter tableDeleter = new TableDeleterImpl(tablePathProvider);

    @Override
    public TableCrater getTableCrater() {
        return tableCrater;
    }

    @Override
    public TableDeleter getTableDeleter() {
        return tableDeleter;
    }

    @Override
    public TableWriter getTableWriter() {
        return tableWriter;
    }

    @Override
    public TableReader getTableReader() {
        return tableReader;
    }

    @Override
    public TablePathProvider getTablePathProvider() {
        return tablePathProvider;
    }

    @Override
    public FreeSpaceManager getFreeSpaceManager(DbManager dbManager) {
        return new FreeSpaceManagerImpl(dbManager);
    }
}
