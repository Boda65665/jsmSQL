package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.Models.TabularData;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public interface FragmentSaver {
    int save(String tableName, TabularData data, FreeSpaceManager freeSpaceManager);
}
