package org.example.Backend.TableStorageManager.TableWriter.PrimitiveType;

import org.example.Backend.TableStorageManager.TH.CleanerTable;
import org.example.Backend.TableStorageManager.TablePathProvider.DatabaseTablePathProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.RandomAccessFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableLongWriterTest {
    private final DatabaseTablePathProvider pathProvider = new DatabaseTablePathProvider();
    private final TableWriter<Long> tableWriter = new TableLongWriter(pathProvider);
    private final String TABLE_NAME = "test_table";
    private final int TEST_OFFSET = 10;
    private static final Long TEST_DATA = 3213213233L;

    private final CleanerTable cleanerTable = new CleanerTable(pathProvider);

    @BeforeEach()
    public void setup() throws IOException {
        cleanerTable.clear(TABLE_NAME);
    }

    @Test
    void write() {
        tableWriter.write(TABLE_NAME, TEST_DATA, TEST_OFFSET);

        long numberRead = getNumberFromFile();
        assertEquals(TEST_DATA, numberRead);
    }

    private long getNumberFromFile() {
        String path = pathProvider.getTablePath(TABLE_NAME);
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.seek(TEST_OFFSET);
            return raf.readLong();
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
