package org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory;

import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer.BinarySerializer;
import org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer.BinarySerializerInt;
import org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer.BinarySerializerString;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BinaryConverterFactoryTest {
    private final BinaryConverterAbstractFactory factory = new BinaryConverterAbstractFactory();

    @ParameterizedTest
    @MethodSource("testCaseForGetBinarySerializer")
    void getBinarySerializer(Class<?> clasz, TypeData typeData) {
        BinarySerializer<?> binarySerializer = factory.getBinarySerializer(typeData);
        assertInstanceOf(clasz ,binarySerializer);
    }

    public static Stream<Arguments> testCaseForGetBinarySerializer() {
        return Stream.of(
                Arguments.of(BinarySerializerInt.class, TypeData.INT),
                Arguments.of(BinarySerializerString.class, TypeData.VARCHAR)
                );
    }

}