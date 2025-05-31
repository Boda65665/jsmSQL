package org.example.Backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Interval implements Serializable{
    private int start;
    private int end;

}
