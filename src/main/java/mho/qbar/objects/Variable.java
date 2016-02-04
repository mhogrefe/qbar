package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A variable in a polynomial. The names of the variables with indices 0, 1, 2, … are "a", "b", "c", …, "aa", "bb",
 * "cc", …, "aaa", "bbb", "ccc", ….</p>
 */
public class Variable implements Comparable<Variable> {
    /**
     * The letters of the English alphabet.
     */
    private static @NotNull String ALPHABET = charsToString(range('a', 'z'));

    /**
     * This {@code Variable}'s nonnegative index.
     */
    public final int index;

    /**
     * Private constructor for {@code Variable}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code index} cannot be zero.</li>
     *  <li>Any {@code Variable} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param index the variable's index
     */
    private Variable(int index) {
        this.index = index;
    }

    /**
     * Creates a {@code Variable} from an index.
     *
     * <ul>
     *  <li>{@code index} cannot be zero.</li>
     *  <li>Any {@code Variable} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param index the variable's index
     */
    public static @NotNull Variable of(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index cannot be zero. Invalid index: " + index);
        }
        return new Variable(index);
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Variable}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Object} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that || that != null && getClass() == that.getClass() && index == ((Variable) that).index;
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Variable}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return index;
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Variable}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull Variable that) {
        return Integer.compare(index, that.index);
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Variable}.</li>
     *  <li>The result is a nonempty {@code String} consisting of some number of repetitions of a single ASCII
     *  letter.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        char letter = (char) ('a' + index % ALPHABET.length());
        int multiplicity = 1 + index / ALPHABET.length();
        return replicate(multiplicity, letter);
    }

    /**
     * Creates a {@code Variable} from a {@code String}. A valid {@code String} consists of some number of repetitions
     * of a single ASCII letter.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code Variable}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Variable}
     * @return the {@code Variable} represented by {@code s}, or an empty {@code Optional} if {@code s} is invalid
     */
    public static @NotNull Optional<Variable> read(@NotNull String s) {
        if (s.isEmpty()) return Optional.empty();
        char letter = head(s);
        if (letter < 'a' || letter > 'z') return Optional.empty();
        if (any(c -> c != letter, s)) return Optional.empty();
        return Optional.of(new Variable(ALPHABET.length() * (s.length() - 1) + letter - 'a'));
    }

    /**
     * Finds the first occurrence of a {@code Variable} in a {@code String}. Returns the {@code Variable} and the index
     * at which it was found. Returns an empty {@code Optional} if no {@code Variable} is found. Only {@code String}s
     * which could have been emitted by {@link Variable#toString} are recognized. The longest possible {@code Variable}
     * is parsed.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code Variable} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<Variable, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(Variable::read, ALPHABET).apply(s);
    }

    /**
     * Ensures that {@code this} is valid. Must return true for any {@code Variable} used outside this class.
     */
    public void validate() {
        assertTrue(this, index >= 0);
    }
}
