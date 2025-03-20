package org.example.Backend.TableStorageManager.BinaryConverter.BinaryConverterFactory.BinarySerializer;

public class BinarySerializerString implements BinarySerializer<String>{
    @Override
    public String serialize(String data) {
        StringBuilder binary = new StringBuilder();
        for (char c : data.toCharArray()) {
            String bin = Integer.toBinaryString(c);
            while (bin.length() < 8) {
                bin = "0" + bin;
            }
            binary.append(bin).append(" ");
        }
        return binary.toString().trim();
    }

    @Override
    public String deserialize(String binaryString) {
        StringBuilder output = new StringBuilder();
        String[] binaryArray = binaryString.split(" ");
        for (String b : binaryArray) {
            int charCode = Integer.parseInt(b, 2);
            output.append((char) charCode);
        }
        return output.toString();
    }
}
