package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.Column;
import org.example.Backend.Models.TabularData;
import org.example.Backend.Models.TypeData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class BytesColumnConverter implements BytesConverters<Column> {
    private final int LENGTH_INDICATOR_BYTE_COUNT = 2;
    private final int LENGTH_TYPE_INDICATOR_BYTE_COUNT = 2;
    private final BytesConverterFactory bytesConverterFactory = new BytesConverterFactory();

    @Override
    public Column toData(byte[] bytes) {
        int indexByte = 0;
        int lenDataBytes = getIntFromBytes(Arrays.copyOfRange(bytes, indexByte, indexByte + LENGTH_INDICATOR_BYTE_COUNT));
        indexByte += LENGTH_INDICATOR_BYTE_COUNT;

        TypeData typeData = getTypeFromBytes(bytes, indexByte, lenDataBytes);
        Object data = getObjectFromBytes(bytes, typeData, indexByte, lenDataBytes);
        return new Column(data, typeData);
    }

    private Object getObjectFromBytes(byte[] bytes, TypeData typeData, int indexByte, int lenDataBytes) {
        BytesConverters bytesConverters = bytesConverterFactory.getBytesConverters(typeData);
        return bytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + lenDataBytes));
    }

    private TypeData getTypeFromBytes(byte[] bytes, int indexByte, int lenDataBytes) {
        int indexType = lenDataBytes + indexByte;
        byte[] typeBytes = Arrays.copyOfRange(bytes, indexType, indexType + LENGTH_TYPE_INDICATOR_BYTE_COUNT);
        return TypeData.values()[getIntFromBytes(typeBytes)];
    }


    private int getIntFromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.position(4 - bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    @Override
    public byte[] toBytes(Column column) {
        ArrayList<byte[]> bytesList = new ArrayList<>();
        int totalLength = 0;
        BytesConverters bytesConverters = bytesConverterFactory.getBytesConverters(column.getTypeData());
        byte[] dataBytes = bytesConverters.toBytes(column.getData());
        byte[] lenDataBytes = getBytesFromInt(dataBytes.length);
        byte[] typeDataBytes = getBytesFromInt(column.getTypeData().ordinal());
        addToBytesList(bytesList, dataBytes, lenDataBytes, typeDataBytes);
        totalLength += lenDataBytes.length + dataBytes.length + typeDataBytes.length;
        return combineArrays(bytesList, totalLength);
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

    private byte[] combineArrays(ArrayList<byte[]> bytesList, int totalLength) {
        byte[] bytes = new byte[totalLength];
        int currentIndex = 0;

        for (byte[] array : bytesList) {
            System.arraycopy(array, 0, bytes, currentIndex, array.length);
            currentIndex += array.length;
        }
        return bytes;
    }
}
