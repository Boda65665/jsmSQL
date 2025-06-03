package org.example.Backend.DataToBytesConverter.ColumnType;

import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.Exception.ValidationException;
import org.example.Backend.Models.ColumnType;

import java.util.Arrays;
import java.util.Date;

import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.NULL_BYTES;

public class BytesDateConverter implements ColumnTypeBytesConverter<Date> {
    private final ColumnTypeBytesConverter<Long> longBytesConverters = (ColumnTypeBytesConverter<Long>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.LONG);

    @Override
    public Date toData(byte[] bytes) {
        validToData(bytes);

        if (Arrays.equals(bytes, NULL_BYTES)) return null;
        return new Date(longBytesConverters.toData(bytes));
    }

    private void validToData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new ValidationException("bytes[] is null or empty");
    }

    @Override
    public byte[] toBytes(Date data) {
        if (data == null) return NULL_BYTES;
        return longBytesConverters.toBytes(data.getTime());
    }
}
