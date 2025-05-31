package org.example.Backend.DataToBytesConverters.TableParts;

import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.TableMetaData;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.Backend.DataToBytesConverters.TableParts.ByteConversionConstants.BOOLEAN_DATA_INDICATOR_BYTE_COUNT;
import static org.example.Backend.DataToBytesConverters.TableParts.ByteConversionConstants.LENGTH_TYPE_INDICATOR_BYTE_COUNT;
import static org.example.Backend.TableStorageManager.FragmentManager.FragmentStructureConstants.LENGTH_INDICATOR_BYTE_COUNT;

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
        int indexByte = LENGTH_INDICATOR_BYTE_COUNT;

        for (int i = 0; i < countColumn; i++) {
            indexByte = addColumn(columnStructs, bytes, indexByte);
        }
        return new TableMetaData(columnStructs, null);
    }

    private int getCountColumn(byte[] bytes) {
        return getIntFromBytes(Arrays.copyOf(bytes, LENGTH_INDICATOR_BYTE_COUNT));
    }

    private Integer addColumn(ArrayList<ColumnStruct> columnStructs, byte[] bytes, Integer indexByte) {
        int lengthName = getLengthName(bytes, indexByte);
        indexByte += LENGTH_INDICATOR_BYTE_COUNT;

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
        return getIntFromBytes(Arrays.copyOfRange(bytes, indexByte, indexByte + LENGTH_INDICATOR_BYTE_COUNT));
    }

    private String getName(byte[] bytes, int indexByte, int lengthName) {
        return stringBytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + lengthName));
    }

    private ColumnType getType(byte[] bytes, int indexByte) {
        int typeId = integerBytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + 2));
        return ColumnType.values()[typeId];
    }

    private int getIntFromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.position(4 - bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    private boolean isPrimary(byte[] bytes, int indexByte) {
        return booleanColumnTypeBytesConverter.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + BOOLEAN_DATA_INDICATOR_BYTE_COUNT));
    }

    @Override
    public ArrayList<Byte> toBytes(TableMetaData data) {
        if (data == null) throw new NullPointerException("data is null");

        ArrayList<Byte> tableMetadata = new ArrayList<>();

        ArrayList<ColumnStruct> columnStructList = data.getColumnStructList();
        ArrayList<Byte> countColumn = getBytesFromInt(columnStructList.size());
        List<Byte> columnStruct = getBytesFromListColumnStruct(columnStructList);

        tableMetadata.addAll(countColumn);
        tableMetadata.addAll(columnStruct);
        return tableMetadata;
    }



    private ArrayList<Byte> getBytesFromInt(int number) {
        ArrayList<Byte> bytes = new ArrayList<>();

        bytes.add((byte) ((number >> 8) & 0xFF));
        bytes.add((byte) (number & 0xFF));
        return bytes;
    }

    private List<Byte> getBytesFromListColumnStruct(List<ColumnStruct> columnsStructList) {
        List<Byte> bytesList = new ArrayList<>();

        for (ColumnStruct columnStruct : columnsStructList) {
            byte[] columnNameBytes = stringBytesConverters.toBytes(columnStruct.getColumnName());
            byte[] isPrimary = booleanColumnTypeBytesConverter.toBytes(columnStruct.isPrimary());
            List<Byte> columnNameLenBytes = getBytesFromInt(columnNameBytes.length);
            List<Byte> columnTypeByte = getBytesFromInt(columnStruct.getType().ordinal());

            bytesList.addAll(columnNameLenBytes);
            addArrayToList(bytesList, columnNameBytes);
            bytesList.addAll(columnTypeByte);
            addArrayToList(bytesList, isPrimary);
        }
        return bytesList;
    }

    private void addArrayToList(List<Byte> bytes, byte[] columnNameBytes) {
        for (byte b : columnNameBytes) {
            bytes.add(b);
        }
    }
}
