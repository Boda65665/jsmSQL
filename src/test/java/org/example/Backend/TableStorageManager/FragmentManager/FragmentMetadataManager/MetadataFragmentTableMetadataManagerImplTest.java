package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetadataManager;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileSize;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetadataFragmentTableMetadataManagerImpl;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class MetadataFragmentTableMetadataManagerImplTest {
    @InjectMocks
    private MetadataFragmentTableMetadataManagerImpl metadataFragmentTableMetadataManager;
    @Mock
    private PositionTableMetadataManager positionTableMetadataManager;
    @Mock
    private GetterFileSize getterFileSize;
    private final int sizeFile = 20;
    private final int LENGTH_LINK_BYTE_COUNT = 4;
    private final int lengthFragment = 10;
    private final String nameTable = "test";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFragmentMetaDataInfoWhenLatestDataIsTableMetaData() {
        mockGetterFileSize();
        mockPositionTableMetadata(sizeFile);

        FragmentMetaDataInfo fragmentMetaDataInfo = metadataFragmentTableMetadataManager.getFragmentMetaDataInfo(nameTable, lengthFragment);
        assertEquals(lengthFragment, fragmentMetaDataInfo.getLengthDataFragment());
        assertEquals(sizeFile - LENGTH_LINK_BYTE_COUNT, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(0, fragmentMetaDataInfo.getLinkOnNextFragment());
    }

    private void mockPositionTableMetadata(int sizeFile) {
        when(positionTableMetadataManager.getEndPosition(anyString())).thenReturn(sizeFile);
    }

    private void mockGetterFileSize() {
        when(getterFileSize.getSize(anyString())).thenReturn(sizeFile);
    }

    @Test
    void getFragmentMetaDataInfoWhenLatestDataIsNotTableMetaData(){
        mockGetterFileSize();
        mockPositionTableMetadata(sizeFile - 1);//sizeFile != endPositionMetaData

        FragmentMetaDataInfo fragmentMetaDataInfo = metadataFragmentTableMetadataManager.getFragmentMetaDataInfo(nameTable, lengthFragment);
        assertEquals(lengthFragment, fragmentMetaDataInfo.getLengthDataFragment());
        assertEquals(-1, fragmentMetaDataInfo.getPositionFragment());
        assertEquals(0, fragmentMetaDataInfo.getLinkOnNextFragment());
    }
}