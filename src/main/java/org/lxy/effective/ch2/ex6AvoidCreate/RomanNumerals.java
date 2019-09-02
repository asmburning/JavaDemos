package org.lxy.effective.ch2.ex6AvoidCreate;

import java.util.regex.Pattern;


/**
 * While String.matches is the easiest way to
 * check if a string matches a regular expression,
 * it’s not suitable for repeated
 * use in performance-critical situations.
 * Creating a Pattern instance is expensive because it requires
 * compiling the regular expression into a finite state machine.
 *
 */
public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})"
                    + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }

    // Hideously slow! Can you spot the object creation?

    /**
     * The variable sum is declared as a Long instead of a long,
     * which means that the program constructs about 231 unnecessary
     * Long instances (roughly one for each time the long i is added to
     * the Long sum).
     *
     * @return
     */
    private static long sum() {
        Long sum = 0L;
        for (long i = 0; i <= Integer.MAX_VALUE; i++)
            sum += i;
        return sum;
    }
}