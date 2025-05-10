package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnStruct {
    private String columnName;
    private ColumnType type;
    boolean isPrimary;

    public ColumnStruct(String columnName, ColumnType type) {
        this.columnName = columnName;
        this.type = type;
    }
}
