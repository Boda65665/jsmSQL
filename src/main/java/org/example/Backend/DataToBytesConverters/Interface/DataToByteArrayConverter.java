package org.example.Backend.DataToBytesConverters.Interface;

public interface DataToByteArrayConverter<T> {
    byte[] toBytes(T data);
}
