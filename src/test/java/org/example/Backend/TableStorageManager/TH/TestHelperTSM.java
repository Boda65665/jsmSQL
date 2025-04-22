package org.example.Backend.TableStorageManager.TH;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class TestHelperTSM {
    private final TablePathProvider tablePathProvider;

    public TestHelperTSM(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    public void clear(String nameTable) {
        String path = tablePathProvider.getTablePath(nameTable);
        try (FileWriter ignored = new FileWriter(path, false)) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String nameTable) {
        String path = tablePathProvider.getTablePath(nameTable);
        File file = new File(path);
        if (file.exists()) file.delete();
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

    public List<Byte> readList(String nameTable, int offset, int length) {
        try (RandomAccessFile file = new RandomAccessFile(tablePathProvider.getTablePath(nameTable), "r")) {
            file.seek(offset);
            byte[] data = new byte[length];
            file.read(data);
            return toList(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Byte> toList(byte[] data) {
        List<Byte> result = new ArrayList<>();
        for (byte b : data) {
            result.add(b);
        }
        return result;
    }

    public void writeList(int offset, List<Byte> data, String nameTable) {
        byte[] bytes = toArray(data);
        write(offset, bytes, nameTable);
    }

    private byte[] toArray(List<Byte> data) {
        byte[] result = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }

    public void write(int offset, byte[] data, String nameTable) {
        String path = tablePathProvider.getTablePath(nameTable);

        try (RandomAccessFile file = new RandomAccessFile(path, "rw")) {
            file.seek(offset);
            file.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
