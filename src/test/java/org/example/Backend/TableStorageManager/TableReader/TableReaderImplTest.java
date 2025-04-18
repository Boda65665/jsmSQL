package org.example.Backend.TableStorageManager.TableReader;

import org.example.Backend.Exception.NotFoundTable;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactoryImpl;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

class TableReaderImplTest {
    private final TableOperationFactoryImpl tableOperationFactory = new TableOperationFactoryImpl();
    private final TablePathProvider tablePathProvider = tableOperationFactory.getTablePathProvider();
    private final TableReader tableReader = new TableReaderImpl(tablePathProvider);
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tablePathProvider);
    private final String NAME_TABLE = "test_table";

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
    }

    @ParameterizedTest
    @MethodSource("testCaseForRead")
    void read(int offset, byte[] data) {
        write(offset, data);

        assertArrayEquals(data, tableReader.read(NAME_TABLE, offset, data.length));
    }

    public static Stream<Arguments> testCaseForRead() {
        return Stream.of(
            Arguments.of(0, new byte[]{1,2,3,4,5}),
            Arguments.of(10, new byte[]{-1,3,123})
        );
    }

    private void write(int offset, byte[] data) {
        String path = tablePathProvider.getTablePath(NAME_TABLE);

        try (RandomAccessFile file = new RandomAccessFile(path, "rw")) {
            file.seek(offset);
            file.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void writeWithNullOrEmptyNameTable() {
        assertThrows(IllegalArgumentException.class, () -> tableReader.read(null, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> tableReader.read("", 0, 0));
    }

    @Test
    void writeWithNegativePosition(){
        assertThrows(IllegalArgumentException.class, () -> tableReader.read("test_table", -1, 0));
    }

    @Test
    void writeWithNegativeLength(){
        assertThrows(IllegalArgumentException.class, () -> tableReader.read("test_table", 0, -1));
    }

    @Test
    void writeInNotExistsTable() {
        assertThrows(NotFoundTable.class, () -> tableReader.read("not_exist", 0, 0));
    }
}