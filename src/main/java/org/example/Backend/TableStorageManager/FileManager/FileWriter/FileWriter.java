package org.example.Backend.TableStorageManager.FileManager.FileWriter;

import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;

import java.util.List;

public abstract class FileWriter {
    protected final FilePathProvider filePathProvider;

    protected FileWriter(FilePathProvider filePathProvider) {
        this.filePathProvider = filePathProvider;
    }

    public abstract void write(String tableName, byte[] bytes, int offset);

    public abstract void write(String tableName, List<Byte> bytes, int offset);
}
