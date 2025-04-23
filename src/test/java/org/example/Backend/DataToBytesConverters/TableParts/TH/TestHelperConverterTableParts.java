package org.example.Backend.DataToBytesConverters.TableParts.TH;

import java.util.ArrayList;
import java.util.List;

public class TestHelperConverterTableParts {
    public byte[] toArray(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    public ArrayList<Byte> getBytesFromInt(int number) {
        ArrayList<Byte> byteList = new ArrayList<>();

        byteList.add((byte) ((number >> 8) & 0xFF));
        byteList.add((byte) (number & 0xFF));
        return byteList;
    }

    public void addArrayToList(List<Byte> bytes, byte[] columnNameBytes) {
        for (byte b : columnNameBytes) {
            bytes.add(b);
        }
    }
}
