package org.example.Backend.DataToBytesConverter.TableParts;

import org.example.Backend.DataToBytesConverter.factory.BytesConverterFactory;
import org.example.Backend.DataToBytesConverter.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverter.Interface.TablePartTypeConverter;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.Record;
import org.example.Backend.Models.ColumnType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.Backend.Utils.ByteUtils.*;
import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.LENGTH_INDICATOR_BYTE_COUNT;
import static org.example.Backend.DataToBytesConverter.ByteConversionConstants.LENGTH_TYPE_INDICATOR_BYTE_COUNT;

public class BytesRecordConverters implements TablePartTypeConverter<Record> {

    @Override
    public Record toData(byte[] bytes) {
        validToData(bytes);

        ArrayList<Column> columns = new ArrayList<>();

        int indexByte = 0;
        while (indexByte < bytes.length) {
            int lenDataBytes = getLenData(bytes, indexByte);
            indexByte += LENGTH_INDICATOR_BYTE_COUNT;

            ColumnType columnType = getTypeFromBytes(bytes, indexByte, lenDataBytes);
            Object data = getObjectFromBytes(bytes, columnType, indexByte, lenDataBytes);
            indexByte += LENGTH_TYPE_INDICATOR_BYTE_COUNT;
            indexByte += lenDataBytes;

            columns.add(new Column(data, columnType));
        }
        return new Record(columns);
    }

    private void validToData(byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException("Null bytes");
        if (bytes.length == 0) throw new IllegalArgumentException("Empty array");
    }

    private int getLenData(byte[] bytes, int indexByte) {
        byte[] lenData = Arrays.copyOfRange(bytes, indexByte, indexByte + LENGTH_INDICATOR_BYTE_COUNT);
        return getIntFromBytesWithPadZero(lenData);
    }

    private ColumnType getTypeFromBytes(byte[] bytes, int indexByte, int lenDataBytes) {
        int indexType = lenDataBytes + indexByte;
        byte[] typeBytes = Arrays.copyOfRange(bytes, indexType, indexType + LENGTH_TYPE_INDICATOR_BYTE_COUNT);
        return ColumnType.values()[getIntFromBytesWithPadZero(typeBytes)];
    }

    private Object getObjectFromBytes(byte[] bytes, ColumnType columnType, int indexByte, int lenDataBytes) {
        ColumnTypeBytesConverter bytesConverters = BytesConverterFactory.getColumnTypeBytesConverters(columnType);
        return bytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + lenDataBytes));
    }

    @Override
    public ArrayList<Byte> toBytes(Record data) {
        validToBytes(data);

        ArrayList<Byte> listBytesColumn = new ArrayList<>();

        for (Column column : data.getColumns()) {
            listBytesColumn.addAll(getListByteFromColumn(column));
        }
        return listBytesColumn;
    }

    private void validToBytes(Record data) {
        if (data == null) throw new NullPointerException("Null data");
        if (data.getColumns().isEmpty()) throw new IllegalArgumentException("Empty array");
    }

    private ArrayList<Byte> getListByteFromColumn(Column column) {
        ColumnTypeBytesConverter bytesConverters = BytesConverterFactory.getColumnTypeBytesConverters(column.getType());

        byte[] dataBytes = bytesConverters.toBytes(column.getData());
        List<Byte> lenDataBytes = intToTwoByteList(dataBytes.length);
        List<Byte> typeDataBytes = intToTwoByteList(column.getType().ordinal());

        ArrayList<Byte> bytesList = new ArrayList<>(lenDataBytes);
        addArrayToList(bytesList, dataBytes);
        bytesList.addAll(typeDataBytes);
        return bytesList;
    }
}
