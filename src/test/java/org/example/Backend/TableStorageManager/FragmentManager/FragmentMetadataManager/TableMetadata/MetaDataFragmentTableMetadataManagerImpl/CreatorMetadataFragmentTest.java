package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetadataManager.TableMetadata.MetaDataFragmentTableMetadataManagerImpl;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerImpl;
import org.example.Backend.Models.Interval;
import org.example.Backend.Models.MetaDataFragmentTableMetadata;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileLength;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManagerImpl.CreatorMetadataFragment;
import org.example.Backend.TableStorageManager.IndexManager.Factory.IndexManagerFactory;
import org.example.Backend.TableStorageManager.IndexManager.IndexManager;
import org.example.Backend.TableStorageManager.IndexManager.IndexManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.example.Backend.TableStorageManager.FragmentManager.FragmentStructureConstants.LENGTH_LINK_BYTE_COUNT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CreatorMetadataFragmentTest {
    @InjectMocks
    private CreatorMetadataFragment creatorNewFragment;
    @Mock
    private IndexManagerFactory indexManagerFactory;
    @Mock
    private GetterFileLength getterFileLength;

    private DbManager dbManager;
    private IndexManager<Integer, Interval> indexManager;
    private final String NAME_TABLE = "test";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dbManager = new DbManagerImpl("test", System.getProperty("user.dir") + "/test");
        dbManager.clear();

        indexManager = new IndexManagerImpl(dbManager);

    }

    @AfterEach
    void tearDown() {
        dbManager.close();
    }

    @Test
    void shouldCreateNewFragment_whenPreviousFragmentCanBeContinued() {
        int lengthFile = 10;
        int positionEndLastFragment = lengthFile - 1; //last entry is last fragment
        mockGetFileLength(lengthFile);
        mockGetIndexManagerFactory(positionEndLastFragment);

        MetaDataFragmentTableMetadata metadata = creatorNewFragment.createNewFragment(NAME_TABLE);

        int positionNewFragment = lengthFile - LENGTH_LINK_BYTE_COUNT; //Продолжаем предыдущий фгармент ,удалив ссылку с конца предыдущего фрагмента
        assertEquals(positionNewFragment, metadata.getPositionFragment());

        assertTrue(metadata.isContinuePreviousFragment());
    }

    private void mockGetIndexManagerFactory(int lengthFragment) {
        indexManager.addIndex(1, new Interval(0, lengthFragment));

        when(indexManagerFactory.getIndexManager(NAME_TABLE)).thenReturn(indexManager);
    }

    private void mockGetFileLength(int length) {
        when(getterFileLength.getLength(NAME_TABLE)).thenReturn(length);
    }

    @Test
    void shouldCreateNewFragment_whenPreviousFragmentCannotBeContinued() {
        int lengthFile = 10;
        int positionEndLastFragment = 10 - 5;//last entry is not last fragment
        mockGetFileLength(lengthFile);
        mockGetIndexManagerFactory(positionEndLastFragment);

        MetaDataFragmentTableMetadata metadata = creatorNewFragment.createNewFragment(NAME_TABLE);

        assertEquals(lengthFile, metadata.getPositionFragment());
        assertFalse(metadata.isContinuePreviousFragment());
    }
}