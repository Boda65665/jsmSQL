package org.example.Backend.TableStorageManager.TableWriter.PrimitiveType;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TableLongWriter extends TableWriter<Long> {

    public TableLongWriter(TablePathProvider tablePathProvider) {
        super(tablePathProvider);
    }

    @Override
    public void write(String tableName, Long data, int offset) {
        validData(data, offset);

        String path = tablePathProvider.getTablePath(tableName);
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.seek(offset);
            raf.writeLong(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validData(Long data, int offset) {
        if (data == null) throw new NullPointerException("Data cannot be null");
        if (offset < 0) throw new IllegalArgumentException("Offset must be positive");
    }
}
