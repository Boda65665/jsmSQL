package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnStruct {
    private String columnName;
    private TypeData type;
}
