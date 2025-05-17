package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager;

import org.example.Backend.Models.FragmentMetaDataInfo;

public interface MetaDataFragmentManager {
    FragmentMetaDataInfo getFragmentMetaDataInfo(String nameTable, int lengthDataFragment);
}
