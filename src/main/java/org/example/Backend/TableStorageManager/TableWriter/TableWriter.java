package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

import java.util.List;

public abstract class TableWriter {
    protected final TablePathProvider tablePathProvider;

    protected TableWriter(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    public abstract void write(String tableName, byte[] bytes, int offset);

    public abstract void write(String tableName, List<Byte> bytes, int offset);
}
