package org.example.Backend.TableStorageManager.BinaryConverter;

import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TabularData;

public interface TableBinaryConverterInterface {
    TabularData fromByteToTabularData(byte[] dataBinary);

    byte[] fromTabularDataToByte(TabularData tabularData);

    TableMetaData fromByteToTableStruct(byte[] dataBinary);

    byte[] fromTableStructToBinary(TableMetaData tableStruct);
}
