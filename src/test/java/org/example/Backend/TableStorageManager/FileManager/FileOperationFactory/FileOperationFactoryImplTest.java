package org.example.Backend.TableStorageManager.FileManager.FileOperationFactory;

import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerImpl;
import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCraterImpl;
import org.example.Backend.TableStorageManager.FileManager.FileDeleater.FileDeleterImpl;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProviderImpl;
import org.example.Backend.TableStorageManager.FileManager.TableReader.FileReaderImpl;
import org.example.Backend.TableStorageManager.FileManager.TableWriter.FileWriterImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class FileOperationFactoryImplTest {
    private final FileOperationFactory fileOperationFactory = new FileOperationFactoryImpl();

    @Test
    void getTableCrater() {
        assertInstanceOf(FileCraterImpl.class, fileOperationFactory.getTableCrater());
    }

    @Test
    void getTableDeleter() {
        assertInstanceOf(FileDeleterImpl.class, fileOperationFactory.getTableDeleter());
    }

    @Test
    void getTableWriter() {
        assertInstanceOf(FileWriterImpl.class, fileOperationFactory.getTableWriter());
    }

    @Test
    void getTableReader() {
        assertInstanceOf(FileReaderImpl.class, fileOperationFactory.getTableReader());
    }

    @Test
    void getTablePathProvider() {
        assertInstanceOf(FilePathProviderImpl.class, fileOperationFactory.getTablePathProvider());
    }

    @Test
    void getFreeSpaceManager() {
        assertInstanceOf(FreeSpaceManagerImpl.class, fileOperationFactory.getFreeSpaceManager(null));
    }
}