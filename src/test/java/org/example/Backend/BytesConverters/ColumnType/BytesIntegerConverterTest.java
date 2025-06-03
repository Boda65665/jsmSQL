package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverter.ColumnType.BytesIntegerConverter;
import org.example.Backend.Exception.ValidationException;

import org.example.Backend.Utils.ByteUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.NULL_BYTES;
import static org.junit.jupiter.api.Assertions.*;

public class BytesIntegerConverterTest {
    private final BytesIntegerConverter bytesConverters = new BytesIntegerConverter();

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void toBytes(Integer input, byte[] expectedOutput) {
        byte[] actualOutput = bytesConverters.toBytes(input);
        assertArrayEquals(expectedOutput, actualOutput, "Conversion failed for input: " + input);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(0, new byte[]{0}),
                Arguments.of(1, new byte[]{1}),
                Arguments.of(255, new byte[]{-1}),
                Arguments.of(256, new byte[]{1, 0}),
                Arguments.of(65535, new byte[]{-1, -1}),
                Arguments.of(65536, new byte[]{1, 0, 0}),
                Arguments.of(16777215, new byte[]{-1, -1, -1}),
                Arguments.of(16777216, new byte[]{1, 0, 0, 0}),

                Arguments.of(-1,    ByteUtils.intToByteArray(-1)),
                Arguments.of(-255,  ByteUtils.intToByteArray(-255)),
                Arguments.of(-256,  ByteUtils.intToByteArray(-256)),
                Arguments.of(-65535, ByteUtils.intToByteArray(-65535)),
                Arguments.of(-65536, ByteUtils.intToByteArray(-65536)),

                Arguments.of(null, NULL_BYTES)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void toData(Integer expectedOutput, byte[] input) {
        Integer actualOutput = bytesConverters.toData(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void toDataWithNullOrEmptyArray() {
        assertThrows(ValidationException.class, () -> bytesConverters.toData(new byte[]{}));
        assertThrows(ValidationException.class, () -> bytesConverters.toData(null));
    }
}