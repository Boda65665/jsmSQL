package org.example.Backend.TableStorageManager.BytesConverters;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BytesStringConverters implements BytesConverters<String> {
    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    public String toData(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("Null or empty byte array");

        return new String(bytes, charset);
    }

    @Override
    public byte[] toBytes(String data) {
        if (data == null) throw new NullPointerException("Null data");

        return data.getBytes(charset);
    }
}
