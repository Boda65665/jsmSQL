package org.example.Backend.DataToBytesConverters.Interface;

import java.util.ArrayList;

public interface ByteCollectionConverter<T> extends ByteArrayToData<T>{
    ArrayList<Byte> toBytes(T data);
}
