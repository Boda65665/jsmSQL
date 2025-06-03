package org.example.Backend.DataToBytesConverter.ColumnType;

import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Exception.ValidationException;
import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.*;

import java.util.Arrays;

public class BytesBooleanConverter implements ColumnTypeBytesConverter<Boolean> {

    @Override
    public Boolean toData(byte[] bytes) {
        validToData(bytes);

        if (Arrays.equals(bytes, NULL_BYTES)) return null;
        return bytes[0] == TRUE_BYTE;
    }

    private void validToData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new ValidationException("Bytes cannot be null or empty");
    }

    @Override
    public byte[] toBytes(Boolean data) {
        if (data == null) return NULL_BYTES;

        if (data) return new byte[]{TRUE_BYTE};
        return new byte[]{FALSE_BYTE};
    }
}
