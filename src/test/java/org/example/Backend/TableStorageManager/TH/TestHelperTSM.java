package org.example.Backend.TableStorageManager.TH;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TestHelperTSM {
    private final TablePathProvider tablePathProvider;

    public TestHelperTSM(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    public void clear(String nameTable) {
        String path = tablePathProvider.getTablePath(nameTable);
        try (FileWriter writer = new FileWriter(path, false)) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] read(String nameTable, int offset, int length) {
        try (RandomAccessFile file = new RandomAccessFile(tablePathProvider.getTablePath(nameTable), "r")) {
            file.seek(offset);
            byte[] data = new byte[length];
            file.read(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
