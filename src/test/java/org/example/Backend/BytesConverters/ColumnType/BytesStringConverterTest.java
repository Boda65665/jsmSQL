package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverter.ColumnType.BytesStringConverter;
import org.example.Backend.Exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.NULL_BYTES;
import static org.junit.jupiter.api.Assertions.*;

class BytesStringConverterTest {
    private final BytesStringConverter converter = new BytesStringConverter();
    private final Charset charset = StandardCharsets.UTF_8;

    @ParameterizedTest
    @MethodSource("provideTestData")
    void toBytes(String inputString) {
        byte[] bytes = converter.toBytes(inputString);
        assertArrayEquals(getBytes(inputString), bytes);
    }

    private byte[] getBytes(String inputString) {
        if(inputString == null) return NULL_BYTES;
        return inputString.getBytes(charset);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("Hello, World!"),
                Arguments.of("Привет, мир!"),
                Arguments.of("12345"),
                Arguments.of("特殊文字"),
                Arguments.of("\uD83D\uDE00"),

                Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void toData(String inputString) {
        byte[] bytes = getBytes(inputString);
        assertEquals(inputString, converter.toData(bytes));
    }

    @Test
    void nullOrEmptyArrayToData() {
        assertThrows(ValidationException.class, () -> converter.toData(null));
        assertThrows(ValidationException.class, () -> converter.toData(new byte[]{}));
    }
}