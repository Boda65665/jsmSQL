package org.example.Backend.TableStorageManager.TableWriter.PrimitiveType;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TableCharWriter extends TableWriter<Character> {

    public TableCharWriter(TablePathProvider tablePathProvider) {
        super(tablePathProvider);
    }

    @Override
    public void write(String tableName, Character data, int offset) {
        validData(data, offset);

        String path = tablePathProvider.getTablePath(tableName);
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.seek(offset);
            raf.writeChar(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validData(Character data, int offset) {
        if (data == null) throw new NullPointerException("Data cannot be null");
        if (offset < 0) throw new IllegalArgumentException("Offset must be positive");
    }
}
