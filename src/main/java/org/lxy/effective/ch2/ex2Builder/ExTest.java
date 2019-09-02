package org.lxy.effective.ch2.ex2Builder;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;

import static org.lxy.effective.ch2.ex2Builder.NyPizza.Size.SMALL;
import static org.lxy.effective.ch2.ex2Builder.Pizza.Topping.*;

@Slf4j
public class ExTest {

    public void test() {
        NyPizza pizza = new NyPizza.Builder(SMALL)
                .addTopping(SAUSAGE).addTopping(ONION).build();
        CalPizza calPizza = new CalPizza.Builder()
                .addTopping(HAM).sauceInside().build();
    }

    @Test
    public void test2() {
        log.info("{}", "java".compareTo("hello"));
        log.info("{}", new BigDecimal("1.0").compareTo(new BigDecimal("1.00")));
        log.info("hello");
    }
}
