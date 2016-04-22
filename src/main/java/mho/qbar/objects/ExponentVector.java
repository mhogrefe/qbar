package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.NoRemoveIterator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>An exponent vector, for example x<sup>2</sup>yz. A component of multivariate polynomials.</p>
 *
 * <p>There is only one instance of {@code ONE}, so it may be compared with other {@code ExponentVector}s using
 * {@code ==}.</p>
 *
 * <p>The exponent vectors are represented by a list of non-negative integers, where the last element is nonzero. Each
 * index of the list represents a different variable, and the value at the index is the variable's exponent.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class ExponentVector implements Comparable<ExponentVector> {
    /**
     * 1, the constant {@code ExponentVector}
     */
    public static final @NotNull ExponentVector ONE = new ExponentVector(Collections.emptyList());

    /**
     * The exponent vector's exponents. The value at the ith index is the ith variable's exponent.
     */
    private final @NotNull List<Integer> exponents;

    /**
     * Private constructor for {@code ExponentVector}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coefficients} cannot have any null or negative elements and cannot end in a 0.</li>
     *  <li>Any {@code ExponentVector} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param exponents the exponent vector's exponents for each variable
     */
    private ExponentVector(@NotNull List<Integer> exponents) {
        this.exponents = exponents;
    }

    /**
     * Returns this {@code ExponentVector}s exponents, from 'a' to the highest variable with nonzero exponent. Makes a
     * defensive copy.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>The result contains no negative {@code Integer}s, and its last element, if it exists, is nonzero.</li>
     * </ul>
     *
     * @return the exponents of {@code this}
     */
    public @NotNull List<Integer> getExponents() {
        return toList(exponents);
    }

    /**
     * Returns the exponent of a given variable. If {@code this} does not contain the variable, 0 is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>{@code variable} cannot be null.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @param variable a variable
     * @return the exponent of {@code variable}
     */
    public int exponent(@NotNull Variable variable) {
        return variable.getIndex() >= exponents.size() ? 0 : exponents.get(variable.getIndex());
    }

    /**
     * Returns the size of {@code this}, or one more than the index of the largest variable with a nonzero power in
     * {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @return the size of {@code this}
     */
    public int size() {
        return exponents.size();
    }

    /**
     * Returns the terms of {@code this} in the form of pairs, where the first element is a variable and the second is
     * the variable's exponent.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>The result is finite and does not contain any nulls. The second element of every pair is positive, and the
     *  first elements are unique and are in ascending order.</li>
     * </ul>
     *
     * @return the terms of {@code this}
     */
    public @NotNull Iterable<Pair<Variable, Integer>> terms() {
        return () -> new NoRemoveIterator<Pair<Variable, Integer>>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i != exponents.size();
            }

            @Override
            public Pair<Variable, Integer> next() {
                if (i == exponents.size()) {
                    throw new NoSuchElementException();
                }
                int e;
                do {
                    e = exponents.get(i);
                    i++;
                } while (e == 0);
                return new Pair<>(Variable.of(i - 1), e);
            }
        };
    }

    /**
     * Creates an {@code ExponentVector} from a list of exponents. The value at the ith index is the ith variable's
     * exponent.
     *
     * <ul>
     *  <li>None of the elements in {@code exponents} may be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param exponents the exponents in this {@code ExponentVector}
     * @return the {@code ExponentVector} with the exponents in {@code exponents}
     */
    public static @NotNull ExponentVector of(@NotNull List<Integer> exponents) {
        if (any(v -> v < 0, exponents)) {
            throw new IllegalArgumentException("None of the elements in exponents may be negative. Invalid " +
                    "exponents: " + exponents);
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

    /**
     * Creates an {@code ExponentVector} from a list of pairs, where the first element is a variable and the second is
     * the variable's exponent. The terms do not have to be in any particular order, and the variables can be repeated.
     *
     * <ul>
     *  <li>{@code term}s cannot contain any nulls. The second element of every pair must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return the {@code ExponentVector} containing the terms in {@code terms}
     */
    public static @NotNull ExponentVector fromTerms(@NotNull List<Pair<Variable, Integer>> terms) {
        if (terms.isEmpty()) return ExponentVector.ONE;
        if (any(t -> t.b == null, terms)) {
            throw new NullPointerException();
        }
        if (any(t -> t.b <= 0, terms)) {
            throw new IllegalArgumentException("The second element of every pair in terms must be positive." +
                    " Invalid terms: " + terms);
        }

        //noinspection RedundantCast
        List<Integer> exponents = toList(
                replicate(maximum((Iterable<Variable>) map(p -> p.a, terms)).getIndex() + 1, 0)
        );
        for (Pair<Variable, Integer> term : terms) {
            int i = term.a.getIndex();
            exponents.set(i, exponents.get(i) + term.b);
        }
        return new ExponentVector(exponents);
    }

    /**
     * Returns the degree, or the sum of the exponents of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return deg({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public int degree() {
        return sumInteger(exponents);
    }

    /**
     * Returns the variables used by {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>The result is in ascending order and has no repetitions.</li>
     * </ul>
     *
     * @return {@code this}'s variables
     */
    public @NotNull List<Variable> variables() {
        List<Variable> variables = new ArrayList<>();
        for (int i = 0; i < exponents.size(); i++) {
            if (exponents.get(i) != 0) {
                variables.add(Variable.of(i));
            }
        }
        return variables;
    }

    /**
     * Removes a variable from {@code this}. Equivalently, substitutes the variable with 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>{@code v} may be any {@code Variable}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param v a {@code Variable}
     * @return {@code this} without {@code v}, or {@code this} at {@code v}=1
     */
    public @NotNull ExponentVector removeVariable(@NotNull Variable v) {
        if (this == ONE || exponent(v) == 0) return this;
        List<Integer> removedExponents = toList(exponents);
        removedExponents.set(v.getIndex(), 0);
        return of(removedExponents);
    }

    /**
     * Removes variables from {@code this}. Equivalently, substitutes the variables with 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>{@code vs} cannot contain nulls.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param vs some {@code Variable}s
     * @return {@code this} without {@code vs}, or {@code this} with each v in {@code vs} set to 1
     */
    public @NotNull ExponentVector removeVariables(@NotNull List<Variable> vs) {
        if (any(v -> v == null, vs)) {
            throw new NullPointerException();
        }
        if (this == ONE) return this;
        vs = toList(filter(v -> exponent(v) != 0, vs));
        if (vs.isEmpty()) return this;
        List<Integer> removedExponents = toList(exponents);
        for (Variable v : vs) {
            removedExponents.set(v.getIndex(), 0);
        }
        return of(removedExponents);
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code ExponentVector} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull ExponentVector multiply(@NotNull ExponentVector that) {
        if (this == ONE) return that;
        if (that == ONE) return this;
        return new ExponentVector(toList(zipWithPadded((e, f) -> e + f, 0, 0, exponents, that.exponents)));
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Object} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that ||
                that != null && getClass() == that.getClass() && exponents.equals(((ExponentVector) that).exponents);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return exponents.hashCode();
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. Uses grevlex ordering (see {@link MonomialOrder#GREVLEX}).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull ExponentVector that) {
        return MonomialOrder.GREVLEX.compare(this, that);
    }

    /**
     * Creates an {@code ExponentVector} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link ExponentVector#toString()}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code ExponentVector}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of an {@code ExponentVector}
     * @return the {@code ExponentVector} represented by {@code s}, or an empty {@code Optional} if {@code s} is
     * invalid
     */
    public static @NotNull Optional<ExponentVector> readStrict(@NotNull String s) {
        if (s.equals("1")) return Optional.of(ONE);
        if (s.isEmpty() || last(s) == '*') return Optional.empty();
        String[] termStrings = s.split("\\*");
        if (termStrings.length == 0) return Optional.empty();
        List<Pair<Variable, Integer>> terms = new ArrayList<>();
        for (String termString : termStrings) {
            int caretIndex = termString.indexOf('^');
            if (caretIndex == -1) {
                Optional<Variable> variable = Variable.readStrict(termString);
                if (!variable.isPresent()) return Optional.empty();
                terms.add(new Pair<>(variable.get(), 1));
            } else {
                Optional<Integer> oExponent = Readers.readIntegerStrict(termString.substring(caretIndex + 1));
                if (!oExponent.isPresent()) return Optional.empty();
                int exponent = oExponent.get();
                if (exponent < 2) return Optional.empty();
                Optional<Variable> variable = Variable.readStrict(termString.substring(0, caretIndex));
                if (!variable.isPresent()) return Optional.empty();
                terms.add(new Pair<>(variable.get(), exponent));
            }
        }
        //noinspection RedundantCast
        if (!increasing((Iterable<Variable>) map(t -> t.a, terms))) return Optional.empty();
        return Optional.of(fromTerms(terms));
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExponentVector}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
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
                sb.append(Variable.of(i));
                if (exponent > 1) {
                    sb.append('^').append(exponent);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code ExponentVector} used outside
     * this class.
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
