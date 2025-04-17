package org.example.Backend.TableStorageManager.TableCreater;

import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.BytesConverters.BytesConverterFactory;
import org.example.Backend.TableStorageManager.BytesConverters.BytesConverters;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TableCrateImplTest {
    private final TablePathProvider pathProvider = new TablePathProviderImpl();
    private final TableCrateImpl tableCrate = new TableCrateImpl(pathProvider);
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(pathProvider);
    private final BytesConverters<TableMetaData> tableMetaDataBytesConverters =
            (BytesConverters<TableMetaData>) BytesConverterFactory.getBytesConverters(TypeData.TABLE_METADATA);
    private final String NAME_TABLE = "test_table";

    @BeforeEach
    void setUp() {
        testHelperTSM.delete(NAME_TABLE);
    }

    @Test
    void create() {
        TableMetaData testData = generateTestData();
        tableCrate.create(NAME_TABLE, testData);
        byte[] excepted = tableMetaDataBytesConverters.toBytes(testData);

        byte[] result = testHelperTSM.read(NAME_TABLE, 0, excepted.length);
        assertArrayEquals(excepted, result);
    }

    private TableMetaData generateTestData() {
        ArrayList<ColumnStruct> columnStructList = new ArrayList<>();
        columnStructList.add(new ColumnStruct("first", TypeData.INT));
        columnStructList.add(new ColumnStruct("second", TypeData.VARCHAR));
        columnStructList.add(new ColumnStruct("third", TypeData.DATE));

        return new TableMetaData(columnStructList);
    }
}