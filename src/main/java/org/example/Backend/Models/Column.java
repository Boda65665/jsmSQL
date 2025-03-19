package org.example.Backend.Models;

import org.example.Backend.Models.Interface.Data;
import org.example.Backend.Models.Interface.TypeData;

@lombok.Data
public class Column {
    private Data data;
    private TypeData typeData;
}
