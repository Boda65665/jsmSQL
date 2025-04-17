package org.example.Backend.TableStorageManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerCloser;
import org.example.Backend.DbManager.DbManagerFactory;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.TabularData;
import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.BytesConverters.BytesColumnConverter;
import org.example.Backend.TableStorageManager.BytesConverters.BytesConverterFactory;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProviderFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TableStorageManagerTest {
    private static final BytesColumnConverter bytesColumnConverters =
            (BytesColumnConverter) BytesConverterFactory.getBytesConverters(TypeData.COLUMN);
    private static final TableStorageManager tableStorageManager = new TableStorageManager();
    private static final DbManagerFactory dbManagerFactory = DbManagerFactory.getDbManagerFactory();
    private static final String basePath = System.getProperty("user.dir") + File.separator + "db";
    private static final DbManagerCloser dbManagerCloser = new DbManagerCloser();
    private static final DbManager freeSpace = dbManagerFactory.getDbManager(basePath, "freeSpace");
    private final TablePathProvider tablePathProvider = TablePathProviderFactory.getTablePathProvider();
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tablePathProvider);
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
    void save() {
        TabularData tabularData = generateTestDataForSave();
        save(tabularData);
        byte[] excepted = getExceptedResultForSave(tabularData);
        byte[] result = testHelperTSM.read(NAME_TABLE, 0, excepted.length);
        assertArrayEquals(excepted, result);
    }

    private byte[] getExceptedResultForSave(TabularData tabularData) {
        ArrayList<byte[]> columns = new ArrayList<>();
        int totalLength = 0;
        for (Column column : tabularData.getColumns()) {
            byte[] columnBytes = bytesColumnConverters.toBytes(column);
            columns.add(columnBytes);
            totalLength += columnBytes.length;
        }
        return toArrayByte(columns, totalLength);
    }

    private byte[] toArrayByte(ArrayList<byte[]> columns, int totalLength) {
        byte[] result = new byte[totalLength];
        int currentIndex = 0;

        for (byte[] column : columns) {
            System.arraycopy(column, 0, result, currentIndex, column.length);
            currentIndex += column.length;
        }

        return result;
    }

    private static TabularData generateTestDataForSave() {
        Column[] columns = new Column[6];
        columns[0] = new Column(1, TypeData.INT);
        columns[1] = new Column(1.1, TypeData.DOUBLE);
        columns[2] = new Column(111111111111L, TypeData.LONG);
        columns[3] = new Column("hello world", TypeData.VARCHAR);
        columns[4] = new Column(false, TypeData.BOOLEAN);
        columns[5] = new Column(new Date(123321), TypeData.DATE);

        return new TabularData(columns);
    }

    private static void save(TabularData tabularData) {
        tableStorageManager.save(NAME_TABLE, tabularData);
    }


//    @Test
//    public void saveWithFreeSpaceLessThanNecessary(){
//        TabularData tabularData = generateTestDataForSave();
//        byte[] excepted = getExceptedResult();
//        freeSpace.put(excepted.length - 10, 10);
//        save(tabularData);
//
//        byte[] result = getResultWithFreeSpaceLessThanNecessary(excepted.length);
//        assertEquals(excepted, result);
//    }
//
//    private byte[] getResultWithFreeSpaceLessThanNecessary(int exceptedLength) {
//        byte[] firstPartResult = testHelperTSM.read(NAME_TABLE, 10, exceptedLength - 10);
//        byte[] secondPartResult = testHelperTSM.read(NAME_TABLE, -1, 10);
//
//        byte[] result = Arrays.copyOf(firstPartResult, firstPartResult.length + secondPartResult.length);
//        System.arraycopy(secondPartResult, 0, result, firstPartResult.length, secondPartResult.length);
//        return result;
//    }
}