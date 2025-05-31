package org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver;

import org.example.Backend.Models.MetaDataFragment;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProviderImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriterImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.Record.MetaDataFragmentRecordManager;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.Backend.TableStorageManager.FragmentManager.FragmentStructureConstants.LENGTH_INDICATOR_BYTE_COUNT;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FragmentRecordSaverImplTest {
    private final FilePathProvider filePathProvider = new FilePathProviderImpl();
    private final FileWriter fileWriter = new FileWriterImpl(filePathProvider);
    private final String NAME_TABLE = "test_table";
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(filePathProvider);

    private final int lengthBytesData = 100;
    private final int posFirstFragment = 10;
    private final int lengthFirstFragment = 60;
    private final int linkOnSecondFragment = 80;//I chose a random place where the next fragment will be recorded
    private final int lengthSecondFragment = lengthBytesData - lengthFirstFragment;
    private final int linkOnThirdFragment = -2; //-2 - means that this was the last fragment

    @BeforeEach
    void setUp() {
        testHelperTSM.clear(NAME_TABLE);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        FragmentRecordSaverImpl fragmentRecordSaver = getFragmentSaver();
        List<Byte> bytesData = generateTestDataForSave();
        fragmentRecordSaver.save(NAME_TABLE, bytesData);

        byte[] result = testHelperTSM.read(NAME_TABLE, 0, -1);
        byte[] excepted = getExceptedResult(bytesData);

        assertArrayEquals(excepted, result);
    }

    private FragmentRecordSaverImpl getFragmentSaver() {
        MetaDataFragmentRecordManager metaDataFragmentRecordManager = mockMetaDataFragmentManager();
        return new FragmentRecordSaverImpl(fileWriter, metaDataFragmentRecordManager);
    }

    private MetaDataFragmentRecordManager mockMetaDataFragmentManager() {
        MetaDataFragment metadataFirstFragment = new MetaDataFragment(posFirstFragment, lengthFirstFragment, linkOnSecondFragment);
        MetaDataFragment metadataSecondFragment
                = new MetaDataFragment(linkOnSecondFragment, lengthSecondFragment, linkOnThirdFragment);

        MetaDataFragmentRecordManager metaDataFragmentRecordManager = mock(MetaDataFragmentRecordManager.class);
        when(metaDataFragmentRecordManager.getMetaDataNewFragment(NAME_TABLE, lengthBytesData)).thenReturn(metadataFirstFragment);
        when(metaDataFragmentRecordManager.getMetaDataNewFragment(NAME_TABLE, lengthSecondFragment)).thenReturn(metadataSecondFragment);

        return metaDataFragmentRecordManager;
    }

    private List<Byte> generateTestDataForSave() {
        List<Byte> bytesData = new ArrayList<>();

        for (int i = 0; i < lengthBytesData; i++) {
            Random random = new Random();
            bytesData.add((byte) (random.nextInt(50) + 1));
        }
        return bytesData;
    }

    private byte[] getExceptedResult(List<Byte> bytesData) {
        byte[] result = new byte[127];

        //firstFragment
        insertMetadata(result, posFirstFragment, lengthFirstFragment, linkOnSecondFragment);
        insertData(result, posFirstFragment, bytesData, 0, lengthFirstFragment);
        //secondFragment
        insertMetadata(result, linkOnSecondFragment, lengthSecondFragment, linkOnThirdFragment);
        insertData(result, linkOnSecondFragment, bytesData, lengthFirstFragment, lengthSecondFragment);
        return result;
    }

    private void insertMetadata(byte[] result, int startPos, int length, int link) {
        insertInt(result, startPos, length);
        int posLink = startPos + length + LENGTH_INDICATOR_BYTE_COUNT;
        insertInt(result, posLink, link);
    }

    private void insertInt(byte[] arr, int pos, int number) {
        byte[] intArr = intToBytes(number);
        System.arraycopy(intArr, 0, arr, pos - 1, intArr.length);
    }

    private byte[] intToBytes(int value) {
        return new byte[] {
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    private void insertData(byte[] result, int posData, List<Byte> bytesData, int startIndexBytesData, int size) {
        posData += LENGTH_INDICATOR_BYTE_COUNT -1;
        for (int i = 0; i < size; i++) {
            result[posData + i] = bytesData.get(startIndexBytesData + i);
        }
    }
}