package org.example.Backend.BytesConverters;

import org.example.Backend.DataToBytesConverters.*;
import org.example.Backend.Models.TypeData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BytesConverterFactoryTest {

    @ParameterizedTest
    @MethodSource("caseForGetBytesConverters")
    void getBytesConverters(TypeData typeData, Class<?> expectedClass) {
        assertInstanceOf(expectedClass, BytesConverterFactory.getBytesConverters(typeData));
    }

    public static Stream<Arguments> caseForGetBytesConverters() {
        return Stream.of(
            Arguments.of(TypeData.INT, BytesIntegerConverters.class),
            Arguments.of(TypeData.LONG, BytesLongConverters.class),
            Arguments.of(TypeData.DOUBLE, BytesDoubleConverters.class),
            Arguments.of(TypeData.VARCHAR, BytesStringConverters.class),
            Arguments.of(TypeData.DATE, BytesDateConverters.class),
            Arguments.of(TypeData.TABLE_METADATA, BytesMetaDataConverters.class),
            Arguments.of(TypeData.BOOLEAN, BytesBooleanConverters.class)
        );
    }
}