package org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer;

public class BinarySerializerInt implements BinarySerializer<Integer> {
    @Override
    public String serialize(Integer data) {
        String binaryString = Integer.toBinaryString(data);
        return (data >= 0 ? "0" : "1") + binaryString;
    }

    @Override
    public Integer deserialize(String binaryString) {
        long value = Long.parseLong(binaryString.substring(1), 2);
        if (binaryString.charAt(0) == '1') {
            return (int) (value - (1L << binaryString.length()));
        } else {
            return (int) value;
        }
    }
}
