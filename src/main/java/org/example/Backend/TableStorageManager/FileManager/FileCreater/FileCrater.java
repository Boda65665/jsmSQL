package org.example.Backend.TableStorageManager.FileManager.FileCreater;

import org.example.Backend.Models.TableMetaData;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;

public abstract class FileCrater {
    protected final FilePathProvider pathProvider;

    protected FileCrater(FilePathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    public abstract void create(String tableName, TableMetaData tableMetaData);
}
