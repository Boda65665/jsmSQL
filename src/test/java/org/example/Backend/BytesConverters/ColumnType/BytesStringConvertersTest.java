package org.example.Backend.BytesConverters.ColumnType;

import org.example.Backend.DataToBytesConverters.ColumnType.BytesStringConverters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class BytesStringConvertersTest {
    private final BytesStringConverters converter = new BytesStringConverters();
    private final Charset charset = StandardCharsets.UTF_8;

    @ParameterizedTest
    @MethodSource("testCaseForToData")
    void toData(String inputString) {
        byte[] bytes = converter.toBytes(inputString);
        assertArrayEquals(inputString.getBytes(charset), bytes);
    }

    private static Stream<Arguments> testCaseForToData() {
        return Stream.of(
                Arguments.of("Hello, World!"),
                Arguments.of("Привет, мир!"),
                Arguments.of(""),
                Arguments.of("12345"),
                Arguments.of("特殊文字"),
                Arguments.of("\uD83D\uDE00")
        );
    }

    @ParameterizedTest
    @MethodSource("testCaseForToBytes")
    void toBytes(String inputString) {
        byte[] bytes = inputString.getBytes(charset);
        assertEquals(inputString, converter.toData(bytes));
    }

    private static Stream<Arguments> testCaseForToBytes() {
        return Stream.of(
                Arguments.of("Hello, World!"),
                Arguments.of("Привет, мир!"),
                Arguments.of("12345"),
                Arguments.of("特殊文字"),
                Arguments.of("\uD83D\uDE00")
        );
    }

    @Test
    void nullOrEmptyArrayToData() {
        assertThrows(IllegalArgumentException.class, () -> converter.toData(null));
        assertThrows(IllegalArgumentException.class, () -> converter.toData(new byte[]{}));
    }

    @Test
    void nullToBytes() {
        assertThrows(NullPointerException.class, () -> converter.toBytes(null));
    }
}