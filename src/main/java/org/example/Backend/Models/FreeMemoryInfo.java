package org.example.Backend.Models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FreeMemoryInfo {
    private int countFreeBytes;
    private int position;

    public int getCountFreeBytes() {
        return countFreeBytes;
    }

    public int getPosition() {
        return position;
    }
}
