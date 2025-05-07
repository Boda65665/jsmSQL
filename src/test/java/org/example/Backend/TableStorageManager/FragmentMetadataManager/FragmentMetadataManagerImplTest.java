package org.example.Backend.TableStorageManager.FragmentMetadataManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FragmentMetaDataManager.FragmentMetadataManagerImpl;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class FragmentMetadataManagerImplTest {
    private final FragmentMetadataManagerImpl manager = new FragmentMetadataManagerImpl();
    private final DbManagerFactory dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();
    private FreeSpaceManager freeSpaceManager;
    private DbManager freeSpace;
    private final String basePath = System.getProperty("user.dir") + File.separator + "test";
    private final String NAME_TABLE = "test_table";
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tableOperationFactory.getTablePathProvider());
    private final int LENGTH_INDICATOR_BYTE_COUNT = 4;
    private final int LENGTH_LINK_BYTE_COUNT = 4;
    private final int LENGTH_METADATA_BYTE_COUNT = LENGTH_INDICATOR_BYTE_COUNT + LENGTH_LINK_BYTE_COUNT;

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
        DbManager dbManager = dbManagerFactory.getDbManager(basePath, NAME_TABLE);
        dbManager.clear();

        freeSpace = dbManager;
        freeSpaceManager = tableOperationFactory.getFreeSpaceManager(dbManager);
    }

    @Test
    void getFragmentMetaDataWhenCanFitIntoOneFragmentInfo() {
        int countFreeBytes = 50;
        int positionFreeSpace = 10;
        freeSpace.put(countFreeBytes, positionFreeSpace);
        int lengthFragment = 20;
        FragmentMetaDataInfo fragmentMetaDataInfo = manager.getFragmentMetaDataInfo(freeSpaceManager, lengthFragment);

        assertEquals(positionFreeSpace, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(lengthFragment, fragmentMetaDataInfo.getLengthFragment());
        assertEquals(1, freeSpace.size());
        assertNotNull(freeSpace.get(countFreeBytes - getMaxLengthFragment(lengthFragment)));
        assertNull(fragmentMetaDataInfo.getPositionNextFragment());
    }

    private int getMaxLengthFragment(int lengthFragment) {
        return lengthFragment + LENGTH_METADATA_BYTE_COUNT;
    }

    @Test
    void getFragmentMetaDataWhenCantFitIntoOneFragmentInfo() {
        freeSpace.put(10, 1);
        freeSpace.put(60, 2);
        int lengthFragment = 70;
        FragmentMetaDataInfo fragmentMetaDataInfo = manager.getFragmentMetaDataInfo(freeSpaceManager, lengthFragment);

        assertEquals(2, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(60, fragmentMetaDataInfo.getLengthFragment());
        assertEquals(1, fragmentMetaDataInfo.getPositionNextFragment());
        assertEquals(1, freeSpace.size());
        assertNotNull(freeSpace.get(10));
    }

    @Test
    void getFragmentMetaDataInfoWhenNotEnoughFreeSpace() {
        freeSpace.put(10, 1);
        int lengthFragment = 20;
        FragmentMetaDataInfo fragmentMetaDataInfo = manager.getFragmentMetaDataInfo(freeSpaceManager, lengthFragment);

        assertEquals(1, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(10, fragmentMetaDataInfo.getLengthFragment());
        assertEquals(-1, fragmentMetaDataInfo.getPositionNextFragment());
        assertEquals(0, freeSpace.size());
    }

    @Test
    void getFragmentMetaDataInfoWhenNotThereFreeSpace() {
        int lengthFragment = 20;
        FragmentMetaDataInfo fragmentMetaDataInfo = manager.getFragmentMetaDataInfo(freeSpaceManager, lengthFragment);

        assertEquals(-1, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(20 + LENGTH_METADATA_BYTE_COUNT, fragmentMetaDataInfo.getLengthFragment());
        assertEquals(-2, fragmentMetaDataInfo.getPositionNextFragment());
        assertEquals(0, freeSpace.size());
    }
}