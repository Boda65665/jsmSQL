package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class TableMetaData {
    private ArrayList<ColumnStruct> columnStructList;
    private String nameColumnPrimaryKey;
}
