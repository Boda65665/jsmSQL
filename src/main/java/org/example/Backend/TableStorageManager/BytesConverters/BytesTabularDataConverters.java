package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.Column;
import org.example.Backend.Models.TabularData;
import org.example.Backend.Models.TypeData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class BytesTabularDataConverters implements BytesConverters<TabularData> {
    private final int LENGTH_INDICATOR_BYTE_COUNT = 2;
    private final int LENGTH_TYPE_INDICATOR_BYTE_COUNT = 2;
    private final BytesConverterFactory bytesConverterFactory = new BytesConverterFactory();

    @Override
    public TabularData toData(byte[] bytes) {
        TabularData tabularData = new TabularData();

        int countColumn = 0;
        int indexByte = LENGTH_INDICATOR_BYTE_COUNT;
        while (indexByte < bytes.length) {
            countColumn++;

            int lenDataBytes = getIntFromBytes(Arrays.copyOf(bytes, indexByte));
            indexByte += LENGTH_INDICATOR_BYTE_COUNT;

            int indexType = lenDataBytes + indexByte;
            byte[] typeBytes = Arrays.copyOfRange(bytes, indexType, indexType + LENGTH_TYPE_INDICATOR_BYTE_COUNT);
            TypeData typeData = TypeData.values()[getIntFromBytes(typeBytes)];

            BytesConverters bytesConverters = bytesConverterFactory.getBytesConverters(typeData);
            Object data = bytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + lenDataBytes));
            indexByte += lenDataBytes;
            indexByte += LENGTH_INDICATOR_BYTE_COUNT;
        }
        return null;
    }

    private int getIntFromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.position(4 - bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    @Override
    public byte[] toBytes(TabularData data) {
        Column[] columns = data.getColumns();
        ArrayList<byte[]> bytesList = new ArrayList<>();
        int totalLength = 0;

        for (Column column : columns) {
            BytesConverters bytesConverters = bytesConverterFactory.getBytesConverters(column.getTypeData());
            byte[] dataBytes = bytesConverters.toBytes(column.getData());
            byte[] lenDataBytes = getBytesFromInt(dataBytes.length);
            byte[] typeDataBytes = getBytesFromInt(column.getTypeData().ordinal());
            
            totalLength += lenDataBytes.length + dataBytes.length + typeDataBytes.length;
            bytesList.add(lenDataBytes);
            bytesList.add(dataBytes);
            bytesList.add(typeDataBytes);
        }
        return combineArrays(bytesList, totalLength);
    }

    private byte[] getBytesFromInt(int number) {
        byte[] byteArray = new byte[LENGTH_INDICATOR_BYTE_COUNT];

        byteArray[0] = (byte) ((number >> 8) & 0xFF);
        byteArray[1] = (byte) (number & 0xFF);

        return byteArray;
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
