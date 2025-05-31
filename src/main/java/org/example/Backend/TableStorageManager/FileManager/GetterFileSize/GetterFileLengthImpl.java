package org.example.Backend.TableStorageManager.FileManager.GetterFileSize;

import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;

import java.io.File;

public class GetterFileLengthImpl extends GetterFileLength {

    public GetterFileLengthImpl(FilePathProvider filePathProvider) {
        super(filePathProvider);
    }

    @Override
    public int getLength(String tableName) {
        String path = filePathProvider.getTablePath(tableName);

        File file = new File(path);
        return (int) file.length();
    }
}
