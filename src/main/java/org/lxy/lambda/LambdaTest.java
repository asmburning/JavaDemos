package org.lxy.lambda;

import com.google.common.base.Suppliers;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
public class LambdaTest {

    @Test
    public void test1() {
        Runnable runnable = () -> log.info("ok");
        new Thread(runnable).start();
    }

    @Test
    public void test2() {
        Supplier<Integer> supplier = () -> Integer.valueOf(6);
        log.info(supplier.get().toString());
    }

    @Test
    public void test3() {
        com.google.common.base.Supplier<Integer> supplier = Suppliers.ofInstance(6);
        log.info(supplier.get().toString());
    }

    @Test
    public void testFunction() {
        Function<String, String> function1 = x -> "function1: " + x;
        Function<String, String> function2 = x -> "function2: " + x;
        Function<String, String> function3 = x -> "function3: " + x;
        log.info("{}", function1.apply("98"));
        log.info("{}", function1.andThen(function2).apply("100"));//先执行function1 然后将其结果作为参数传递到function2中
        log.info("{}", function2.andThen(function1).apply("100"));
        log.info("{}", function2.compose(function3).apply("fun100"));//先执行function3 在执行function2
        log.info("{}", Function.identity());
    }

    @Test
    public void testPredicate() {
        Predicate<String> predicate = s -> s.length() > 5;
        log.info("" + predicate.test("predicate"));
    }

    @Test
    public void testConsumer() {
        List<Integer> list = List.of(2, 3, 8, 5, 6, 4, 7, 1);
        list.forEach(integer -> log.info(integer + ""));
    }

    @Test
    public void testArray() {
        String[] array = Arrays.array("java", "hadoop", "hello");
        List<String> list = List.of("list", "set", "map");
        log.info("{}", list.stream().map(String::toUpperCase).filter(s -> s.contains("A")).count());
        log.info("{}", Stream.of(array).count());
    }
}
