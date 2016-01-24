package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.math.MathUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>An exponent vector, for example x<sup>2</sup>yz. A component of multivariate polynomials.</p>
 *
 * <p>There is only one instance of {@code ONE}, so it may be compared with other {@code ExponentVector}s using
 * {@code ==}.</p>
 *
 * <p>The exponent vectors are represented by a list of non-negative integers, where the last element is nonzero. Each
 * index of the list represents a different variable, and the value at the index is the variable's exponent. The names
 * of the variables 0, 1, 2, … are "a", "b", "c", …, "aa", "bb", "cc", …, "aaa", "bbb", "ccc", ….</p>
 *
 * <p>This class is immutable.</p>
 */
public class ExponentVector implements Comparable<ExponentVector> {
    public static final @NotNull ExponentVector ONE = new ExponentVector(Collections.emptyList());

    private final @NotNull List<Integer> exponents;

    private ExponentVector(@NotNull List<Integer> exponents) {
        this.exponents = exponents;
    }

    public static @NotNull ExponentVector of(@NotNull List<Integer> exponents) {
        if (any(v -> v < 0, exponents)) {
            throw new IllegalArgumentException();
        }
        int actualSize;
        for (actualSize = exponents.size(); actualSize > 0; actualSize--) {
            if (exponents.get(actualSize - 1) != 0) {
                break;
            }
        }
        if (actualSize == 0) return ONE;
        return new ExponentVector(toList(take(actualSize, exponents)));
    }

    public static @NotNull ExponentVector fromTerms(@NotNull List<Pair<Integer, Integer>> terms) {
        if (terms.isEmpty()) return ExponentVector.ONE;
        //noinspection RedundantCast
        if (!increasing((Iterable<Integer>) map(t -> t.a, terms))) {
            throw new IllegalArgumentException();
        }
        List<Integer> exponents = toList(replicate(last(terms).a + 1, 0));
        for (Pair<Integer, Integer> term : terms) {
            exponents.set(term.a, term.b);
        }
        return new ExponentVector(exponents);
    }

    public int degree() {
        return sumInteger(exponents);
    }

    @Override
    public boolean equals(Object that) {
        return this == that ||
                that != null && getClass() == that.getClass() && exponents.equals(((ExponentVector) that).exponents);
    }

    @Override
    public int hashCode() {
        return exponents.hashCode();
    }

    //grevlex
    @Override
    public int compareTo(@NotNull ExponentVector that) {
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

    public static @NotNull Optional<ExponentVector> read(@NotNull String s) {
        if (s.equals("1")) return Optional.of(ONE);
        String[] termStrings = s.split("\\*");
        if (termStrings.length == 0) return Optional.empty();
        List<Pair<Integer, Integer>> terms = new ArrayList<>();
        for (String termString : termStrings) {
            int caretIndex = termString.indexOf('^');
            if (caretIndex == -1) {
                Optional<Integer> variableIndex = MathUtils.stringToVariableIndex(termString);
                if (!variableIndex.isPresent()) return Optional.empty();
                terms.add(new Pair<>(variableIndex.get(), 1));
            } else {
                Optional<Integer> oExponent = Readers.readInteger(termString.substring(caretIndex + 1));
                if (!oExponent.isPresent()) return Optional.empty();
                int exponent = oExponent.get();
                if (exponent < 2) return Optional.empty();
                Optional<Integer> variableIndex = MathUtils.stringToVariableIndex(termString.substring(0, caretIndex));
                if (!variableIndex.isPresent()) return Optional.empty();
                terms.add(new Pair<>(variableIndex.get(), exponent));
            }
        }
        //noinspection RedundantCast
        if (!increasing((Iterable<Integer>) map(t -> t.a, terms))) return Optional.empty();
        return Optional.of(fromTerms(terms));
    }

    public static @NotNull Optional<Pair<ExponentVector, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(ExponentVector::read, "*0123456789^abcdefghijklmnopqrstuvwxyz").apply(s);
    }

    public @NotNull String toString() {
        if (this == ONE) return "1";
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
        if (equals(ONE)) {
            assertTrue(this, this == ONE);
        }
    }
}
