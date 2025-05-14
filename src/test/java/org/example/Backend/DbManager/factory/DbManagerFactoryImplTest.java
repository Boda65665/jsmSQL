package org.example.Backend.DbManager.factory;

import org.example.Backend.DbManager.DbManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class DbManagerFactoryImplTest {
    private final DbManagerFactoryImpl dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private final String basePath = System.getProperty("user.dir") + File.separator + "test";

    @BeforeEach
    void setUp() throws IOException {
        clearDirectory();
    }

    void clearDirectory() throws IOException {
        List<Path> files = Files.list(Paths.get(basePath))
                .filter(Files::isRegularFile)
                .toList();

        for (Path path : files) Files.delete(path);
    }

    @Test
    void getDbManagerFactory() {
        assertInstanceOf(DbManagerFactoryImpl.class, DbManagerFactoryImpl.getDbManagerFactory());
    }

    @Test
    void getDbManager() {
        int oldSize = dbManagerFactory.getDbManagers().size();

        DbManager dbManager = dbManagerFactory.getDbManager(basePath, "test_name");
        dbManager.close();
        assertEquals(oldSize + 1, dbManagerFactory.getDbManagers().size());
        assertNotNull(dbManager);
    }

    @Test
    void getDbManagers() {
        assertEquals(0, dbManagerFactory.getDbManagers().size());
        int count = addRandomCountDbManagers();
        assertEquals(count, dbManagerFactory.getDbManagers().size());
    }

    private int addRandomCountDbManagers() {
        Random rand = new Random();
        int count = rand.nextInt(10) + 1;

        for (int i = 0; i < count; i++) {
            DbManager dbManager = dbManagerFactory.getDbManager(basePath, String.valueOf(i));
            dbManager.close();
        }
        return count;
    }


}