package org.example.Backend.TableStorageManager.FileManager.GetterFileSize;

import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;

public abstract class GetterFileSize {
    protected final FilePathProvider filePathProvider;

    public GetterFileSize(FilePathProvider filePathProvider) {
        this.filePathProvider = filePathProvider;
    }

    public abstract int getSize(String tableName);

}
