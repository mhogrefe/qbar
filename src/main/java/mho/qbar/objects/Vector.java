package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.ordering.comparators.ShortlexComparator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

/**
 * <p>A vector with {@link java.math.BigInteger} coordinates. May be zero-dimensional.</p>
 *
 * <p>There is only one instance of {@code ZERO_DIMENSIONAL}, so it may be compared with other {@code Vector}s using
 * {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public class Vector implements Comparable<Vector>, Iterable<BigInteger> {
    /**
     * []
     */
    public static final @NotNull Vector ZERO_DIMENSIONAL = new Vector(Collections.emptyList());

    /**
     * Used by {@link mho.qbar.objects.Vector#compareTo}
     */
    private static final @NotNull Comparator<Iterable<BigInteger>> BIG_INTEGER_ITERABLE_COMPARATOR =
            new ShortlexComparator<>();

    /**
     * The vector's coordinates
     */
    private final @NotNull List<BigInteger> coordinates;

    /**
     * Private constructor for {@code Vector}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>Any {@code Vector} may be constructed with this constructor.</li>
     * </ul>
     *
     * Length is |{@code coordinates}|
     *
     * @param coordinates the vector's coordinates
     */
    private Vector(@NotNull List<BigInteger> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Returns an {@code Iterator} over this {@code Vector}'s coordinates. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Vector}.</li>
     *  <li>The result is finite and contains no nulls.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @return an {@code Iterator} over this {@code Vector}'s coordinates
     */
    @Override
    public @NotNull Iterator<BigInteger> iterator() {
        return new NoRemoveIterable<>(coordinates).iterator();
    }

    /**
     * Returns one of {@code this}'s coordinates. 0-indexed.
     *
     * <ul>
     *  <li>{@code this} must be non-empty.</li>
     *  <li>{@code i} cannot be negative.</li>
     *  <li>{@code i} must be less than the dimension of {@code this}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param i the 0-based coordinate index
     * @return the {@code i}th coordinate of {@code this}
     */
    public @NotNull BigInteger get(int i) {
        return coordinates.get(i);
    }

    /**
     * Creates a {@code Vector} from a list of {@code BigInteger}s. Throws an exception if any element is null. Makes a
     * defensive copy of {@code coordinates}.
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param coordinates the vector's coordinates
     * @return the {@code Vector} with the specified coordinates
     */
    public static @NotNull Vector of(@NotNull List<BigInteger> coordinates) {
        if (coordinates.isEmpty()) return ZERO_DIMENSIONAL;
        if (any(a -> a == null, coordinates)) {
            throw new NullPointerException();
        }
        return new Vector(toList(coordinates));
    }

    /**
     * Creates a one-dimensional {@code Vector} from a single {@code BigInteger} coordinate.
     *
     * <ul>
     *  <li>{@code a} must be non-null.</li>
     *  <li>The result is a one-dimensional vector.</li>
     * </ul>
     *
     * Length is 1
     *
     * @param a the vector's coordinate
     * @return [a]
     */
    public static @NotNull Vector of(@NotNull BigInteger a) {
        return new Vector(Collections.singletonList(a));
    }

    /**
     * Returns this {@code Vector}'s dimension
     *
     * <ul>
     *  <li>{@code this} may be any {@code Vector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return this {@code Vector}'s dimension
     */
    public int dimension() {
        return coordinates.size();
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Vector}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Object} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that || that != null && getClass() == that.getClass() &&
                coordinates.equals(((Vector) that).coordinates);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Vector}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. Shortlex ordering is used; shorter {@code Vector}s come before longer ones, and two
     * {@code Vector}s of the same length are compared left-to-right, element by element.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Vector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code Vector} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull Vector that) {
        return BIG_INTEGER_ITERABLE_COMPARATOR.compare(coordinates, that.coordinates);
    }

    /**
     * Creates a {@code Vector} from a {@code String}. A valid {@code String} begins with '[' and ends with ']', and
     * the rest is a possibly-empty list of comma-separated {@code String}s, each of which validly represents a
     * {@code BigInteger}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code Vector}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Vector}
     * @return the {@code Vector} represented by {@code s}, or an empty {@code Optional} if {@code s} is invalid
     */
    public static @NotNull Optional<Vector> read(@NotNull String s) {
        Optional<List<BigInteger>> ois = Readers.readList(Readers::readBigInteger).apply(s);
        if (!ois.isPresent()) return Optional.empty();
        if (ois.get().isEmpty()) return Optional.of(ZERO_DIMENSIONAL);
        return Optional.of(new Vector(ois.get()));
    }

    /**
     * Finds the first occurrence of a {@code Vector} in a {@code String}. Returns the {@code Vector} and the index at
     * which it was found. Returns an empty {@code Optional} if no {@code Vector} is found. Only {@code String}s which
     * could have been emitted by {@link Vector#toString} are recognized. The longest possible {@code Vector} is
     * parsed.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code Vector} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<Vector, Integer>> findIn(@NotNull String s) {
        Optional<Pair<List<BigInteger>, Integer>> op = Readers.findListIn(Readers::readBigInteger, "-0123456789", s);
        if (!op.isPresent()) return Optional.empty();
        Pair<List<BigInteger>, Integer> p = op.get();
        if (p.a.isEmpty()) return Optional.of(new Pair<>(ZERO_DIMENSIONAL, p.b));
        return Optional.of(new Pair<>(new Vector(p.a), p.b));
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Vector}.</li>
     *  <li>The result is a {@code String} which begins with '[' and ends with ']', and the rest is a possibly-empty
     *  list of comma-separated {@code String}s, each of which validly represents a {@code BigInteger}.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return coordinates.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return true for any {@code Vector} used outside this class.
     */
    public void validate() {
        assertTrue(this, all(r -> r != null, coordinates));
        if (equals(ZERO_DIMENSIONAL)) {
            assertTrue(this, this == ZERO_DIMENSIONAL);
        }
    }
}
