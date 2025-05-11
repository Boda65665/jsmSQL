package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.FragmentMetaDataManager;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.FragmentMetadataManagerImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentRecordSaverImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public class FragmentOperationFactoryImpl implements FragmentOperationFactory {
    private final FragmentMetaDataManager fragmentMetaDataManager = new FragmentMetadataManagerImpl();

    public FragmentSaver getFragmentRecordSaver(FileWriter fileWriter, FreeSpaceManager freeSpaceManager) {
        return new FragmentRecordSaverImpl(fileWriter, fragmentMetaDataManager, freeSpaceManager);
    }
}
