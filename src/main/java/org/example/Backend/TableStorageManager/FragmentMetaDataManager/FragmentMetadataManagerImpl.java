package org.example.Backend.TableStorageManager.FragmentMetaDataManager;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.Models.FreeMemoryInfo;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public class FragmentMetadataManagerImpl implements FragmentMetaDataManager {
    private final int LENGTH_INDICATOR_BYTE_COUNT = 4;
    private final int LENGTH_LINK_BYTE_COUNT = 4;
    private final int LENGTH_METADATA_BYTE_COUNT = LENGTH_INDICATOR_BYTE_COUNT + LENGTH_LINK_BYTE_COUNT;

    @Override
    public FragmentMetaDataInfo getFragmentMetaDataInfo(FreeSpaceManager freeSpaceManager, int lengthDataFragment) {
        int maxLengthFragmentsBytes = getMaxLengthFragmentsBytes(lengthDataFragment);
        FreeMemoryInfo freeMemoryInfo = getCountFreeBytes(maxLengthFragmentsBytes, freeSpaceManager);
        if (freeMemoryInfo == null) return new FragmentMetaDataInfo(-1, lengthDataFragment + LENGTH_METADATA_BYTE_COUNT, -2);

        freeSpaceManager.adjustFreeSpace(maxLengthFragmentsBytes, freeMemoryInfo.getCountFreeBytes());

        Integer positionNextFragment = getPositionNextFragment(maxLengthFragmentsBytes - freeMemoryInfo.getCountFreeBytes(), freeSpaceManager);
        return new FragmentMetaDataInfo(freeMemoryInfo.getPosition(), Math.min(lengthDataFragment, freeMemoryInfo.getCountFreeBytes()), positionNextFragment);
    }

    private Integer getPositionNextFragment(int lengthNextFragment, FreeSpaceManager freeSpaceManager) {
        if (lengthNextFragment <= 0) return null;
        int maxLength = getMaxLengthFragmentsBytes(lengthNextFragment);
        FreeMemoryInfo freeMemoryInfo = freeSpaceManager.getInsertionPoint(maxLength);
        if (freeMemoryInfo == null) return -1;
        return freeMemoryInfo.getPosition();
    }

    private FreeMemoryInfo getCountFreeBytes(int lengthFragment, FreeSpaceManager freeSpaceManager) {
        return freeSpaceManager.getInsertionPoint(lengthFragment);
    }

    private int getMaxLengthFragmentsBytes(int countBytesInLengthFragment) {
        return countBytesInLengthFragment + LENGTH_METADATA_BYTE_COUNT;
    }
}
