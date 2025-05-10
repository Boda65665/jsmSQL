package org.example.Backend.TableStorageManager.RecordManager.RecordSaver;

import org.example.Backend.Models.Record;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public interface RecordSaver {
    int save(String tableName, Record data, FreeSpaceManager freeSpaceManager);
}
