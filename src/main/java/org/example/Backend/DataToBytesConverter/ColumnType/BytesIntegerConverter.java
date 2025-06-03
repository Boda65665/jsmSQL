package org.example.Backend.DataToBytesConverter.ColumnType;

import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Exception.ValidationException;

import java.util.Arrays;

import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.NULL_BYTES;
import static org.example.Backend.Utils.ByteUtils.*;

public class BytesIntegerConverter implements ColumnTypeBytesConverter<Integer> {

    @Override
    public Integer toData(byte[] bytes) {
        validToData(bytes);

        if (Arrays.equals(NULL_BYTES, bytes)) return null;

        if (bytes.length < 4) bytes = padWithZero(bytes, Integer.BYTES);
        return getIntFromBytes(bytes);
    }

    private void validToData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new ValidationException("bytes is null or empty");
    }

    @Override
    public byte[] toBytes(Integer number) {
        if (number == null) return NULL_BYTES;

        if (number < 0) return getBytesWithNegativeNumber(number);
        return getBytesWithPositiveNumber(number);
    }

    private byte[] getBytesWithNegativeNumber(Integer number) {
        return intToByteArray(number);
    }

    private byte[] getBytesWithPositiveNumber(Integer number) {
        return intToCompactArray(number);
    }
}

