package org.example.Backend.TableStorageManager.DataToPrimitiveSerializer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DateToPrimitiveSerializerTest {
    private final DateToPrimitiveSerializer dateToPrimitiveSerializer = new DateToPrimitiveSerializer();

    @ParameterizedTest
    @MethodSource("testCaseForSerialize")
    @DisplayName("serialize date to primitive type")
    void serialize(Date input, Long expected) {
        assertEquals(expected, dateToPrimitiveSerializer.serialize(input));
    }

    public static Stream<Arguments> testCaseForSerialize() {
        return Stream.of(
                Arguments.of(new Date(), System.currentTimeMillis()),
                Arguments.of(new Date(0), 0L),
                Arguments.of(new Date(1577836800000L), 1577836800000L),
                Arguments.of(new Date(946684800000L), 946684800000L),
                Arguments.of(new Date(1893456000000L), 1893456000000L)
        );
    }


    @ParameterizedTest
    @MethodSource("testCaseForDeserialize")
    @DisplayName("deserialize primitive type to date")
    void deserialize(Long input, Date expected) {
        assertEquals(expected, dateToPrimitiveSerializer.deserialize(input));
    }

    public static Stream<Arguments> testCaseForDeserialize() {
        return Stream.of(
                Arguments.of(System.currentTimeMillis(), new Date()),
                Arguments.of(0L, new Date(0)),
                Arguments.of(1577836800000L, new Date(1577836800000L)),
                Arguments.of(946684800000L, new Date(946684800000L)),
                Arguments.of(1893456000000L, new Date(1893456000000L))
        );
    }
}