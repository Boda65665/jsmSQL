package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FragmentMetaData {
    private int lengthFragment;
    private Integer positionNextFragment;
}
