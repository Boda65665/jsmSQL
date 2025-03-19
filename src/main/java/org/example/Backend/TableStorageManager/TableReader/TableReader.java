package org.example.Backend.TableStorageManager.TableReader;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

public abstract class TableReader {
    private final TablePathProvider pathProvider;

    public TableReader(TablePathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    public abstract byte[] read(String tableName, int offset);
}
