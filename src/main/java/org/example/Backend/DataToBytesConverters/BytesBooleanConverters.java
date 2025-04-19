package org.example.Backend.DataToBytesConverters;

public class BytesBooleanConverters implements BytesConverters<Boolean> {

    @Override
    public Boolean toData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("Bytes cannot be null or empty");
        
        return bytes[0] == 1;
    }

    @Override
    public byte[] toBytes(Boolean data) {
        if (data == null) throw new NullPointerException("data is null");

        if (data) return new byte[]{1};
        return new byte[]{0};
    }
}
