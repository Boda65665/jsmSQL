package org.example.Backend.TableStorageManager.FileManager.GetterFileSize;

import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GetterFileSizeImplTest {
    private final FileOperationFactory fileOperationFactory = new FileOperationFactoryImpl();
    private final FilePathProvider filePathProvider = fileOperationFactory.getTablePathProvider();
    private final GetterFileSizeImpl getterFileSize = new GetterFileSizeImpl(filePathProvider);
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(filePathProvider);
    private final String NAME_TABLE = "test_table";


    @Test
    void getSize() {
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            testHelperTSM.clear(NAME_TABLE);
            int randomSize = random.nextInt(101);
            writeBytes(randomSize);

            assertEquals(randomSize, getterFileSize.getSize(NAME_TABLE));
        }
    }

    private void writeBytes(int randomSize) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(randomSize);
        byte[] bytes = byteBuffer.array();

        testHelperTSM.write(0, bytes, NAME_TABLE);
    }
}