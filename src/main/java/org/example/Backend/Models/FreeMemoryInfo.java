package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FreeMemoryInfo {
    private Integer countFreeBytes;
    private Integer position;

}
