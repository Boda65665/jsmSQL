package org.example.Backend.DataToBytesConverters;

import org.example.Backend.DataToBytesConverters.ColumnType.*;
import org.example.Backend.DataToBytesConverters.Interface.ArrayByteConverter;
import org.example.Backend.DataToBytesConverters.TableParts.BytesMetaDataConverters;
import org.example.Backend.Models.TypeData;

public class BytesConverterFactory {

    public static ArrayByteConverter<?> getBytesConverters(TypeData typeData) {
        return  switch (typeData){
            case INT -> new BytesIntegerConverters();
            case LONG -> new BytesLongConverters();
            case VARCHAR -> new BytesStringConverters();
            case BOOLEAN -> new BytesBooleanConverters();
            case DOUBLE -> new BytesDoubleConverters();
            case DATE -> new BytesDateConverters();
            case TABLE_METADATA -> null;
            case TABULAR_DATA -> null;
        };
    }
}
