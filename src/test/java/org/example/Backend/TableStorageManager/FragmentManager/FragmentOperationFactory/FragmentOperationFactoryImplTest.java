package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaverImpl;
import org.example.Backend.TableStorageManager.RecordManager.RecordSaver.RecordSaver;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactory;
import org.example.Backend.TableStorageManager.FileManager.FileOperationFactory.FileOperationFactoryImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FragmentOperationFactoryImplTest {
    private final FragmentOperationFactory fragmentOperationFactory = new FragmentOperationFactoryImpl();
    private final FileOperationFactory fileOperationFactory = new FileOperationFactoryImpl();

    @Test
    void getFragmentSaver() {
        assertInstanceOf(FragmentSaverImpl.class, fragmentOperationFactory.getFragmentSaver(fileOperationFactory.getTableWriter()));
    }
}