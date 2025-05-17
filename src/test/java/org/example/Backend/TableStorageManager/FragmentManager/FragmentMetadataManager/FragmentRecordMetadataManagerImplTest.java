package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetadataManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerImpl;
import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetadataFragmentRecordManagerImpl;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactory;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class FragmentRecordMetadataManagerImplTest {
    @InjectMocks
    private MetadataFragmentRecordManagerImpl metadataFragmentRecordManager;
    @Mock
    private FreeSpaceManagerFactory freeSpaceManagerFactory;
    private final String NAME_TABLE = "test";
    private final int LENGTH_METADATA = 8;
    private DbManager freeSpace;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        freeSpace = new DbManagerImpl("test", "test");
        freeSpace.clear();
    }

    @AfterEach
    void tearDown() {
        freeSpace.close();
    }

    @Test
    void getFragmentMetaDataWhenCanFitIntoOneFragmentInfo() {
        int countFreeBytes = 50;
        int positionFreeSpace = 10;
        freeSpace.put(countFreeBytes, positionFreeSpace);
        mockFreeSpaceManagerFactory();
        int lengthFragment = 20;

        FragmentMetaDataInfo fragmentMetaDataInfo = metadataFragmentRecordManager.getFragmentMetaDataInfo(NAME_TABLE, lengthFragment);
        assertEqualsFragmentMetadataInfo(new FragmentMetaDataInfo(positionFreeSpace, lengthFragment - LENGTH_METADATA, -2), fragmentMetaDataInfo);
    }

    private void mockFreeSpaceManagerFactory() {
        FreeSpaceManagerImpl freeSpaceManager = new FreeSpaceManagerImpl(freeSpace);
        when(freeSpaceManagerFactory.getFreeSpaceManager(anyString())).thenReturn(freeSpaceManager);
    }
    
    private void assertEqualsFragmentMetadataInfo(FragmentMetaDataInfo excepted, FragmentMetaDataInfo actual) {
        assertEquals(excepted.getPositionFragment(), actual.getPositionFragment());
        assertEquals(excepted.getLengthDataFragment(), actual.getLengthDataFragment());
        assertEquals(excepted.getLinkOnNextFragment(), actual.getLinkOnNextFragment());
    }

    @Test
    void getFragmentMetaDataWhenCantFitIntoOneFragmentInfo() {
        freeSpace.put(10, 1);
        freeSpace.put(60, 2);
        mockFreeSpaceManagerFactory();
        int lengthFragment = 70;

        FragmentMetaDataInfo fragmentMetaDataInfo = metadataFragmentRecordManager.getFragmentMetaDataInfo(NAME_TABLE, lengthFragment);
        assertEqualsFragmentMetadataInfo(new FragmentMetaDataInfo(2, 52, 1), fragmentMetaDataInfo);
    }

    @Test
    void getFragmentMetaDataInfoWhenNotEnoughFreeSpace() {
        int lengthFragment = 20;
        freeSpace.put(10, 1);
        mockFreeSpaceManagerFactory();

        FragmentMetaDataInfo fragmentMetaDataInfo = metadataFragmentRecordManager.getFragmentMetaDataInfo(NAME_TABLE, lengthFragment);
        assertEqualsFragmentMetadataInfo(new FragmentMetaDataInfo(1, 2, -1), fragmentMetaDataInfo);
    }

    @Test
    void getFragmentMetaDataInfoWhenNotThereFreeSpace() {
        int lengthFragment = 20;
        mockFreeSpaceManagerFactory();

        FragmentMetaDataInfo fragmentMetaDataInfo = metadataFragmentRecordManager.getFragmentMetaDataInfo(NAME_TABLE, lengthFragment);
        assertEqualsFragmentMetadataInfo(new FragmentMetaDataInfo(-1, lengthFragment - LENGTH_METADATA, -2), fragmentMetaDataInfo);
    }
}