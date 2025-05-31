package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;
import org.example.Backend.TableStorageManager.FreeSpaceManager.Factory.FreeSpaceManagerFactory;

public interface FragmentOperationFactory {
    FragmentSaver getFragmentRecordSaver(FreeSpaceManagerFactory freeSpaceManagerFactory);
}
