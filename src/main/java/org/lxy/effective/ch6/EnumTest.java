package org.lxy.effective.ch6;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Slf4j
public class EnumTest {

    @Test
    public void test() {
        log.info("{}", Operat.PLUS.apply(2.35, 2.65));
    }

    @Test
    public void test1() {
        log.info("{}", PayrollDay.MONDAY.getPayType());
        log.info("{}", PayrollDay.SUNDAY.getPayType());
        log.info("{}", PayrollDay.TUESDAY.pay(60 * 7, 1));
        log.info("{}", PayrollDay.TUESDAY.pay(60 * 9, 1));
        log.info("{}", PayrollDay.SUNDAY.pay(60 * 7, 1));
        log.info("{}", PayrollDay.SUNDAY.pay(60 * 9, 1));
    }

    @Test
    public void test2() {
        log.info("{}", Ensemble.DUET.numberOfMusicians());
    }

    @Test
    public void test3() {
        Text text = new Text();
        text.applyStyles(EnumSet.of(Text.Style.BOLD, Text.Style.ITALIC));
    }

    @Test
    public void test4() {
        Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
                new EnumMap<>(Plant.LifeCycle.class);
        for (Plant.LifeCycle lc : Plant.LifeCycle.values())
            plantsByLifeCycle.put(lc, new HashSet<>());
        for (Plant p : Plant.demoList())
            plantsByLifeCycle.get(p.lifeCycle).add(p);
        System.out.println(plantsByLifeCycle);

        log.info("{}", Plant.demoList().stream()
                .collect(groupingBy(p -> p.lifeCycle,
                        () -> new EnumMap<>(Plant.LifeCycle.class), toSet())));
    }
}
