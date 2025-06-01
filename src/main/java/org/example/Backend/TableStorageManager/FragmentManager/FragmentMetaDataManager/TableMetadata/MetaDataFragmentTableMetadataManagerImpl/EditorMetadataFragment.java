package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManagerImpl;

import org.example.Backend.Models.Interval;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.IndexManager.Factory.IndexManagerFactory;
import org.example.Backend.TableStorageManager.IndexManager.IndexManager;

public class EditorMetadataFragment {
    private final IndexManagerFactory indexManagerFactory;
    private final FileWriter fileWriter;

    public EditorMetadataFragment(IndexManagerFactory indexManagerFactory, FileWriter fileWriter) {
        this.indexManagerFactory = indexManagerFactory;
        this.fileWriter = fileWriter;
    }

    public void editAfterCreateNewFragment(String tableName){
        IndexManager<Integer, Interval> indexManager = indexManagerFactory.getIndexManager(tableName);
        Interval positionPenultFragment = getPositionPenultFragment(indexManager, indexManager.size());
        Interval positionLastFragment = indexManager.getLast();

        addLinkToEndPenultFragment(tableName, positionLastFragment, positionPenultFragment);
    }

    private void addLinkToEndPenultFragment(String tableName, Interval positionLastFragment, Interval positionPenultFragment) {
        byte[] linkOnStartNextFragmentBytes = intToBytes(positionLastFragment.getStart());

        fileWriter.write(tableName, linkOnStartNextFragmentBytes, positionPenultFragment.getEnd());
    }

    private byte[] intToBytes(int value) {
        return new byte[] {
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    private Interval getPositionPenultFragment(IndexManager<Integer, Interval> indexManager, int size) {
        int indexPenultFragment = size - 2;
        return indexManager.getIndex(indexPenultFragment);
    }
}
