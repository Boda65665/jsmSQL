package org.example.Backend.Models;

import lombok.AllArgsConstructor;

@lombok.Data
@AllArgsConstructor
public class Column {
    private Object data;
    private ColumnType type;
    private String name;

    public Column(Object data, ColumnType type) {
        this.data = data;
        this.type = type;
    }
}
