package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.Exception.NotFoundTable;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class TableWriterImplTest {
    private final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();
    private final TableWriter tableWriter = new TableWriterImpl(tableOperationFactory.getTablePathProvider());
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tableOperationFactory.getTablePathProvider());
    private final String NAME_TABLE = "test_table";

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
    }

    @ParameterizedTest
    @MethodSource("testCaseForWrite")
    void write(int offset, byte[] data) {
        tableWriter.write(NAME_TABLE, data, offset);
        assertArrayEquals(data, testHelperTSM.read(NAME_TABLE, offset, data.length));
    }

    public static Stream<Arguments> testCaseForWrite() {
        return Stream.of(
                Arguments.of(0, new byte[]{1,2,3,4,5}),
                Arguments.of(3123, new byte[]{1,2,3,4,5})
        );
    }

    @Test
    void writeWithNullOrEmptyData(){
        assertThrows(IllegalArgumentException.class, () -> tableWriter.write("test_table", null, 0));
        assertThrows(IllegalArgumentException.class, () -> tableWriter.write("test_table", new byte[]{}, 0));
    }

    @Test
    void writeWithNegativePosition(){
        assertThrows(IllegalArgumentException.class, () -> tableWriter.write("test_table", new byte[]{0}, -2));
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

