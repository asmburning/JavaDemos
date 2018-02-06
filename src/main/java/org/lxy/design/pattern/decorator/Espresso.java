package org.lxy.design.pattern.decorator;

public class Espresso extends Beverage {

    public Espresso() {
        description = "Espresso";
    }

    public double cost() {
        return .69;
    }
}