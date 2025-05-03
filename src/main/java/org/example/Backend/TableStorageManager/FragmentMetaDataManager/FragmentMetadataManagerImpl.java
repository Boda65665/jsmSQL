package org.example.Backend.TableStorageManager.FragmentMetaDataManager;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.Models.FreeMemoryInfo;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public class FragmentMetadataManagerImpl implements FragmentMetaDataManager {
    private final int LENGTH_INDICATOR_BYTE_COUNT = 1;
    private final int MAX_LENGTH_LINK_BYTE_COUNT = 8;
    private final int MAX_LENGTH_METADATA_BYTE_COUNT = LENGTH_INDICATOR_BYTE_COUNT * 2 + MAX_LENGTH_LINK_BYTE_COUNT;

    @Override
    public FragmentMetaDataInfo getFragmentMetaDataInfo(FreeSpaceManager freeSpaceManager, int lengthDataFragment) {
        int maxLengthFragmentsBytes = getMaxLengthFragmentsBytes(lengthDataFragment);
        int countFreeBytes = getCountFreeBytes(maxLengthFragmentsBytes, freeSpaceManager);
        freeSpaceManager.adjustFreeSpace(maxLengthFragmentsBytes, countFreeBytes);

        Integer positionNextFragment = getPositionNextFragment(maxLengthFragmentsBytes - countFreeBytes, freeSpaceManager);
        return new FragmentMetaDataInfo(Math.min(lengthDataFragment, countFreeBytes), positionNextFragment);
    }

    private Integer getPositionNextFragment(int lengthNextFragment, FreeSpaceManager freeSpaceManager) {
        if (lengthNextFragment <= 0) return null;
        int maxLength = getMaxLengthFragmentsBytes(lengthNextFragment);
        FreeMemoryInfo freeMemoryInfo = freeSpaceManager.getInsertionPoint(maxLength);
        if (freeMemoryInfo == null) return -1;
        return freeMemoryInfo.getPosition();
    }

    private int getCountFreeBytes(int lengthFragment, FreeSpaceManager freeSpaceManager) {
        FreeMemoryInfo freeMemoryInfo = freeSpaceManager.getInsertionPoint(lengthFragment);
        return freeMemoryInfo.getCountFreeBytes();
    }

    private int getMaxLengthFragmentsBytes(int countBytesInLengthFragment) {
        return countBytesInLengthFragment + MAX_LENGTH_METADATA_BYTE_COUNT;
    }
}
