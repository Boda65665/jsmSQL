package org.example.Backend.TableStorageManager.BytesConverters;

public interface BytesConverters<T> {

    T toData(byte[] bytes);

    byte[] toBytes(T data);
}
