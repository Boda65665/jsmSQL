package org.example.Backend.TableStorageManager.FileManager.GetterFileSize;

import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;

public abstract class GetterFileLength {
    protected final FilePathProvider filePathProvider;

    public GetterFileLength(FilePathProvider filePathProvider) {
        this.filePathProvider = filePathProvider;
    }

    public abstract int getLength(String tableName);

}
