package org.example.Backend.TableStorageManager.TableWriter.PrimitiveType;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TableStringWriter extends TableWriter<String> {

    public TableStringWriter(TablePathProvider tablePathProvider) {
        super(tablePathProvider);
    }

    @Override
    public void write(String tableName, String data, int offset) {
        validData(data, offset);

        String path = tablePathProvider.getTablePath(tableName);
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.seek(offset);
            raf.writeChars(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validData(String data, int offset) {
        if (data == null) throw new NullPointerException("Data cannot be null");
        if (data.isEmpty()) throw new IllegalArgumentException("Data cannot be an empty string");
        if (offset < 0) throw new IllegalArgumentException("Offset must be positive");
    }
}
