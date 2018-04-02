package org.lxy;

import lombok.Data;

@Data
public class Student {

    private String name;

    private String email;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }
}
