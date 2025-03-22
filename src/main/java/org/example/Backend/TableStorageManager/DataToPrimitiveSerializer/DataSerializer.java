package org.example.Backend.TableStorageManager.DataToPrimitiveSerializer;

public interface DataSerializer<T, P> {
    P serialize(T data);
    T deserialize(P serializeData);
}
