package org.example.Backend.TableStorageManager.DataToPrimitiveSerializer;

import org.example.Backend.Models.TypeData;

public class AbstractFactoryDataSerializer {

    public DataSerializer<?, ?> getDataSerializer(TypeData typeData) {
        return switch (typeData) {
            case DATE -> new DateToPrimitiveSerializer();
            default -> throw new IllegalArgumentException("Unsupported type: " + typeData);
        };
    }
}
