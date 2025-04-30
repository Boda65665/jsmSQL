package org.example.Backend.DataToBytesConverters.TableParts;

import org.example.Backend.DataToBytesConverters.Interface.ColumnTypeBytesConverter;
import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.Models.TableFragmentLink;
import java.util.ArrayList;
import java.util.Arrays;

public class BytesTableFragmentLink implements TablePartTypeConverter<TableFragmentLink> {
    private final ColumnTypeBytesConverter<Integer> intConverter;
    private final ColumnTypeBytesConverter<Long> longConverter;

    public BytesTableFragmentLink(ColumnTypeBytesConverter<Integer> intConverter, ColumnTypeBytesConverter<Long> longConverter) {
        this.intConverter = intConverter;
        this.longConverter = longConverter;
    }

    @Override
    public ArrayList<Byte> toBytes(TableFragmentLink data) {
        ArrayList<Byte> linkBytes = new ArrayList<>();
        addArrayToList(linkBytes, intConverter.toBytes(data.getByteLength()));
        addArrayToList(linkBytes, longConverter.toBytes(data.offset));
        return linkBytes;
    }

    private void addArrayToList(ArrayList<Byte> linkBytes, byte[] array) {
        for (byte b : array) {
            linkBytes.add(b);
        }
    }

    @Override
    public TableFragmentLink toData(byte[] bytes) {
        int bytesLength = bytes.length - 1;
        long offset = longConverter.toData(Arrays.copyOfRange(bytes, 1, bytes.length));
        return new TableFragmentLink(bytesLength, offset);
    }
}
