package org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory;

import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public interface FreeSpaceManagerFactory {
    FreeSpaceManager getFreeSpaceManager(String nameTable);
}
