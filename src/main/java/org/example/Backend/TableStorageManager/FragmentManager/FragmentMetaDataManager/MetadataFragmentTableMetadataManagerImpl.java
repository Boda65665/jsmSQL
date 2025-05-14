package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileSize;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManager;
import org.example.Backend.TableStorageManager.TableMetadataManager.TableMetadataOperationFactory.TableMetadataOperationFactory;

public class MetadataFragmentTableMetadataManagerImpl implements MetaDataFragmentManager {
    private final PositionTableMetadataManager positionTableMetadataManager;
    private final GetterFileSize getterFileSize;
    private final int LENGTH_LINK_BYTE_COUNT = 4;

    public MetadataFragmentTableMetadataManagerImpl(PositionTableMetadataManager positionTableMetadataManager, GetterFileSize getterFileSize) {
        this.positionTableMetadataManager = positionTableMetadataManager;
        this.getterFileSize = getterFileSize;
    }

    @Override
    public FragmentMetaDataInfo getFragmentMetaDataInfo(String nameTable, int lengthDataFragment) {
        int link = 0;//link generate after create new Fragment
        int position = getPosition(nameTable);
        return new FragmentMetaDataInfo(position, lengthDataFragment, link);
    }

    private int getPosition(String nameTable) {
        int sizeFile = getterFileSize.getSize(nameTable);
        int positionEndMetadata = positionTableMetadataManager.getEndPosition(nameTable);

        if (isLatestDataTableMetadata(sizeFile, positionEndMetadata)) return sizeFile - LENGTH_LINK_BYTE_COUNT; //continue writing metadata
        return -1;
    }

    private boolean isLatestDataTableMetadata(long sizeFile, long positionEndMetadata) {
        return sizeFile == positionEndMetadata;
    }
}
