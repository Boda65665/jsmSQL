package org.example.Backend.TableStorageManager.RecordManager.RecordOperationFactory;

import org.example.Backend.TableStorageManager.RecordManager.RecordSaver.RecordSaverImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordOperationFactoryImplTest {
    private final RecordOperationFactoryImpl recordOperationFactory = new RecordOperationFactoryImpl();

    @Test
    void getRecordSaver() {
        assertInstanceOf(RecordSaverImpl.class, recordOperationFactory.getRecordSaver(null));
    }
}