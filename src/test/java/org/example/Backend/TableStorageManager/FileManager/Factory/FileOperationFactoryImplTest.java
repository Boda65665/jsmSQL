package org.example.Backend.TableStorageManager.FileManager.Factory;

import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCraterImpl;
import org.example.Backend.TableStorageManager.FileManager.FileDeleater.FileDeleterImpl;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProviderImpl;
import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReaderImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriterImpl;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileLengthImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class FileOperationFactoryImplTest {
    private final FileOperationFactory fileOperationFactory = new FileOperationFactoryImpl();

    @Test
    void getFileCrater() {
        assertInstanceOf(FileCraterImpl.class, fileOperationFactory.getFileCrater());
    }

    @Test
    void getFileDeleter() {
        assertInstanceOf(FileDeleterImpl.class, fileOperationFactory.getFileDeleter());
    }

    @Test
    void getFileWriter() {
        assertInstanceOf(FileWriterImpl.class, fileOperationFactory.getFileWriter());
    }

    @Test
    void getFileReader() {
        assertInstanceOf(FileReaderImpl.class, fileOperationFactory.getFileReader());
    }

    @Test
    void getFilePathProvider() {
        assertInstanceOf(FilePathProviderImpl.class, fileOperationFactory.getFilePathProvider());
    }

    @Test
    void getGetterFileSize() {
        assertInstanceOf(GetterFileLengthImpl.class, fileOperationFactory.getGetterFileSize());
    }
}