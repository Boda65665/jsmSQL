package org.example.Backend.DataToBytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Exception.ValidationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BytesStringConverters implements ColumnTypeBytesConverter<String> {
    private final Charset charset = StandardCharsets.UTF_8;
    private final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};

    @Override
    public String toData(byte[] bytes) {
        validToData(bytes);

        if (Arrays.equals(NULL_BYTES, bytes)) return null;
        return new String(bytes, charset);
    }

    private void validToData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new ValidationException("Null or empty byte array");
    }

    @Override
    public byte[] toBytes(String data) {
        if (data == null) return NULL_BYTES;

        return data.getBytes(charset);
    }
}
