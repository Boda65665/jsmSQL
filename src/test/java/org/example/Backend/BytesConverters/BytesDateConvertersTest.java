package org.example.Backend.BytesConverters;

import org.example.Backend.DataToBytesConverters.factory.ColumnTypeBytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverters.ColumnType.BytesDateConverters;
import org.example.Backend.Models.ColumnType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Date;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class BytesDateConvertersTest {
    private final BytesDateConverters converter = new BytesDateConverters();
    private final static ColumnTypeBytesConverter<Long> longBytesConverters = (ColumnTypeBytesConverter<Long>) ColumnTypeBytesConverterFactory.getBytesConverters(ColumnType.LONG);

    @Test
    void nullToBytes() {
        assertThrows(NullPointerException.class, () -> converter.toBytes(null));
    }

    @ParameterizedTest
    @MethodSource("provideTestToBytes")
    void toBytes(Date originalDate) {
        byte[] bytes = converter.toBytes(originalDate);
        assertArrayEquals(bytes, longBytesConverters.toBytes(originalDate.getTime()));
    }

    private static Stream<Arguments> provideTestToBytes() {
        return Stream.of(
                Arguments.of(new Date(0)),
                Arguments.of(new Date()),
                Arguments.of(new Date(Long.MAX_VALUE)),
                Arguments.of(new Date(1672531200000L))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestToDate")
    void toDate(byte[] bytes) {
        Date date = converter.toData(bytes);

        assertEquals(new Date(longBytesConverters.toData(bytes)), date);
    }

    private static Stream<Arguments> provideTestToDate() {
        return Stream.of(
                Arguments.of(longBytesConverters.toBytes(0L)),
                Arguments.of(longBytesConverters.toBytes(new Date().getTime())),
                Arguments.of(longBytesConverters.toBytes(Long.MAX_VALUE)),
                Arguments.of(longBytesConverters.toBytes(Long.MIN_VALUE))
        );
    }

    @Test
    void nullOrEmptyArrayToDate() {
        assertThrows(IllegalArgumentException.class, () -> converter.toData(null));
        assertThrows(IllegalArgumentException.class, () -> converter.toData(new byte[]{}));
    }
}