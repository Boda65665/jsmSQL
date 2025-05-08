package org.example.Backend.TableStorageManager.TableManager.TableIndexManager;

import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TableIndexManagerTest {
    private final String basePath = System.getProperty("user.dir") + File.separator + "test";
    private final String INDEX_NAME = "test_index";
    private final DbManagerFactory dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private final TableIndexManager<Integer, Integer> indexManager = new TableIndexManagerImpl(dbManagerFactory.getDbManager(basePath, INDEX_NAME));
    private final int TEST_KEY = 1;
    private final int TEST_VALUE = 1;

    @Test
    void addIndex() {
        assertNull(indexManager.getIndex(TEST_KEY));
        indexManager.addIndex(TEST_KEY, TEST_VALUE);
        assertNotNull(indexManager.getIndex(TEST_KEY));
    }

    @Test
    void removeIndex() {
        indexManager.addIndex(TEST_KEY, TEST_VALUE);
        assertNotNull(indexManager.getIndex(TEST_KEY));

        indexManager.removeIndex(TEST_KEY);
        assertNull(indexManager.getIndex(TEST_KEY));
    }

    @Test
    void getIndex() {
        indexManager.addIndex(TEST_KEY, TEST_VALUE);

        assertEquals(TEST_VALUE, indexManager.getIndex(TEST_KEY));
    }
}