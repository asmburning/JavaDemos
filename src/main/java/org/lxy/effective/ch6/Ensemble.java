package org.lxy.effective.ch6;

/**
 * Never derive a value associated with an enum from its ordinal;
 * store it in an instance field instead:
 */
public enum  Ensemble {
    SOLO, DUET, TRIO, QUARTET, QUINTET,
    SEXTET, SEPTET, OCTET, NONET, DECTET;

    /**
     * While this enum works, it is a maintenance nightmare.
     * If the constants are reordered, the numberOfMusicians method will break.
     *
     * There is no standard term for an ensemble consisting of eleven musicians,
     * so you are forced to add a dummy constant for the unused int value (11).
     * At best, this is ugly. If many int values are unused, it’s impractical.
     * @return
     */
    public int numberOfMusicians() { return ordinal() + 1; }
}
