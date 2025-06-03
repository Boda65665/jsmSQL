package org.example.Backend.TableStorageManager.FileManager.FileCreater;

import org.example.Backend.DataToBytesConverter.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TablePartType;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.FileManager.Factory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.Factory.FileOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileCraterImplTest {
    private final FileOperationFactory fileOperationFactory = new FileOperationFactoryImpl();
    private final FilePathProvider pathProvider = fileOperationFactory.getFilePathProvider();
    private final FileCraterImpl tableCrate = new FileCraterImpl(pathProvider, new FileWriterImpl(pathProvider));
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(pathProvider);
    private final TablePartTypeConverter<TableMetaData> tableMetaDataConverter
            = BytesConverterFactory.getTablePartTypeConverter(TablePartType.METADATA);
    private final String NAME_TABLE = "test_table";

    @BeforeEach
    void setUp() {
        testHelperTSM.deleteTable(NAME_TABLE);
    }

    @Test
    void create() {
        TableMetaData testData = generateTestData();
        tableCrate.create(NAME_TABLE, testData);
        ArrayList<Byte> excepted = tableMetaDataConverter.toBytes(testData);

        byte[] result = testHelperTSM.read(NAME_TABLE, 0, excepted.size());
        assertArrayEquals(testHelperTSM.toArray(excepted), result);
    }

    private TableMetaData generateTestData() {
        ArrayList<ColumnStruct> columnStructList = new ArrayList<>();
        columnStructList.add(new ColumnStruct("first", ColumnType.INT, true));
        columnStructList.add(new ColumnStruct("second", ColumnType.VARCHAR, false));
        columnStructList.add(new ColumnStruct("third", ColumnType.DATE, false));

        return new TableMetaData(columnStructList, "test");
    }

    @Test
    void validCreate(){
        assertThrows(NullPointerException.class, () -> tableCrate.create(null, generateTestData()));
        assertThrows(IllegalArgumentException.class, () -> tableCrate.create("", generateTestData()));
        assertThrows(IllegalArgumentException.class, () -> tableCrate.create("    ", generateTestData()));
        assertThrows(NullPointerException.class, () -> tableCrate.create(NAME_TABLE, null));
    }
}