package org.example.Backend.BytesConverters;

import org.example.Backend.DataToBytesConverters.BytesBooleanConverters;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BytesBooleanConvertersTest {
    BytesBooleanConverters booleanConverters = new BytesBooleanConverters();

    @Test
    void toData() {
        assertEquals(true, booleanConverters.toData(new byte[]{1}));
        assertEquals(false, booleanConverters.toData(new byte[]{0}));
    }

    @Test
    void nullOrEmptyArrayToData(){
        assertThrows(IllegalArgumentException.class, () -> booleanConverters.toData(null));
        assertThrows(IllegalArgumentException.class, () -> booleanConverters.toData(new byte[]{}));
    }

    @Test
    void toBytes() {
        assertArrayEquals(new byte[]{1}, booleanConverters.toBytes(true));
        assertArrayEquals(new byte[]{0}, booleanConverters.toBytes(false));
    }

    @Test
    void nullToBytes() {
        assertThrows(NullPointerException.class, () -> booleanConverters.toBytes(null));
    }
}