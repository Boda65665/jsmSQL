package org.example.Backend.TableStorageManager.RecordManager.RecordOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.RecordManager.RecordSaver.RecordSaver;

public interface RecordOperationFactory {
    RecordSaver getRecordSaver(FragmentSaver fragmentSaver);
}
