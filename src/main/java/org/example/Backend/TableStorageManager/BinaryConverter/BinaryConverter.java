package org.example.Backend.TableStorageManager.BinaryConverter;

import org.example.Backend.Models.TableStruct;
import org.example.Backend.Models.TabularData;

public class BinaryConverter implements BinaryConverterInterface {

    @Override
    public TabularData fromByteToTabularData(byte[] dataBinary) {
        return null;
    }

    @Override
    public byte[] fromTabularDataToByte(TabularData tabularData) {
        return new byte[0];
    }

    @Override
    public TableStruct fromByteToTableStruct(String string) {
        return null;
    }

    @Override
    public byte[] fromTableStructToBinary(TableStruct tableStruct) {
        return new byte[0];
    }
}
