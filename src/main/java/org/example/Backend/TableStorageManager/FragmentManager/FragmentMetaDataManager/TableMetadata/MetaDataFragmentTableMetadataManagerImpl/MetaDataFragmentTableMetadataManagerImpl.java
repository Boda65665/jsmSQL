package org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManagerImpl;

import org.example.Backend.Models.MetaDataFragment;
import org.example.Backend.Models.MetaDataFragmentTableMetadata;
import org.example.Backend.TableStorageManager.FragmentManager.FragmentMetaDataManager.TableMetadata.MetaDataFragmentTableMetadataManager;

public class MetaDataFragmentTableMetadataManagerImpl implements MetaDataFragmentTableMetadataManager {
    private final CreatorMetadataFragment creatorNewFragment;

    public MetaDataFragmentTableMetadataManagerImpl(CreatorMetadataFragment creatorNewFragment) {
        this.creatorNewFragment = creatorNewFragment;
    }

    @Override
    public MetaDataFragmentTableMetadata getMetaDataNewFragment(String nameTable) {
        return creatorNewFragment.createNewFragment(nameTable);
    }


}
