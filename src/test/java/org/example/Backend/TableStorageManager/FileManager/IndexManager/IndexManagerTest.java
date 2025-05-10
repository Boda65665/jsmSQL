package org.example.Backend.TableStorageManager.FileManager.IndexManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.TableStorageManager.IndexManager.IndexManager;
import org.example.Backend.TableStorageManager.IndexManager.IndexManagerImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class IndexManagerTest {
    private static final String basePath = System.getProperty("user.dir") + File.separator + "test";
    private static final String INDEX_NAME = "test_index";
    private static final DbManagerFactory dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private static final DbManager indexes = dbManagerFactory.getDbManager(basePath, INDEX_NAME);
    private static final IndexManager<Integer, Integer> indexManager = new IndexManagerImpl(indexes);
    private final int TEST_KEY = 1;
    private final int TEST_VALUE = 1;

    @BeforeAll
    public static void setUp() {
        indexes.clear();
    }

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