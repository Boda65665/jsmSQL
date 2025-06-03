package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class TableMetaData {
    private List<ColumnStruct> columnStructList;
    private String nameColumnPrimaryKey;
}
