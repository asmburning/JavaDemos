package org.lxy.effective.ch6;

public enum Operat {
    PLUS("+") {
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        public double apply(double x, double y) { return x / y; }
    };
    private final String symbol;
    Operat(String symbol) { this.symbol = symbol; }
    @Override public String toString() { return symbol; }
    // every enum should implements this method
    public abstract double apply(double x, double y);
}
