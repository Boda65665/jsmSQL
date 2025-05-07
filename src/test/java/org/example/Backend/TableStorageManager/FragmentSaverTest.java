package org.example.Backend.TableStorageManager;

import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.Models.*;
import org.example.Backend.TableStorageManager.FragmentMetaDataManager.FragmentMetaDataManager;
import org.example.Backend.TableStorageManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.FragmentOperationFactory.FragmentOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.TableOperationFactory.TableOperationFactoryImpl;
import org.example.Backend.TableStorageManager.TableWriter.TableWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class FragmentSaverTest {
    private final TableOperationFactoryImpl tableOperationFactory = new TableOperationFactoryImpl();
    private final FragmentOperationFactory fragmentOperationFactory = new FragmentOperationFactoryImpl();
    private final TableWriter tableWriter = tableOperationFactory.getTableWriter();
    private final FragmentSaver fragmentSaver = new FragmentSaver(tableWriter, fragmentOperationFactory);
    private FreeSpaceManager freeSpaceManager;
    private DbManager freeSpace;
    private final DbManagerFactory dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tableOperationFactory.getTablePathProvider());
    private final FragmentMetaDataManager fragmentMetaDataManager = fragmentOperationFactory.getFragmentMetaDataManager();
    private static final String NAME_TABLE = "test_table";
    private final String basePath = System.getProperty("user.dir") + File.separator + "test";

    private final int LENGTH_INDICATOR_BYTE_COUNT = 4;
    private final int LENGTH_LINK_BYTE_COUNT = 4;
    private final int LENGTH_METADATA_BYTE_COUNT = LENGTH_INDICATOR_BYTE_COUNT + LENGTH_LINK_BYTE_COUNT;
    private final int LENGTH_FRAGMENT_BYTES = 94;
    private final int POSITION_THIRD_FRAGMENT_BYTES = 42;

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
        DbManager dbManager = dbManagerFactory.getDbManager(basePath, NAME_TABLE);
        dbManager.clear();

        freeSpace = dbManager;
        freeSpaceManager = tableOperationFactory.getFreeSpaceManager(dbManager);
    }

    @Test
    void save() {
        initFreeSpace();
        TabularData tabularData = generateTestDataForSave();

        byte[] exceptedResult = getExceptedResult(tabularData);

        initFreeSpace();
        fragmentSaver.save(NAME_TABLE, tabularData, freeSpaceManager);
        byte[] result = testHelperTSM.read(NAME_TABLE, 0, -1);

        assertArrayEquals(exceptedResult, result);
    }

    private void initFreeSpace() {
        freeSpace.put(10, 10);
        freeSpace.put(15, 28);
    }

    private static TabularData generateTestDataForSave() {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column(1, ColumnType.INT));
        columns.add(new Column(1.1, ColumnType.DOUBLE));
        columns.add(new Column(111111111111L, ColumnType.LONG));
        columns.add(new Column("hello world", ColumnType.VARCHAR));
        columns.add(new Column(false, ColumnType.BOOLEAN));
        columns.add(new Column(new Date(123321), ColumnType.DATE));

        return new TabularData(columns);
    }

    private byte[] getExceptedResult(TabularData tabularData) {
        TablePartTypeConverter<TabularData> tabularDataConverter =  BytesConverterFactory.getTablePartTypeConverter(TablePartType.TABULAR_DATA);
        ArrayList<Byte> dataBytes = tabularDataConverter.toBytes(tabularData);

        int dataIndex = 0;
        byte[] excepted = new byte[LENGTH_FRAGMENT_BYTES];
        while (dataIndex < dataBytes.size()) {
            FragmentMetaDataInfo fragmentMetaDataInfo = fragmentMetaDataManager.getFragmentMetaDataInfo(freeSpaceManager, dataBytes.size() - dataIndex);
            int setPos = getStartingPositionFragment(fragmentMetaDataInfo);

            addLengthFragment(setPos, fragmentMetaDataInfo, excepted);
            setPos += LENGTH_INDICATOR_BYTE_COUNT;

            addFragmentData(setPos, dataBytes, dataIndex, fragmentMetaDataInfo, excepted);
            int lengthDataFragment = getLengthFragmentData(fragmentMetaDataInfo);
            setPos += lengthDataFragment;
            dataIndex += lengthDataFragment;

            addLink(setPos, excepted, fragmentMetaDataInfo);
        }
        return excepted;
    }

    private int getLengthFragmentData(FragmentMetaDataInfo fragmentMetaDataInfo) {
        return fragmentMetaDataInfo.getLengthFragment() - LENGTH_METADATA_BYTE_COUNT;
    }

    private int getStartingPositionFragment(FragmentMetaDataInfo fragmentMetaDataInfo) {
        return fragmentMetaDataInfo.getPositionFragment()==-1 ?POSITION_THIRD_FRAGMENT_BYTES : fragmentMetaDataInfo.getPositionFragment()-1;
    }

    private void addLengthFragment(int setPos, FragmentMetaDataInfo fragmentMetaDataInfo, byte[] excepted) {
        ArrayList<Byte> lengthFragment = intToBytes(fragmentMetaDataInfo.getLengthFragment());
        setListToArray(setPos, excepted, lengthFragment);
    }

    private void addFragmentData(int setPos, ArrayList<Byte> dataBytes, int dataIndex, FragmentMetaDataInfo fragmentMetaDataInfo, byte[] excepted) {
        int lengthDataFragment = getLengthFragmentData(fragmentMetaDataInfo);
        List<Byte> dataFragment = dataBytes.subList(dataIndex, dataIndex + lengthDataFragment);
        setListToArray(setPos, excepted, dataFragment);
    }


    private void addLink(int setPos, byte[] excepted, FragmentMetaDataInfo fragmentMetaDataInfo) {
        setListToArray(setPos, excepted, intToBytes(fragmentMetaDataInfo.getPositionNextFragment()));
    }

    private void setListToArray(int start, byte[] arr, List<Byte> list){
        for (int i = 0; i < list.size(); i++) {
            byte b = list.get(i);
            arr[i + start] = b;
        }
    }

    private ArrayList<Byte> intToBytes(int value) {
        ArrayList<Byte> byteList = new ArrayList<>();
        byteList.add((byte) (value >> 24));
        byteList.add((byte) (value >> 16));
        byteList.add((byte) (value >> 8));
        byteList.add((byte) value);
        return byteList;
    }
}