package org.example.Backend.TableStorageManager.FileManager.Factory;

import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCrater;
import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCraterImpl;
import org.example.Backend.TableStorageManager.FileManager.FileDeleater.FileDeleter;
import org.example.Backend.TableStorageManager.FileManager.FileDeleater.FileDeleterImpl;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProviderImpl;
import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReader;
import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReaderImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriterImpl;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileLength;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileLengthImpl;

public class FileOperationFactoryImpl implements FileOperationFactory {
    private final FilePathProvider filePathProvider = new FilePathProviderImpl();
    private final FileWriter fileWriter = new FileWriterImpl(filePathProvider);
    private final FileCrater fileCrater = new FileCraterImpl(filePathProvider, fileWriter);
    private final FileReader fileReader = new FileReaderImpl(filePathProvider);
    private final FileDeleter fileDeleter = new FileDeleterImpl(filePathProvider);
    private final GetterFileLength getGetterFileLength = new GetterFileLengthImpl(filePathProvider);

    @Override
    public FileCrater getFileCrater() {
        return fileCrater;
    }

    @Override
    public FileDeleter getFileDeleter() {
        return fileDeleter;
    }

    @Override
    public FileWriter getFileWriter() {
        return fileWriter;
    }

    @Override
    public FileReader getFileReader() {
        return fileReader;
    }

    @Override
    public FilePathProvider getFilePathProvider() {
        return filePathProvider;
    }

    @Override
    public GetterFileLength getGetterFileSize() {
        return getGetterFileLength;
    }

}
