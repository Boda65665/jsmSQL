package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

import java.util.ArrayList;

public interface FragmentSaver {
    int save(String tableName, ArrayList<Byte> bytesData, FreeSpaceManager freeSpaceManager);
}
