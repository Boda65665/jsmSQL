package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverter.ColumnType.BytesDoubleConverter;
import org.example.Backend.Exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.NULL_BYTES;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BytesDoubleConverterTest {
    private final BytesDoubleConverter converter = new BytesDoubleConverter();

    @ParameterizedTest
    @MethodSource("provideTestData")
    void toData(byte[] inputBytes, Double expected) {
            assertEquals(expected, converter.toData(inputBytes));
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(new byte[]{64, 9, 33, -5, 73, 15, -12, -16}, 3.141592570113623),
                Arguments.of(new byte[]{64, 73, 15, -12, -16, 0, 0, 0}, 50.12466239929199),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}, 0.0),

                Arguments.of(NULL_BYTES, null)
        );
    }

    @Test
    void nullOrEmptyArrayToData(){
        assertThrows(ValidationException.class, () -> converter.toData(null));
        assertThrows(ValidationException.class, () -> converter.toData(new byte[]{}));
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void toBytes(byte[] expectedBytes, Double inputData) {
            assertArrayEquals(expectedBytes, converter.toBytes(inputData));
    }
}

