package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FragmentMetaDataInfo {
    private int positionFragment;
    private int lengthFragment;
    private Integer linkOnNextFragment;
}
