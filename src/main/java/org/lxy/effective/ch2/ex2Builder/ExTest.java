package org.lxy.effective.ch2.ex2Builder;

import static org.lxy.effective.ch2.ex2Builder.NyPizza.Size.SMALL;
import static org.lxy.effective.ch2.ex2Builder.Pizza.Topping.HAM;
import static org.lxy.effective.ch2.ex2Builder.Pizza.Topping.ONION;
import static org.lxy.effective.ch2.ex2Builder.Pizza.Topping.SAUSAGE;

public class ExTest {

    public void test(){
        Bird sparrow = Sparrow.builder()
                .color("red")
                .build();
        NyPizza pizza = new NyPizza.Builder(SMALL)
                .addTopping(SAUSAGE).addTopping(ONION).build();
        CalPizza calPizza = new CalPizza.Builder()
                .addTopping(HAM).sauceInside().build();
    }
}
