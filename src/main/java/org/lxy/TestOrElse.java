package org.lxy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TestOrElse {

    @Test
    public void test() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(new BigDecimal("100")));
        log.info("{}", list.stream().map(Student::getScore).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
    }

    private BigDecimal getScore(Student student) {
        if (Objects.isNull(student.getScore())) {
            return BigDecimal.ZERO;
        }
        return student.getScore();
    }
}
