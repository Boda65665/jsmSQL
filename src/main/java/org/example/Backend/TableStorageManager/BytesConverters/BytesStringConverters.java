package org.example.Backend.TableStorageManager.BytesConverters;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BytesStringConverters implements BytesConverters<String> {
    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    public String toData(byte[] bytes) {
        return new String(bytes, charset);
    }

    @Override
    public byte[] toBytes(String data) {
        return data.getBytes(charset);
    }
}
