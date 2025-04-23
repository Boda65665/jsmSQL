package org.example.Backend.DataToBytesConverters.Interface;

public interface ByteArrayToData<T> {
    T toData(byte[] bytes);
}
