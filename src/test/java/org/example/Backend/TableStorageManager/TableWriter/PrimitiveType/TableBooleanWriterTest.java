package org.example.Backend.TableStorageManager.TableWriter.PrimitiveType;

import org.example.Backend.TableStorageManager.TH.CleanerTable;
import org.example.Backend.TableStorageManager.TablePathProvider.DatabaseTablePathProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TableBooleanWriterTest {
    private final DatabaseTablePathProvider pathProvider = new DatabaseTablePathProvider();
    private final TableWriter<Boolean> tableWriter = new TableBooleanWriter(pathProvider);
    private final String TABLE_NAME = "test_table";
    private final int TEST_OFFSET = 10;

    private final CleanerTable cleanerTable = new CleanerTable(pathProvider);

    @BeforeEach()
    public void setup() throws IOException {
        cleanerTable.clear(TABLE_NAME);
    }

    @Test
    void writeTrue() {
        Boolean valueToWrite = true;
        tableWriter.write(TABLE_NAME, valueToWrite, TEST_OFFSET);
        Boolean valueRead = getBooleanFromFile();
        assertEquals(valueToWrite, valueRead);
    }

    @Test
    void writeFalse() {
        Boolean valueToWrite = false;
        tableWriter.write(TABLE_NAME, valueToWrite, TEST_OFFSET);

        Boolean valueRead = getBooleanFromFile();
        assertEquals(valueToWrite, valueRead);
    }

    private Boolean getBooleanFromFile() {
        String path = pathProvider.getTablePath(TABLE_NAME);
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.seek(TEST_OFFSET);
            return raf.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test()
    void writeWithNegativeOffset() {
        Boolean valueToWrite = false;

        assertThrows(IllegalArgumentException.class, () -> {
            tableWriter.write(TABLE_NAME, valueToWrite, -1);
        });
    }

    @Test
    void writeNull() {
        assertThrows(NullPointerException.class, () -> {
            tableWriter.write(TABLE_NAME, null, TEST_OFFSET);
        });
    }
}