package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.ColumnType.*;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.Models.ColumnType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTypeBytesConverterFactoryTest {

    @ParameterizedTest
    @MethodSource("caseForGetColumnTypeBytesConverters")
    void getColumnTypeBytesConverters(ColumnType columnType, Class<?> expectedClass) {
        assertInstanceOf(expectedClass, BytesConverterFactory.getColumnTypeBytesConverters(columnType));
    }

    public static Stream<Arguments> caseForGetColumnTypeBytesConverters() {
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