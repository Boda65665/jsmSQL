package org.example.Backend.TableStorageManager.FreeSpaceManager.Factory;

import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public interface FreeSpaceManagerFactory {
    FreeSpaceManager getFreeSpaceManager(String nameTable);
}
