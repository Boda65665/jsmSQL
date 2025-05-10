package org.example.Backend.DataToBytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Exception.ValidationException;

import java.util.Arrays;

public class BytesBooleanConverters implements ColumnTypeBytesConverter<Boolean> {
    private final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};

    @Override
    public Boolean toData(byte[] bytes) {
        validToData(bytes);

        if (Arrays.equals(bytes, NULL_BYTES)) return null;
        return bytes[0] == 1;
    }

    private void validToData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new ValidationException("Bytes cannot be null or empty");
    }

    @Override
    public byte[] toBytes(Boolean data) {
        if (data == null) return NULL_BYTES;

        if (data) return new byte[]{1};
        return new byte[]{0};
    }
}
