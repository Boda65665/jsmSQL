package org.example.Backend.DataToBytesConverters.TableParts;

import org.example.Backend.DataToBytesConverters.BytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ArrayByteConverter;
import org.example.Backend.DataToBytesConverters.Interface.ByteCollectionConverter;
import org.example.Backend.DataToBytesConverters.TableParts.TH.TestHelperConverterTableParts;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.TabularData;
import org.example.Backend.Models.TypeData;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class BytesTabularDataConvertersTest {
    private final ByteCollectionConverter<TabularData> converter = new BytesTabularDataConverters();
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
            ArrayByteConverter<Object> arrayByteConverter = (ArrayByteConverter<Object>) BytesConverterFactory.getBytesConverters(column.getTypeData());
            byte[] dataBytes = arrayByteConverter.toBytes(column.getData());

            addBytesLengthData(result, dataBytes.length);
            addBytesData(result, dataBytes);
            addBytesTypeColumn(result, column.getTypeData());
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

    private void addBytesTypeColumn(ArrayList<Byte> result, TypeData typeData) {
        ArrayList<Byte> bytesType = testHelperConverterTableParts.getBytesFromInt(typeData.ordinal());
        result.addAll(bytesType);
    }

    private TabularData generateTestData() {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column(1, TypeData.INT));
        columns.add(new Column(122222222333L, TypeData.LONG));
        columns.add(new Column(1.1, TypeData.DOUBLE));
        columns.add(new Column(true, TypeData.BOOLEAN));
        columns.add(new Column("hello", TypeData.VARCHAR));
        columns.add(new Column(new Date(122L), TypeData.DATE));

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