package org.example.Backend.TableStorageManager.FileManager.FileOperationFactory;

import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCrater;
import org.example.Backend.TableStorageManager.FileManager.FileDeleater.FileDeleter;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReader;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileSize;

public interface FileOperationFactory {
    FileCrater getFileCrater();
    FileDeleter getFileDeleter();
    FileWriter getFileWriter();
    FileReader getFileReader();
    FilePathProvider getFilePathProvider();
    GetterFileSize getGetterFileSize();
}
