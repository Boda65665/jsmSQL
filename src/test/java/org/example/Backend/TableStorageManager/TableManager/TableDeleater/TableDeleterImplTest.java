package org.example.Backend.TableStorageManager.TableManager.TableDeleater;

import org.example.Backend.Models.TableMetaData;
import org.example.Backend.TableStorageManager.TableManager.TableDeleater.TableDeleter;
import org.example.Backend.TableStorageManager.TableManager.TableDeleater.TableDeleterImpl;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TableManager.TableCreater.TableCrater;
import org.example.Backend.TableStorageManager.TableManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableManager.TableOperationFactory.TableOperationFactoryImpl;
import org.example.Backend.TableStorageManager.TableManager.TablePathProvider.TablePathProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class TableDeleterImplTest {
    private final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();
    private final TableCrater tableCrater = tableOperationFactory.getTableCrater();
    private final TablePathProvider tablePathProvider = tableOperationFactory.getTablePathProvider();
    private final TableDeleter tableDeleter = new TableDeleterImpl(tablePathProvider);
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tablePathProvider);
    private final String NAME_TABLE = "test_table";

    @BeforeEach
    void setUp() {
        testHelperTSM.delete(NAME_TABLE);
    }

    @Test
    void delete() {
        String pathTable = tablePathProvider.getTablePath(NAME_TABLE);

        tableCrater.create(NAME_TABLE, new TableMetaData(new ArrayList<>(), "test"));
        assertTrue(new File(pathTable).exists());

        tableDeleter.delete(NAME_TABLE);
        assertFalse(new File(pathTable).exists());
    }
}