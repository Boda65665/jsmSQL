package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.Record;

import org.example.Backend.Models.MetaDataFragment;

public interface MetaDataFragmentRecordManager {
    MetaDataFragment getMetaDataNewFragment(String nameTable, int lengthDataFragment);
}
