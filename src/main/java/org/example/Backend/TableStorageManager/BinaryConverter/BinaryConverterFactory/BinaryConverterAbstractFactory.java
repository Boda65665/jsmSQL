package org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory;

import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer.BinarySerializer;
import org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer.BinarySerializerInt;
import org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer.BinarySerializerString;

public class BinaryConverterAbstractFactory {
    public BinarySerializer<?> getBinarySerializer(TypeData typeData) {
        switch (typeData) {
            case INT: return new BinarySerializerInt();
            case VARCHAR: return new BinarySerializerString();
            default: return null;
        }
    }
}
