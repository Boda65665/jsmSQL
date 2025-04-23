package org.example.Backend.DataToBytesConverters.TableParts;

import org.example.Backend.DataToBytesConverters.BytesConverterFactory;
import org.example.Backend.DataToBytesConverters.Interface.ArrayByteConverter;
import org.example.Backend.DataToBytesConverters.TableParts.TH.TestHelperConverterTableParts;
import org.example.Backend.Models.ColumnStruct;
import org.example.Backend.Models.TableMetaData;
import org.example.Backend.Models.TypeData;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BytesMetaDataConvertersTest {
    private final BytesMetaDataConverters metaDatConverters = new BytesMetaDataConverters();
    private final ArrayByteConverter<String> stingBytesConverter = (ArrayByteConverter<String>) BytesConverterFactory.getBytesConverters(TypeData.VARCHAR);

    private final TestHelperConverterTableParts testHelperConverterTableParts = new TestHelperConverterTableParts();

    @Test
    void toBytes() {
        TableMetaData tmd = generateTestDataForToConvertToBytes();

        ArrayList<Byte> excepted = toBytes(tmd.getColumnStructList());
        ArrayList<Byte> result = metaDatConverters.toBytes(tmd);
        assertEquals(excepted, result);
    }

    private ArrayList<Byte> toBytes(List<ColumnStruct> columnsStruct) {
        int countColumn = columnsStruct.size();
        ArrayList<Byte> result = new ArrayList<>((testHelperConverterTableParts.getBytesFromInt(countColumn)));
        for (ColumnStruct columnStruct : columnsStruct) {
            byte[] bytesName = stingBytesConverter.toBytes(columnStruct.getColumnName());

            addBytesLengthColumnName(result, bytesName.length);
            addBytesColumnName(result, bytesName);
            addBytesTypeColumn(result, columnStruct.getType());
        }
        return result;
    }

    private void addBytesLengthColumnName(ArrayList<Byte> result, int length) {
        ArrayList<Byte> bytesLength = testHelperConverterTableParts.getBytesFromInt(length);
        result.addAll(bytesLength);
    }

    private void addBytesColumnName(ArrayList<Byte> result, byte[] bytesName) {
        testHelperConverterTableParts.addArrayToList(result, bytesName);
    }

    private void addBytesTypeColumn(ArrayList<Byte> result, TypeData typeData) {
        result.addAll(testHelperConverterTableParts.getBytesFromInt(typeData.ordinal()));
    }

    private TableMetaData generateTestDataForToConvertToBytes() {
        ArrayList<ColumnStruct> columnStructs = new ArrayList<>();

        columnStructs.add(new ColumnStruct("fourth", TypeData.INT));
        columnStructs.add(new ColumnStruct("fifth", TypeData.LONG));
        columnStructs.add(new ColumnStruct("second", TypeData.DOUBLE));
        columnStructs.add(new ColumnStruct("sixth", TypeData.BOOLEAN));
        columnStructs.add(new ColumnStruct("first", TypeData.VARCHAR));
        columnStructs.add(new ColumnStruct("third", TypeData.DATE));
        return new TableMetaData(columnStructs, null);
    }

    @Test
    void toData() {
        TableMetaData tmd = generateTestDataForToConvertToBytes();
        ArrayList<Byte> testData = toBytes(tmd.getColumnStructList());

        assertEquals(tmd, metaDatConverters.toData(testHelperConverterTableParts.toArray(testData)));
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

