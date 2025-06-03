package org.example.Backend.BytesConverters.TableParts;

import org.example.Backend.DataToBytesConverter.TableParts.BytesMetaDataConverters;
import org.example.Backend.DataToBytesConverter.TableParts.BytesRecordConverters;
import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.Models.TablePartType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TablePartBytesConverterFactoryTest {

    @ParameterizedTest
    @MethodSource("caseForGetBytesConverters")
    void getBytesConverters(TablePartType type, Class<?> expectedClass) {
        assertInstanceOf(expectedClass, BytesConverterFactory.getTablePartTypeConverter(type));
    }

    public static Stream<Arguments> caseForGetBytesConverters() {
        return Stream.of(
                Arguments.of(TablePartType.METADATA, BytesMetaDataConverters.class),
                Arguments.of(TablePartType.RECORD, BytesRecordConverters.class));
    }
}
