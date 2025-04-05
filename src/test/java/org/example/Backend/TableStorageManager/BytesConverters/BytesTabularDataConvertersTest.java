package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.Column;
import org.example.Backend.Models.TabularData;
import org.example.Backend.Models.TypeData;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BytesTabularDataConvertersTest {
    private final BytesTabularDataConverters converters = new BytesTabularDataConverters();
    private final int LENGTH_INDICATOR_BYTE_COUNT = 2;
    private final int LENGTH_TYPE_INDICATOR_BYTE_COUNT = 2;

    @Test
    void toBytes() {
        int currentIndex = 0;

        TabularData tabularData = generateTestTabularData();
        byte[] bytes = converters.toBytes(tabularData);

        currentIndex = testConvertFirstColumn(bytes, currentIndex);

        testConvertSecondColumn(bytes, currentIndex);
    }

    private TabularData generateTestTabularData() {
        TabularData tabularData = new TabularData();
        Column[] columns = new Column[2];
        columns[0] = new Column("hello", TypeData.VARCHAR);
        columns[1] = new Column(false, TypeData.BOOLEAN);
        tabularData.setColumns(columns);
        return tabularData;
    }

    private int testConvertFirstColumn(byte[] bytes, int currentIndex) {
        int lenDataFirstColumn = getIntFromBytes(Arrays.copyOf(bytes, LENGTH_INDICATOR_BYTE_COUNT));
        currentIndex += LENGTH_INDICATOR_BYTE_COUNT;

        assertEquals("hello".getBytes().length, lenDataFirstColumn);
        assertArrayEquals("hello".getBytes(), Arrays.copyOfRange(bytes, currentIndex, currentIndex + lenDataFirstColumn));
        currentIndex += lenDataFirstColumn;

        TypeData typeData = getTypeFromBytes(Arrays.copyOfRange(bytes, currentIndex, currentIndex + LENGTH_TYPE_INDICATOR_BYTE_COUNT));
        currentIndex += LENGTH_TYPE_INDICATOR_BYTE_COUNT;

        assertEquals(TypeData.VARCHAR, typeData);
        return currentIndex;
    }

    private int getIntFromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.position(4 - bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    private TypeData getTypeFromBytes(byte[] bytes) {
        int idType = getIntFromBytes(bytes);
        return TypeData.values()[idType];
    }

    private void testConvertSecondColumn(byte[] bytes, int currentIndex) {
        int lenDataSecondColumn = getIntFromBytes(Arrays.copyOfRange(bytes, currentIndex, currentIndex + LENGTH_INDICATOR_BYTE_COUNT));
        currentIndex += LENGTH_INDICATOR_BYTE_COUNT;

        int booleanWeight = 1;
        assertEquals(booleanWeight, lenDataSecondColumn);


        assertArrayEquals(new byte[]{0}, Arrays.copyOfRange(bytes, currentIndex, currentIndex + booleanWeight));
        currentIndex += booleanWeight;

        TypeData typeData = getTypeFromBytes(Arrays.copyOfRange(bytes, currentIndex, currentIndex + LENGTH_TYPE_INDICATOR_BYTE_COUNT));
        assertEquals(TypeData.BOOLEAN, typeData);
    }

    @Test
    void toData(){
        byte[] bytes = converters.toBytes(generateTestTabularData());
        TabularData tabularData = converters.toData(bytes);

        assertEquals(generateTestTabularData(), tabularData);
    }
}