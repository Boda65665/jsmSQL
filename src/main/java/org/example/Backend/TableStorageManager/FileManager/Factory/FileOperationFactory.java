package org.example.Backend.TableStorageManager.FileManager.Factory;

import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCrater;
import org.example.Backend.TableStorageManager.FileManager.FileDeleater.FileDeleter;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReader;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileLength;

public interface FileOperationFactory {
    FileCrater getFileCrater();
    FileDeleter getFileDeleter();
    FileWriter getFileWriter();
    FileReader getFileReader();
    FilePathProvider getFilePathProvider();
    GetterFileLength getGetterFileSize();
}
