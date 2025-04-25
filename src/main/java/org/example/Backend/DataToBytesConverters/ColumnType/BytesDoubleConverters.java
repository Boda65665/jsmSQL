package org.example.Backend.DataToBytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;

import java.nio.ByteBuffer;

public class BytesDoubleConverters implements ColumnTypeBytesConverter<Double> {

    @Override
    public Double toData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("bytes[] is null or empty");
        
        return ByteBuffer.wrap(bytes).getDouble();
    }

    @Override
    public byte[] toBytes(Double data) {
        if (data == null) throw new NullPointerException("data is null");

        return ByteBuffer.allocate(8).putDouble(data).array();
    }
}
