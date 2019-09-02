package org.lxy.design.pattern.decorator;

public class Soy extends CondimentDecorator {
    /**
     * 保留一个被装饰者的引用
     */
    Beverage beverage;

    public Soy(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription() + ", Soy";
    }

    public double cost() {
        return .5 + beverage.cost();
    }
}