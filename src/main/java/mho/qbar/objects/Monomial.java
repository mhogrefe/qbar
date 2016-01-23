package mho.qbar.objects;

import mho.wheels.math.MathUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

public class Monomial implements Comparable<Monomial> {
    private static final @NotNull Monomial CONSTANT = new Monomial(Collections.emptyList());

    private final @NotNull List<Integer> variables;

    private Monomial(@NotNull List<Integer> variables) {
        this.variables = variables;
    }

    public static @NotNull Monomial of(@NotNull List<Integer> variables) {
        if (any(v -> v == null, variables)) {
            throw new NullPointerException();
        }
        if (any(v -> v < 0, variables)) {
            throw new IllegalArgumentException();
        }
        int actualSize;
        for (actualSize = variables.size(); actualSize > 0; actualSize--) {
            if (variables.get(actualSize - 1) != 0) {
                break;
            }
        }
        if (actualSize == 0) return CONSTANT;
        return new Monomial(toList(variables));
    }

    public int degree() {
        return sumInteger(variables);
    }

    @Override
    public boolean equals(Object that) {
        return this == that ||
                that != null && getClass() == that.getClass() && variables.equals(((Monomial) that).variables);

    }

    @Override
    public int hashCode() {
        return variables.hashCode();
    }

    //grevlex
    @Override
    public int compareTo(@NotNull Monomial that) {
        if (this == that) return 0;
        int thisDegree = degree();
        int thatDegree = that.degree();
        if (thisDegree > thatDegree) return 1;
        if (thisDegree < thatDegree) return -1;
        int size = variables.size();
        if (size > that.variables.size()) return -1;
        if (size < that.variables.size()) return 1;
        for (int i = size - 1; i >= 0; i--) {
            int thisVariable = variables.get(i);
            int thatVariable = that.variables.get(i);
            if (thisVariable > thatVariable) return -1;
            if (thisVariable < thatVariable) return 1;
        }
        return 0;
    }

    public @NotNull String toString() {
        if (this == CONSTANT) return "constant";
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < variables.size(); i++) {
            int exponent = variables.get(i);
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
        assertTrue(this, all(v -> v >= 0, variables));
        if (!variables.isEmpty()) {
            assertTrue(this, last(variables) != 0);
        }
        if (equals(CONSTANT)) {
            assertTrue(this, this == CONSTANT);
        }
    }
}
