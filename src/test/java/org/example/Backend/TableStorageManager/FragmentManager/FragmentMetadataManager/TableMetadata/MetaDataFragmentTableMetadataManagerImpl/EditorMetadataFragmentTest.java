package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetadataManager.TableMetadata.MetaDataFragmentTableMetadataManagerImpl;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerImpl;
import org.example.Backend.Models.Interval;
import org.example.Backend.TableStorageManager.FileManager.Factory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.Factory.FileOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManagerImpl.EditorMetadataFragment;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentStructureConstants;
import org.example.Backend.TableStorageManager.IndexManager.Factory.IndexManagerFactory;
import org.example.Backend.TableStorageManager.IndexManager.IndexManager;
import org.example.Backend.TableStorageManager.IndexManager.IndexManagerImpl;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EditorMetadataFragmentTest {
    private IndexManagerFactory indexManagerFactory;

    private final FileOperationFactory fileOperationFactory = new FileOperationFactoryImpl();
    private DbManager dbManager;
    private final FileWriter fileWriter = fileOperationFactory.getFileWriter();
    private IndexManager<Integer, Interval> indexManager;
    private final String NAME_TABLE = "test";
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(fileOperationFactory.getFilePathProvider());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dbManager = new DbManagerImpl("test", System.getProperty("user.dir") + "/test");
        dbManager.clear();

        indexManager = new IndexManagerImpl(dbManager);
    }

    @Test
    void editAfterCreateNewFragment() {
        addTwoNonOverlappingRandomFragments();
        mockIndexManagerFactory();
        EditorMetadataFragment editorMetadataFragment = getEditorMetadataFragment();

        editorMetadataFragment.editAfterCreateNewFragment(NAME_TABLE);

        int indexPenultFragment = indexManager.size() - 2;
        Interval positionPenultFragment = indexManager.getIndex(indexPenultFragment);
        Interval positionLastFragment = indexManager.getLast();

        int positionLink = positionPenultFragment.getEnd();
        byte[] exceptedLinkBytes = intToBytes(positionLastFragment.getStart());

        byte[] resultLinkBytes = testHelperTSM.read(NAME_TABLE, positionLink, FragmentStructureConstants.LENGTH_LINK_BYTE_COUNT);
        assertArrayEquals(exceptedLinkBytes, resultLinkBytes);
    }

    private byte[] intToBytes(int value) {
        return new byte[] {
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    private EditorMetadataFragment getEditorMetadataFragment() {
        return new EditorMetadataFragment(indexManagerFactory, fileWriter);
    }

    private void addTwoNonOverlappingRandomFragments() {
        Random random = new Random();

        int startIndexPenultFragment = random.nextInt(10);
        int endIndexPenultFragment = random.nextInt(10) + startIndexPenultFragment;
        Interval positionPenultFragment = new Interval(startIndexPenultFragment, endIndexPenultFragment);

        int startIndexLastFragment = random.nextInt(10) + endIndexPenultFragment;
        int endIndexLastFragment = random.nextInt(10) + startIndexLastFragment;
        Interval positionLastFragment = new Interval(startIndexLastFragment, endIndexLastFragment);

        indexManager.addIndex(0, positionPenultFragment);
        indexManager.addIndex(1, positionLastFragment);
    }

    private void mockIndexManagerFactory() {
        indexManagerFactory = mock(IndexManagerFactory.class);
        when(indexManagerFactory.getIndexManager(NAME_TABLE)).thenReturn(indexManager);
    }
}