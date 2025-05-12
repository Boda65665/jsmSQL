package org.example.Backend.TableStorageManager.RecordManager.RecordSaver;

import org.example.Backend.DataToBytesConverters.Interface.TablePartTypeConverter;
import org.example.Backend.DataToBytesConverters.factory.BytesConverterFactory;
import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactory;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.Models.*;
import org.example.Backend.Models.Record;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetaDataFragmentManager;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetadataFragmentRecordManagerImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactory;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactoryImpl;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordSaverImplTest {
    private final FileOperationFactoryImpl tableOperationFactory = new FileOperationFactoryImpl();
    private static final String NAME_TABLE = "test_table";
    private final String prefixName = "freespace_";
    private final String basePath = System.getProperty("user.dir") + File.separator + "test";
    private final DbManagerFactory dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private final FreeSpaceManagerFactory freeSpaceManagerFactory = new FreeSpaceManagerFactoryImpl(basePath, dbManagerFactory);
    private final FragmentOperationFactory fragmentOperationFactory = new FragmentOperationFactoryImpl(freeSpaceManagerFactory);
    private final FileWriter fileWriter = tableOperationFactory.getTableWriter();
    private RecordSaverImpl recordSaver;
    private DbManager freeSpace;
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(tableOperationFactory.getTablePathProvider());
    private MetaDataFragmentManager metaDataFragmentManager;


    private final int LENGTH_INDICATOR_BYTE_COUNT = 4;
    private final int LENGTH_LINK_BYTE_COUNT = 4;
    private final int LENGTH_METADATA_BYTE_COUNT = LENGTH_INDICATOR_BYTE_COUNT + LENGTH_LINK_BYTE_COUNT;
    private final int LENGTH_FRAGMENT_BYTES = 94;
    private final int POSITION_THIRD_FRAGMENT_BYTES = 42;

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
        DbManager dbManager = dbManagerFactory.getDbManager(basePath, prefixName+NAME_TABLE);
        dbManager.clear();

        freeSpace = dbManager;
        recordSaver = new RecordSaverImpl(fragmentOperationFactory.getFragmentRecordSaver(fileWriter));
        metaDataFragmentManager = new MetadataFragmentRecordManagerImpl(freeSpaceManagerFactory);
    }

    @Test
    void save() {
        initFreeSpace();
        Record record = generateTestDataForSave();

        byte[] exceptedResult = getExceptedResult(record);

        initFreeSpace();
        int startPos = recordSaver.save(NAME_TABLE, record);
        assertEquals(28, startPos);

        byte[] result = testHelperTSM.read(NAME_TABLE, 0, -1);
        assertArrayEquals(exceptedResult, result);
    }

    private void initFreeSpace() {
        freeSpace.put(10, 10);
        freeSpace.put(15, 28);
    }

    private static Record generateTestDataForSave() {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column(1, ColumnType.INT));
        columns.add(new Column(1.1, ColumnType.DOUBLE));
        columns.add(new Column(111111111111L, ColumnType.LONG));
        columns.add(new Column("hello world", ColumnType.VARCHAR));
        columns.add(new Column(false, ColumnType.BOOLEAN));
        columns.add(new Column(new Date(123321), ColumnType.DATE));

        return new Record(columns);
    }

    private byte[] getExceptedResult(Record record) {
        TablePartTypeConverter<Record> tabularDataConverter =  BytesConverterFactory.getTablePartTypeConverter(TablePartType.RECORD);
        ArrayList<Byte> dataBytes = tabularDataConverter.toBytes(record);

        int dataIndex = 0;
        byte[] excepted = new byte[LENGTH_FRAGMENT_BYTES];
        while (dataIndex < dataBytes.size()) {
            FragmentMetaDataInfo fragmentMetaDataInfo = metaDataFragmentManager.getFragmentMetaDataInfo(NAME_TABLE, dataBytes.size() - dataIndex);
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
        setListToArray(setPos, excepted, intToBytes(fragmentMetaDataInfo.getLinkOnNextFragment()));
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