package org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BytesSerializerStringTest {
    private final BinarySerializerString binarySerializerString = new BinarySerializerString();

    @ParameterizedTest
    @MethodSource("testCaseForSerialize")
    @DisplayName("serialize string to binary")
    public void serialize(String input, String expected) {
        assertEquals(expected, binarySerializerString.serialize(input));
    }

    public static Stream<Arguments> testCaseForSerialize() {
        return Stream.of(
                // Тестовые случаи
                Arguments.of("A", "01000001"),
                Arguments.of("AB", "01000001 01000010"),
                Arguments.of("Hello", "01001000 01100101 01101100 01101100 01101111"),
                Arguments.of(" ", "00100000"),
                Arguments.of("123", "00110001 00110010 00110011"),
                Arguments.of("!@#", "00100001 01000000 00100011"),
                Arguments.of("abc", "01100001 01100010 01100011")
        );
    }

    @ParameterizedTest
    @MethodSource("testCaseForDeserialize")
    @DisplayName("deserialize binary to string")
    public void deserialize(String input, String expected) {
        assertEquals(expected, binarySerializerString.deserialize(input));
    }

    public static Stream<Arguments> testCaseForDeserialize() {
        return Stream.of(
                Arguments.of("01000001", "A"),
                Arguments.of("01001000 01100101 01101100 01101100 01101111", "Hello"),
                Arguments.of("01001000 01100101 01101100 01101100 01101111 00100000 01010111 01101111 01110010 01101100 01100100", "Hello World"),
                Arguments.of("00100001 1000000 00100011 00100100 00100101", "!@#$%"),
                Arguments.of("01000001 01100010 01100011", "Abc"),
                Arguments.of("00100000", " "),
                Arguments.of("00001010", "\n")
        );
    }
}
