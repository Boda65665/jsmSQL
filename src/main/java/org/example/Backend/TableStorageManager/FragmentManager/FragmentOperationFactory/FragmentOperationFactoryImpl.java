package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FileManager.GetterFileSize.GetterFileSize;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetaDataFragmentManager;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetadataFragmentRecordManagerImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.MetadataFragmentTableMetadataManagerImpl;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentRecordSaverImpl;
import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentTableMetadataSaver;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManagerFactory.FreeSpaceManagerFactory;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManager;

public class FragmentOperationFactoryImpl implements FragmentOperationFactory {
    private final FileWriter fileWriter;

    public FragmentOperationFactoryImpl(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    @Override
    public FragmentSaver getFragmentRecordSaver(FreeSpaceManagerFactory freeSpaceManagerFactory) {
        MetaDataFragmentManager metaDataFragmentManager = new MetadataFragmentRecordManagerImpl(freeSpaceManagerFactory);
        return new FragmentRecordSaverImpl(fileWriter, metaDataFragmentManager);
    }

    @Override
    public FragmentSaver getFragmentTableMetadataSave(GetterFileSize getterFileSize, PositionTableMetadataManager positionTableMetadataManager) {
        MetadataFragmentTableMetadataManagerImpl metadataFragmentTableMetadataManager = new MetadataFragmentTableMetadataManagerImpl(positionTableMetadataManager,getterFileSize);
        return new FragmentTableMetadataSaver(fileWriter, metadataFragmentTableMetadataManager);
    }
}
