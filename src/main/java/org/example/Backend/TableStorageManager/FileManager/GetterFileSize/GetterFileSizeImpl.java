package org.example.Backend.TableStorageManager.FileManager.GetterFileSize;

import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;

import java.io.File;

public class GetterFileSizeImpl extends GetterFileSize {

    public GetterFileSizeImpl(FilePathProvider filePathProvider) {
        super(filePathProvider);
    }

    @Override
    public int getSize(String tableName) {
        String path = filePathProvider.getTablePath(tableName);

        File file = new File(path);
        return (int) file.length();
    }
}
