package org.lxy.effective.ch6.item37;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static org.lxy.effective.ch6.item37.Phase.*;

/**
 * If you add too many or too few elements to the array or place an element out of order, you are out of luck:
 * the program will compile, but it will fail at runtime.
 *
 */
public enum Transition {
    MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
    BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
    SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID);

    private final Phase from;
    private final Phase to;

    Transition(Phase from, Phase to) {
        this.from = from;
        this.to = to;
    }

    // Initialize the phase transition map
    private static final Map<Phase, Map<Phase, Transition>>
            m = Stream.of(values()).collect(groupingBy(t -> t.from,
            () -> new EnumMap<>(Phase.class), toMap(t -> t.to, t -> t,
                    (x, y) -> y, () -> new EnumMap<>(Phase.class))));

    public static Transition from(Phase from, Phase to) {
        return m.get(from).get(to);
    }
}


