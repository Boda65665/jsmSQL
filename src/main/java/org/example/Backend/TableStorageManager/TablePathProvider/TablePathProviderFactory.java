package org.example.Backend.TableStorageManager.TablePathProvider;

public class TablePathProviderFactory {
    public static TablePathProvider getTablePathProvider() {
        return new TablePathProviderImpl();
    }
}
