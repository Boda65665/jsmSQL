package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.Column;
import org.example.Backend.Models.TypeData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BytesColumnConverterTest {
    private final BytesColumnConverter converter = new BytesColumnConverter();

    @Test
    void convert() {
        Column column = new Column(1, TypeData.INT);
        byte[] bytesData = converter.toBytes(column);

        assertEquals(column, converter.toData(bytesData));
    }
}