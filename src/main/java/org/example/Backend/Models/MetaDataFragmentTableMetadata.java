package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetaDataFragmentTableMetadata {
    private int positionFragment;
    private boolean continuePreviousFragment;
}
