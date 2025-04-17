package org.example.Backend.TableStorageManager.BytesConverters;

import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TypeData;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BytesMetaDataConvertersTest {
    private final BytesMetaDataConverters metaDatConverters = new BytesMetaDataConverters();

    @Test
    void testConvert() {
        TableMetaData excepted = generateTestDataForToConvertToBytes();
        byte[] metaDataByte = metaDatConverters.toBytes(excepted);
        TableMetaData tableMetaData = metaDatConverters.toData(metaDataByte);

        assertEquals(excepted, tableMetaData);
    }

    private TableMetaData generateTestDataForToConvertToBytes() {
        List<ColumnStruct> columnStructs = new ArrayList<>();

        columnStructs.add(new ColumnStruct("first", TypeData.VARCHAR));
        columnStructs.add(new ColumnStruct("second", TypeData.DOUBLE));
        return new TableMetaData(columnStructs);
    }

    @Test
    void nullOrEmptyArrayToData() {
        assertThrows(IllegalArgumentException.class, () -> metaDatConverters.toData(null));
        assertThrows(IllegalArgumentException.class, () -> metaDatConverters.toData(new byte[]{}));
    }

    @Test
    void nullToBytes() {
        assertThrows(NullPointerException.class, () -> metaDatConverters.toBytes(null));
    }
}

