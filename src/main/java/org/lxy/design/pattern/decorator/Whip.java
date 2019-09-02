package org.lxy.design.pattern.decorator;

public class Whip extends CondimentDecorator {
    /**
     * 保留一个被装饰者的引用
     */
    Beverage beverage;

    public Whip(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription() + ", Whip";
    }

    public double cost() {
        return .3 + beverage.cost();
    }
}