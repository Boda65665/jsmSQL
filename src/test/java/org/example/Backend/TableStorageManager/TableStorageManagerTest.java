package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerCloser;
import org.example.Backend.DbManager.DbManagerFactory;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class TableStorageManagerTest {
    private static final TableStorageManager tableStorageManager = new TableStorageManager(new TableOperationFactoryImpl());
    private static final DbManagerFactory dbManagerFactory = DbManagerFactory.getDbManagerFactory();
    private static final String basePath = System.getProperty("user.dir") + File.separator + "db";
    private static final DbManagerCloser dbManagerCloser = new DbManagerCloser();
    private static final DbManager freeSpace = dbManagerFactory.getDbManager(basePath, "freeSpace");
    private static final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tableOperationFactory.getTablePathProvider());
    private static final String NAME_TABLE = "test_table";

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
}
//    @Test
//    void validSave() {
//        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.save(null, null));
//        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.save("", null));
//        assertThrows(NullPointerException.class, () -> tableStorageManager.save("not_emtpy_and_null", null));
//    }
//
//    @Test
//    void save() {
//        TabularData tabularData = generateTestDataForSave();
//        save(tabularData);
//        byte[] excepted = getExceptedResultForSave(tabularData);
//        byte[] result = testHelperTSM.read(NAME_TABLE, 0, excepted.length);
//        assertArrayEquals(excepted, result);
//    }
//
//    private byte[] getExceptedResultForSave(TabularData tabularData) {
//        ArrayList<byte[]> columns = new ArrayList<>();
//        int totalLength = 0;
//        for (Column column : tabularData.getColumns()) {
//            byte[] columnBytes = bytesColumnConverters.toBytes(column);
//            columns.add(columnBytes);
//            totalLength += columnBytes.length;
//        }
//        return toArrayByte(columns, totalLength);
//    }
//
//    private byte[] toArrayByte(ArrayList<byte[]> columns, int totalLength) {
//        byte[] result = new byte[totalLength];
//        int currentIndex = 0;
//
//        for (byte[] column : columns) {
//            System.arraycopy(column, 0, result, currentIndex, column.length);
//            currentIndex += column.length;
//        }
//
//        return result;
//    }
//
//    private static TabularData generateTestDataForSave() {
//        Column[] columns = new Column[6];
//        columns[0] = new Column(1, ColumnType.INT);
//        columns[1] = new Column(1.1, ColumnType.DOUBLE);
//        columns[2] = new Column(111111111111L, ColumnType.LONG);
//        columns[3] = new Column("hello world", ColumnType.VARCHAR);
//        columns[4] = new Column(false, ColumnType.BOOLEAN);
//        columns[5] = new Column(new Date(123321), ColumnType.DATE);
//
//        return new TabularData(columns);
//    }
//
//    private static void save(TabularData tabularData) {
//        tableStorageManager.save(NAME_TABLE, tabularData);
//    }
//
//}