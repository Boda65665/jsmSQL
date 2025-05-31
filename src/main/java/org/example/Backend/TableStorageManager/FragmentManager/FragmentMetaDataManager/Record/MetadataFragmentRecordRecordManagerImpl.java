package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.Record;

import org.example.Backend.Models.MetaDataFragment;
import org.example.Backend.Models.FreeMemoryInfo;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.Factory.FreeSpaceManagerFactory;

import static org.example.Backend.TableStorageManager.FragmentManager.FragmentStructureConstants.LENGTH_METADATA_BYTE_COUNT;

public class MetadataFragmentRecordRecordManagerImpl implements MetaDataFragmentRecordManager {
    private final FreeSpaceManagerFactory freeSpaceManagerFactory;

    public MetadataFragmentRecordRecordManagerImpl(FreeSpaceManagerFactory freeSpaceManagerFactory) {
        this.freeSpaceManagerFactory = freeSpaceManagerFactory;
    }

    @Override
    public MetaDataFragment getMetaDataNewFragment(String nameTable, int lengthData) {
        FreeSpaceManager freeSpaceManager = freeSpaceManagerFactory.getFreeSpaceManager(nameTable);

        int maxLengthFragmentsBytes = getMaxLengthFragmentsBytes(lengthData);
        FreeMemoryInfo freeMemoryInfo = getCountFreeBytes(maxLengthFragmentsBytes, freeSpaceManager);
        if (freeMemoryInfo == null) return new MetaDataFragment(-1, getLengthDataFragment(lengthData), -2);

        freeSpaceManager.redactFreeSpace(maxLengthFragmentsBytes, freeMemoryInfo.getCountFreeBytes());

        Integer linkOnNextFragment = getPositionNextFragment(maxLengthFragmentsBytes - freeMemoryInfo.getCountFreeBytes(), freeSpaceManager);
        return new MetaDataFragment(freeMemoryInfo.getPosition(),
                getLengthDataFragment(Math.min(lengthData, freeMemoryInfo.getCountFreeBytes())), linkOnNextFragment);
    }

    private int getLengthDataFragment(int lengthData) {
        return lengthData - LENGTH_METADATA_BYTE_COUNT;
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
