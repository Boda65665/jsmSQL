package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManagerImpl;

import org.example.Backend.Models.Interval;
import org.example.Backend.Models.MetaDataFragmentTableMetadata;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileLength;
import org.example.Backend.TableStorageManager.IndexManager.Factory.IndexManagerFactory;
import org.example.Backend.TableStorageManager.IndexManager.IndexManager;
import static org.example.Backend.TableStorageManager.FragmentManager.FragmentStructureConstants.LENGTH_LINK_BYTE_COUNT;

public class CreatorMetadataFragment{
    private final IndexManagerFactory indexManagerFactory;
    private final GetterFileLength getterFileLength;

    public CreatorMetadataFragment(IndexManagerFactory indexManagerFactory, GetterFileLength getterFileLength) {
        this.indexManagerFactory = indexManagerFactory;
        this.getterFileLength = getterFileLength;
    }

    public MetaDataFragmentTableMetadata createNewFragment(String nameTable) {
        IndexManager<Integer, Interval> metadataIndexes = indexManagerFactory.getIndexManager(nameTable);
        int lengthFile = getterFileLength.getLength(nameTable);

        boolean continuePreviousFragment = isContinuePreviousFragment(lengthFile, metadataIndexes);
        int position = getPositionNewFragment(lengthFile, continuePreviousFragment);
        return new MetaDataFragmentTableMetadata(position, continuePreviousFragment);
    }

    private boolean isContinuePreviousFragment(int lengthFile, IndexManager<Integer, Interval> metadataIndexes) {
        Interval positionIntervalLastFragment = metadataIndexes.getLast();
        int endPositionLastFragment = positionIntervalLastFragment.getEnd();

        return isLastEntryMetadata(endPositionLastFragment, lengthFile);
    }

    private boolean isLastEntryMetadata(int endPositionLastFragmentMetadata, int lengthFile) {
        int positionLastEntry = lengthFile - 1;
        return endPositionLastFragmentMetadata == positionLastEntry;
    }

    private int getPositionNewFragment(int lengthFile, boolean continuePreviousFragment) {
        if (continuePreviousFragment) return getPositionContinuationLastFragment(lengthFile);
        return lengthFile;
    }

    private int getPositionContinuationLastFragment(int lengthFile) {
        return lengthFile - LENGTH_LINK_BYTE_COUNT; //delete link last fragment
    }
}
