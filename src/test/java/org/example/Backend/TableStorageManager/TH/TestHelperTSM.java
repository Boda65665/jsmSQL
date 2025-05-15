package org.example.Backend.TableStorageManager.TH;

import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class TestHelperTSM {
    private final FilePathProvider filePathProvider;

    public TestHelperTSM(FilePathProvider filePathProvider) {
        this.filePathProvider = filePathProvider;
    }

    public void clear(String nameTable) {
        String path = filePathProvider.getTablePath(nameTable);
        try (FileWriter ignored = new FileWriter(path, false)) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTable(String nameTable) {
        String path = filePathProvider.getTablePath(nameTable);
        File file = new File(path);
        if (file.exists()) file.delete();
    }

    public byte[] read(String nameTable, int offset, int length) {
        try (RandomAccessFile file = new RandomAccessFile(filePathProvider.getTablePath(nameTable), "r")) {
            if (offset == -1) offset = (int) file.length();
            if (length == -1) length = (int) file.length();

            file.seek(offset);
            byte[] data = new byte[length];
            file.read(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Byte> readList(String nameTable, int offset, int length) {
        try (RandomAccessFile file = new RandomAccessFile(filePathProvider.getTablePath(nameTable), "r")) {
            file.seek(offset);
            byte[] data = read(nameTable, offset, length);
            return toList(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Byte> toList(byte[] data) {
        ArrayList<Byte> result = new ArrayList<>();
        for (byte b : data) {
            result.add(b);
        }
        return result;
    }

    public void write(int offset, byte[] data, String nameTable) {
        String path = filePathProvider.getTablePath(nameTable);

        try (RandomAccessFile file = new RandomAccessFile(path, "rw")) {
            file.seek(offset);
            file.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] toArray(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }
}
