package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TypeData;
import org.junit.jupiter.api.Test;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BytesMetaDataConvertersTest {
    private final BytesMetaDataConverters metaDatConverters = new BytesMetaDataConverters();
    private final BytesConverterFactory bytesConverterFactory = new BytesConverterFactory();
    private final BytesIntegerConverters integerConverters = (BytesIntegerConverters) bytesConverterFactory.getBytesConverters(TypeData.INT);

    @Test
    void countColumnToBytes() {
        TableMetaData tableMetaData = generateTestDataForToBytes();
        byte[] metaDataByte = metaDatConverters.toBytes(tableMetaData);
        int countColumn = byteArrayToInt(Arrays.copyOf(metaDataByte, 2));

        int countColumnExcepted = tableMetaData.getColumnStructList().size();
        assertEquals(countColumnExcepted, countColumn);
    }

    private TableMetaData generateTestDataForToBytes() {
        TableMetaData tableMetaData = new TableMetaData();
        List<ColumnStruct> columnStructs = new ArrayList<>();

        columnStructs.add(new ColumnStruct("first", TypeData.VARCHAR));
        columnStructs.add(new ColumnStruct("second", TypeData.DOUBLE));
        tableMetaData.setColumnStructList(columnStructs);
        return tableMetaData;
    }

    private int byteArrayToInt(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.position(4 - byteArray.length);
        buffer.put(byteArray);
        buffer.flip();
        return buffer.getInt();
    }

    @Test
    void columnStructToBytes() {
        TableMetaData tableMetaData = generateTestDataForToBytes();
        byte[] metaDataByte = metaDatConverters.toBytes(tableMetaData);

        testConvertColumn(metaDataByte);
    }

    private void testConvertColumn(byte[] metaDataByte) {
        int nameColumnLength = byteArrayToInt(Arrays.copyOfRange(metaDataByte, 3,4));
        int nameColumnLengthExcepted = "first".getBytes(StandardCharsets.UTF_8).length;
        assertEquals(nameColumnLengthExcepted, nameColumnLength);

        byte[] excepted = Arrays.copyOfRange(metaDataByte, 4, 4 + nameColumnLengthExcepted);
        byte[] output = "first".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(excepted, output);

        int typeId = integerConverters.toData(Arrays.copyOfRange(metaDataByte, 4 + nameColumnLengthExcepted, 5 + nameColumnLengthExcepted));
        assertEquals(TypeData.VARCHAR.ordinal(), typeId);
    }

    @Test
    void toData() {
        TableMetaData excepted = generateTestDataForToBytes();
        byte[] metaDataByte = metaDatConverters.toBytes(excepted);
        TableMetaData tableMetaData = metaDatConverters.toData(metaDataByte);

        assertEquals(excepted, tableMetaData);
    }
}

