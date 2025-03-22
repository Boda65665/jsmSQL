package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.TablePathProvider.DatabaseTablePathProvider;
import org.example.Backend.TableStorageManager.TableWriter.PrimitiveType.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WriterAbstractFactoryTest {
    private final DatabaseTablePathProvider tablePathProvider = new DatabaseTablePathProvider();
    private final WriterAbstractFactory writerAbstractFactory = new WriterAbstractFactory(tablePathProvider);

    @ParameterizedTest
    @MethodSource("testCaseForGetTableWriter")
    @DisplayName("getTableWriter")
    void getTableWriter(TypeData input, Class<?> expected) {
        assertInstanceOf(expected, writerAbstractFactory.getTableWriter(input));
    }

    public static Stream<Arguments> testCaseForGetTableWriter() {
        return Stream.of(
                Arguments.of(TypeData.INT, TableIntegerWriter.class),
                Arguments.of(TypeData.LONG, TableLongWriter.class),
                Arguments.of(TypeData.DOUBLE, TableDoubleWriter.class),
                Arguments.of(TypeData.BOOLEAN, TableBooleanWriter.class),
                Arguments.of(TypeData.CHAR, TableCharWriter.class),
                Arguments.of(TypeData.VARCHAR, TableStringWriter.class)
        );
    }
}