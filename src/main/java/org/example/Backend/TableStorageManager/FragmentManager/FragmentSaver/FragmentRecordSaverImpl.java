package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.Models.MetaDataFragment;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.Record.MetaDataFragmentRecordManager;

import java.util.ArrayList;
import java.util.List;

import static org.example.Backend.TableStorageManager.FragmentManager.FragmentStructureConstants.LENGTH_INDICATOR_BYTE_COUNT;

public class FragmentRecordSaverImpl implements FragmentSaver {
    private final FileWriter fileWriter;
    private final MetaDataFragmentRecordManager metaDataFragmentRecordManager;

    public FragmentRecordSaverImpl(FileWriter fileWriter, MetaDataFragmentRecordManager metaDataFragmentRecordManager) {
        this.fileWriter = fileWriter;
        this.metaDataFragmentRecordManager = metaDataFragmentRecordManager;
    }

    public int save(String tableName, List<Byte> bytesData) {
        int len = bytesData.size();
        MetaDataFragment metaDataFragment = metaDataFragmentRecordManager.getMetaDataNewFragment(tableName, len);
        writeFragment(tableName, bytesData, metaDataFragment);

        int lengthDataFragment = metaDataFragment.getLengthDataFragment();
        if (lengthDataFragment < len) {
            List<Byte> remainingData = bytesData.subList(lengthDataFragment, len);
            save(tableName, new ArrayList<>(remainingData));
        }
        return metaDataFragment.getPositionFragment();
    }

    private void writeFragment(String tableName, List<Byte> bytesData, MetaDataFragment metaDataFragment) {
        int pos = getStartingPositionFragment(metaDataFragment.getPositionFragment());
        writeLengthFragment(tableName, metaDataFragment, pos);
        if (pos != -1) pos += LENGTH_INDICATOR_BYTE_COUNT;

        writeDataFragment(tableName, bytesData, metaDataFragment.getLengthDataFragment(), pos);
        if (pos != -1) pos += metaDataFragment.getLengthDataFragment();

        writeLinkOnNextFragment(tableName, metaDataFragment, pos);
    }

    private int getStartingPositionFragment(int positionFragment) {
        if (positionFragment == -1 || positionFragment == 0) return positionFragment;
        return positionFragment - 1;
    }

    private void writeLengthFragment(String tableName, MetaDataFragment metaDataFragment, int pos) {
        fileWriter.write(tableName, intToBytes(metaDataFragment.getLengthDataFragment()), pos);
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


    private void writeLinkOnNextFragment(String tableName, MetaDataFragment metaDataFragment, int pos) {
        fileWriter.write(tableName, intToBytes(metaDataFragment.getLinkOnNextFragment()), pos);
    }
}
