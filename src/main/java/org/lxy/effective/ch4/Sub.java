package org.lxy.effective.ch4;

import org.junit.Test;

import java.time.Instant;

/**
 * neither clone nor readObject may invoke an overridable method, directly or indirectly
 * 
 * In the case of readObject, the overriding method will run
 * before the subclass’s state has been deserialized.
 * In the case of clone, the overriding method will run
 * before the subclass’s clone method has a chance to fix the clone’s state.
 */

public final class Sub extends Super {
    // Blank final, set by constructor
    private final Instant instant;

   public Sub() {
        instant = Instant.now();
    } // Overriding method invoked by superclass constructor

    @Override
    public void overrideMe() {
        System.out.println(instant);
    }

    /**
     * you might expect this program to print out the instant twice,
     * but it prints out null the first time
     * because overrideMe is invoked by the Super
     * constructor before the Sub constructor
     * has a chance to initialize the instant field.
     */
    @Test
    public void test() {
        Sub sub = new Sub();
        sub.overrideMe();
    }
}
