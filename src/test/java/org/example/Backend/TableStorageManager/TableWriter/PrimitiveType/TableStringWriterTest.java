package org.example.Backend.TableStorageManager.TableWriter.PrimitiveType;

import org.example.Backend.TableStorageManager.TH.CleanerTable;
import org.example.Backend.TableStorageManager.TablePathProvider.DatabaseTablePathProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class TableStringWriterTest {
    private final DatabaseTablePathProvider pathProvider = new DatabaseTablePathProvider();
    private final TableWriter<String> tableWriter = new TableStringWriter(pathProvider);
    private final String TABLE_NAME = "test_table";
    private final int TEST_OFFSET = 10;
    private static final String TEST_DATA = "Hello, World!";

    private final CleanerTable cleanerTable = new CleanerTable(pathProvider);

    @BeforeEach()
    public void setup() throws IOException {
        cleanerTable.clear(TABLE_NAME);
    }

    @Test
    void write() {
        tableWriter.write(TABLE_NAME, TEST_DATA, TEST_OFFSET);

        String dataRead = getStringFromFile();
        assertEquals(TEST_DATA, dataRead);
    }

    private String getStringFromFile() {
        String path = pathProvider.getTablePath(TABLE_NAME);
        try (RandomAccessFile raf = new RandomAccessFile(path, "r")) {
            raf.seek(TEST_OFFSET);
            StringBuilder sb = new StringBuilder();
            while (raf.getFilePointer() < raf.length()) {
                sb.append(raf.readChar());
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    @Test()
    void writeWithNegativeOffset() {
        assertThrows(IllegalArgumentException.class, () -> {
            tableWriter.write(TABLE_NAME, TEST_DATA, -1);
        });
    }

    @Test
    void writeNull() {
        assertThrows(NullPointerException.class, () -> {
            tableWriter.write(TABLE_NAME, null, TEST_OFFSET);
        });
    }

    @Test
    void writeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            tableWriter.write(TABLE_NAME, "", TEST_OFFSET);
        });
    }
}