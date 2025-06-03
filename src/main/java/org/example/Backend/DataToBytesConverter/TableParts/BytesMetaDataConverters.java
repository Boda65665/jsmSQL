package org.example.Backend.DataToBytesConverter.TableParts;

import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverter.Interface.TablePartTypeConverter;
import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.TableMetaData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.Backend.Utils.ByteUtils.*;
import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.BOOLEAN_DATA_INDICATOR_BYTE_COUNT;
import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.LENGTH_TYPE_INDICATOR_BYTE_COUNT;

public class BytesMetaDataConverters implements TablePartTypeConverter<TableMetaData> {
    private final ColumnTypeBytesConverter<String> stringBytesConverters =
            (ColumnTypeBytesConverter<String>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.VARCHAR);
    private final ColumnTypeBytesConverter<Integer> integerBytesConverters =
            (ColumnTypeBytesConverter<Integer>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.INT);
    private final ColumnTypeBytesConverter<Boolean> booleanColumnTypeBytesConverter =
            (ColumnTypeBytesConverter<Boolean>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.BOOLEAN);

    @Override
    public TableMetaData toData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("bytes is null or empty");

        ArrayList<ColumnStruct> columnStructs = new ArrayList<>();

        int countColumn = getCountColumn(bytes);
        int indexByte = LENGTH_TYPE_INDICATOR_BYTE_COUNT;

        for (int i = 0; i < countColumn; i++) {
            indexByte = addColumn(columnStructs, bytes, indexByte);
        }
        return new TableMetaData(columnStructs, null);
    }

    private int getCountColumn(byte[] bytes) {
        return getIntFromBytesWithPadZero(Arrays.copyOf(bytes, LENGTH_TYPE_INDICATOR_BYTE_COUNT));
    }

    private Integer addColumn(ArrayList<ColumnStruct> columnStructs, byte[] bytes, Integer indexByte) {
        int lengthName = getLengthName(bytes, indexByte);
        indexByte += LENGTH_TYPE_INDICATOR_BYTE_COUNT;

        String name = getName(bytes, indexByte, lengthName);
        indexByte += lengthName;

        ColumnType columnType = getType(bytes, indexByte);
        indexByte += LENGTH_TYPE_INDICATOR_BYTE_COUNT;

        boolean isPrimary = isPrimary(bytes, indexByte);
        indexByte += BOOLEAN_DATA_INDICATOR_BYTE_COUNT;

        columnStructs.add(new ColumnStruct(name, columnType, isPrimary));
        return indexByte;
    }

    private int getLengthName(byte[] bytes, int indexByte) {
        return getIntFromBytesWithPadZero(Arrays.copyOfRange(bytes, indexByte, indexByte + LENGTH_TYPE_INDICATOR_BYTE_COUNT));
    }

    private String getName(byte[] bytes, int indexByte, int lengthName) {
        return stringBytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + lengthName));
    }

    private ColumnType getType(byte[] bytes, int indexByte) {
        int typeId = integerBytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + 2));
        return ColumnType.values()[typeId];
    }

    private boolean isPrimary(byte[] bytes, int indexByte) {
        return booleanColumnTypeBytesConverter.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + BOOLEAN_DATA_INDICATOR_BYTE_COUNT));
    }

    @Override
    public ArrayList<Byte> toBytes(TableMetaData data) {
        if (data == null) throw new NullPointerException("data is null");

        ArrayList<Byte> tableMetadata = new ArrayList<>();

        List<ColumnStruct> columnStructList = data.getColumnStructList();
        List<Byte> countColumn = intToTwoByteList(columnStructList.size());
        List<Byte> columnStruct = getBytesFromListColumnStruct(columnStructList);

        tableMetadata.addAll(countColumn);
        tableMetadata.addAll(columnStruct);
        return tableMetadata;
    }

    private List<Byte> getBytesFromListColumnStruct(List<ColumnStruct> columnsStructList) {
        List<Byte> bytesList = new ArrayList<>();

        for (ColumnStruct columnStruct : columnsStructList) {
            byte[] columnNameBytes = stringBytesConverters.toBytes(columnStruct.getColumnName());
            byte[] isPrimary = booleanColumnTypeBytesConverter.toBytes(columnStruct.isPrimary());
            List<Byte> columnNameLenBytes = intToTwoByteList(columnNameBytes.length);
            List<Byte> columnTypeByte = intToTwoByteList(columnStruct.getType().ordinal());

            bytesList.addAll(columnNameLenBytes);
            addArrayToList(bytesList, columnNameBytes);
            bytesList.addAll(columnTypeByte);
            addArrayToList(bytesList, isPrimary);
        }
        return bytesList;
    }
}
