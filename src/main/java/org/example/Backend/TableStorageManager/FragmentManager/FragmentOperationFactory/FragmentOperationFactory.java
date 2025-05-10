package org.example.Backend.TableStorageManager.FragmentManager.FragmentOperationFactory;

import org.example.Backend.TableStorageManager.FragmentManager.FragmentSaver.FragmentSaver;

import org.example.Backend.TableStorageManager.FileManager.TableWriter.FileWriter;

public interface FragmentOperationFactory {
    FragmentSaver getFragmentSaver(FileWriter fileWriter);
}
