package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.TypeData;

public class BytesConverterFactory {
    public BytesConverters<?> getBytesConverters(TypeData typeData) {
        return switch (typeData){
            case INT -> new BytesToIntegerConverters();
            default -> null;
        };
    }
}
