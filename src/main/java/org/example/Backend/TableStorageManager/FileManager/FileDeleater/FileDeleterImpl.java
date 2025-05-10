package org.example.Backend.TableStorageManager.FileManager.FileDeleater;

import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import java.io.File;

public class FileDeleterImpl extends FileDeleter {

    public FileDeleterImpl(FilePathProvider pathProvider) {
        super(pathProvider);
    }

    @Override
    public void delete(String tableName) {
        File file = new File(pathProvider.getTablePath(tableName));
        if (file.exists()) file.delete();
    }
}
