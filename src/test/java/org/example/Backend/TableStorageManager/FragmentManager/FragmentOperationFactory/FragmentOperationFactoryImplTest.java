package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.FragmentMetaDataManager;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactory;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory.FragmentOperationFactoryImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.TableManager.TableOperationFactory.TableOperationFactory;
import org.example.Backend.TableStorageManager.TableManager.TableOperationFactory.TableOperationFactoryImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FragmentOperationFactoryImplTest {
    private final FragmentOperationFactory fragmentOperationFactory = new FragmentOperationFactoryImpl();
    private final TableOperationFactory tableOperationFactory = new TableOperationFactoryImpl();

    @Test
    void getFragmentMetaDataManager() {
        assertInstanceOf(FragmentMetaDataManager.class, fragmentOperationFactory.getFragmentMetaDataManager());
    }

    @Test
    void getFragmentSaver() {
        assertInstanceOf(FragmentSaver.class, fragmentOperationFactory.getFragmentSaver(tableOperationFactory.getTableWriter()));
    }
}