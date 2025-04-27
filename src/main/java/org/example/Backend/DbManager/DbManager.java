package org.example.Backend.DbManager;

public interface DbManager<K, V> {
    V get(K key);
    void put(K key, V value);
    void delete(K key);
    K higherKey(K key);
    K maxKey();
    void close();
    void clear();
    int size();
}
