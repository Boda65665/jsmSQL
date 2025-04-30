package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TableFragmentLink {
    private int byteLength;
    public long offset;
}
