package org.example.Backend.TableStorageManager.TableManager.TableOperationFactory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.TableManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableManager.TableDeleater.TableDeleter;
import org.example.Backend.TableStorageManager.TableManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TableManager.TableReader.TableReader;
import org.example.Backend.TableStorageManager.TableManager.TableWriter.TableWriter;

public interface TableOperationFactory {
    TableCrater getTableCrater();
    TableDeleter getTableDeleter();
    TableWriter getTableWriter();
    TableReader getTableReader();
    TablePathProvider getTablePathProvider();
    FreeSpaceManager getFreeSpaceManager(DbManager dbManager);
}
