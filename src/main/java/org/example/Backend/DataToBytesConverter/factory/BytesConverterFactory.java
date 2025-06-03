package org.example.Backend.DataToBytesConverter.factory;

import org.example.Backend.DataToBytesConverter.ColumnType.*;
import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverter.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverter.TableParts.BytesMetaDataConverters;
import org.example.Backend.DataToBytesConverter.TableParts.BytesRecordConverters;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.TablePartType;

public class BytesConverterFactory {
    public static ColumnTypeBytesConverter<?> getColumnTypeBytesConverters(ColumnType columnType) {
        return  switch (columnType){
            case INT -> new BytesIntegerConverter();
            case LONG -> new BytesLongConverter();
            case VARCHAR -> new BytesStringConverter();
            case BOOLEAN -> new BytesBooleanConverter();
            case DOUBLE -> new BytesDoubleConverter();
            case DATE -> new BytesDateConverter();
        };
    }

    public static TablePartTypeConverter getTablePartTypeConverter(TablePartType tablePartType) {
        return switch(tablePartType){
            case RECORD -> new BytesRecordConverters();
            case METADATA -> new BytesMetaDataConverters();
        };
    }
}
