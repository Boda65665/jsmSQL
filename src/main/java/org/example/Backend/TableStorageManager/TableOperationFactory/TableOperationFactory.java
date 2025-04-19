package org.example.Backend.TableStorageManager.TableOperationFactory;

import org.example.Backend.TableStorageManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableDeleater.TableDeleter;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TableReader.TableReader;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;

public interface TableOperationFactory {
    TableCrater getTableCrater();
    TableDeleter getTableDeleter();
    TableWriter getTableWriter();
    TableReader getTableReader();
    TablePathProvider getTablePathProvider();
}
