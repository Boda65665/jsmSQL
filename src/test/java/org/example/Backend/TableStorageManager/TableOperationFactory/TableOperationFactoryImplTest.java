package org.example.Backend.TableStorageManager.TableOperationFactory;

import org.example.Backend.TableStorageManager.TableCreater.TableCraterImpl;
import org.example.Backend.TableStorageManager.TableDeleater.TableDeleterImpl;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProviderImpl;
import org.example.Backend.TableStorageManager.TableReader.TableReaderImpl;
import org.example.Backend.TableStorageManager.TableWriter.TableWriterImpl;
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
}