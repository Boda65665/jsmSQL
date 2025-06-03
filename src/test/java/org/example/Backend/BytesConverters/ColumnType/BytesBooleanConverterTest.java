package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverter.ColumnType.BytesBooleanConverter;
import org.example.Backend.Exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class BytesBooleanConverterTest {
    private final BytesBooleanConverter booleanConverters = new BytesBooleanConverter();

    @Test
    void toData() {
        assertEquals(true, booleanConverters.toData(new byte[]{TRUE_BYTE}));
        assertEquals(false, booleanConverters.toData(new byte[]{FALSE_BYTE}));
        assertNull(booleanConverters.toData(NULL_BYTES));
    }

    @Test
    void nullOrEmptyArrayToData(){
        assertThrows(ValidationException.class, () -> booleanConverters.toData(null));
        assertThrows(ValidationException.class, () -> booleanConverters.toData(new byte[]{}));
    }

    @Test
    void toBytes() {
        assertArrayEquals(new byte[]{TRUE_BYTE}, booleanConverters.toBytes(true));
        assertArrayEquals(new byte[]{FALSE_BYTE}, booleanConverters.toBytes(false));
        assertArrayEquals(NULL_BYTES, booleanConverters.toBytes(null));
    }
}