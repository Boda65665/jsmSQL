package org.example.Backend.TableStorageManager.TablePathProvider;

import java.io.File;

public class TablePathProviderImpl extends TablePathProvider {
    private final String FOLDERS_WITH_TABLES = "tables";

    @Override
    public String getTablePath(String tableName) {
        String currentDirectory = System.getProperty("user.dir");
        return String.format("%s\\%s\\%s.bin", currentDirectory, FOLDERS_WITH_TABLES, tableName);
    }
}
