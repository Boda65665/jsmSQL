package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetaDataFragmentManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FragmentTableMetadataSaver implements FragmentSaver{
    private final FileWriter fileWriter;
    private final MetaDataFragmentManager metaDataFragmentManager;

    public FragmentTableMetadataSaver(FileWriter fileWriter, MetaDataFragmentManager metaDataFragmentManager) {
        this.fileWriter = fileWriter;
        this.metaDataFragmentManager = metaDataFragmentManager;
    }

    @Override
    public int save(String tableName, List<Byte> bytes) {
        FragmentMetaDataInfo fragmentMetaDataInfo = metaDataFragmentManager.getFragmentMetaDataInfo(tableName, bytes.size());

        if (possibleContinuePreviousFragment(fragmentMetaDataInfo.getPositionFragment())) addLengthBytesMetadataData(bytes);
        addLink(bytes, fragmentMetaDataInfo);

        fileWriter.write(tableName, bytes, fragmentMetaDataInfo.getPositionFragment());
        return 0;
    }

    private boolean possibleContinuePreviousFragment(int positionThisFragment) {
        return positionThisFragment != -1;
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

    private void addLink(List<Byte> bytes, FragmentMetaDataInfo fragmentMetaDataInfo) {
        bytes.addAll(intToBytes(fragmentMetaDataInfo.getLinkOnNextFragment()));
    }
}
