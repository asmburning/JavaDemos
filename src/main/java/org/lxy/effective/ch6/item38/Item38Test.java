package org.lxy.effective.ch6.item38;

import org.junit.Test;

public class Item38Test {

    private static <T extends Enum<T> & Operation> void doTest(
            Class<T> opEnumType, double x, double y) {
        for (Operation op : opEnumType.getEnumConstants())
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));
    }

    @Test
    public void test() {
        doTest(ExtendedOperation.class, 3.2, 2.5);
    }

    @Test
    public void test2() {
        doTest(BasicOperation.class, 3.2, 2.5);
        
    }
}
