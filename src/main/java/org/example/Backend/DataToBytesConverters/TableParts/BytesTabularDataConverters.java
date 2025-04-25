package org.example.Backend.DataToBytesConverters.TableParts;

import org.example.Backend.DataToBytesConverters.factory.ColumnTypeBytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.Models.Column;
import org.example.Backend.Models.TabularData;
import org.example.Backend.Models.ColumnType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BytesTabularDataConverters implements TablePartTypeConverter<TabularData> {
    private final int LENGTH_INDICATOR_BYTE_COUNT = 2;
    private final int LENGTH_TYPE_INDICATOR_BYTE_COUNT = 2;

    @Override
    public TabularData toData(byte[] bytes) {
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
        return new TabularData(columns);
    }

    private void validToData(byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException("Null bytes");
        if (bytes.length == 0) throw new IllegalArgumentException("Empty array");
    }

    private int getLenData(byte[] bytes, int indexByte) {
        byte[] lenData = Arrays.copyOfRange(bytes, indexByte, indexByte + LENGTH_INDICATOR_BYTE_COUNT);
        return getIntFromBytes(lenData);
    }

    private int getIntFromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.position(4 - bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    private ColumnType getTypeFromBytes(byte[] bytes, int indexByte, int lenDataBytes) {
        int indexType = lenDataBytes + indexByte;
        byte[] typeBytes = Arrays.copyOfRange(bytes, indexType, indexType + LENGTH_TYPE_INDICATOR_BYTE_COUNT);
        return ColumnType.values()[getIntFromBytes(typeBytes)];
    }

    private Object getObjectFromBytes(byte[] bytes, ColumnType columnType, int indexByte, int lenDataBytes) {
        ColumnTypeBytesConverter bytesConverters = ColumnTypeBytesConverterFactory.getBytesConverters(columnType);
        return bytesConverters.toData(Arrays.copyOfRange(bytes, indexByte, indexByte + lenDataBytes));
    }

    @Override
    public ArrayList<Byte> toBytes(TabularData data) {
        validToBytes(data);

        ArrayList<Byte> listBytesColumn = new ArrayList<>();

        for (Column column : data.getColumns()) {
            listBytesColumn.addAll(getListByteFromColumn(column));
        }
        return listBytesColumn;
    }

    private void validToBytes(TabularData data) {
        if (data == null) throw new NullPointerException("Null data");
        if (data.getColumns().isEmpty()) throw new IllegalArgumentException("Empty array");
    }

    private ArrayList<Byte> getListByteFromColumn(Column column) {
        ColumnTypeBytesConverter bytesConverters = ColumnTypeBytesConverterFactory.getBytesConverters(column.getColumnType());

        byte[] dataBytes = bytesConverters.toBytes(column.getData());
        List<Byte> lenDataBytes = getBytesFromInt(dataBytes.length);
        List<Byte> typeDataBytes = getBytesFromInt(column.getColumnType().ordinal());

        ArrayList<Byte> bytesList = new ArrayList<>(lenDataBytes);
        addArrayToList(bytesList, dataBytes);
        bytesList.addAll(typeDataBytes);
        return bytesList;
    }

    private List<Byte> getBytesFromInt(int number) {
        List<Byte> byteList = new ArrayList<>();

        byteList.add((byte) ((number >> 8) & 0xFF));
        byteList.add((byte) (number & 0xFF));
        return byteList;
    }

    private void addArrayToList(List<Byte> bytes, byte[] columnNameBytes) {
        for (byte b : columnNameBytes) {
            bytes.add(b);
        }
    }
}
