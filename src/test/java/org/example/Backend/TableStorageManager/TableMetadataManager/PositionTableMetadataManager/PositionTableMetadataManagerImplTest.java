package org.example.Backend.TableStorageManager.TableMetadataManager.PositionTableMetadataManager;

import org.example.Backend.DbManager.DbManager;
import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.TableStorageManager.TableMetadataManager.TableMetadataOperationFactory.TableMetadataOperationFactory;
import org.example.Backend.TableStorageManager.TableMetadataManager.TableMetadataOperationFactory.TableMetadataOperationFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PositionTableMetadataManagerImplTest {
  private final DbManagerFactoryImpl dbManagerFactory = DbManagerFactoryImpl.getDbManagerFactory();
  private static final String basePath = System.getProperty("user.dir") + File.separator + "test";
  private static final String NAME_DB = "test";
  private final TableMetadataOperationFactory tableMetadataOperationFactory = new TableMetadataOperationFactoryImpl(dbManagerFactory, basePath, NAME_DB);
  private final PositionTableMetadataManager positionTableMetadataManager = tableMetadataOperationFactory.getPositionTableMetadataManager();
  private final String NAME_TABLE = "test";

  @BeforeEach
  void setUp() {
    for (DbManager dbManager : dbManagerFactory.getDbManagers()) {
      dbManager.clear();
    }
  }

    @Test
    void getPositionStartLastMetadataFragment() {
      Random random = new Random();
      int testPosition = random.nextInt(100);

      assertNull(positionTableMetadataManager.getPositionStartLastMetadataFragment(NAME_TABLE));
      positionTableMetadataManager.setPositionStartLastMetadataFragment(NAME_TABLE, testPosition);
      assertEquals(testPosition, positionTableMetadataManager.getPositionStartLastMetadataFragment(NAME_TABLE));
    }

    @Test
    void setPositionStartLastMetadataFragment() {
      Random random = new Random();

      for (int i = 0; i < 10; i++) {
        int testPosition = random.nextInt(100);
        positionTableMetadataManager.setPositionStartLastMetadataFragment(NAME_TABLE, testPosition);
        assertEquals(testPosition, positionTableMetadataManager.getPositionStartLastMetadataFragment(NAME_TABLE));
      }
    }

    @Test
    void getEndPosition() {
      Random random = new Random();
      int testPosition = random.nextInt(100);

      assertNull(positionTableMetadataManager.getEndPosition(NAME_TABLE));
      positionTableMetadataManager.setEndPosition(NAME_TABLE, testPosition);
      assertEquals(testPosition, positionTableMetadataManager.getEndPosition(NAME_TABLE));
    }

    @Test
    void setEndPosition() {
      Random random = new Random();

      for (int i = 0; i < 10; i++) {
        int testPosition = random.nextInt(100);
        positionTableMetadataManager.setEndPosition(NAME_TABLE, testPosition);
        assertEquals(testPosition, positionTableMetadataManager.getEndPosition(NAME_TABLE));
      }
    }
}