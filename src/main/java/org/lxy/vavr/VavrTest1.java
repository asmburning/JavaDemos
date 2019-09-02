package org.lxy.vavr;

import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Queue;
import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * http://www.vavr.io/vavr-docs/
 * www.baeldung.com/vavr-tutorial
 * http://www.vavr.io/
 */
@Slf4j
public class VavrTest1 {

    @Test
    public void test() {
        Try<Integer> try1 = divide2(3, 3);
        try1.isSuccess();
        log.info("{}", try1);
        log.info("{}", divide2(3, 0));
        log.info("{}", divide(3, 3));
        log.info("{}", divide(3, 0));
    }

    private int divide(int dividend, int divisor) {
        // throws if divisor is zero
        return dividend / divisor;
    }

    // = Success(result) or Failure(exception)
    private Try<Integer> divide2(Integer dividend, Integer divisor) {
        return Try.of(() -> dividend / divisor);
    }


    @Test
    public void test2() {
        //List list = Collections.unmodifiableList(List.of("java", "python"));
        //list.add("C++");
    }

    @Test
    public void test3() {
        io.vavr.collection.List list = io.vavr.collection.List.of("java", "Spring", "hibernate");
        list.tail().append("Mybatis");
        List defaultList = list.asJava();
        List mutableList = list.asJavaMutable();
        mutableList.add("Redis");
        defaultList.add("Redis");
    }

    @Test
    public void test4() {
        Queue<Integer> queue = Queue.of(1, 2, 3)
                .enqueue(4).enqueue(5);

        Tuple2<Integer, Queue<Integer>> dequeue =
                queue.dequeue();

        log.info(dequeue.toString());

        Queue.of(1).dequeueOption();

        Queue.empty().dequeueOption();

    }

    @Test
    public void test5() {
        Comparator<Integer> c = (a, b) -> b - a;

        SortedSet<Integer> reversed = TreeSet.of(c, 2, 3, 1, 2);

        log.info(reversed.toString());
    }

    @Test
    public void test6() {
        io.vavr.collection.Map<Integer, io.vavr.collection.List<Integer>> map = io.vavr.collection.List.of(1, 2, 3, 4).groupBy(i -> i % 2);
        Map<Integer, io.vavr.collection.List<Integer>> map2 = io.vavr.collection.List.of(1, 2, 3, 4).groupBy(i -> i % 2).toJavaMap();
    }

    @Test
    public void test7() {
        // (Java, 8)
        Tuple2<String, Integer> java8 = Tuple.of("Java", 8);
        // String s = java8._1;
        // Integer i = java8._2;
        Tuple2<String, Integer> that = java8.map(
                s -> s.substring(2) + "vr",
                i -> i / 8
        );

        String apply = java8.apply(
                (s, i) -> s.substring(2) + "vr " + i / 8
        );

        Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;
    }

    // A lifted function returns None instead of throwing an exception, if the function is invoked with disallowed input values.
    // A lifted function returns Some, if the function is invoked with allowed input values.
    @Test
    public void test8() {
        Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);
        // = None
        Option<Integer> i1 = safeDivide.apply(1, 0);

        // = Some(2)
        Option<Integer> i2 = safeDivide.apply(4, 2);
    }

    @Test
    public void test9(){
        Function2<Integer, Integer, Option<Integer>> sum = Function2.lift(this::sum);
        // = None
        Option<Integer> optionalResult = sum.apply(-1, 2);
        //The lifted function catches the IllegalArgumentException and maps it to None.
    }

    int sum(int first, int second) {
        if (first < 0 || second < 0) {
            throw new IllegalArgumentException("Only positive integers are allowed");
        }
        return first + second;
    }
}
