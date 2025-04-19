package org.example.Backend.DataToBytesConverters;

public interface BytesConverters<T> {

    T toData(byte[] bytes);

    byte[] toBytes(T data);
}
