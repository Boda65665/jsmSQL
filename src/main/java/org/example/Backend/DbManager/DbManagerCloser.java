package org.example.Backend.DbManager;

import org.example.Backend.DbManager.factory.DmManagerFactory;
import java.util.List;

public class DbManagerCloser {
    private final DmManagerFactory factory;

    public DbManagerCloser(DmManagerFactory factory) {
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
