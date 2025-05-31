package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetadataManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerImpl;
import org.example.Backend.Models.MetaDataFragment;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.Record.MetadataFragmentRecordRecordManagerImpl;
import org.example.Backend.TableStorageManager.FreeSpaceManager.Factory.FreeSpaceManagerFactory;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.example.Backend.TableStorageManager.FragmentManager.FragmentStructureConstants.LENGTH_METADATA_BYTE_COUNT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class FragmentRecordMetadataManagerImplTest {
    @InjectMocks
    private MetadataFragmentRecordRecordManagerImpl metadataFragmentRecordManager;
    @Mock
    private FreeSpaceManagerFactory freeSpaceManagerFactory;
    private final String NAME_TABLE = "test";
    private DbManager freeSpace;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        freeSpace = new DbManagerImpl("test", System.getProperty("user.dir") + "/test");
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

        MetaDataFragment metaDataFragment = metadataFragmentRecordManager.getMetaDataNewFragment(NAME_TABLE, lengthFragment);
        assertEqualsFragmentMetadataInfo(new MetaDataFragment(positionFreeSpace, lengthFragment - LENGTH_METADATA_BYTE_COUNT, -2), metaDataFragment);
    }

    private void mockFreeSpaceManagerFactory() {
        FreeSpaceManagerImpl freeSpaceManager = new FreeSpaceManagerImpl(freeSpace);
        when(freeSpaceManagerFactory.getFreeSpaceManager(anyString())).thenReturn(freeSpaceManager);
    }
    
    private void assertEqualsFragmentMetadataInfo(MetaDataFragment excepted, MetaDataFragment actual) {
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

        MetaDataFragment metaDataFragment = metadataFragmentRecordManager.getMetaDataNewFragment(NAME_TABLE, lengthFragment);
        assertEqualsFragmentMetadataInfo(new MetaDataFragment(2, 52, 1), metaDataFragment);
    }

    @Test
    void getFragmentMetaDataInfoWhenNotEnoughFreeSpace() {
        int lengthFragment = 20;
        freeSpace.put(10, 1);
        mockFreeSpaceManagerFactory();

        MetaDataFragment metaDataFragment = metadataFragmentRecordManager.getMetaDataNewFragment(NAME_TABLE, lengthFragment);
        assertEqualsFragmentMetadataInfo(new MetaDataFragment(1, 2, -1), metaDataFragment);
    }

    @Test
    void getFragmentMetaDataInfoWhenNotThereFreeSpace() {
        int lengthFragment = 20;
        mockFreeSpaceManagerFactory();

        MetaDataFragment metaDataFragment = metadataFragmentRecordManager.getMetaDataNewFragment(NAME_TABLE, lengthFragment);
        assertEqualsFragmentMetadataInfo(new MetaDataFragment(-1, lengthFragment - LENGTH_METADATA_BYTE_COUNT, -2), metaDataFragment);
    }
}