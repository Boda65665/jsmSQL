package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverter.ColumnType.BytesDateConverter;
import org.example.Backend.Exception.ValidationException;
import org.example.Backend.Models.ColumnType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.NULL_BYTES;
import static org.junit.jupiter.api.Assertions.*;

class BytesDateConverterTest {
    private final BytesDateConverter converter = new BytesDateConverter();
    private final static ColumnTypeBytesConverter<Long> longBytesConverters =
            (ColumnTypeBytesConverter<Long>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.LONG);

    @ParameterizedTest
    @MethodSource("provideTestToBytes")
    void toBytes(Date originalDate) {
        byte[] bytes = converter.toBytes(originalDate);
        if (originalDate == null) assertArrayEquals(NULL_BYTES, longBytesConverters.toBytes(null));
        else assertArrayEquals(bytes, longBytesConverters.toBytes(originalDate.getTime()));
    }

    private static Stream<Arguments> provideTestToBytes() {
        return Stream.of(
                Arguments.of(new Date(0)),
                Arguments.of(new Date()),
                Arguments.of(new Date(Long.MAX_VALUE)),
                Arguments.of(new Date(Long.MIN_VALUE)),
                Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestToDate")
    void toDate(byte[] bytes) {
        Date date = converter.toData(bytes);

        if (Arrays.equals(bytes, NULL_BYTES)) assertNull(date);
        else assertEquals(new Date(longBytesConverters.toData(bytes)), date);
    }

    private static Stream<Arguments> provideTestToDate() {
        return Stream.of(
                Arguments.of(longBytesConverters.toBytes(0L)),
                Arguments.of(longBytesConverters.toBytes(new Date().getTime())),
                Arguments.of(longBytesConverters.toBytes(Long.MAX_VALUE)),
                Arguments.of(longBytesConverters.toBytes(Long.MIN_VALUE)),

                Arguments.of(NULL_BYTES)
        );
    }

    @Test
    void nullOrEmptyArrayToData(){
        assertThrows(ValidationException.class, () -> longBytesConverters.toData(null));
        assertThrows(ValidationException.class, () -> longBytesConverters.toData(new byte[]{}));
    }
}