package org.example.Backend.DataToBytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Models.ColumnType;

import java.util.Date;

public class BytesDateConverters implements ColumnTypeBytesConverter<Date> {
    private final ColumnTypeBytesConverter<Long> longBytesConverters = (ColumnTypeBytesConverter<Long>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.LONG);

    @Override
    public Date toData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("Null or empty byte array");

        return new Date(longBytesConverters.toData(bytes));
    }

    @Override
    public byte[] toBytes(Date data) {
        if (data == null) throw new NullPointerException("Null date");

        return longBytesConverters.toBytes(data.getTime());
    }
}
