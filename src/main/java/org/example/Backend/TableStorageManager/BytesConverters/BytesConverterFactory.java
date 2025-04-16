package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.TypeData;

public class BytesConverterFactory {

    public static BytesConverters<?> getBytesConverters(TypeData typeData) {
        return switch (typeData){
            case INT -> new BytesIntegerConverters();
            case LONG -> new BytesLongConverters();
            case VARCHAR -> new BytesStringConverters();
            case BOOLEAN -> new BytesBooleanConverters();
            case DOUBLE -> new BytesDoubleConverters();
            case DATE -> new BytesDateConverters();
            case TABLE_METADATA -> new BytesMetaDataConverters();
            case COLUMN -> new BytesColumnConverter();
        };
    }
}
