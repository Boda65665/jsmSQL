package org.example.Backend.TableStorageManager.TableReader;

import org.example.Backend.Exception.NotFoundTable;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TableReaderImpl extends TableReader {

    public TableReaderImpl(TablePathProvider pathProvider) {
        super(pathProvider);
    }

    @Override
    public byte[] read(String tableName, int offset, int length) {
        valid(tableName, offset, length);

        String path = tablePathProvider.getTablePath(tableName);
        try (RandomAccessFile file = new RandomAccessFile(path, "r")) {
            if (offset == -1) offset = (int) file.length();

            file.seek(offset);
            byte[] data = new byte[length];
            file.read(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void valid(String tableName, int offset, int length) {
        if (tableName == null || tableName.trim().isEmpty()) throw new IllegalArgumentException("tableName is null or empty");
        if (offset < 0) throw new IllegalArgumentException("offset is negative");
        if (length < 0) throw new IllegalArgumentException("length is negative");

        String path = tablePathProvider.getTablePath(tableName);
        if (!new File(path).exists()) throw new NotFoundTable();
    }
}
