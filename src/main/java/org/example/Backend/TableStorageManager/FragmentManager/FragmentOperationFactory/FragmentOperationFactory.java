package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;

import org.example.Backend.TableStorageManager.FileManager.FileWriter.FileWriter;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public interface FragmentOperationFactory {
    FragmentSaver getFragmentRecordSaver(FileWriter fileWriter);
}
