package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetaDataFragmentManager;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetadataFragmentRecordManagerImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentRecordSaverImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactory;

public class FragmentOperationFactoryImpl implements FragmentOperationFactory {
    private final FreeSpaceManagerFactory freeSpaceManagerFactory;


    public FragmentOperationFactoryImpl(FreeSpaceManagerFactory freeSpaceManagerFactory) {
        this.freeSpaceManagerFactory = freeSpaceManagerFactory;
    }

    public FragmentSaver getFragmentRecordSaver(FileWriter fileWriter) {
        MetaDataFragmentManager metaDataFragmentManager = new MetadataFragmentRecordManagerImpl(freeSpaceManagerFactory);
        return new FragmentRecordSaverImpl(fileWriter, metaDataFragmentManager);
    }
}
