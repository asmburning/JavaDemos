package org.lxy.lambda.filter;

import lombok.Data;

@Data
public class Box {
    private int weight = 0;
    private String color = "";

    public Box() {
    }

    public Box(int weight, String color) {
        this.weight = weight;
        this.color = color;
    }

}
