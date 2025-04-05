package org.example.Backend.Models;

import lombok.Data;

import java.util.List;

@Data
public class TableMetaData {
    private List<ColumnStruct> columnStructList;
}
