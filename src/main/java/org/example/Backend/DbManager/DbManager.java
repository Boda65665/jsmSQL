package org.example.Backend.DbManager;

public interface DbManager<K, V> {
    V get(K key);
    void put(K key, V value);
    void delete(K key);
    Integer higherKey(K key);
    Integer maxKey();
    void close();
    void clear();
}
