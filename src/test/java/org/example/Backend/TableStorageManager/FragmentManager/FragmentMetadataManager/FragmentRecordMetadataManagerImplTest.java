package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetadataManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerCloser;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetadataFragmentRecordManagerImpl;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactory;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactoryImpl;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactoryImpl;
import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class FragmentRecordMetadataManagerImplTest {
    private static final DbManagerFactory dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private final FileOperationFactory fileOperationFactory = new FileOperationFactoryImpl();
    private final String basePath = System.getProperty("user.dir") + File.separator + "test";
    private final String NAME_TABLE = "test_table";
    private final String prefixName = "freespace_";
    private final FreeSpaceManagerFactory freeSpaceManagerFactory = new FreeSpaceManagerFactoryImpl(basePath, dbManagerFactory);
    private final MetadataFragmentRecordManagerImpl manager = new MetadataFragmentRecordManagerImpl(freeSpaceManagerFactory);
    private DbManager freeSpace;
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(fileOperationFactory.getTablePathProvider());
    private final int LENGTH_INDICATOR_BYTE_COUNT = 4;
    private final int LENGTH_LINK_BYTE_COUNT = 4;
    private final int LENGTH_METADATA_BYTE_COUNT = LENGTH_INDICATOR_BYTE_COUNT + LENGTH_LINK_BYTE_COUNT;
    private static final DbManagerCloser dbManagerCloser = new DbManagerCloser(dbManagerFactory);

    @AfterAll
    static void tearDown() {
        dbManagerCloser.closeAll();
    }

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
        DbManager dbFreeSpaceManager = dbManagerFactory.getDbManager(basePath, prefixName + NAME_TABLE);
        dbFreeSpaceManager.clear();

        freeSpace = dbFreeSpaceManager;
    }

    @Test
    void getFragmentMetaDataWhenCanFitIntoOneFragmentInfo() {
        int countFreeBytes = 50;
        int positionFreeSpace = 10;
        freeSpace.put(countFreeBytes, positionFreeSpace);
        int lengthFragment = 20;
        FragmentMetaDataInfo fragmentMetaDataInfo = manager.getFragmentMetaDataInfo(NAME_TABLE, lengthFragment);

        assertEquals(positionFreeSpace, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(lengthFragment, fragmentMetaDataInfo.getLengthFragment());
        assertEquals(1, freeSpace.size());
        assertNotNull(freeSpace.get(countFreeBytes - getMaxLengthFragment(lengthFragment)));
        assertNull(fragmentMetaDataInfo.getLinkOnNextFragment());
    }

    private int getMaxLengthFragment(int lengthFragment) {
        return lengthFragment + LENGTH_METADATA_BYTE_COUNT;
    }

    @Test
    void getFragmentMetaDataWhenCantFitIntoOneFragmentInfo() {
        freeSpace.put(10, 1);
        freeSpace.put(60, 2);
        int lengthFragment = 70;
        FragmentMetaDataInfo fragmentMetaDataInfo = manager.getFragmentMetaDataInfo(NAME_TABLE, lengthFragment);

        assertEquals(2, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(60, fragmentMetaDataInfo.getLengthFragment());
        assertEquals(1, fragmentMetaDataInfo.getLinkOnNextFragment());
        assertEquals(1, freeSpace.size());
        assertNotNull(freeSpace.get(10));
    }

    @Test
    void getFragmentMetaDataInfoWhenNotEnoughFreeSpace() {
        freeSpace.put(10, 1);
        int lengthFragment = 20;
        FragmentMetaDataInfo fragmentMetaDataInfo = manager.getFragmentMetaDataInfo(NAME_TABLE, lengthFragment);

        assertEquals(1, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(10, fragmentMetaDataInfo.getLengthFragment());
        assertEquals(-1, fragmentMetaDataInfo.getLinkOnNextFragment());
        assertEquals(0, freeSpace.size());
    }

    @Test
    void getFragmentMetaDataInfoWhenNotThereFreeSpace() {
        int lengthFragment = 20;
        FragmentMetaDataInfo fragmentMetaDataInfo = manager.getFragmentMetaDataInfo(NAME_TABLE, lengthFragment);

        assertEquals(-1, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(20 + LENGTH_METADATA_BYTE_COUNT, fragmentMetaDataInfo.getLengthFragment());
        assertEquals(-2, fragmentMetaDataInfo.getLinkOnNextFragment());
        assertEquals(0, freeSpace.size());
    }
}