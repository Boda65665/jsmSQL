package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.Exception.NotFoundTable;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TableWriterImpl extends TableWriter {

    protected TableWriterImpl(TablePathProvider tablePathProvider) {
        super(tablePathProvider);
    }

    @Override
    public void write(String tableName, byte[] data, int offset) {
        valid(data, offset, tableName);
        String path = tablePathProvider.getTablePath(tableName);

        try (RandomAccessFile file = new RandomAccessFile(path, "rw")) {
            if (offset == -1) offset = (int) file.length();

            file.seek(offset);
            file.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void valid(byte[] data, int offset, String tableName) {
        if (data == null || data.length == 0) throw new IllegalArgumentException("data is null or empty");
        if (offset < -1) throw new IllegalArgumentException("offset is negative");
        if (tableName == null || tableName.trim().isEmpty()) throw new IllegalArgumentException("tableName is null or empty");

        String path = tablePathProvider.getTablePath(tableName);
        if (path == null) throw new NotFoundTable();
    }
}
