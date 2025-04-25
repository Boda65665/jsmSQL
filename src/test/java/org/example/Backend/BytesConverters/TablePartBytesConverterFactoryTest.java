package org.example.Backend.BytesConverters;

import org.example.Backend.DataToBytesConverters.TableParts.BytesMetaDataConverters;
import org.example.Backend.DataToBytesConverters.TableParts.BytesTabularDataConverters;
import org.example.Backend.DataToBytesConverters.factory.TablePartTypeBytesConverterFactory;
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
        assertInstanceOf(expectedClass, TablePartTypeBytesConverterFactory.getTablePartTypeConverter(type));
    }

    public static Stream<Arguments> caseForGetBytesConverters() {
        return Stream.of(
                Arguments.of(TablePartType.TABLE_METADATA, BytesMetaDataConverters.class),
                Arguments.of(TablePartType.TABULAR_DATA, BytesTabularDataConverters.class));
    }
}
