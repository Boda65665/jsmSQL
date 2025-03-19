package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

public abstract class TableWriter {
    private final TablePathProvider tablePathProvider;

    protected TableWriter(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    public abstract void write(String tableName, byte[] data);
}
