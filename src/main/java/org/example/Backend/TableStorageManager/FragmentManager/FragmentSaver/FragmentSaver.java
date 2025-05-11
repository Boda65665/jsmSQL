package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import java.util.List;

public interface FragmentSaver {
    int save(String tableName, List<Byte> bytesData);
}
