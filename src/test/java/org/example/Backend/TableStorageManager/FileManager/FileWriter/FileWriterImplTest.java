package org.example.Backend.TableStorageManager.FileManager.FileWriter;

import org.example.Backend.Exception.NotFoundTable;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.FileManager.Factory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.Factory.FileOperationFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class FileWriterImplTest {
    private final FileOperationFactory fileOperationFactory = new FileOperationFactoryImpl();
    private final FileWriter fileWriter = new FileWriterImpl(fileOperationFactory.getFilePathProvider());
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(fileOperationFactory.getFilePathProvider());
    private final String NAME_TABLE = "test_table";

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
    }

    @ParameterizedTest
    @MethodSource("testCaseForWriteList")
    void writeList(int offset, List<Byte> data) {
        fileWriter.write(NAME_TABLE, data, offset);
        assertEquals(data, testHelperTSM.readList(NAME_TABLE, offset, data.size()));
    }

    public static Stream<Arguments> testCaseForWriteList() {
        List<Byte> bytes = new ArrayList<>(Arrays.asList((byte) 1, (byte) 2, (byte) 3));
        return Stream.of(
                Arguments.of(0, bytes),
                Arguments.of(3123, bytes)
        );
    }

    @ParameterizedTest
    @MethodSource("testCaseForWrite")
    void write(int offset, byte[] data) {
        fileWriter.write(NAME_TABLE, data, offset);
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
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write("test_table", (byte[]) null, 0));
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write("test_table", new byte[]{}, 0));
    }

    @Test
    void writeWithNegativePosition(){
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write("test_table", new byte[]{0}, -2));
    }

    @Test
    void writeWithNullOrEmptyNameTable() {
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write(null, new byte[]{0}, 0));
        assertThrows(IllegalArgumentException.class, () -> fileWriter.write("", new byte[]{0}, 0));
    }

    @Test
    void writeInNotExistingTable() {
        assertThrows(NotFoundTable.class, () -> fileWriter.write("not_exist", new byte[]{0}, 0));
    }
}

