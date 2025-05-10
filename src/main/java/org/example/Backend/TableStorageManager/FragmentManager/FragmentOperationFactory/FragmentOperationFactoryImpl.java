package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.FragmentMetaDataManager;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.FragmentMetadataManagerImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaverImpl;
import org.example.Backend.TableStorageManager.FileManager.TableWriter.FileWriter;

public class FragmentOperationFactoryImpl implements FragmentOperationFactory {
    private final FragmentMetaDataManager fragmentMetaDataManager = new FragmentMetadataManagerImpl();

    public FragmentSaver getFragmentSaver(FileWriter fileWriter) {
        return new FragmentSaverImpl(fileWriter, fragmentMetaDataManager);
    }
}
