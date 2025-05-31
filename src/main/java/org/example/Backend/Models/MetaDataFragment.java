package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetaDataFragment {
    private int positionFragment;
    private int lengthDataFragment;
    private Integer linkOnNextFragment;
}
