package org.example.Backend.DataToBytesConverter;

public class ByteConversionConstants {
    public static final int LENGTH_INDICATOR_BYTE_COUNT = 2;
    public static final int BOOLEAN_DATA_INDICATOR_BYTE_COUNT = 1;
    public static final int LENGTH_TYPE_INDICATOR_BYTE_COUNT = 2;

    public static final byte[] NULL_BYTES = new byte[]{-1,-1,-1, -1,-1,-1, -1,-1,-1};
    public static final byte TRUE_BYTE  = 1;
    public static final byte FALSE_BYTE  = 0;

}
