package org.example.Backend.TableStorageManager.TableWriter.PrimitiveType;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

public abstract class TableWriter<T> {
    protected final TablePathProvider tablePathProvider;

    protected TableWriter(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    public abstract void write(String tableName, T data, int offset);
}
