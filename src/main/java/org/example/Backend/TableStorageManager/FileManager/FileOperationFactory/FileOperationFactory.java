package org.example.Backend.TableStorageManager.FileManager.FileOperationFactory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCrater;
import org.example.Backend.TableStorageManager.FileManager.FileDeleater.FileDeleter;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReader;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;

public interface FileOperationFactory {
    FileCrater getTableCrater();
    FileDeleter getTableDeleter();
    FileWriter getTableWriter();
    FileReader getTableReader();
    FilePathProvider getTablePathProvider();
    FreeSpaceManager getFreeSpaceManager(DbManager dbManager);
}
