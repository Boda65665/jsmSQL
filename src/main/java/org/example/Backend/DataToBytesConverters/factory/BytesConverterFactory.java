package org.example.Backend.DataToBytesConverters.factory;

import org.example.Backend.DataToBytesConverters.ColumnType.*;
import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.TableParts.BytesMetaDataConverters;
import org.example.Backend.DataToBytesConverters.TableParts.BytesTableFragmentLink;
import org.example.Backend.DataToBytesConverters.TableParts.BytesTabularDataConverters;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.TablePartType;

public class BytesConverterFactory {
    public static ColumnTypeBytesConverter<?> getColumnTypeBytesConverters(ColumnType columnType) {
        return  switch (columnType){
            case INT -> new BytesIntegerConverters();
            case LONG -> new BytesLongConverters();
            case VARCHAR -> new BytesStringConverters();
            case BOOLEAN -> new BytesBooleanConverters();
            case DOUBLE -> new BytesDoubleConverters();
            case DATE -> new BytesDateConverters();
        };
    }

    public static TablePartTypeConverter getTablePartTypeConverter(TablePartType tablePartType) {
        return switch(tablePartType){
            case TABULAR_DATA -> new BytesTabularDataConverters();
            case TABLE_METADATA -> new BytesMetaDataConverters();
            case TABLE_FRAGMENT_LINK -> new BytesTableFragmentLink((ColumnTypeBytesConverter<Integer>) getColumnTypeBytesConverters(ColumnType.INT),
                    (ColumnTypeBytesConverter<Long>) getColumnTypeBytesConverters(ColumnType.LONG));
        };
    }
}
