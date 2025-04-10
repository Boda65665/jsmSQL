package org.example.Backend.IndexManager;

public interface IndexDbManager<K, V> {
    V get(K key);
    void insert(K key, V value);
    void delete(K key);
    void close();
}
