package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetaDataFragmentManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentRecordSaverImpl implements FragmentSaver {
    private final FileWriter fileWriter;
    private final MetaDataFragmentManager metaDataFragmentManager;
    private final int LENGTH_INDICATOR_BYTE_COUNT = 4;

    public FragmentRecordSaverImpl(FileWriter fileWriter, MetaDataFragmentManager metaDataFragmentManager) {
        this.fileWriter = fileWriter;
        this.metaDataFragmentManager = metaDataFragmentManager;
    }

    public int save(String tableName, List<Byte> bytesData) {
        int len = bytesData.size();
        FragmentMetaDataInfo fragmentMetaDataInfo = metaDataFragmentManager.getFragmentMetaDataInfo(tableName, len);
        writeFragment(tableName, bytesData, fragmentMetaDataInfo);

        int lengthDataFragment = fragmentMetaDataInfo.getLengthDataFragment();
        if (lengthDataFragment < len) {
            List<Byte> remainingData = bytesData.subList(lengthDataFragment, len);
            save(tableName, new ArrayList<>(remainingData));
        }
        return fragmentMetaDataInfo.getPositionFragment();
    }

    private void writeFragment(String tableName, List<Byte> bytesData, FragmentMetaDataInfo fragmentMetaDataInfo) {
        int pos = getStartingPositionFragment(fragmentMetaDataInfo.getPositionFragment());
        writeLengthFragment(tableName, fragmentMetaDataInfo, pos);
        if (pos != -1) pos += LENGTH_INDICATOR_BYTE_COUNT;

        writeDataFragment(tableName, bytesData, fragmentMetaDataInfo.getLengthDataFragment(), pos);
        if (pos != -1) pos += fragmentMetaDataInfo.getLengthDataFragment();

        writeLinkOnNextFragment(tableName, fragmentMetaDataInfo, pos);
    }

    private int getStartingPositionFragment(int positionFragment) {
        if (positionFragment == -1 || positionFragment == 0) return positionFragment;
        return positionFragment - 1;
    }

    private void writeLengthFragment(String tableName, FragmentMetaDataInfo fragmentMetaDataInfo, int pos) {
        fileWriter.write(tableName, intToBytes(fragmentMetaDataInfo.getLengthDataFragment()), pos);
    }

    private byte[] intToBytes(int value) {
        return new byte[] {
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    private void writeDataFragment(String tableName, List<Byte> bytesData, int lengthFragmentData, int position) {
        List<Byte> sublist = bytesData.subList(0, lengthFragmentData);
        ArrayList<Byte> bytesDataForSave = new ArrayList<>(sublist);
        fileWriter.write(tableName, bytesDataForSave, position);
    }


    private void writeLinkOnNextFragment(String tableName, FragmentMetaDataInfo fragmentMetaDataInfo, int pos) {
        fileWriter.write(tableName, intToBytes(fragmentMetaDataInfo.getLinkOnNextFragment()), pos);
    }
}
