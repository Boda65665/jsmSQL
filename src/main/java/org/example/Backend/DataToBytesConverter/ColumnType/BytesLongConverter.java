package org.example.Backend.DataToBytesConverter.ColumnType;

import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Exception.ValidationException;

import java.util.Arrays;

import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.NULL_BYTES;
import static org.example.Backend.Utils.ByteUtils.*;

public class BytesLongConverter implements ColumnTypeBytesConverter<Long> {

    @Override
    public Long toData(byte[] bytes) {
        validToData(bytes);

        if (Arrays.equals(bytes, NULL_BYTES)) return null;

        if (bytes.length < 8) bytes = padWithZero(bytes, Long.BYTES);
        return getLongFromBytes(bytes);
    }

    private void validToData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new ValidationException("bytes is null or empty");
    }

    @Override
    public byte[] toBytes(Long number) {
        if (number == null) return NULL_BYTES;

        if(number < 0) return getBytesWithNegativeNumber(number);
        return getBytesWithPositiveNumber(number);
    }

    private byte[] getBytesWithNegativeNumber(Long number) {
        return longToByteArray(number);
    }

    private byte[] getBytesWithPositiveNumber(Long number) {
        return longToCompactArray(number);
    }
}
