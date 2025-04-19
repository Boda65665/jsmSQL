package org.example.Backend.DataToBytesConverters;

import org.example.Backend.Models.TypeData;
import java.util.Date;

public class BytesDateConverters implements BytesConverters<Date> {
    private final BytesConverters<Long> longBytesConverters = (BytesConverters<Long>) BytesConverterFactory.getBytesConverters(TypeData.LONG);

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
