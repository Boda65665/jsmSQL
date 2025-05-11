package org.example.Backend.TableStorageManager.FileManager.FileReader;

import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;


public abstract class FileReader {
    protected final FilePathProvider filePathProvider;

    protected FileReader(FilePathProvider filePathProvider) {
        this.filePathProvider = filePathProvider;
    }

    public abstract byte[] read(String tableName, int offset, int length);
}
