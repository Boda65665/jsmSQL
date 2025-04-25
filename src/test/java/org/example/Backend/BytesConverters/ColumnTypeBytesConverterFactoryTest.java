package org.example.Backend.BytesConverters;

import org.example.Backend.DataToBytesConverters.ColumnType.*;
import org.example.Backend.DataToBytesConverters.factory.ColumnTypeBytesConverterFactory;
import org.example.Backend.Models.ColumnType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTypeBytesConverterFactoryTest {

    @ParameterizedTest
    @MethodSource("caseForGetBytesConverters")
    void getBytesConverters(ColumnType columnType, Class<?> expectedClass) {
        assertInstanceOf(expectedClass, ColumnTypeBytesConverterFactory.getBytesConverters(columnType));
    }

    public static Stream<Arguments> caseForGetBytesConverters() {
        return Stream.of(
            Arguments.of(ColumnType.INT, BytesIntegerConverters.class),
            Arguments.of(ColumnType.LONG, BytesLongConverters.class),
            Arguments.of(ColumnType.DOUBLE, BytesDoubleConverters.class),
            Arguments.of(ColumnType.VARCHAR, BytesStringConverters.class),
            Arguments.of(ColumnType.DATE, BytesDateConverters.class),
            Arguments.of(ColumnType.BOOLEAN, BytesBooleanConverters.class)
        );
    }
}