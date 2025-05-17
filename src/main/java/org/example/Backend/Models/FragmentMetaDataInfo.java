package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class FragmentMetaDataInfo {
    private int positionFragment;
    private int lengthDataFragment;
    private Integer linkOnNextFragment;
}
