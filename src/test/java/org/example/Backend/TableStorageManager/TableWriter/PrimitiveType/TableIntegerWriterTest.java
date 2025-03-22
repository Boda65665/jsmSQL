package org.example.Backend.TableStorageManager.TableWriter.PrimitiveType;

import org.example.Backend.TableStorageManager.TH.CleanerTable;
import org.example.Backend.TableStorageManager.TablePathProvider.DatabaseTablePathProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TableIntegerWriterTest {
    private final DatabaseTablePathProvider pathProvider = new DatabaseTablePathProvider();
    private final TableWriter<Integer> tableWriter = new TableIntegerWriter(pathProvider);
    private final String TABLE_NAME = "test_table";
    private final int TEST_OFFSET = 10;
    private final int TEST_DATA = 324214323;

    private final CleanerTable cleanerTable = new CleanerTable(pathProvider);

    @BeforeEach()
    public void setup() throws IOException {
        cleanerTable.clear(TABLE_NAME);
    }

    @Test
    void write() {
        tableWriter.write(TABLE_NAME, TEST_DATA, TEST_OFFSET);

        int numberRead = getNumberFromFile();
        assertEquals(TEST_DATA, numberRead);
    }

    private int getNumberFromFile() {
        String path = pathProvider.getTablePath(TABLE_NAME);
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.seek(TEST_OFFSET);
            return raf.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
