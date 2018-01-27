package org.lxy.effective.ch2.ex1Factory;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;


public class StaticFactory {

    public void test1() {
        ServiceLoader serviceLoader;
    }

    /**
     * One advantage of static factory methods is that, unlike constructors, they have names.
     * A second advantage of static factory methods is that, unlike constructors,
     *     they are not required to create a new object each time they’re invoked.
     * A third advantage of static factory methods is that, unlike constructors,
     *     they can return an object of any subtype of their return type.
     * A fourth advantage of static factories is that the class of the returned
     *     object can vary from call to call as a function of the input parameters.
     *     The EnumSet class (Item 36) has no public constructors, only static factories.
     *      In the OpenJDK implementation, they return an instance of one of two
     *      subclasses, depending on the size of the underlying enum type:
     *      if it has sixtyfour or fewer elements, as most enum types do, the static factories return a
     *      RegularEnumSet instance, which is backed by a single long; if the enum
     *      type has sixty-five or more elements, the factories return a JumboEnumSet
     *      instance, backed by a long array.
     * A fifth advantage of static factories is that the class of the returned object
     *      need not exist when the class containing the method is written. jdbc jca
     *
     * The main limitation of providing only static factory methods is that
     *      classes without public or protected constructors cannot be subclassed.
     * A second shortcoming of static factory methods is that they are hard for
     *      programmers to find.
     */
    public void test2() throws Exception{
        Boolean.valueOf("true");
        Date d = Date.from( Instant.now());
        Set<Rank> faceCards = EnumSet.of(Rank.JACK, Rank.QUEEN, Rank.KING);
        BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
        StackWalker luke = StackWalker.getInstance(StackWalker.Option.SHOW_HIDDEN_FRAMES);
        Object newArray = Array.newInstance(String.class, 5);
        FileStore fs = Files.getFileStore(Paths.get(""));
        BufferedReader br = Files.newBufferedReader(Paths.get(""));
        List<Rank> litany = Collections.list(Collections.enumeration(faceCards));
    }
}
