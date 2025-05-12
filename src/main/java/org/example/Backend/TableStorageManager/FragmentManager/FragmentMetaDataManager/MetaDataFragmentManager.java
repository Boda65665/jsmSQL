package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public interface MetaDataFragmentManager {
    FragmentMetaDataInfo getFragmentMetaDataInfo(String nameTable, int lengthDataFragment);
}
