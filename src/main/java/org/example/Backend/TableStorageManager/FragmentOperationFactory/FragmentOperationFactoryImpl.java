package org.example.Backend.TableStorageManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentMetaDataManager.FragmentMetaDataManager;
import org.example.Backend.TableStorageManager.FragmentMetaDataManager.FragmentMetadataManagerImpl;

public class FragmentOperationFactoryImpl implements FragmentOperationFactory {
    private final FragmentMetaDataManager fragmentMetaDataManager = new FragmentMetadataManagerImpl();

    public FragmentMetaDataManager getFragmentMetaDataManager() {return fragmentMetaDataManager;}

}
