package org.lxy.lambda.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class FilterTest {

    private static final String GREEN = "green";

    public static List<Box> filter(List<Box> inventory,
                                   Predicate<Box> p) {
        List<Box> result = new ArrayList<>();
        for (Box apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    @Test
    public void test() {
        Predicate<Box> isGreenBox = box -> StringUtils.equalsIgnoreCase(box.getColor(), GREEN);

        List<Box> inventory = Arrays.asList(new Box(80, GREEN), new Box(155, GREEN), new Box(120, "red"));

        //filter中的参数更好像是定义了一组规则  按照这个规则  然后predicate调用test函数
        List<Box> greenApples = filter(inventory, isGreenBox);
        log.info("{}",greenApples);

        List<Box> heavyApples = filter(inventory, box -> box.getWeight() > 150);
        log.info("{}",heavyApples);

        List<Box> greenApples2 = filter(inventory, (Box a) -> GREEN.equals(a.getColor()));
        log.info("{}",greenApples2);

        List<Box> heavyApples2 = filter(inventory, (Box a) -> a.getWeight() > 150);
        log.info("{}",heavyApples2);

        List<Box> weirdApples = filter(inventory, (Box a) -> a.getWeight() < 80 || "brown".equals(a.getColor()));
        log.info("{}",weirdApples);
    }
}
