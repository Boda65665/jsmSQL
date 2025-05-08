package org.example.Backend.TableStorageManager.TableManager.TableDeleater;

import org.example.Backend.TableStorageManager.TableManager.TablePathProvider.TablePathProvider;
import java.io.File;

public class TableDeleterImpl extends TableDeleter {

    public TableDeleterImpl(TablePathProvider pathProvider) {
        super(pathProvider);
    }

    @Override
    public void delete(String tableName) {
        File file = new File(pathProvider.getTablePath(tableName));
        if (file.exists()) file.delete();
    }
}
