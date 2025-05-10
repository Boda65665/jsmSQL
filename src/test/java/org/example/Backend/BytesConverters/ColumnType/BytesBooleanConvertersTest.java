package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.ColumnType.BytesBooleanConverters;
import org.example.Backend.Exception.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BytesBooleanConvertersTest {
    private final BytesBooleanConverters booleanConverters = new BytesBooleanConverters();
    private final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};

    @Test
    void toData() {
        assertEquals(true, booleanConverters.toData(new byte[]{1}));
        assertEquals(false, booleanConverters.toData(new byte[]{0}));
        assertNull(booleanConverters.toData(NULL_BYTES));
    }

    @Test
    void nullOrEmptyArrayToData(){
        assertThrows(ValidationException.class, () -> booleanConverters.toData(null));
        assertThrows(ValidationException.class, () -> booleanConverters.toData(new byte[]{}));
    }

    @Test
    void toBytes() {
        assertArrayEquals(new byte[]{1}, booleanConverters.toBytes(true));
        assertArrayEquals(new byte[]{0}, booleanConverters.toBytes(false));
        assertArrayEquals(NULL_BYTES, booleanConverters.toBytes(null));
    }
}