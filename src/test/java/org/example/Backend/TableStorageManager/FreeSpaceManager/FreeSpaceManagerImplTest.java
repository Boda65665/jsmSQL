package org.example.Backend.TableStorageManager.FreeSpaceManager;

import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.DbManager.DbManagerImpl;
import org.example.Backend.Models.FreeMemoryInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class FreeSpaceManagerImplTest {

    @InjectMocks
    private FreeSpaceManagerImpl freeSpaceManager;
    @Mock
    private static DbManagerFactoryImpl dbManagerFactoryImpl;
    private static DbManagerImpl<Integer, Integer> dbManager;
    private final int countFreeBytes = 50;
    private final int position = 4;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dbManager = new DbManagerImpl("test", System.getProperty("user.dir") + "/test");
        dbManager.clear();

        freeSpaceManager = new FreeSpaceManagerImpl(dbManager);
    }

    @AfterEach
    void tearDown() {
        dbManager.close();
    }

    @ParameterizedTest
    @MethodSource("caseForGetInsertionPoint")
    void getInsertionPoint(int length, int exceptedOffset, int exceptedCountFreeBytes) {
        mockGetDbManager();

        FreeMemoryInfo exceptedFreeMemoryInfo = new FreeMemoryInfo(exceptedCountFreeBytes, exceptedOffset);
        FreeMemoryInfo result = freeSpaceManager.getInsertionPoint(length);

        assertEquals(exceptedFreeMemoryInfo.getPosition(), result.getPosition());
        assertEquals(exceptedFreeMemoryInfo.getCountFreeBytes(), result.getCountFreeBytes());
    }

    public static Stream<Arguments> caseForGetInsertionPoint() {
        return Stream.of(
                Arguments.of(30, 0, 30),
                Arguments.of(65, 1, 70),
                Arguments.of(101, 2, 100)
        );
    }

    private static void mockGetDbManager() {
        dbManager.put(30, 0);
        dbManager.put(70, 1);
        dbManager.put(100, 2);
        when(dbManagerFactoryImpl.getDbManager(anyString(), anyString())).thenReturn(dbManager);
    }

    @Test
    void freeSpaceIsOver() {
        assertTrue(freeSpaceManager.freeSpaceIsOver());
        freeSpaceManager.addFreeSpace(countFreeBytes, position);
        assertFalse(freeSpaceManager.freeSpaceIsOver());
    }

    @Test
    void redactFreeSpace() {
        freeSpaceManager.addFreeSpace(countFreeBytes, position);
        int lengthFragment = 20;

        freeSpaceManager.redactFreeSpace(lengthFragment, countFreeBytes);
        int newCountFreeBytes = countFreeBytes - lengthFragment;
        int newPosition = position + lengthFragment;
        FreeMemoryInfo freeMemoryInfo = freeSpaceManager.getInsertionPoint(newCountFreeBytes);
        assertEquals(newCountFreeBytes, freeMemoryInfo.getCountFreeBytes());
        assertEquals(newPosition, freeMemoryInfo.getPosition());
    }

    @Test
    void addFreeSpace() {
        freeSpaceManager.addFreeSpace(countFreeBytes, position);
        FreeMemoryInfo freeMemoryInfo = freeSpaceManager.getInsertionPoint(5);
        assertEquals(position, freeMemoryInfo.getPosition());
    }
}