package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.TypeData;

public class BytesConverterFactory {
    public BytesConverters<?> getBytesConverters(TypeData typeData) {
        return switch (typeData){
            case INT -> new BytesToIntegerConverters();
            case LONG -> new BytesToLongConverters();
            case VARCHAR -> new BytesToStringConverters();
            case BOOLEAN -> new BytesToBooleanConverters();
            case TABLE_METADATA -> new BytesToMetaDataConverters();
            default -> null;
        };
    }
}
