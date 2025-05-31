package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.Models.MetaDataFragment;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.Record.MetaDataFragmentRecordManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentTableMetadataSaver implements FragmentSaver{
    private final FileWriter fileWriter;
    private final MetaDataFragmentRecordManager metaDataFragmentRecordManager;

    public FragmentTableMetadataSaver(FileWriter fileWriter, MetaDataFragmentRecordManager metaDataFragmentRecordManager) {
        this.fileWriter = fileWriter;
        this.metaDataFragmentRecordManager = metaDataFragmentRecordManager;
    }

    @Override
    public int save(String tableName, List<Byte> bytes) {
        MetaDataFragment metaDataFragment = metaDataFragmentRecordManager.getMetaDataNewFragment(tableName, bytes.size());

        if (possibleContinuePreviousFragment(metaDataFragment.getPositionFragment())) addLengthBytesMetadataData(bytes);
        addLink(bytes, metaDataFragment);

        fileWriter.write(tableName, bytes, metaDataFragment.getPositionFragment());
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

    private void addLink(List<Byte> bytes, MetaDataFragment metaDataFragment) {
        bytes.addAll(intToBytes(metaDataFragment.getLinkOnNextFragment()));
    }
}
