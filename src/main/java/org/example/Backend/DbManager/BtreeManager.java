package org.example.Backend.DbManager;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.serializer.GroupSerializer;
import java.io.File;
import java.util.NoSuchElementException;

public class BtreeManager<K, V> {
    private final BTreeMap<K, V> bTree;
    private final DB db;

    public BtreeManager(String nameDb, String basePath) {
        String pathIndexTree = basePath + File.separator + nameDb;
        creatDbDirectoryIfDoesntExist(basePath);

        db = DBMaker.fileDB(pathIndexTree).make();
        bTree = db.treeMap(nameDb)
                .keySerializer(GroupSerializer.JAVA)
                .valueSerializer(GroupSerializer.JAVA)
                .createOrOpen();
    }

    private void creatDbDirectoryIfDoesntExist(String pathIndexesTree) {
        File file = new File(pathIndexesTree);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    private void commit(){
        db.commit();
    }

    public V get(K key) {
        if (key == null) throw new NullPointerException("key is null");

        return bTree.get(key);
    }

    public void insert(K key, V value) {
        if (key == null) throw new NullPointerException("key is null");

        bTree.put(key, value);
        commit();
    }

    public void delete(K key) {
        if (key == null) throw new NullPointerException("key is null");

        bTree.remove(key);
        commit();
    }

    public K higherKey(K key){
        if (key == null) throw new NullPointerException("key is null");

        return bTree.higherKey(key);
    }

    public K maxKey() {
        try {
            return bTree.lastKey();
        } catch (NoSuchElementException noSuchElementException) {
            return null;
        }
    }

    public void close() {
        db.close();
    }

    public void clear(){
        bTree.clear();
    }

    public int size() {
        return bTree.size();
    }
}
