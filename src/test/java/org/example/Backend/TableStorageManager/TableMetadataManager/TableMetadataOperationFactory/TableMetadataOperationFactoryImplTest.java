package org.example.Backend.TableStorageManager.TableMetadataManager.TableMetadataOperationFactory;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.DbManagerCloser;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManager;
import org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager.PositionTableMetadataManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class TableMetadataOperationFactoryImplTest {
    private static final DbManagerFactoryImpl dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
    private static final String basePath = System.getProperty("user.dir") + File.separator + "test";
    private static final String NAME_DB = "test";
    private final TableMetadataOperationFactoryImpl tableMetadataOperationFactory = new TableMetadataOperationFactoryImpl(dbManagerFactory, basePath, NAME_DB);
    private final DbManagerCloser dbManagerCloser = new DbManagerCloser(dbManagerFactory);

    @AfterEach
    public void tearDown(){
        dbManagerCloser.closeAll();
    }

    @Test
    void getPositionTableMetadataManager() {
        PositionTableMetadataManager positionTableMetadataManager = tableMetadataOperationFactory.getPositionTableMetadataManager();
        assertInstanceOf(PositionTableMetadataManagerImpl.class, positionTableMetadataManager);
        dbManagerFactory.getDbManager(basePath, NAME_DB).close();
    }
}