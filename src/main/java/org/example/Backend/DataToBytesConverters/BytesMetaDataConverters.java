package org.example.Backend.DataToBytesConverters;

import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TypeData;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BytesMetaDataConverters implements BytesConverters<TableMetaData> {
    private final BytesConverters<String> stringBytesConverters =
            (BytesConverters<String>) BytesConverterFactory.getBytesConverters(TypeData.VARCHAR);
    private final BytesConverters<Integer> integerBytesConverters =
            (BytesConverters<Integer>) BytesConverterFactory.getBytesConverters(TypeData.INT);
    private final int LENGTH_INDICATOR_BYTE_COUNT = 2;

    @Override
    public TableMetaData toData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("bytes is null or empty");

        ArrayList<ColumnStruct> columnStructs = new ArrayList<>();

        int countColumn = getCountColumn(bytes);
        Integer indexByte = LENGTH_INDICATOR_BYTE_COUNT;

        for (int i = 0; i < countColumn; i++) {
            indexByte = addColumn(columnStructs, bytes, indexByte);
        }
        String nameColumnPrimaryKey = getNameColumnPrimaryKey(bytes, indexByte);
        return new TableMetaData(columnStructs, nameColumnPrimaryKey);
    }

    private int getCountColumn(byte[] bytes) {
        return getIntFromBytes(Arrays.copyOf(bytes, LENGTH_INDICATOR_BYTE_COUNT));
    }

    private Integer addColumn(ArrayList<ColumnStruct> columnStructs, byte[] bytes, Integer indexByte) {
        int lengthName = getLengthName(bytes, indexByte);
        indexByte += LENGTH_INDICATOR_BYTE_COUNT;

        String name = getName(bytes, indexByte, lengthName);
        indexByte += lengthName;

        TypeData typeData = getType(bytes, indexByte);

        columnStructs.add(new ColumnStruct(name, typeData));
        indexByte+=2;
        return indexByte;
    }

    private int getLengthName(byte[] bytes, int indexByte) {
        return getIntFromBytes(Arrays.copyOfRange(bytes, indexByte, indexByte + LENGTH_INDICATOR_BYTE_COUNT));
    }

    private String getName(byte[] bytes, int indexByte, int lengthName) {
        return stringBytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + lengthName));
    }

    private TypeData getType(byte[] bytes, int indexByte) {
        int typeId = integerBytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + 2));
        return TypeData.values()[typeId];
    }

    private int getIntFromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.position(4 - bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    private String getNameColumnPrimaryKey(byte[] bytes, int indexByte) {
        int lengthNamePrimaryKey = getLengthName(bytes, indexByte);
        indexByte += LENGTH_INDICATOR_BYTE_COUNT;

        return getName(bytes, indexByte, lengthNamePrimaryKey);
    }

    @Override
    public byte[] toBytes(TableMetaData data) {
        if (data == null) throw new NullPointerException("data is null");

        List<ColumnStruct> columnStructList = data.getColumnStructList();
        byte[] countColumn = getBytesFromInt(columnStructList.size());
        byte[] columnStruct = getBytesFromColumnStruct(columnStructList);
        byte[] namePrimaryKey = getBytesFromNameColumnPrimaryKey(data.getNameColumnPrimaryKey());

        byte[] tableMetadata = new byte[columnStruct.length + namePrimaryKey.length + LENGTH_INDICATOR_BYTE_COUNT];
        System.arraycopy(countColumn, 0, tableMetadata, 0, LENGTH_INDICATOR_BYTE_COUNT);
        System.arraycopy(columnStruct, 0, tableMetadata, LENGTH_INDICATOR_BYTE_COUNT, columnStruct.length);
        System.arraycopy(namePrimaryKey, 0, tableMetadata, LENGTH_INDICATOR_BYTE_COUNT + columnStruct.length, namePrimaryKey.length);
        return tableMetadata;
    }

    private byte[] getBytesFromNameColumnPrimaryKey(String nameColumnPrimaryKey) {
        byte[] nameColumnPrimaryKeyBytes = stringBytesConverters.toBytes(nameColumnPrimaryKey);
        byte[] length = getBytesFromInt(nameColumnPrimaryKeyBytes.length);

        byte[] result = new byte[nameColumnPrimaryKeyBytes.length + length.length];
        System.arraycopy(length, 0, result, 0, length.length);
        System.arraycopy(nameColumnPrimaryKeyBytes, 0, result, length.length, nameColumnPrimaryKeyBytes.length);
        return result;
    }

    private byte[] getBytesFromInt(int number) {
        byte[] byteArray = new byte[LENGTH_INDICATOR_BYTE_COUNT];

        byteArray[0] = (byte) ((number >> 8) & 0xFF);
        byteArray[1] = (byte) (number & 0xFF);

        return byteArray;
    }

    private byte[] getBytesFromColumnStruct(List<ColumnStruct> columnsStructList) {
        List<byte[]> columnsStructByte = getBytesFromListColumnStruct(columnsStructList);
        int totalLength = getTotalLength(columnsStructByte);
        return combineArrays(columnsStructByte, totalLength);
    }

    private List<byte[]> getBytesFromListColumnStruct(List<ColumnStruct> columnsStructList) {
        List<byte[]> bytes = new ArrayList<>();

        for (ColumnStruct columnStruct : columnsStructList) {
            byte[] columnNameBytes = stringBytesConverters.toBytes(columnStruct.getColumnName());
            byte[] columnNameLenBytes = getBytesFromInt(columnNameBytes.length);
            byte[] columnTypeByte = getBytesFromInt(columnStruct.getType().ordinal());

            bytes.add(columnNameLenBytes);
            bytes.add(columnNameBytes);
            bytes.add(columnTypeByte);
        }

        return bytes;
    }

    private int getTotalLength(List<byte[]> bytesList) {
        int totalLength = 0;
        for (byte[] array : bytesList) {
            if (array != null) {
                totalLength += array.length;
            }
        }
        return totalLength;
    }

    private byte[] combineArrays(List<byte[]> bytesList, int totalLength) {
        ByteBuffer buffer = ByteBuffer.allocate(totalLength);
        for (byte[] array : bytesList) {
            buffer.put(array);
        }
        return buffer.array();
    }
}
