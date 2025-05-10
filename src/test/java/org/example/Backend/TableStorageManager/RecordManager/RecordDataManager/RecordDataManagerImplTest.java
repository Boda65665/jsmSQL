package org.example.Backend.TableStorageManager.RecordManager.RecordDataManager;

import org.example.Backend.Exception.ValidationException;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.Record;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class RecordDataManagerImplTest {
    private final RecordDataManagerImpl recordDataManager = new RecordDataManagerImpl();

    @ParameterizedTest
    @MethodSource("caseForValidData")
    void validData(Record record, ArrayList<ColumnStruct> columnStructList, boolean isValid) {
        if (isValid) {
            assertDoesNotThrow(() -> recordDataManager.validData(columnStructList, record));
        }
        else {
            assertThrows(ValidationException.class, () -> recordDataManager.validData(columnStructList, record));
        }
    }

    public static Stream<Arguments> caseForValidData() {
        Record record = getRecord();

        ArrayList<ColumnStruct> invalidTypeColumn = getColumnsStruct();
        invalidTypeColumn.get(0).setType(ColumnType.LONG);

        ArrayList<ColumnStruct> invalidNameColumn = getColumnsStruct();
        invalidNameColumn.get(0).setColumnName("invalid");

        ArrayList<ColumnStruct> invalidSizeColumns = getColumnsStruct();
        invalidSizeColumns.remove(0);

        return Stream.of(
                Arguments.of(record, invalidNameColumn, false),
                Arguments.of(record, invalidTypeColumn, false),
                Arguments.of(record, invalidSizeColumns, false),
                Arguments.of(record, getColumnsStruct(), true)
        );
    }

    private static Record getRecord() {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("Alex", ColumnType.VARCHAR, "name"));
        columns.add(new Column(1, ColumnType.INT, "age"));

        Record record = new Record();
        record.setColumns(columns);
        return record;
    }

    private static ArrayList<ColumnStruct> getColumnsStruct() {
        ArrayList<ColumnStruct> columnStructList = new ArrayList<>();
        columnStructList.add(new ColumnStruct("age", ColumnType.INT));
        columnStructList.add(new ColumnStruct("name", ColumnType.VARCHAR));
        return columnStructList;
    }

    @Test
    void editDataForSave() {
        Record record = getRecord();
        ArrayList<ColumnStruct> columnStructList = getColumnsStruct();
        columnStructList.add(new ColumnStruct("balance", ColumnType.DOUBLE));

        assertEquals(2, record.getColumns().size());

        recordDataManager.editDataForSave(columnStructList, record);
        assertEquals(3, record.getColumns().size());
        assertEquals("age", record.getColumns().get(0).getName());
        assertEquals("name", record.getColumns().get(1).getName());


        assertEquals("balance", record.getColumns().get(2).getName());
        assertEquals(ColumnType.DOUBLE, record.getColumns().get(2).getType());
        assertNull(record.getColumns().get(2).getData());
    }
}