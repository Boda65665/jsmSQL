package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.ColumnType.BytesDoubleConverters;
import org.example.Backend.Exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BytesDoubleConvertersTest {
    private final BytesDoubleConverters converter = new BytesDoubleConverters();
    private static final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};

    @ParameterizedTest
    @MethodSource("provideBytesForToData")
    void toData(byte[] inputBytes, Double expected) {
            assertEquals(expected, converter.toData(inputBytes));
    }

    private static Stream<Arguments> provideBytesForToData() {
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
    @MethodSource("provideDoublesForToBytes")
    void toBytes(Double inputData, byte[] expectedBytes) {
            assertArrayEquals(expectedBytes, converter.toBytes(inputData));
    }

    private static Stream<Arguments> provideDoublesForToBytes() {
        return Stream.of(
                Arguments.of(3.141592570113623, new byte[]{64, 9, 33, -5, 73, 15, -12, -16}),
                Arguments.of(50.12466239929199, new byte[]{64, 73, 15, -12, -16, 0, 0, 0}),
                Arguments.of(0.0, new byte[]{0, 0, 0, 0, 0, 0, 0, 0}),

                Arguments.of(null, NULL_BYTES)
        );
    }
}

