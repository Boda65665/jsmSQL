package org.example.Backend.TableStorageManager.TableMetadataManager.BytesMetaDataReader;

import org.example.Backend.TableStorageManager.FileManager.FileReader.FileReader;

public class BytesMetaDataReaderImpl implements BytesMetaDataReader{
    private final FileReader fileReader;

    public BytesMetaDataReaderImpl(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public byte[] read(String tableName) {
        return new byte[0];
    }
}
