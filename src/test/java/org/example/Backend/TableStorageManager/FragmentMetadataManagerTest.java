package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.Models.FragmentMetaData;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class FragmentMetadataManagerTest {
    private final FragmentMetadataManager manager = new FragmentMetadataManager();
    private final DbManagerFactory dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();
    private FreeSpaceManager freeSpaceManager;
    private DbManager freeSpace;
    private final String basePath = System.getProperty("user.dir") + File.separator + "test";
    private final String NAME_TABLE = "test_table";
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tableOperationFactory.getTablePathProvider());
    private final int LENGTH_INDICATOR_BYTE_COUNT = 1;
    private final int MAX_LENGTH_LINK_BYTE_COUNT = 8;
    private final int MAX_LENGTH_METADATA_BYTE_COUNT = LENGTH_INDICATOR_BYTE_COUNT * 2 + MAX_LENGTH_LINK_BYTE_COUNT;

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
        DbManager dbManager = dbManagerFactory.getDbManager(basePath, NAME_TABLE);
        dbManager.clear();

        freeSpace = dbManager;
        freeSpaceManager = tableOperationFactory.getFreeSpaceManager(dbManager);
    }

    @Test
    void getFragmentMetaDataWhenCanFitIntoOneFragment() {
        int countFreeBytes = 50;
        int positionFreeSpace = 10;
        freeSpace.put(countFreeBytes, positionFreeSpace);
        int lengthFragment = 20;
        FragmentMetaData fragmentMetaData = manager.getFragmentMetaData(freeSpaceManager, lengthFragment);

        assertEquals(lengthFragment, fragmentMetaData.getLengthFragment());
        assertEquals(1, freeSpace.size());
        assertNotNull(freeSpace.get(countFreeBytes - getMaxLengthFragment(lengthFragment)));
        assertNull(fragmentMetaData.getPositionNextFragment());
    }

    private int getMaxLengthFragment(int lengthFragment) {
        return lengthFragment + MAX_LENGTH_METADATA_BYTE_COUNT;
    }

    @Test
    void getFragmentMetaDataWhenCantFitIntoOneFragment() {
        freeSpace.put(10, 1);
        freeSpace.put(60, 2);
        int lengthFragment = 70;
        FragmentMetaData fragmentMetaData = manager.getFragmentMetaData(freeSpaceManager, lengthFragment);

        assertEquals(60, fragmentMetaData.getLengthFragment());
        assertEquals(1, fragmentMetaData.getPositionNextFragment());
        assertEquals(1, freeSpace.size());
        assertNotNull(freeSpace.get(10));
    }

    @Test
    void getFragmentMetaDataWhenNotEnoughFreeSpace() {
        freeSpace.put(10, 1);
        int lengthFragment = 20;
        FragmentMetaData fragmentMetaData = manager.getFragmentMetaData(freeSpaceManager, lengthFragment);
        assertEquals(10, fragmentMetaData.getLengthFragment());
        assertEquals(-1, fragmentMetaData.getPositionNextFragment());
        assertEquals(0, freeSpace.size());
    }
}