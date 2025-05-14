package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerCloser;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.Models.Record;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactory;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactoryImpl;
import org.example.Backend.TableStorageManager.RecordManager.RecordOperationFactory.RecordOperationFactoryImpl;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TableStorageManagerTest {
    private static final String NAME_TABLE = "test_table";
    private static final String basePath = System.getProperty("user.dir") + File.separator + "test";
    private static final String PREFIX_NAME_FREESPACE = "freeSpace_";

    private static final DbManagerFactoryImpl dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private static final FreeSpaceManagerFactory freeSpaceManagerFactory = new FreeSpaceManagerFactoryImpl(basePath, dbManagerFactory);
    private static final FragmentOperationFactory fragmentOperationFactory = new FragmentOperationFactoryImpl(freeSpaceManagerFactory);
    private static final RecordOperationFactoryImpl recordOperationFactory = new RecordOperationFactoryImpl();
    private static final TableStorageManager tableStorageManager =
            new TableStorageManager(new FileOperationFactoryImpl(), fragmentOperationFactory, recordOperationFactory);
    private static final DbManagerCloser dbManagerCloser = new DbManagerCloser(dbManagerFactory);
    private static final DbManager freeSpace = dbManagerFactory.getDbManager(basePath, PREFIX_NAME_FREESPACE + NAME_TABLE);
    private static final FileOperationFactory FILE_OPERATION_FACTORY = new FileOperationFactoryImpl();
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(FILE_OPERATION_FACTORY.getFilePathProvider());

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
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.save("", new Record(new ArrayList<>())));

    }
}