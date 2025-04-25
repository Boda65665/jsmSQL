package org.example.Backend.DataToBytesConverters.factory;

import org.example.Backend.DataToBytesConverters.ColumnType.*;
import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.Models.ColumnType;

public class ColumnTypeBytesConverterFactory {

    public static ColumnTypeBytesConverter<?> getBytesConverters(ColumnType columnType) {
        return  switch (columnType){
            case INT -> new BytesIntegerConverters();
            case LONG -> new BytesLongConverters();
            case VARCHAR -> new BytesStringConverters();
            case BOOLEAN -> new BytesBooleanConverters();
            case DOUBLE -> new BytesDoubleConverters();
            case DATE -> new BytesDateConverters();
        };
    }
}
