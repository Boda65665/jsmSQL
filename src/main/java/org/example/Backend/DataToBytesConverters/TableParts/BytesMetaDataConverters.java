package org.example.Backend.DataToBytesConverters.TableParts;

import org.example.Backend.DataToBytesConverters.BytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ArrayByteConverter;
import org.example.Backend.DataToBytesConverters.Interface.ByteCollectionConverter;
import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TypeData;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BytesMetaDataConverters implements ByteCollectionConverter<TableMetaData> {
    private final ArrayByteConverter<String> stringBytesConverters =
            (ArrayByteConverter<String>) BytesConverterFactory.getBytesConverters(TypeData.VARCHAR);
    private final ArrayByteConverter<Integer> integerBytesConverters =
            (ArrayByteConverter<Integer>) BytesConverterFactory.getBytesConverters(TypeData.INT);
    private final int LENGTH_INDICATOR_BYTE_COUNT = 2;

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
        List<Byte> bytes = new ArrayList<>();

        for (ColumnStruct columnStruct : columnsStructList) {
            byte[] columnNameBytes = stringBytesConverters.toBytes(columnStruct.getColumnName());

            List<Byte> columnNameLenBytes = getBytesFromInt(columnNameBytes.length);
            List<Byte> columnTypeByte = getBytesFromInt(columnStruct.getType().ordinal());

            bytes.addAll(columnNameLenBytes);
            addArrayToList(bytes, columnNameBytes);
            bytes.addAll(columnTypeByte);
        }

        return bytes;
    }

    private void addArrayToList(List<Byte> bytes, byte[] columnNameBytes) {
        for (byte b : columnNameBytes) {
            bytes.add(b);
        }
    }
}
