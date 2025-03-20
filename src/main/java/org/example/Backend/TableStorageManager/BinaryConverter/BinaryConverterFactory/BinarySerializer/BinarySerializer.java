package org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer;

public interface BinarySerializer<T> {
    String serialize(T data);
    T deserialize(String binaryString);
}
