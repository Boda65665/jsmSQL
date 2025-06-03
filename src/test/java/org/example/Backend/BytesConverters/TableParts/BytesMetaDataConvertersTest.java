package org.example.Backend.BytesConverters.TableParts;

import org.example.Backend.DataToBytesConverter.TableParts.BytesMetaDataConverters;
import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.ColumnType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.example.Backend.Utils.ByteUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class BytesMetaDataConvertersTest {
    private final BytesMetaDataConverters metaDatConverters = new BytesMetaDataConverters();
    private final ColumnTypeBytesConverter<String> stingBytesConverter = (ColumnTypeBytesConverter<String>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.VARCHAR);
    private final ColumnTypeBytesConverter<Boolean> booleanConverter = (ColumnTypeBytesConverter<Boolean>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.BOOLEAN);

    @Test
    void toBytes() {
        TableMetaData tmd = generateTestDataForToConvertToBytes();

        List<Byte> excepted = toBytes(tmd.getColumnStructList());
        List<Byte> result = metaDatConverters.toBytes(tmd);
        assertEquals(excepted, result);
    }

    private List<Byte> toBytes(List<ColumnStruct> columnsStruct) {
        int countColumn = columnsStruct.size();
        List<Byte> result = intToTwoByteList(countColumn);
        for (ColumnStruct columnStruct : columnsStruct) {
            byte[] bytesName = stingBytesConverter.toBytes(columnStruct.getColumnName());

            addBytesLengthColumnName(result, bytesName.length);
            addBytesColumnName(result, bytesName);
            addBytesTypeColumn(result, columnStruct.getType());
            addBytesIsPrimaryKey(result, columnStruct.isPrimary());
        }
        return result;
    }

    private void addBytesIsPrimaryKey(List<Byte> result, boolean primary) {
        byte[] bytesIsPrimary = booleanConverter.toBytes(primary);
        addArrayToList(result, bytesIsPrimary);
    }

    private void addBytesLengthColumnName(List<Byte> result, int length) {
        List<Byte> bytesLength = intToTwoByteList(length);
        result.addAll(bytesLength);
    }

    private void addBytesColumnName(List<Byte> result, byte[] bytesName) {
        addArrayToList(result, bytesName);
    }

    private void addBytesTypeColumn(List<Byte> result, ColumnType columnType) {
        result.addAll(intToTwoByteList(columnType.ordinal()));
    }

    private TableMetaData generateTestDataForToConvertToBytes() {
        List<ColumnStruct> columnStructs = new ArrayList<>();

        columnStructs.add(new ColumnStruct("fourth", ColumnType.INT, true));
        columnStructs.add(new ColumnStruct("fifth", ColumnType.LONG, false));
        columnStructs.add(new ColumnStruct("second", ColumnType.DOUBLE, false));
        columnStructs.add(new ColumnStruct("sixth", ColumnType.BOOLEAN, false));
        columnStructs.add(new ColumnStruct("first", ColumnType.VARCHAR, true));
        columnStructs.add(new ColumnStruct("third", ColumnType.DATE, true));
        return new TableMetaData(columnStructs, null);
    }

    @Test
    void  toData() {
        TableMetaData tmd = generateTestDataForToConvertToBytes();
        List<Byte> testData = toBytes(tmd.getColumnStructList());

        assertEquals(tmd, metaDatConverters.toData(byteListToArray(testData)));
    }

    @Test
    void nullOrEmptyArrayToData() {
        assertThrows(IllegalArgumentException.class, () -> metaDatConverters.toData(null));
        assertThrows(IllegalArgumentException.class, () -> metaDatConverters.toData(new byte[]{}));
    }

    @Test
    void nullToBytes() {
        assertThrows(NullPointerException.class, () -> metaDatConverters.toBytes(null));
    }
}

