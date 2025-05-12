package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileSize;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManager;

public class MetadataFragmentTableMetadataManagerImpl implements MetaDataFragmentManager {
    private final PositionTableMetadataManager positionTableMetadataManager;
    private final GetterFileSize getterFileSize;

    public MetadataFragmentTableMetadataManagerImpl(PositionTableMetadataManager positionTableMetadataManager, GetterFileSize getterFileSize) {
        this.positionTableMetadataManager = positionTableMetadataManager;
        this.getterFileSize = getterFileSize;
    }

    @Override
    public FragmentMetaDataInfo getFragmentMetaDataInfo(String nameTable, int lengthDataFragment) {
        int link = 0;//link generate after create new Fragment
        int position = getPosition(nameTable);
        FragmentMetaDataInfo fragmentMetaDataInfo = new FragmentMetaDataInfo(-1, lengthDataFragment, link);
        return null;
    }

    private int getPosition(String nameTable) {
        getterFileSize.getSize(nameTable);
        int positionEndMetadata = positionTableMetadataManager.getEndPosition(nameTable);
        return -1;
    }
}
