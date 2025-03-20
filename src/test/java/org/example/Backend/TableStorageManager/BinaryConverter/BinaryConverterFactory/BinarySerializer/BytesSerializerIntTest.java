package org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BytesSerializerIntTest {
    private final BinarySerializerInt binarySerializerInt = new BinarySerializerInt();

    @ParameterizedTest
    @MethodSource("testCaseForSerialize")
    @DisplayName("serialize int to binary")
    public void serialize(int input, String expected) {
        assertEquals(expected, binarySerializerInt.serialize(input));
    }

    public static Stream<Arguments> testCaseForSerialize() {
        return Stream.of(
                Arguments.of(0, "00"),
                Arguments.of(1, "01"),
                Arguments.of(2, "010"),
                Arguments.of(5, "0101"),
                Arguments.of(10, "01010"),
                Arguments.of(255, "011111111"),
                Arguments.of(-1, "111111111111111111111111111111111"),
                Arguments.of(Integer.MAX_VALUE, "01111111111111111111111111111111"),
                Arguments.of(Integer.MIN_VALUE, "110000000000000000000000000000000")
        );
    }

    @ParameterizedTest
    @MethodSource("testCaseForDeserialize")
    @DisplayName("deserialize binary to int")
    public void deserialize(String input, int expected) {
        assertEquals(expected, binarySerializerInt.deserialize(input));
    }

    public static Stream<Arguments> testCaseForDeserialize() {
        return Stream.of(
                Arguments.of("00", 0),          // "0" в двоичной системе равно 0
                Arguments.of("01", 1),          // "1" в двоичной системе равно 1
                Arguments.of("010", 2),         // "10" в двоичной системе равно 2
                Arguments.of("0101", 5),       // "101" в двоичной системе равно 5
                Arguments.of("01010", 10),      // "1010" в двоичной системе равно 10
                Arguments.of("011111111", 255), // "11111111" в двоичной системе равно 255
                Arguments.of("111111111111111111111111111111111", -1), // "11111111111111111111111111111111" (32 бита) равно -1
                Arguments.of("01111111111111111111111111111111", Integer.MAX_VALUE), // "1111111111111111111111111111111" равно MAX_VALUE
                Arguments.of("110000000000000000000000000000000", Integer.MIN_VALUE)  // "10000000000000000000000000000000" равно MIN_VALUE
        );
    }
}