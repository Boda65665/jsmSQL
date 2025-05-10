package org.example.Backend.TableStorageManager.FileManager.FilePathProvider;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FilePathProviderImplTest {
    @Test
    void getTablePath_shouldReturnCorrectPathIfFileExists() {
        FilePathProviderImpl provider = new FilePathProviderImpl();
        String tableName = "test_table";

        String result = provider.getTablePath(tableName);
        assertNotNull(result);

        String excepted = getExceptedPath(tableName);
        assertEquals(excepted, result);
    }

    private String getExceptedPath(String tableName) {
        return String.format("%s\\tables\\%s.bin", System.getProperty("user.dir"), tableName);
    }
}