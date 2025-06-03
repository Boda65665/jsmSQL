package org.example.Backend.DataToBytesConverter.Interface;

import java.util.ArrayList;

public interface TablePartTypeConverter<T> extends ByteArrayToData<T>{
    ArrayList<Byte> toBytes(T data);
}
