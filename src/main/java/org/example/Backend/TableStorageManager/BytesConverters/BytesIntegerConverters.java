package org.example.Backend.TableStorageManager.BytesConverters;

import java.nio.ByteBuffer;

public class BytesIntegerConverters implements BytesConverters<Integer> {

    @Override
    public Integer toData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("bytes is null or empty");

        if (bytes.length < 4) bytes = padWithZero(bytes);
        return getData(bytes);
    }

    private Integer getData(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    private byte[] padWithZero(byte[] bytes) {
        byte[] paddedArray = new byte[4];
        System.arraycopy(bytes, 0, paddedArray, 4 - bytes.length, bytes.length);
        return paddedArray;
    }

    @Override
    public byte[] toBytes(Integer number) {
        if (number == null) throw new NullPointerException("data is null");

        if(number < 0) return getBytesWithNegativeNumber(number);
        return getBytesWithPositiveNumber(number);
    }

    private byte[] getBytesWithNegativeNumber(Integer number) {
        return ByteBuffer.allocate(4).putInt(number).array();
    }

    private byte[] getBytesWithPositiveNumber(Integer number) {
        int length = getLengthByte(number);
        byte[] byteArray = new byte[length];

        for (int i = length - 1; i >= 0; i--) {
            byteArray[i] = extractLowByte(number);
            number = shiftByOneByte(number);
        }
        return byteArray;
    }

    private Integer shiftByOneByte(Integer number) {
        number >>= 8;
        return number;
    }

    private byte extractLowByte(Integer number) {
        return (byte)(number & 0xFF);
    }

    private int getLengthByte(Integer data) {
        if (data == 0) return 1;
        return (int)(Math.log(data) / Math.log(256)) + 1;
    }
}
