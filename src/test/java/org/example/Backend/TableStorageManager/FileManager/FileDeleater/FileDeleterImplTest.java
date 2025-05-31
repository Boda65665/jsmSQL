package org.example.Backend.TableStorageManager.FileManager.FileDeleater;

import org.example.Backend.Models.TableMetaData;
import org.example.Backend.TableStorageManager.TH.TestHelperTSM;
import org.example.Backend.TableStorageManager.FileManager.FileCreater.FileCrater;
import org.example.Backend.TableStorageManager.FileManager.Factory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.Factory.FileOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FileManager.FilePathProvider.FilePathProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class FileDeleterImplTest {
    private final FileOperationFactory fileOperationFactory = new FileOperationFactoryImpl();
    private final FileCrater fileCrater = fileOperationFactory.getFileCrater();
    private final FilePathProvider filePathProvider = fileOperationFactory.getFilePathProvider();
    private final FileDeleter fileDeleter = new FileDeleterImpl(filePathProvider);
    private final TestHelperTSM testHelperTSM = new TestHelperTSM(filePathProvider);
    private final String NAME_TABLE = "test_table";

    @BeforeEach
    void setUp() {
        testHelperTSM.deleteTable(NAME_TABLE);
    }

    @Test
    void delete() {
        String pathTable = filePathProvider.getTablePath(NAME_TABLE);

        fileCrater.create(NAME_TABLE, new TableMetaData(new ArrayList<>(), "test"));
        assertTrue(new File(pathTable).exists());

        fileDeleter.delete(NAME_TABLE);
        assertFalse(new File(pathTable).exists());
    }
}