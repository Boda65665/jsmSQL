package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerCloser;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.Models.TabularData;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactoryImpl;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TableManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableManager.TableOperationFactory.TableOperationFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TableStorageManagerTest {
    private static final String baseDbPath = "/test";
    private static final DbManagerFactory DB_MANAGER_FACTORY = DbManagerFactoryImpl.getDbManagerFactory();
    private static final FragmentOperationFactory fragmentOperationFactory = new FragmentOperationFactoryImpl();
    private static final TableStorageManager tableStorageManager =
            new TableStorageManager(baseDbPath, DB_MANAGER_FACTORY, new TableOperationFactoryImpl(), fragmentOperationFactory);
    private static final DbManagerFactoryImpl dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private static final String basePath = System.getProperty("user.dir") + File.separator + "test";
    private static final DbManagerCloser dbManagerCloser = new DbManagerCloser(dbManagerFactory);
    private static final String NAME_TABLE = "test_table";
    private static final DbManager freeSpace = dbManagerFactory.getDbManager(basePath, "freeSpace_"+NAME_TABLE);
    private static final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tableOperationFactory.getTablePathProvider());

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
        freeSpace.clear();
    }

    @AfterAll
    static void tearDown() {
        dbManagerCloser.closeAll();
    }

    @Test
    void validCreateTable() {
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.createTable(null, null));
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.createTable("", null));
        assertThrows(NullPointerException.class, () -> tableStorageManager.createTable("not_emtpy_and_null", null));
    }

    @Test
    void validSave() {
        assertThrows(NullPointerException.class, () -> tableStorageManager.save(null, null));
        assertThrows(NullPointerException.class, () -> tableStorageManager.save(NAME_TABLE, null));
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.save("", new TabularData(new ArrayList<>())));

    }
}