package org.example.Backend.TableStorageManager.TableManager.TableOperationFactory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerImpl;
import org.example.Backend.TableStorageManager.TableManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableManager.TableCreater.TableCraterImpl;
import org.example.Backend.TableStorageManager.TableManager.TableDeleater.TableDeleter;
import org.example.Backend.TableStorageManager.TableManager.TableDeleater.TableDeleterImpl;
import org.example.Backend.TableStorageManager.TableManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TableManager.TablePathProvider.TablePathProviderImpl;
import org.example.Backend.TableStorageManager.TableManager.TableReader.TableReader;
import org.example.Backend.TableStorageManager.TableManager.TableReader.TableReaderImpl;
import org.example.Backend.TableStorageManager.TableManager.TableWriter.TableWriter;
import org.example.Backend.TableStorageManager.TableManager.TableWriter.TableWriterImpl;

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
