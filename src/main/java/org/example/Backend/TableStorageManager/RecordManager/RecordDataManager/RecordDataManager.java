package org.example.Backend.TableStorageManager.RecordManager.RecordDataManager;


import org.example.Backend.Exception.ValidationException;
import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.Record;

import java.util.List;

public interface RecordDataManager {
    void validData(List<ColumnStruct> columnStructList, Record record) throws ValidationException;
    Record editDataForSave(List<ColumnStruct> columnStructList, Record record);
}
