package org.example.Backend.DbManager;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import java.io.File;
import java.util.NoSuchElementException;

public class BtreeManager {
    private final BTreeMap<Integer, Integer> bTree;
    private final DB db;

    public BtreeManager(String nameDb, String basePath) {
        String pathIndexTree = basePath + File.separator + nameDb;
        creatDbDirectoryIfDoesntExist(basePath);

        db = DBMaker.fileDB(pathIndexTree).make();
        bTree = db.treeMap(nameDb)
                .keySerializer(org.mapdb.Serializer.INTEGER)
                .valueSerializer(Serializer.INTEGER)
                .createOrOpen();
    }

    private void creatDbDirectoryIfDoesntExist(String pathIndexesTree) {
        File file = new File(pathIndexesTree);
        if (!file.exists()) file.mkdir();
    }

    private void commit(){
        db.commit();
    }

    public Integer get(int key) {
        if (key < 0) throw new IllegalArgumentException("key is negative");

        return bTree.get(key);
    }

    public void insert(int key, int value) {
        if (key < 0) throw new IllegalArgumentException("key is negative");

        bTree.put(key, value);
        commit();
    }

    public void delete(int key) {
        if (key < 0) throw new IllegalArgumentException("key is negative");

        bTree.remove(key);
        commit();
    }

    public void close() {
        db.close();
    }

    public Integer higherKey(int key){
        if (key < 0) throw new IllegalArgumentException("key is negative");

        return bTree.higherKey(key);
    }

    public Integer maxKey() {
        try {
            return bTree.lastKey();
        } catch (NoSuchElementException noSuchElementException) {
            return null;
        }
    }

    public void clear(){
        bTree.clear();
    }
}
