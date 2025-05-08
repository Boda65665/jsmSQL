package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.Models.TablePartType;
import org.example.Backend.Models.TabularData;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.FragmentMetaDataManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.TableManager.TableWriter.TableWriter;
import java.util.ArrayList;
import java.util.List;

public class FragmentSaverImpl implements FragmentSaver {
    private final TableWriter tableWriter;
    private final FragmentMetaDataManager fragmentMetaDataManager;
    private final int LENGTH_INDICATOR_BYTE_COUNT = 4;
    private final int LENGTH_LINK_BYTE_COUNT = 4;
    private final int LENGTH_METADATA_BYTE_COUNT = LENGTH_INDICATOR_BYTE_COUNT + LENGTH_LINK_BYTE_COUNT;

    public FragmentSaverImpl(TableWriter tableWriter, FragmentMetaDataManager fragmentMetaDataManager) {
        this.tableWriter = tableWriter;
        this.fragmentMetaDataManager = fragmentMetaDataManager;
    }

    public int save(String tableName, TabularData data, FreeSpaceManager freeSpaceManager) {
        TablePartTypeConverter<TabularData> tabularDataConverter =  BytesConverterFactory.getTablePartTypeConverter(TablePartType.TABULAR_DATA);
        ArrayList<Byte> bytesData = tabularDataConverter.toBytes(data);
        return save(tableName, bytesData, freeSpaceManager);
    }

    private int save(String tableName, ArrayList<Byte> bytesData, FreeSpaceManager freeSpaceManager) {
        int len = bytesData.size();

        FragmentMetaDataInfo fragmentMetaDataInfo = fragmentMetaDataManager.getFragmentMetaDataInfo(freeSpaceManager, len);
        writeFragment(tableName, bytesData, fragmentMetaDataInfo);

        int lengthDataFragment = getLengthFragmentData(fragmentMetaDataInfo.getLengthFragment());
        if (lengthDataFragment < len) {
            List<Byte> sublist = bytesData.subList(lengthDataFragment, len);
            save(tableName, new ArrayList<>(sublist), freeSpaceManager);
        }
        return fragmentMetaDataInfo.getPositionFragment();
    }

    private void writeFragment(String tableName, ArrayList<Byte> bytesData, FragmentMetaDataInfo fragmentMetaDataInfo) {
        int pos = getStartingPositionFragment(fragmentMetaDataInfo.getPositionFragment());

        writeLengthFragment(tableName, fragmentMetaDataInfo, pos);
        if (pos != -1) pos += LENGTH_INDICATOR_BYTE_COUNT;

        writeDataFragment(tableName, bytesData, fragmentMetaDataInfo.getLengthFragment(), pos);
        if (pos != -1) pos += getLengthFragmentData(fragmentMetaDataInfo.getLengthFragment());

        writeLinkOnNextFragment(tableName, fragmentMetaDataInfo, pos);
    }

    private int getStartingPositionFragment(int positionFragment) {
        if (positionFragment == -1 || positionFragment == 0) return positionFragment;
        return positionFragment - 1;
    }

    private void writeLengthFragment(String tableName, FragmentMetaDataInfo fragmentMetaDataInfo, int pos) {
        tableWriter.write(tableName, intToBytes(fragmentMetaDataInfo.getLengthFragment()), pos);
    }

    private byte[] intToBytes(int value) {
        return new byte[] {
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    private void writeDataFragment(String tableName, ArrayList<Byte> bytesData, int lengthFragment, int position) {
        int lengthFragmentData = getLengthFragmentData(lengthFragment);
        List<Byte> sublist = bytesData.subList(0, lengthFragmentData);
        ArrayList<Byte> bytesDataForSave = new ArrayList<>(sublist);
        tableWriter.write(tableName, bytesDataForSave, position);
    }

    private int getLengthFragmentData(int lengthFragment) {
        return lengthFragment - LENGTH_METADATA_BYTE_COUNT;
    }

    private void writeLinkOnNextFragment(String tableName, FragmentMetaDataInfo fragmentMetaDataInfo, int pos) {
        tableWriter.write(tableName, intToBytes(fragmentMetaDataInfo.getPositionNextFragment()), pos);
    }
}
