package org.lxy;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class A {

    @Test
    public void test() {

        List<Student> studentList = Lists.newArrayList(new Student("jack"), new Student("tom"));

        List<String> list1 = Lists.newArrayList("jack", "tom", "lily", "Eric");

        List<String> list2 = Lists.newArrayList("apple", "jack", "banana", "orange");

        List<List<String>> list = Lists.newArrayList(list1, list2);

        List<String> list11 = list1.stream().map(s -> s + 1).collect(Collectors.toList());

        List<String> studentList1 = studentList.stream().map(Student::getName).collect(Collectors.toList());

        List list3 = list.stream().map(stringList -> stringList.stream()).collect(Collectors.toList());

        List list4 = list.stream().flatMap(strings -> strings.stream()).collect(Collectors.toList());

        log.info("list11:{},studentList1:{} ,list3:{},list3:{}", list11, studentList1, list3, list4);

        String a = "java";
        Arrays.asList(a.toCharArray()).stream().count();


        Student student = new Student("jack");
        student.setEmail("jack@qq.com");
        String upperName = Optional.of(student)
                .map(Student::getName)
                .map(String::toUpperCase)
                .get();
        log.info("upperName:{}",upperName);

    }
}
