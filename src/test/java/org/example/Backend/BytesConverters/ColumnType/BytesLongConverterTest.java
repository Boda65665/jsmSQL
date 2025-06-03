package org.example.Backend.BytesConverters.ColumnType;


import org.example.Backend.Utils.ByteUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.example.Backend.DataToBytesConverter.ColumnType.BytesLongConverter;
import org.example.Backend.Exception.ValidationException;
import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.NULL_BYTES;

class BytesLongConverterTest {
    private final BytesLongConverter bytesConverters = new BytesLongConverter();

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

                Arguments.of(-1L,             ByteUtils.longToByteArray(-1L)),
                Arguments.of(-255L,           ByteUtils.longToByteArray(-255L)),
                Arguments.of(-256L,           ByteUtils.longToByteArray(-256L)),
                Arguments.of(-65535L,         ByteUtils.longToByteArray(-65535L)),
                Arguments.of(-12321333333L,   ByteUtils.longToByteArray(-12321333333L)),

                Arguments.of(null, NULL_BYTES)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void toData(Long expectedOutput, byte[] input) {
        Long actualOutput = bytesConverters.toData(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void validToData() {
        assertThrows(ValidationException.class, () -> bytesConverters.toData(null));
        assertThrows(ValidationException.class, () -> bytesConverters.toData(new byte[]{}));
    }

}