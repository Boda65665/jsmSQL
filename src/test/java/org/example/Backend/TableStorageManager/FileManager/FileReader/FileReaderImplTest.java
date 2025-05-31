package org.example.Backend.TableStorageManager.FileManager.FileReader;

import org.example.Backend.Exception.NotFoundTable;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.FileManager.Factory.FileOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileReaderImplTest {
    private final FileOperationFactoryImpl tableOperationFactory = new FileOperationFactoryImpl();
    private final FilePathProvider filePathProvider = tableOperationFactory.getFilePathProvider();
    private final FileReader fileReader = new FileReaderImpl(filePathProvider);
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(filePathProvider);
    private final String NAME_TABLE = "test_table";

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
    }

    @ParameterizedTest
    @MethodSource("testCaseForRead")
    void read(int offset, byte[] data) {
        testHelperTSM.write(offset, data, NAME_TABLE);

        assertArrayEquals(data, fileReader.read(NAME_TABLE, offset, data.length));
    }

    public static Stream<Arguments> testCaseForRead() {
        return Stream.of(
            Arguments.of(0, new byte[]{1,2,3,4,5}),
            Arguments.of(10, new byte[]{-1,3,123})
        );
    }

    @Test
    void writeWithNullOrEmptyNameTable() {
        assertThrows(IllegalArgumentException.class, () -> fileReader.read(null, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> fileReader.read("", 0, 0));
    }

    @Test
    void writeWithNegativePosition(){
        assertThrows(IllegalArgumentException.class, () -> fileReader.read("test_table", -1, 0));
    }

    @Test
    void writeWithNegativeLength(){
        assertThrows(IllegalArgumentException.class, () -> fileReader.read("test_table", 0, -1));
    }

    @Test
    void writeInNotExistsTable() {
        assertThrows(NotFoundTable.class, () -> fileReader.read("not_exist", 0, 0));
    }
}