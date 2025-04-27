package org.example.Backend.BytesConverters.TableParts;

import org.example.Backend.DataToBytesConverters.TableParts.BytesTabularDataConverters;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.BytesConverters.TableParts.TH.TestHelperConverterTableParts;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.TabularData;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class BytesTabularDataConvertersTest {
    private final TablePartTypeConverter<TabularData> converter = new BytesTabularDataConverters();
    private final TestHelperConverterTableParts testHelperConverterTableParts = new TestHelperConverterTableParts();

    @Test
    void toBytes() {
        TabularData td = generateTestData();

        ArrayList<Byte> bytes = converter.toBytes(td);
        ArrayList<Byte> expectedBytes = toBytes(td.getColumns());
        assertEquals(expectedBytes, bytes);
    }

    private ArrayList<Byte> toBytes(ArrayList<Column> columns) {
        ArrayList<Byte> result = new ArrayList<>();

        for (Column column : columns) {
            ColumnTypeBytesConverter<Object> columnTypeBytesConverter = (ColumnTypeBytesConverter<Object>) BytesConverterFactory.getColumnTypeBytesConverters(column.getColumnType());
            byte[] dataBytes = columnTypeBytesConverter.toBytes(column.getData());

            addBytesLengthData(result, dataBytes.length);
            addBytesData(result, dataBytes);
            addBytesTypeColumn(result, column.getColumnType());
        }
        return result;
    }

    private void addBytesLengthData(ArrayList<Byte> result, int length) {
        ArrayList<Byte> bytesLength = testHelperConverterTableParts.getBytesFromInt(length);
        result.addAll(bytesLength);
    }

    private void addBytesData(ArrayList<Byte> result, byte[] dataBytes) {
        testHelperConverterTableParts.addArrayToList(result, dataBytes);
    }

    private void addBytesTypeColumn(ArrayList<Byte> result, ColumnType columnType) {
        ArrayList<Byte> bytesType = testHelperConverterTableParts.getBytesFromInt(columnType.ordinal());
        result.addAll(bytesType);
    }

    private TabularData generateTestData() {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column(1, ColumnType.INT));
        columns.add(new Column(122222222333L, ColumnType.LONG));
        columns.add(new Column(1.1, ColumnType.DOUBLE));
        columns.add(new Column(true, ColumnType.BOOLEAN));
        columns.add(new Column("hello", ColumnType.VARCHAR));
        columns.add(new Column(new Date(122L), ColumnType.DATE));

        return new TabularData(columns);
    }

    @Test
    public void toData(){
        TabularData testData = generateTestData();
        ArrayList<Byte> bytes = toBytes(testData.getColumns());

        TabularData result = converter.toData(testHelperConverterTableParts.toArray(bytes));
        assertEquals(testData, result);
    }
}