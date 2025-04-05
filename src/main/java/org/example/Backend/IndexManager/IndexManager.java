package org.example.Backend.IndexManager;

public interface IndexManager<K, V> {
    V get(K key);
    void insert(K key, V value);
    void delete(K key);
}
