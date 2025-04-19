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
        assertThrows(IllegalArgumentException.class, () -> manager.get(-1));
        assertDoesNotThrow(() -> manager.get(0));
    }

    @Test
    void insertValid() {
        assertThrows(IllegalArgumentException.class, () -> manager.insert(-1, -1));
        assertDoesNotThrow(() -> manager.insert(1, -1));
    }

    @Test
    void deleteValid() {
        assertThrows(IllegalArgumentException.class, () -> manager.delete(-1));
        assertDoesNotThrow(() -> manager.delete(0));
    }

    @Test
    void higherKeyValid() {
        assertThrows(IllegalArgumentException.class, () -> manager.higherKey(-1));
        assertDoesNotThrow(() -> manager.higherKey(0));
    }
}