package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.Exception.NotFoundTable;
import org.example.Backend.TableStorageManager.TH.CleanerTable;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TableWriterImplTest {
    private final TablePathProviderImpl tablePathProvider = new TablePathProviderImpl();
    private final TableWriter tableWriter = new TableWriterImpl(tablePathProvider);
    private final CleanerTable cleanerTable = new CleanerTable(tablePathProvider);
    private final String NAME_TABLE = "test_table";

    @BeforeEach
    void setUp() {
        cleanerTable.clear(NAME_TABLE);
    }

    @ParameterizedTest
    @MethodSource("testCaseForWrite")
    void write(int offset, byte[] data) {
        tableWriter.write(NAME_TABLE, data, offset);
        assertArrayEquals(data, read(offset, data.length));
    }

    public static Stream<Arguments> testCaseForWrite() {
        return Stream.of(
                Arguments.of(0, new byte[]{1,2,3,4,5}),
                Arguments.of(3123, new byte[]{1,2,3,4,5})
        );
    }

    byte[] read(int offset, int length) {
        try (RandomAccessFile file = new RandomAccessFile(tablePathProvider.getTablePath(NAME_TABLE), "r")) {
            file.seek(offset);
            byte[] data = new byte[length];
            file.read(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void writeWithNullOrEmptyData(){
        assertThrows(IllegalArgumentException.class, () -> tableWriter.write("test_table", null, 0));
        assertThrows(IllegalArgumentException.class, () -> tableWriter.write("test_table", new byte[]{}, 0));
    }

    @Test
    void writeWithNegativePosition(){
        assertThrows(IllegalArgumentException.class, () -> tableWriter.write("test_table", new byte[]{0}, -1));
    }

    @Test
    void writeWithNullOrEmptyNameTable() {
        assertThrows(IllegalArgumentException.class, () -> tableWriter.write(null, new byte[]{0}, 0));
        assertThrows(IllegalArgumentException.class, () -> tableWriter.write("", new byte[]{0}, 0));
    }

    @Test
    void writeInNotExistingTable() {
        assertThrows(NotFoundTable.class, () -> tableWriter.write("not_exist", new byte[]{0}, 0));
    }
}

