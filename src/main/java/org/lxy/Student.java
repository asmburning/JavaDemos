package org.lxy;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Student {

    private String name;

    private String email;

    private BigDecimal score;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public Student(BigDecimal score) {
        this.score = score;
    }
}
