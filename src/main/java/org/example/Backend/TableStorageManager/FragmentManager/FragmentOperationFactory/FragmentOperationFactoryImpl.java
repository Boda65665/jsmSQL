package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.FragmentMetaDataManager;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.FragmentMetadataManagerImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaverImpl;
import org.example.Backend.TableStorageManager.TableManager.TableWriter.TableWriter;

public class FragmentOperationFactoryImpl implements FragmentOperationFactory {
    private final FragmentMetaDataManager fragmentMetaDataManager = new FragmentMetadataManagerImpl();

    public FragmentMetaDataManager getFragmentMetaDataManager() {return fragmentMetaDataManager;}

    public FragmentSaver getFragmentSaver(TableWriter tableWriter) {
        return new FragmentSaverImpl(tableWriter, fragmentMetaDataManager);
    }
}
