package org.example.Backend.DataToBytesConverter.Interface;

public interface ByteArrayToData<T> {
    T toData(byte[] bytes);
}
