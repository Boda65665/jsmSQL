package org.example.Backend.TableStorageManager.TableWriter.CustomType;

import org.example.Backend.TableStorageManager.TH.CleanerTable;
import org.example.Backend.TableStorageManager.TablePathProvider.DatabaseTablePathProvider;
import org.example.Backend.TableStorageManager.TableWriter.PrimitiveType.TableDateWriter;
import org.example.Backend.TableStorageManager.TableWriter.PrimitiveType.TableLongWriter;
import org.example.Backend.TableStorageManager.TableWriter.PrimitiveType.TableWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableDateWriterTest {
    private final DatabaseTablePathProvider pathProvider = new DatabaseTablePathProvider();
    private final TableWriter<Long> tableWriterLong = new TableLongWriter(pathProvider);
    private final TableWriter<Date> tableWriterDate = new TableDateWriter(pathProvider, tableWriterLong);
    private final String TABLE_NAME = "test_table";
    private final int TEST_OFFSET = 10;
    private static final Date TEST_DATA = new Date(2143213213);

    private final CleanerTable cleanerTable = new CleanerTable(pathProvider);

    @BeforeEach()
    public void setup() throws IOException {
        cleanerTable.clear(TABLE_NAME);
    }

    @Test
    void write() {
        tableWriterDate.write(TABLE_NAME, TEST_DATA, TEST_OFFSET);

        Date dateRead = getDateFromFile();
        assertEquals(TEST_DATA, dateRead);
    }

    private Date getDateFromFile() {
        String path = pathProvider.getTablePath(TABLE_NAME);
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.seek(TEST_OFFSET);
            return new Date(raf.readLong());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test()
    void writeWithNegativeOffset() {
        assertThrows(IllegalArgumentException.class, () -> {
            tableWriterDate.write(TABLE_NAME, TEST_DATA, -1);
        });
    }

    @Test
    void writeNull() {
        assertThrows(NullPointerException.class, () -> {
            tableWriterDate.write(TABLE_NAME, null, TEST_OFFSET);
        });
    }
}
