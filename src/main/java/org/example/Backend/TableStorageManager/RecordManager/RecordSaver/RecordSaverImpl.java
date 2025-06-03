package org.example.Backend.TableStorageManager.RecordManager.RecordSaver;

import org.example.Backend.DataToBytesConverter.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.Models.Record;
import org.example.Backend.Models.TablePartType;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import java.util.ArrayList;

public class RecordSaverImpl implements RecordSaver {
    private final FragmentSaver fragmentSaver;

    public RecordSaverImpl(FragmentSaver fragmentSaver) {
        this.fragmentSaver = fragmentSaver;
    }

    public int save(String tableName, Record data) {
        TablePartTypeConverter<Record> tabularDataConverter =  BytesConverterFactory.getTablePartTypeConverter(TablePartType.RECORD);
        ArrayList<Byte> bytesData = tabularDataConverter.toBytes(data);
        return fragmentSaver.save(tableName, bytesData);
    }
}
