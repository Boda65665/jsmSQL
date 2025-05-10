package org.example.Backend.DataToBytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Exception.ValidationException;
import org.example.Backend.Models.ColumnType;

import java.util.Arrays;
import java.util.Date;

public class BytesDateConverters implements ColumnTypeBytesConverter<Date> {
    private final ColumnTypeBytesConverter<Long> longBytesConverters = (ColumnTypeBytesConverter<Long>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.LONG);
    private final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};

    @Override
    public Date toData(byte[] bytes) {
        if (Arrays.equals(bytes, NULL_BYTES)) return null;
        return new Date(longBytesConverters.toData(bytes));
    }

    @Override
    public byte[] toBytes(Date data) {
        if (data == null) return NULL_BYTES;
        return longBytesConverters.toBytes(data.getTime());
    }
}
