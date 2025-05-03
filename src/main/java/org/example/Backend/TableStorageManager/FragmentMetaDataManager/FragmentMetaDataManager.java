package org.example.Backend.TableStorageManager.FragmentMetaDataManager;

import org.example.Backend.Models.FragmentMetaDataInfo;
import org.example.Backend.TableStorageManager.FreeSpaceManager.FreeSpaceManager;

public interface FragmentMetaDataManager {
    FragmentMetaDataInfo getFragmentMetaDataInfo(FreeSpaceManager freeSpaceManager, int lengthDataFragment);
}
