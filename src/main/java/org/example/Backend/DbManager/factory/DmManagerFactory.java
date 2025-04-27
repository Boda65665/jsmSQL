package org.example.Backend.DbManager.factory;

import org.example.Backend.DbManager.DbManager;
import java.util.List;

public interface DmManagerFactory {
    DbManager getDbManager(String basePath, String nameDb);
    List<DbManager> getDbManagers();
}
