package org.example.Backend.TableStorageManager.FreeSpaceManager;

import org.example.Backend.DbManager.factory.DbManagerFactoryImpl;
import org.example.Backend.DbManager.DbManagerImpl;
import org.example.Backend.Models.FreeMemoryInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class FreeSpaceManagerImplTest {
    @InjectMocks
    private FreeSpaceManagerImpl freeSpaceManager;
    @Mock
    private static DbManagerFactoryImpl dbManagerFactoryImpl;
    private static DbManagerImpl<Integer> dbManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        dbManager.close();
    }

    @ParameterizedTest
    @MethodSource("caseForGetInsertionPoint")
    void getInsertionPoint(int length, int exceptedOffset, int exceptedCountFreeBytes) {
        mockGetDbManager();
        freeSpaceManager = new FreeSpaceManagerImpl(dbManager);

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
        dbManager = new DbManagerImpl("test", "test");
        dbManager.put(30, 0);
        dbManager.put(70, 1);
        dbManager.put(100, 2);
        when(dbManagerFactoryImpl.getDbManager(anyString(), anyString())).thenReturn(dbManager);
    }
}