package org.example.Backend.TableStorageManager.BinaryConverter;

import org.example.Backend.Models.TableStruct;
import org.example.Backend.Models.TabularData;

public interface BinaryConverterInterface {
    TabularData fromByteToTabularData(byte[] dataBinary);

    byte[] fromTabularDataToByte(TabularData tabularData);

    TableStruct fromByteToTableStruct(String string);

    byte[] fromTableStructToBinary(TableStruct tableStruct);
}
