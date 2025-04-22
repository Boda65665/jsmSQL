package org.example.Backend.TableStorageManager.TableReader;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

import java.util.List;

public abstract class TableReader {
    protected final TablePathProvider tablePathProvider;

    protected TableReader(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    public abstract byte[] read(String tableName, int offset, int length);

    public abstract List<Byte> readList(String tableName, int offset, int length);
}
