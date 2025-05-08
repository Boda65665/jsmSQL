package org.example.Backend.TableStorageManager.TableManager.TablePathProvider;

import org.example.Backend.TableStorageManager.TableManager.TablePathProvider.TablePathProviderImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TablePathProviderImplTest {
    @Test
    void getTablePath_shouldReturnCorrectPathIfFileExists() {
        TablePathProviderImpl provider = new TablePathProviderImpl();
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