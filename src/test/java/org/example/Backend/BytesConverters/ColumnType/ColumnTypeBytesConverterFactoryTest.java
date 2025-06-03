package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverter.ColumnType.*;
import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.Models.ColumnType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTypeBytesConverterFactoryTest {

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getColumnTypeBytesConverters(ColumnType columnType, Class<?> expectedClass) {
        assertInstanceOf(expectedClass, BytesConverterFactory.getColumnTypeBytesConverters(columnType));
    }

    public static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(ColumnType.INT, BytesIntegerConverter.class),
            Arguments.of(ColumnType.LONG, BytesLongConverter.class),
            Arguments.of(ColumnType.DOUBLE, BytesDoubleConverter.class),
            Arguments.of(ColumnType.VARCHAR, BytesStringConverter.class),
            Arguments.of(ColumnType.DATE, BytesDateConverter.class),
            Arguments.of(ColumnType.BOOLEAN, BytesBooleanConverter.class)
        );
    }
}