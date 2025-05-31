package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.Record.MetaDataFragmentRecordManager;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.Record.MetadataFragmentRecordRecordManagerImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentRecordSaverImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FreeSpaceManager.Factory.FreeSpaceManagerFactory;

public class FragmentOperationFactoryImpl implements FragmentOperationFactory {
    private final FileWriter fileWriter;

    public FragmentOperationFactoryImpl(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    @Override
    public FragmentSaver getFragmentRecordSaver(FreeSpaceManagerFactory freeSpaceManagerFactory) {
        MetaDataFragmentRecordManager metaDataFragmentRecordManager = new MetadataFragmentRecordRecordManagerImpl(freeSpaceManagerFactory);
        return new FragmentRecordSaverImpl(fileWriter, metaDataFragmentRecordManager);
    }
    
}
