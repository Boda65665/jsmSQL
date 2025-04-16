package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProviderFactory;

public class TableWriterFactory {
    public static TableWriter getTableWriter() {
        return new TableWriterImpl(TablePathProviderFactory.getTablePathProvider());
    }
}
