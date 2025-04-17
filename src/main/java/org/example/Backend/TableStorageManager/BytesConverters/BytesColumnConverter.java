package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.Column;
import org.example.Backend.Models.TypeData;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class BytesColumnConverter implements BytesConverters<Column> {
    private final int LENGTH_INDICATOR_BYTE_COUNT = 2;
    private final int LENGTH_TYPE_INDICATOR_BYTE_COUNT = 2;

    @Override
    public Column toData(byte[] bytes) {
        int indexByte = 0;
        int lenDataBytes = getLenData(bytes, indexByte);
        indexByte += LENGTH_INDICATOR_BYTE_COUNT;

        TypeData typeData = getTypeFromBytes(bytes, indexByte, lenDataBytes);
        Object data = getObjectFromBytes(bytes, typeData, indexByte, lenDataBytes);
        return new Column(data, typeData);
    }

    private int getLenData(byte[] bytes, int indexByte) {
        byte[] lenData = Arrays.copyOfRange(bytes, indexByte, indexByte + LENGTH_INDICATOR_BYTE_COUNT);
        return getIntFromBytes(lenData);
    }

    private int getIntFromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.position(4 - bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    private TypeData getTypeFromBytes(byte[] bytes, int indexByte, int lenDataBytes) {
        int indexType = lenDataBytes + indexByte;
        byte[] typeBytes = Arrays.copyOfRange(bytes, indexType, indexType + LENGTH_TYPE_INDICATOR_BYTE_COUNT);
        return TypeData.values()[getIntFromBytes(typeBytes)];
    }

    private Object getObjectFromBytes(byte[] bytes, TypeData typeData, int indexByte, int lenDataBytes) {
        BytesConverters bytesConverters = BytesConverterFactory.getBytesConverters(typeData);
        return bytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + lenDataBytes));
    }

    @Override
    public byte[] toBytes(Column column) {
        ArrayList<byte[]> bytesList = getListBytesFromColumn(column);
        int totalLength = getTotalLength(bytesList);

        return combineArrays(bytesList, totalLength);
    }

    private ArrayList<byte[]> getListBytesFromColumn(Column column) {
        ArrayList<byte[]> bytesList = new ArrayList<>();
        BytesConverters bytesConverters = BytesConverterFactory.getBytesConverters(column.getTypeData());

        byte[] dataBytes = bytesConverters.toBytes(column.getData());
        byte[] lenDataBytes = getBytesFromInt(dataBytes.length);
        byte[] typeDataBytes = getBytesFromInt(column.getTypeData().ordinal());

        addToBytesList(bytesList, dataBytes, lenDataBytes, typeDataBytes);
        return bytesList;
    }

    private byte[] getBytesFromInt(int number) {
        byte[] byteArray = new byte[LENGTH_INDICATOR_BYTE_COUNT];

        byteArray[0] = (byte) ((number >> 8) & 0xFF);
        byteArray[1] = (byte) (number & 0xFF);

        return byteArray;
    }

    private void addToBytesList(ArrayList<byte[]> bytesList, byte[] dataBytes, byte[] lenDataBytes, byte[] typeDataBytes) {
        bytesList.add(lenDataBytes);
        bytesList.add(dataBytes);
        bytesList.add(typeDataBytes);
    }

    private int getTotalLength(ArrayList<byte[]> bytesList) {
        int totalLength = 0;
        for (byte[] bytes : bytesList) {
            totalLength += bytes.length;
        }
        return totalLength;
    }

    private byte[] combineArrays(ArrayList<byte[]> bytesList, int totalLength) {
        ByteBuffer buffer = ByteBuffer.allocate(totalLength);
        for (byte[] array : bytesList) {
            buffer.put(array);
        }
        return buffer.array();
    }
}
