package org.example.Backend.DbManager;

import org.example.Backend.DbManager.factory.DbManagerFactory;
import java.util.List;

public class DbManagerCloser {
    private final DbManagerFactory factory;

    public DbManagerCloser(DbManagerFactory factory) {
        this.factory = factory;
    }

    public void closeAll(){

        List<DbManager> dbManagers = factory.getDbManagers();

        for (DbManager dbManager : dbManagers){
            try {
                dbManager.close();
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
