package org.example.Backend.TableStorageManager.TableManager.TableOperationFactory;

import org.example.Backend.TableStorageManager.TableManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableManager.TableOperationFactory.TableOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerImpl;
import org.example.Backend.TableStorageManager.TableManager.TableCreater.TableCraterImpl;
import org.example.Backend.TableStorageManager.TableManager.TableDeleater.TableDeleterImpl;
import org.example.Backend.TableStorageManager.TableManager.TablePathProvider.TablePathProviderImpl;
import org.example.Backend.TableStorageManager.TableManager.TableReader.TableReaderImpl;
import org.example.Backend.TableStorageManager.TableManager.TableWriter.TableWriterImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class TableOperationFactoryImplTest {
    private final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();

    @Test
    void getTableCrater() {
        assertInstanceOf(TableCraterImpl.class, tableOperationFactory.getTableCrater());
    }

    @Test
    void getTableDeleter() {
        assertInstanceOf(TableDeleterImpl.class, tableOperationFactory.getTableDeleter());
    }

    @Test
    void getTableWriter() {
        assertInstanceOf(TableWriterImpl.class, tableOperationFactory.getTableWriter());
    }

    @Test
    void getTableReader() {
        assertInstanceOf(TableReaderImpl.class, tableOperationFactory.getTableReader());
    }

    @Test
    void getTablePathProvider() {
        assertInstanceOf(TablePathProviderImpl.class, tableOperationFactory.getTablePathProvider());
    }

    @Test
    void getFreeSpaceManager() {
        assertInstanceOf(FreeSpaceManagerImpl.class, tableOperationFactory.getFreeSpaceManager(null));
    }
}