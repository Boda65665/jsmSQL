package org.example.Backend.DataToBytesConverter.Interface;

public interface DataToByteArrayConverter<T> {
    byte[] toBytes(T data);
}
