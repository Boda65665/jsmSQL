package org.example.Backend.BytesConverters.TableParts;

import org.example.Backend.DataToBytesConverter.TableParts.BytesRecordConverters;
import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverter.Interface.TablePartTypeConverter;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.Record;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.example.Backend.Utils.ByteUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class BytesRecordConvertersTest {
    private final TablePartTypeConverter<Record> converter = new BytesRecordConverters();

    @Test
    void toBytes() {
        Record td = generateTestData();

        ArrayList<Byte> bytes = converter.toBytes(td);
        ArrayList<Byte> expectedBytes = toBytes(td.getColumns());
        assertEquals(expectedBytes, bytes);
    }

    private ArrayList<Byte> toBytes(ArrayList<Column> columns) {
        ArrayList<Byte> result = new ArrayList<>();

        for (Column column : columns) {
            ColumnTypeBytesConverter<Object> columnTypeBytesConverter = (ColumnTypeBytesConverter<Object>) BytesConverterFactory.getColumnTypeBytesConverters(column.getType());
            byte[] dataBytes = columnTypeBytesConverter.toBytes(column.getData());

            addBytesLengthData(result, dataBytes.length);
            addBytesData(result, dataBytes);
            addBytesTypeColumn(result, column.getType());
        }
        return result;
    }

    private void addBytesLengthData(ArrayList<Byte> result, int length) {
        List<Byte> bytesLength = intToTwoByteList(length);
        result.addAll(bytesLength);
    }

    private void addBytesData(ArrayList<Byte> result, byte[] dataBytes) {
        addArrayToList(result, dataBytes);
    }

    private void addBytesTypeColumn(ArrayList<Byte> result, ColumnType columnType) {
        List<Byte> bytesType = intToTwoByteList(columnType.ordinal());
        result.addAll(bytesType);
    }

    private Record generateTestData() {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column(1, ColumnType.INT));
        columns.add(new Column(122222222333L, ColumnType.LONG));
        columns.add(new Column(1.1, ColumnType.DOUBLE));
        columns.add(new Column(true, ColumnType.BOOLEAN));
        columns.add(new Column("hello", ColumnType.VARCHAR));
        columns.add(new Column(new Date(122L), ColumnType.DATE));

        return new Record(columns);
    }

    @Test
    public void toData(){
        Record testData = generateTestData();
        ArrayList<Byte> bytes = toBytes(testData.getColumns());

        Record result = converter.toData(byteListToArray(bytes));
        assertEquals(testData, result);
    }
}