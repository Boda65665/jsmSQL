package org.example.Backend.TableStorageManager.FileManager.TableWriter;

import org.example.Backend.Exception.NotFoundTable;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class FileWriterImpl extends FileWriter {

    public FileWriterImpl(FilePathProvider filePathProvider) {
        super(filePathProvider);
    }

    @Override
    public void write(String tableName, List<Byte> byteList, int offset) {
        byte[] bytes = listByteToArray(byteList);

        write(tableName, bytes, offset);
    }

    private byte[] listByteToArray(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    @Override
    public void write(String tableName, byte[] data, int offset) {
        valid(data, offset, tableName);

        String path = filePathProvider.getTablePath(tableName);
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

        String path = filePathProvider.getTablePath(tableName);
        if (!new File(path).exists()) throw new NotFoundTable();
    }
}
