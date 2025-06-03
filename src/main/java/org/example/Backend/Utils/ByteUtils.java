package org.example.Backend.Utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class ByteUtils {

    private ByteUtils() {
        throw new AssertionError("ByteUtils не предназначен для инстанцирования");
    }

    public static byte[] intToByteArray(Integer number) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(number).array();
    }

    public static byte[] longToByteArray(Long number) {
        return ByteBuffer.allocate(Long.BYTES).putLong(number).array();
    }

    public static List<Byte> intToTwoByteList(int number) {
        List<Byte> byteList = new ArrayList<>();

        byteList.add((byte) (shiftByOneByte(number) & extractLowByte(number)));
        byteList.add(extractLowByte(number));
        return byteList;
    }

    public static void addArrayToList(List<Byte> bytes, byte[] columnNameBytes) {
        for (byte b : columnNameBytes) {
            bytes.add(b);
        }
    }

    public static long getLongFromBytes(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static int getIntFromBytesWithPadZero(byte[] bytes) {
        return getIntFromBytes(padWithZero(bytes, Integer.BYTES));
    }

    public static int getIntFromBytes(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static byte[] padWithZero(byte[] bytes, int newSize) {
        byte[] paddedArray = new byte[newSize];
        System.arraycopy(bytes, 0, paddedArray, newSize - bytes.length, bytes.length);
        return paddedArray;
    }

    public static byte[] byteListToArray(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    public static byte[] intToCompactArray(Integer number) {
        int length = getLengthCompactByteArray(number);
        byte[] byteArray = new byte[length];

        for (int i = length - 1; i >= 0; i--) {
            byteArray[i] = extractLowByte(number);
            number = shiftByOneByte(number);
        }
        return byteArray;
    }

    private static int getLengthCompactByteArray(Integer data) {
        if (data == 0) return 1;
        return (int)(Math.log(data) / Math.log(256)) + 1;
    }

    private static byte extractLowByte(Integer number) {
        return (byte) (number & 0xFF);
    }

    private static Integer shiftByOneByte(Integer number) {
        number >>= 8;
        return number;
    }

    public static byte[] longToCompactArray(Long number) {
        int length = getLengthCompactByteArray(number);
        byte[] byteArray = new byte[length];

        for (int i = length - 1; i >= 0; i--) {
            byteArray[i] = extractLowByte(number);
            number = shiftByOneByte(number);
        }
        return byteArray;
    }

    private static int getLengthCompactByteArray(Long data) {
        if (data == 0) return 1;
        return (int)(Math.log(data) / Math.log(256)) + 1;
    }

    private static byte extractLowByte(Long number) {
        return (byte) (number & 0xFF);
    }

    private static Long shiftByOneByte(Long number) {
        number >>= 8;
        return number;
    }
}
