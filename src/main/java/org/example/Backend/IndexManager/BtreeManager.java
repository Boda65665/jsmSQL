package org.example.Backend.IndexManager;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import java.io.File;

public class BtreeManager {
    private final BTreeMap<Integer, Integer> bTree;
    private final DB db;

    public BtreeManager(String tableName) {
        String basePathIndexesTree = System.getProperty("user.dir") + File.separator + "indexes" + File.separator;
        String pathIndexTree = basePathIndexesTree + tableName;
        creatDbDirectoryIfDoesntExist(basePathIndexesTree);

        db = DBMaker.fileDB(pathIndexTree).make();
        bTree = db.treeMap(tableName)
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

    public Integer get(Integer key) {
        return bTree.get(key);
    }

    public void insert(Integer key, Integer value) {
        bTree.put(key, value);
        commit();
    }

    public void delete(Integer key) {
        bTree.remove(key);
        commit();
    }

    public void close() {
        db.close();
    }
}
