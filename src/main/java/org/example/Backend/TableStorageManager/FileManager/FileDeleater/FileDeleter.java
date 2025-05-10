package org.example.Backend.TableStorageManager.FileManager.FileDeleater;

import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;

public abstract class FileDeleter {
    protected final FilePathProvider pathProvider;

    protected FileDeleter(FilePathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    public abstract void delete(String tableName);
}
