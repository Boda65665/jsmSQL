package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.ColumnType.BytesStringConverters;
import org.example.Backend.Exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class BytesStringConvertersTest {
    private final BytesStringConverters converter = new BytesStringConverters();
    private final Charset charset = StandardCharsets.UTF_8;
    private final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};

    @ParameterizedTest
    @MethodSource("testCase")
    void toBytes(String inputString) {
        byte[] bytes = converter.toBytes(inputString);
        assertArrayEquals(getBytes(inputString), bytes);
    }

    private byte[] getBytes(String inputString) {
        if(inputString == null) return NULL_BYTES;
        return inputString.getBytes(charset);
    }

    private static Stream<Arguments> testCase() {
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
    @MethodSource("testCase")
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