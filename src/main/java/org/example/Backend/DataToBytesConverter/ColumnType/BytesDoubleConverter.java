package org.example.Backend.DataToBytesConverter.ColumnType;

import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Exception.ValidationException;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.NULL_BYTES;

public class BytesDoubleConverter implements ColumnTypeBytesConverter<Double> {

    @Override
    public Double toData(byte[] bytes) {
        validToData(bytes);

        if (Arrays.equals(bytes, NULL_BYTES)) return null;
        return ByteBuffer.wrap(bytes).getDouble();
    }

    private void validToData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new ValidationException("bytes[] is null or empty");
    }

    @Override
    public byte[] toBytes(Double data) {
        if (data == null) return NULL_BYTES;
        return ByteBuffer.allocate(8).putDouble(data).array();
    }
}
