package org.lxy.lombok;

import lombok.Data;

import java.io.Serializable;

@Data
public class Book implements Serializable {
    private String name;
}
