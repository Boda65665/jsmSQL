package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetaDataFragmentManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FragmentTableMetadataSaver implements FragmentSaver{
    private final FileWriter fileWriter;
    private final int LENGTH_LINK_BYTE_COUNT = 4;
    private final MetaDataFragmentManager metaDataFragmentManager;

    public FragmentTableMetadataSaver(FileWriter fileWriter, MetaDataFragmentManager metaDataFragmentManager) {
        this.fileWriter = fileWriter;
        this.metaDataFragmentManager = metaDataFragmentManager;
    }

    @Override
    public int save(String tableName, List<Byte> metadataBytes) {
        addLengthBytesMetadataData(metadataBytes);
        allocateBytesForLink(metadataBytes);

        int positionLastMetadata = getPositionLastFragmentMetadata();
        fileWriter.write(tableName, metadataBytes, positionLastMetadata);
        return 0;
    }

    private int getPositionLastFragmentMetadata() {
        return -1;
    }

    private void addLengthBytesMetadataData(List<Byte> bytesData) {
        bytesData.addAll(0, intToBytes(bytesData.size()));;
    }

    private List<Byte> intToBytes(int value) {
        List<Byte> byteList = new ArrayList<>();
        byteList.add((byte) (value >> 24));
        byteList.add((byte) (value >> 16));
        byteList.add((byte) (value >> 8));
        byteList.add((byte) value);
        return byteList;
    }

    private void allocateBytesForLink(List<Byte> bytesData) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(LENGTH_LINK_BYTE_COUNT);
        addArrayToList(bytesData, byteBuffer.array());
    }

    private void addArrayToList(List<Byte> bytesData, byte[] byteArray) {
        for (byte b : byteArray) {
            bytesData.add(b);
        }
    }
}
