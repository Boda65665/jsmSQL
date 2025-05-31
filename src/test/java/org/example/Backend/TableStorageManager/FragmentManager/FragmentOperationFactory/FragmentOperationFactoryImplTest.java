package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentRecordSaverImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FragmentOperationFactoryImplTest {
    private final FragmentOperationFactory fragmentOperationFactory = new FragmentOperationFactoryImpl(null);

    @Test
    void getFragmentRecordSaver() {
        assertInstanceOf(FragmentRecordSaverImpl.class, fragmentOperationFactory.getFragmentRecordSaver(null));
    }
}