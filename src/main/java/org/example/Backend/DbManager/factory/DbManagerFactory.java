package org.example.Backend.DbManager.factory;

import org.example.Backend.DbManager.DbManager;
import java.util.List;

public interface DbManagerFactory {
    DbManager getDbManager(String basePath, String nameDb);
    List<DbManager> getDbManagers();
}
