package org.example.Backend.TableStorageManager;

import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerCloser;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.DbManager.factory.DmManagerFactory;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.TablePartType;
import org.example.Backend.Models.TabularData;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class TableStorageManagerTest {
    private static final String baseDbPath = "/test";
    private static final DmManagerFactory dmManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private static final TableStorageManager tableStorageManager = new TableStorageManager(baseDbPath, dmManagerFactory, new TableOperationFactoryImpl());
    private static final DbManagerFactoryImpl dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private static final String basePath = System.getProperty("user.dir") + File.separator + "db";
    private static final DbManagerCloser dbManagerCloser = new DbManagerCloser(dbManagerFactory);
    private static final String NAME_TABLE = "test_table";
    private static final DbManager freeSpace = dbManagerFactory.getDbManager(basePath, "freeSpace_"+NAME_TABLE);
    private static final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tableOperationFactory.getTablePathProvider());

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
        freeSpace.clear();
    }

    @AfterAll
    static void tearDown() {
        dbManagerCloser.closeAll();
    }

    @Test
    void validCreateTable() {
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.createTable(null, null));
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.createTable("", null));
        assertThrows(NullPointerException.class, () -> tableStorageManager.createTable("not_emtpy_and_null", null));
    }

    @Test
    void validSave() {
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.save(null, null));
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.save("", null));
        assertThrows(NullPointerException.class, () -> tableStorageManager.save("not_emtpy_and_null", null));
    }

    @Test
    void save() {
        final int TOTAL_LENGTH = 78;
        freeSpace.put(15, 10);
        freeSpace.put(10, 40);
        TabularData tabularData = generateTestDataForSave();
        save(tabularData);

        ArrayList<Byte> excepted = getExceptedResultForSave(tabularData);
        ArrayList<Byte> result = testHelperTSM.readList(NAME_TABLE, 0, TOTAL_LENGTH);
        equalsArrayAndList(excepted, result);
    }

    private void equalsArrayAndList(ArrayList<Byte> excepted, ArrayList<Byte> result) {
        assertEquals(excepted, result);
    }

    private ArrayList<Byte> getExceptedResultForSave(TabularData tabularData) {
        TablePartTypeConverter<TabularData> tabularDataTablePartTypeConverter = BytesConverterFactory.getTablePartTypeConverter(TablePartType.TABULAR_DATA);
        ArrayList<Byte> result = tabularDataTablePartTypeConverter.toBytes(tabularData);
        ArrayList<Byte> excepted = new ArrayList<>();
        addZero(excepted, 10);
        excepted.addAll(result.subList(0, 16));

        addZero(excepted, 15);
        excepted.addAll(result.subList(16, 53));
        return excepted;
    }

    private void addZero(ArrayList<Byte> excepted, int count) {
        for (int i = 0; i < count; i++) {
            excepted.add((byte) 0);
        }
    }

    private static TabularData generateTestDataForSave() {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column(1, ColumnType.INT));
        columns.add(new Column(1.1, ColumnType.DOUBLE));
        columns.add(new Column(111111111111L, ColumnType.LONG));
        columns.add(new Column("hello world", ColumnType.VARCHAR));
        columns.add(new Column(false, ColumnType.BOOLEAN));
        columns.add(new Column(new Date(123321), ColumnType.DATE));

        return new TabularData(columns);
    }

    private static void save(TabularData tabularData) {
        tableStorageManager.save(NAME_TABLE, tabularData);
    }

}