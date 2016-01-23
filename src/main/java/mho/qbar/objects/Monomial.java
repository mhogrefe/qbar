package mho.qbar.objects;

import mho.wheels.math.MathUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

public class Monomial implements Comparable<Monomial> {
    private static final @NotNull Monomial CONSTANT = new Monomial(Collections.emptyList());

    private final @NotNull List<Integer> exponents;

    private Monomial(@NotNull List<Integer> exponents) {
        this.exponents = exponents;
    }

    public static @NotNull Monomial of(@NotNull List<Integer> exponents) {
        if (any(v -> v == null, exponents)) {
            throw new NullPointerException();
        }
        if (any(v -> v < 0, exponents)) {
            throw new IllegalArgumentException();
        }
        int actualSize;
        for (actualSize = exponents.size(); actualSize > 0; actualSize--) {
            if (exponents.get(actualSize - 1) != 0) {
                break;
            }
        }
        if (actualSize == 0) return CONSTANT;
        return new Monomial(toList(exponents));
    }

    public int degree() {
        return sumInteger(exponents);
    }

    @Override
    public boolean equals(Object that) {
        return this == that ||
                that != null && getClass() == that.getClass() && exponents.equals(((Monomial) that).exponents);
    }

    @Override
    public int hashCode() {
        return exponents.hashCode();
    }

    //grevlex
    @Override
    public int compareTo(@NotNull Monomial that) {
        if (this == that) return 0;
        int thisDegree = degree();
        int thatDegree = that.degree();
        if (thisDegree > thatDegree) return 1;
        if (thisDegree < thatDegree) return -1;
        int size = that.exponents.size();
        if (exponents.size() > size) return -1;
        if (exponents.size() < size) return 1;
        for (int i = size - 1; i >= 0; i--) {
            int thisExponent = exponents.get(i);
            int thatExponent = that.exponents.get(i);
            if (thisExponent > thatExponent) return -1;
            if (thisExponent < thatExponent) return 1;
        }
        return 0;
    }

    public @NotNull String toString() {
        if (this == CONSTANT) return "constant";
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < exponents.size(); i++) {
            int exponent = exponents.get(i);
            if (exponent != 0) {
                if (first) {
                    first = false;
                } else {
                    sb.append('*');
                }
                sb.append(MathUtils.variableIndexToString(i));
                if (exponent > 1) {
                    sb.append('^').append(exponent);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return true for any {@code Polynomial} used outside this class.
     */
    public void validate() {
        assertTrue(this, all(v -> v >= 0, exponents));
        if (!exponents.isEmpty()) {
            assertTrue(this, last(exponents) != 0);
        }
        if (equals(CONSTANT)) {
            assertTrue(this, this == CONSTANT);
        }
    }
}
