package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.FragmentMetaDataManager;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.TableManager.TableWriter.TableWriter;

public interface FragmentOperationFactory {
    FragmentMetaDataManager getFragmentMetaDataManager();
    FragmentSaver getFragmentSaver(TableWriter tableWriter);
}
