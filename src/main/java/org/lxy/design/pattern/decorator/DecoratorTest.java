package org.lxy.design.pattern.decorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class DecoratorTest {

    @Test
    public void test() {
        Beverage beverage = new Espresso();
        log.info(beverage.getDescription() + " $" + beverage.cost());

        Beverage beverage2 = new DarkRoast();
        beverage2 = new Mocha(beverage2);
        beverage2 = new Mocha(beverage2);
        beverage2 = new Whip(beverage2);
        log.info(beverage2.getDescription() + " $" + beverage2.cost());

        Beverage beverage3 = new HouseBlend();
        beverage3 = new Soy(beverage3);
        beverage3 = new Mocha(beverage3);
        beverage3 = new Whip(beverage3);
        log.info(beverage3.getDescription() + " $" + beverage3.cost());
    }
}
