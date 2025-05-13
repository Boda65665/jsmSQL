package org.example.Backend.TableStorageManager.FreeSpaceManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.Models.FreeMemoryInfo;

public abstract class FreeSpaceManager {
    protected final DbManager<Integer, Integer> freeSpace;

    public FreeSpaceManager(DbManager<Integer, Integer> btreeManager) {
        this.freeSpace = btreeManager;
    }

    public abstract FreeMemoryInfo getInsertionPoint(int length);

    public abstract boolean freeSpaceIsOver();

    public abstract void redactFreeSpace(int length, int countFreeBytes);

    public abstract void addFreeSpace(int length, int countFreeBytes);
}
