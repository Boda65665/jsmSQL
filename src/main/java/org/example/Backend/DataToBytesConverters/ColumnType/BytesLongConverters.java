package org.example.Backend.DataToBytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.Interface.ArrayByteConverter;
import java.nio.ByteBuffer;

public class BytesLongConverters implements ArrayByteConverter<Long> {

    @Override
    public Long toData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("bytes is null or empty");

        if (bytes.length < 8) bytes = padWithZero(bytes);
        return getData(bytes);
    }

    private Long getData(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    private byte[] padWithZero(byte[] bytes) {
        byte[] paddedArray = new byte[8];
        System.arraycopy(bytes, 0, paddedArray, 8 - bytes.length, bytes.length);
        return paddedArray;
    }

    @Override
    public byte[] toBytes(Long number) {
        if (number == null) throw new NullPointerException("data is null");

        if(number < 0) return getBytesWithNegativeNumber(number);
        return getBytesWithPositiveNumber(number);
    }

    private byte[] getBytesWithNegativeNumber(Long number) {
        return ByteBuffer.allocate(8).putLong(number).array();
    }

    private byte[] getBytesWithPositiveNumber(Long number) {
        int length = getLength(number);
        byte[] byteArray = new byte[length];

        for (int i = length - 1; i >= 0; i--) {
            byteArray[i] = extractLowByte(number);
            number = shiftByOneByte(number);
        }
        return byteArray;
    }

    private int getLength(Long data) {
        if (data == 0) return 1;
        return (int)(Math.log(data) / Math.log(256)) + 1;
    }

    private byte extractLowByte(Long number) {
        return (byte)(number & 0xFF);
    }

    private Long shiftByOneByte(Long number) {
        number >>= 8;
        return number;
    }
}
