package org.example.Backend.TableStorageManager.RecordManager.RecordSaver;

import org.example.Backend.Models.Record;

public interface RecordSaver {
    int save(String tableName, Record data);
}
