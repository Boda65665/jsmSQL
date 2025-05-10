package org.example.Backend.TableStorageManager.FileManager.FileOperationFactory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerImpl;
import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCrater;
import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCraterImpl;
import org.example.Backend.TableStorageManager.FileManager.FileDeleater.FileDeleter;
import org.example.Backend.TableStorageManager.FileManager.FileDeleater.FileDeleterImpl;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProviderImpl;
import org.example.Backend.TableStorageManager.FileManager.TableReader.FileReader;
import org.example.Backend.TableStorageManager.FileManager.TableReader.FileReaderImpl;
import org.example.Backend.TableStorageManager.FileManager.TableWriter.FileWriter;
import org.example.Backend.TableStorageManager.FileManager.TableWriter.FileWriterImpl;

public class FileOperationFactoryImpl implements FileOperationFactory {
    private final FilePathProvider filePathProvider = new FilePathProviderImpl();
    private final FileWriter fileWriter = new FileWriterImpl(filePathProvider);
    private final FileCrater fileCrater = new FileCraterImpl(filePathProvider, fileWriter);
    private final FileReader fileReader = new FileReaderImpl(filePathProvider);
    private final FileDeleter fileDeleter = new FileDeleterImpl(filePathProvider);

    @Override
    public FileCrater getTableCrater() {
        return fileCrater;
    }

    @Override
    public FileDeleter getTableDeleter() {
        return fileDeleter;
    }

    @Override
    public FileWriter getTableWriter() {
        return fileWriter;
    }

    @Override
    public FileReader getTableReader() {
        return fileReader;
    }

    @Override
    public FilePathProvider getTablePathProvider() {
        return filePathProvider;
    }

    @Override
    public FreeSpaceManager getFreeSpaceManager(DbManager dbManager) {
        return new FreeSpaceManagerImpl(dbManager);
    }
}
