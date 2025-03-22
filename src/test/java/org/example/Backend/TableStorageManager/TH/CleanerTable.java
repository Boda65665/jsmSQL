package org.example.Backend.TableStorageManager.TH;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

import java.io.FileWriter;
import java.io.IOException;

public class CleanerTable {
    private final TablePathProvider tablePathProvider;

    public CleanerTable(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    public void clear(String nameTable) throws IOException {
        String path = tablePathProvider.getTablePath(nameTable);
        try (FileWriter writer = new FileWriter(path, false)) {
        }
    }
}
