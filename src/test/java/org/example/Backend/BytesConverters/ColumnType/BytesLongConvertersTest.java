package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.ColumnType.BytesLongConverters;
import org.example.Backend.Exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BytesLongConvertersTest {
    private final BytesLongConverters bytesConverters = new BytesLongConverters();
    private static final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void toBytes(Long input, byte[] expectedOutput) {
        byte[] actualOutput = bytesConverters.toBytes(input);
        assertArrayEquals(expectedOutput, actualOutput, "Conversion failed for input: " + input);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(0L, new byte[]{0}),
                Arguments.of(1L, new byte[]{1}),
                Arguments.of(256L, new byte[]{1, 0}),
                Arguments.of(65535L, new byte[]{-1, -1}),
                Arguments.of(16777215L, new byte[]{-1, -1, -1}),
                Arguments.of(12321333333L, new byte[]{2, -34, 104, -96, 85}),

                Arguments.of(-1L, ByteBuffer.allocate(8).putLong(-1L).array()),
                Arguments.of(-255L, ByteBuffer.allocate(8).putLong(-255L).array()),
                Arguments.of(-256L, ByteBuffer.allocate(8).putLong(-256L).array()),
                Arguments.of(-65535L, ByteBuffer.allocate(8).putLong(-65535L).array()),
                Arguments.of(-12321333333L, ByteBuffer.allocate(8).putLong(-12321333333L).array()),

                Arguments.of(null, NULL_BYTES)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForToData")
    public void toData(byte[] input, Long expectedOutput) {
        Long actualOutput = bytesConverters.toData(input);
        assertEquals(expectedOutput, actualOutput);
    }

    private static Stream<Arguments> provideTestDataForToData() {
        return Stream.of(
                Arguments.of(new byte[]{0}, 0L),
                Arguments.of(new byte[]{1}, 1L),
                Arguments.of(new byte[]{1, 0}, 256L),
                Arguments.of(new byte[]{1, 0, 0}, 65536L),
                Arguments.of(new byte[]{2, -34, 104, -96, 85}, 12321333333L),

                Arguments.of(ByteBuffer.allocate(8).putLong(-1L).array(), -1L),
                Arguments.of(ByteBuffer.allocate(8).putLong(-256L).array(), -256L),
                Arguments.of(ByteBuffer.allocate(8).putLong(-65536L).array(), -65536L),
                Arguments.of(ByteBuffer.allocate(8).putLong(-12321333333L).array(), -12321333333L),

                Arguments.of(NULL_BYTES, null)
        );
    }

    @Test
    public void validToData() {
        assertThrows(ValidationException.class, () -> bytesConverters.toData(null));
        assertThrows(ValidationException.class, () -> bytesConverters.toData(new byte[]{}));
    }

}