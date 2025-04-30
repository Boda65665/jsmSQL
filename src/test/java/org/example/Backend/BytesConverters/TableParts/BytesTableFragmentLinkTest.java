package org.example.Backend.BytesConverters.TableParts;

import org.example.Backend.BytesConverters.TableParts.TH.TestHelperConverterTableParts;
import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.Models.ColumnType;
import org.example.Backend.Models.TableFragmentLink;
import org.example.Backend.Models.TablePartType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BytesTableFragmentLinkTest {
    private final ColumnTypeBytesConverter<Integer> intConverter = (ColumnTypeBytesConverter<Integer>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.INT);
    private final ColumnTypeBytesConverter<Long> longConverter = (ColumnTypeBytesConverter<Long>) BytesConverterFactory.getColumnTypeBytesConverters(ColumnType.LONG);
    private final TestHelperConverterTableParts testHelperConverterTableParts = new TestHelperConverterTableParts();
    private final TablePartTypeConverter<TableFragmentLink> linkTablePartTypeConverter = BytesConverterFactory.getTablePartTypeConverter(TablePartType.TABLE_FRAGMENT_LINK);

    @Test
    void toBytes() {
        TableFragmentLink tableFragmentLink = generateTestData();
        ArrayList<Byte> exceptedLinkBytes = linkToByte(tableFragmentLink);
        ArrayList<Byte> result = linkTablePartTypeConverter.toBytes(tableFragmentLink);

        assertEquals(exceptedLinkBytes, result);
    }

    private ArrayList<Byte> linkToByte(TableFragmentLink tableFragmentLink) {
        ArrayList<Byte> exceptedLinkBytes = new ArrayList<>();
        testHelperConverterTableParts.addArrayToList(exceptedLinkBytes, intConverter.toBytes(tableFragmentLink.getByteLength()));
        testHelperConverterTableParts.addArrayToList(exceptedLinkBytes, longConverter.toBytes(tableFragmentLink.getOffset()));

        return exceptedLinkBytes;
    }

    private TableFragmentLink generateTestData() {
        return new TableFragmentLink(2, 300);
    }

    @Test
    void toData() {
        TableFragmentLink excepted = generateTestData();
        ArrayList<Byte> testData = linkToByte(excepted);
        TableFragmentLink result = linkTablePartTypeConverter.toData(testHelperConverterTableParts.toArray(testData));

        assertEquals(excepted, result);
    }
}