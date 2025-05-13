package org.example.Backend.TableStorageManager.FreeSpaceManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.Models.FreeMemoryInfo;

public class FreeSpaceManagerImpl extends FreeSpaceManager {

    public FreeSpaceManagerImpl(DbManager<Integer, Integer> btreeManager) {
        super(btreeManager);
    }

    @Override
    public FreeMemoryInfo getInsertionPoint(int length) {
        Integer countFreeBytes = getMoreSuitablePlace(length);
        if (countFreeBytes == null) return null;
        return new FreeMemoryInfo(countFreeBytes, freeSpace.get(countFreeBytes));
    }

    private Integer getMoreSuitablePlace(int length) {
        Integer countFreeBytes = freeSpace.get(length);
        if (countFreeBytes != null) return length;

        countFreeBytes = freeSpace.higherKey(length);
        if (countFreeBytes == null) countFreeBytes = freeSpace.maxKey();
        return countFreeBytes;
    }

    @Override
    public boolean freeSpaceIsOver() {
        return freeSpace.size() == 0;
    }

    @Override
    public void redactFreeSpace(int length, int countFreeBytes) {
        int newCountFreeBytes = countFreeBytes - length;
        Integer offset = freeSpace.get(countFreeBytes);
        if (offset == null) return;

        freeSpace.delete(countFreeBytes);
        if (newCountFreeBytes < 10) return;
        freeSpace.put(newCountFreeBytes, offset + length);
    }

    @Override
    public void addFreeSpace(int length, int position) {
        freeSpace.put(length, position);
    }
}
