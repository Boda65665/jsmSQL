package org.example.Backend.TableStorageManager.FileManager.FilePathProvider;

public class FilePathProviderImpl extends FilePathProvider {
    private final String FOLDERS_WITH_TABLES = "tables";

    @Override
    public String getTablePath(String tableName) {
        String currentDirectory = System.getProperty("user.dir");
        return String.format("%s\\%s\\%s.bin", currentDirectory, FOLDERS_WITH_TABLES, tableName);
    }
}
