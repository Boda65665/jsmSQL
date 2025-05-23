package org.example.Backend.DataToBytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Exception.ValidationException;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BytesIntegerConverters implements ColumnTypeBytesConverter<Integer> {
    private final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};

    @Override
    public Integer toData(byte[] bytes) {
        validToData(bytes);

        if (Arrays.equals(NULL_BYTES, bytes)) return null;

        if (bytes.length < 4) bytes = padWithZero(bytes);
        return getData(bytes);
    }

    private void validToData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new ValidationException("bytes is null or empty");
    }

    private byte[] padWithZero(byte[] bytes) {
        byte[] paddedArray = new byte[4];
        System.arraycopy(bytes, 0, paddedArray, 4 - bytes.length, bytes.length);
        return paddedArray;
    }

    private Integer getData(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    @Override
    public byte[] toBytes(Integer number) {
        if (number == null) return NULL_BYTES;

        if (number < 0) return getBytesWithNegativeNumber(number);
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

    private int getLengthByte(Integer data) {
        if (data == 0) return 1;
        return (int) (Math.log(data) / Math.log(256)) + 1;
    }

    private byte extractLowByte(Integer number) {
        return (byte) (number & 0xFF);
    }

    private Integer shiftByOneByte(Integer number) {
        number >>= 8;
        return number;
    }
}

