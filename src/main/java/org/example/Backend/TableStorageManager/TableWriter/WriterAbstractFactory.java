package org.example.Backend.TableStorageManager.TableWriter;

import org.example.Backend.Models.TypeData;
import org.example.Backend.TableStorageManager.TablePathProvider.TablePathProvider;
import org.example.Backend.TableStorageManager.TableWriter.PrimitiveType.*;

public class WriterAbstractFactory {
    private final TablePathProvider tablePathProvider;

    public WriterAbstractFactory(TablePathProvider tablePathProvider) {
        this.tablePathProvider = tablePathProvider;
    }

    public TableWriter getTableWriter(TypeData typeData) {
        return switch (typeData){
            case INT -> new TableIntegerWriter(tablePathProvider);
            case DOUBLE -> new TableDoubleWriter(tablePathProvider);
            case VARCHAR -> new TableStringWriter(tablePathProvider);
            case LONG -> new TableLongWriter(tablePathProvider);
            case BOOLEAN -> new TableBooleanWriter(tablePathProvider);
            case CHAR -> new TableCharWriter(tablePathProvider);
            case DATE -> null;
        };
    }
}
