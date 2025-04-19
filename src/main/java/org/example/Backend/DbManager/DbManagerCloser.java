package org.example.Backend.DbManager;

import java.util.List;

public class DbManagerCloser {
    private final DbManagerFactory factory = DbManagerFactory.getDbManagerFactory();

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
