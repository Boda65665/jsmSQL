package org.example.Backend.DataToBytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.BytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ArrayByteConverter;
import org.example.Backend.Models.TypeData;
import java.util.Date;

public class BytesDateConverters implements ArrayByteConverter<Date> {
    private final ArrayByteConverter<Long> longBytesConverters = (ArrayByteConverter<Long>) BytesConverterFactory.getBytesConverters(TypeData.LONG);

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
