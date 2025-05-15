package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.Models.FreeMemoryInfo;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactory;

public class MetadataFragmentRecordManagerImpl implements MetaDataFragmentManager {
    private final int LENGTH_INDICATOR_BYTE_COUNT = 4;
    private final int LENGTH_LINK_BYTE_COUNT = 4;
    private final int LENGTH_METADATA_BYTE_COUNT = LENGTH_INDICATOR_BYTE_COUNT + LENGTH_LINK_BYTE_COUNT;
    private final FreeSpaceManagerFactory freeSpaceManagerFactory;

    public MetadataFragmentRecordManagerImpl(FreeSpaceManagerFactory freeSpaceManagerFactory) {
        this.freeSpaceManagerFactory = freeSpaceManagerFactory;
    }

    @Override
    public FragmentMetaDataInfo getFragmentMetaDataInfo(String nameTable, int lengthDataFragment) {
        FreeSpaceManager freeSpaceManager = freeSpaceManagerFactory.getFreeSpaceManager(nameTable);


        int maxLengthFragmentsBytes = getMaxLengthFragmentsBytes(lengthDataFragment);
        FreeMemoryInfo freeMemoryInfo = getCountFreeBytes(maxLengthFragmentsBytes, freeSpaceManager);
        if (freeMemoryInfo == null) return new FragmentMetaDataInfo(-1, lengthDataFragment + LENGTH_METADATA_BYTE_COUNT, -2);

        freeSpaceManager.redactFreeSpace(maxLengthFragmentsBytes, freeMemoryInfo.getCountFreeBytes());

        Integer positionNextFragment = getPositionNextFragment(maxLengthFragmentsBytes - freeMemoryInfo.getCountFreeBytes(), freeSpaceManager);
        return new FragmentMetaDataInfo(freeMemoryInfo.getPosition(), Math.min(lengthDataFragment, freeMemoryInfo.getCountFreeBytes()), positionNextFragment);
    }

    private int getMaxLengthFragmentsBytes(int countBytesInLengthFragment) {
        return countBytesInLengthFragment + LENGTH_METADATA_BYTE_COUNT;
    }

    private FreeMemoryInfo getCountFreeBytes(int lengthFragment, FreeSpaceManager freeSpaceManager) {
        return freeSpaceManager.getInsertionPoint(lengthFragment);
    }

    private Integer getPositionNextFragment(int lengthNextFragment, FreeSpaceManager freeSpaceManager) {
        if (lengthNextFragment <= 0) return -2;
        int maxLength = getMaxLengthFragmentsBytes(lengthNextFragment);
        FreeMemoryInfo freeMemoryInfo = freeSpaceManager.getInsertionPoint(maxLength);
        if (freeMemoryInfo == null) return -1;
        return freeMemoryInfo.getPosition();
    }
}
