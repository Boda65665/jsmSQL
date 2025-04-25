package org.example.Backend.DbManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BtreeManagerTest {
    private static BtreeManager manager = new BtreeManager("testDb", "test");

    @BeforeAll
    static void setUp() {
        manager.close();
        manager = new BtreeManager("testDb", "test");
    }

    @Test
    void getValid() {
        assertThrows(NullPointerException.class, () -> manager.get(null));
        assertDoesNotThrow(() -> manager.get(0));
    }

    @Test
    void insertValid() {
        assertThrows(NullPointerException.class, () -> manager.insert(null, -1));
        assertDoesNotThrow(() -> manager.insert(1, -1));
    }

    @Test
    void deleteValid() {
        assertThrows(NullPointerException.class, () -> manager.delete(null));
        assertDoesNotThrow(() -> manager.delete(0));
    }

    @Test
    void higherKeyValid() {
        assertThrows(NullPointerException.class, () -> manager.higherKey(null));
        assertDoesNotThrow(() -> manager.higherKey(0));
    }
}