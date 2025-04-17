package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TypeData;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BytesMetaDataConverters implements BytesConverters<TableMetaData> {
    private final BytesConverterFactory bytesConverterFactory = new BytesConverterFactory();
    private final BytesConverters<String> stringBytesConverters =
            (BytesConverters<String>) bytesConverterFactory.getBytesConverters(TypeData.VARCHAR);
    private final BytesConverters<Integer> integerBytesConverters =
            (BytesConverters<Integer>) bytesConverterFactory.getBytesConverters(TypeData.INT);
    private final int LENGTH_INDICATOR_BYTE_COUNT = 2;

    @Override
    public TableMetaData toData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("bytes is null or empty");

        ArrayList<ColumnStruct> columnStructs = new ArrayList<>();

        int countColumn = getIntFromBytes(Arrays.copyOf(bytes, LENGTH_INDICATOR_BYTE_COUNT));
        Integer indexByte = LENGTH_INDICATOR_BYTE_COUNT;
        for (int i = 0; i < countColumn; i++) {
            indexByte = addColumn(columnStructs, bytes, indexByte);
        }
        return new TableMetaData(columnStructs);
    }

    private Integer addColumn(ArrayList<ColumnStruct> columnStructs, byte[] bytes, Integer indexByte) {
        int lengthName = getLengthName(bytes, indexByte);
        indexByte += LENGTH_INDICATOR_BYTE_COUNT;

        String name = getName(bytes, indexByte, lengthName);
        indexByte += lengthName;

        TypeData typeData = getType(bytes, indexByte);

        columnStructs.add(new ColumnStruct(name, typeData));
        indexByte++;
        return indexByte;
    }

    private int getLengthName(byte[] bytes, int indexByte) {
        return getIntFromBytes(Arrays.copyOfRange(bytes, indexByte, indexByte + LENGTH_INDICATOR_BYTE_COUNT));
    }

    private String getName(byte[] bytes, int indexByte, int lengthName) {
        return stringBytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + lengthName));
    }

    private TypeData getType(byte[] bytes, int indexByte) {
        int typeId = integerBytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + 1));
        return TypeData.values()[typeId];
    }

    private int getIntFromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.position(4 - bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    @Override
    public byte[] toBytes(TableMetaData data) {
        if (data == null) throw new NullPointerException("data is null");

        List<ColumnStruct> columnStructList = data.getColumnStructList();
        byte[] countColumn = getBytesFromInt(columnStructList.size());
        byte[] columnStruct = getBytesFromColumnStruct(columnStructList);

        byte[] tableMetadata = new byte[columnStruct.length + LENGTH_INDICATOR_BYTE_COUNT];
        System.arraycopy(countColumn, 0, tableMetadata, 0, LENGTH_INDICATOR_BYTE_COUNT);
        System.arraycopy(columnStruct, 0, tableMetadata, LENGTH_INDICATOR_BYTE_COUNT, columnStruct.length);
        return tableMetadata;
    }

    private byte[] getBytesFromColumnStruct(List<ColumnStruct> columnsStructList) {
        List<byte[]> columnsStructByte = new ArrayList<>();

        for (ColumnStruct columnStruct : columnsStructList) {
            byte[] columnNameBytes = stringBytesConverters.toBytes(columnStruct.getColumnName());
            byte[] columnNameLenBytes = getBytesFromInt(columnNameBytes.length);
            byte[] columnTypeByte = getBytesFromInt(columnStruct.getType().ordinal());

            columnsStructByte.add(columnNameLenBytes);
            columnsStructByte.add(columnNameBytes);
            columnsStructByte.add(columnTypeByte);
        }

        return toArrayByte(columnsStructByte);
    }

    private byte[] toArrayByte(List<byte[]> columnsStruct) {
        int totalLength = 0;
        for (byte[] array : columnsStruct) {
            if (array != null) {
                totalLength += array.length;
            }
        }

        byte[] result = new byte[totalLength];
        int currentIndex = 0;

        for (byte[] array : columnsStruct) {
            if (array != null) {
                System.arraycopy(array, 0, result, currentIndex, array.length);
                currentIndex += array.length;
            }
        }
        return result;
    }

    private byte[] getBytesFromInt(int number) {
        byte[] byteArray = new byte[LENGTH_INDICATOR_BYTE_COUNT];

        byteArray[0] = (byte) ((number >> 8) & 0xFF);
        byteArray[1] = (byte) (number & 0xFF);

        return byteArray;
    }
}
