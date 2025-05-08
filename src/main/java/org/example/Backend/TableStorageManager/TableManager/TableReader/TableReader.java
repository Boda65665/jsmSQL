package org.example.Backend.TableStorageManager.TableManager.TableReader;

import org.example.Backend.TableStorageManager.TableManager.TablePathProvider.TablePathProvider;


public abstract class TableReader {
    protected final TablePathProvider tablePathProvider;

    protected TableReader(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    public abstract byte[] read(String tableName, int offset, int length);
}
