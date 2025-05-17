package org.example.Backend.TableStorageManager;

import org.example.Backend.Models.Record;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class TableStorageManagerTest {
    private final TableStorageManager tableStorageManager
            = new TableStorageManager(null,null,null,null);

    @Test
    void validCreateTable() {
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.createTable(null, null));
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.createTable("", null));
        assertThrows(NullPointerException.class, () -> tableStorageManager.createTable("not_emtpy_and_null", null));
    }

    @Test
    void validSave() {
        assertThrows(NullPointerException.class, () -> tableStorageManager.save(null, null));
        assertThrows(NullPointerException.class, () -> tableStorageManager.save("NAME_TABLE", null));
        assertThrows(IllegalArgumentException.class, () -> tableStorageManager.save("", new Record(new ArrayList<>())));
    }
}