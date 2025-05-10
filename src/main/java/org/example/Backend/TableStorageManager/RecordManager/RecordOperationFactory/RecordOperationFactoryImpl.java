package org.example.Backend.TableStorageManager.RecordManager.RecordOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.RecordManager.RecordSaver.RecordSaver;
import org.example.Backend.TableStorageManager.RecordManager.RecordSaver.RecordSaverImpl;

public class RecordOperationFactoryImpl implements RecordOperationFactory {

    @Override
    public RecordSaver getRecordSaver(FragmentSaver fragmentSaver) {
        return new RecordSaverImpl(fragmentSaver);
    }
}
