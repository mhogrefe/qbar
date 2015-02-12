package mho.qbar.objects;

import mho.wheels.misc.Readers;
import mho.wheels.ordering.comparators.ShortlexComparator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;

/**
 * <p>A vector with {@link Rational} coordinates. May be zero-dimensional.
 *
 * <p>There is only one instance of {@code ZERO_DIMENSIONAL}, so it may be compared with other {@code RationalVector}s
 * using {@code ==}.
 *
 * <p>This class is immutable.
 */
@SuppressWarnings("ConstantConditions")
public class RationalVector implements Comparable<RationalVector>, Iterable<Rational> {
    /**
     * []
     */
    public static final RationalVector ZERO_DIMENSIONAL = new RationalVector(new ArrayList<>());

    /**
     * Used by {@link mho.qbar.objects.RationalVector#compareTo}
     */
    private static Comparator<Iterable<Rational>> RATIONAL_ITERABLE_COMPARATOR = new ShortlexComparator<>();

    /**
     * The vector's coordinates
     */
    private @NotNull List<Rational> coordinates;

    /**
     * Private constructor for {@code RationalVector}; assumes arguments are valid
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>Any {@code RationalVector} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param coordinates the vector's coordinates
     */
    private RationalVector(@NotNull List<Rational> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Returns an {@code Iterator} over this {@code RationalVector}'s coordinates. Does not support removal.
     *
     * <ul>
     *  <li>The result is finite and contains no nulls.</li>
     * </ul>
     *
     * Length is |{@code coordinates}|
     *
     * @return an {@code Iterator} over this {@code RationalVector}'s coordinates
     */
    @Override
    public @NotNull Iterator<Rational> iterator() {
        return new Iterator<Rational>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < coordinates.size();
            }

            @Override
            public Rational next() {
                return coordinates.get(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Returns {@code this}'s x-coordinate.
     *
     * <ul>
     *  <li>{@code this} must be non-empty.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return the x-coordinate of {@code this}
     */
    public @NotNull Rational x() {
        return coordinates.get(0);
    }

    /**
     * Returns {@code this}'s y-coordinate.
     *
     * <ul>
     *  <li>{@code this} must have dimension at least 2.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return the y-coordinate of {@code this}
     */
    public @NotNull Rational y() {
        return coordinates.get(1);
    }

    /**
     * Returns {@code this}'s z-coordinate.
     *
     * <ul>
     *  <li>{@code this} must have dimension at least 3.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return the z-coordinate of {@code this}
     */
    public @NotNull Rational z() {
        return coordinates.get(2);
    }

    /**
     * Returns {@code this}'s w-coordinate (the coordinate of the 4th dimension).
     *
     * <ul>
     *  <li>{@code this} must have dimension at least 4.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return the w-coordinate of {@code this}
     */
    public @NotNull Rational w() {
        return coordinates.get(3);
    }

    /**
     * Returns one of {@code this}'s coordinates. 0-indexed.
     *
     * <ul>
     *  <li>{@code this} must be non-empty.</li>
     *  <li>{@code i} must be non-negative.</li>
     *  <li>{@code i} must be less than the dimension of {@code this}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param i the 0-based coordinate index
     * @return the {@code i}th coordinate of {@code this}
     */
    public @NotNull Rational x(int i) {
        return coordinates.get(i);
    }

    /**
     * Creates a {@code RationalVector} from a list of {@code Rational}s. Throws an exception if any element is null.
     * Makes a defensive copy of {@code coordinates}.
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is |{@code coordinates}|
     *
     * @param coordinates the vector's coordinates
     * @return the {@code RationalVector} with the specified coordinates
     */
    public static @NotNull RationalVector of(@NotNull List<Rational> coordinates) {
        if (coordinates.isEmpty()) return ZERO_DIMENSIONAL;
        if (any(a -> a == null, coordinates))
            throw new NullPointerException();
        return new RationalVector(toList(coordinates));
    }

    /**
     * Creates a one-dimensional {@code RationalVector} from a single {@code Rational} coordinate.
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
    public static @NotNull RationalVector of(@NotNull Rational a) {
        return new RationalVector(Arrays.asList(a));
    }

    /**
     * Returns this {@code RationalVector}'s dimension
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return this {@code RationalVector}'s dimension
     */
    public int dimension() {
        return coordinates.size();
    }

    /**
     * Creates the zero vector with a given dimension.
     *
     * <ul>
     *  <li>{@code dimension} must be non-negative.</li>
     *  <li>The result is a {@code RationalVector} all of whose coordinates are 0.</li>
     * </ul>
     *
     * Length is {@code dimension}
     *
     * @param dimension the zero vector's dimension
     * @return 0
     */
    public static @NotNull RationalVector zero(int dimension) {
        if (dimension == 0) return ZERO_DIMENSIONAL;
        if (dimension < 0)
            throw new IllegalArgumentException("dimension must be non-negative");
        return new RationalVector(toList(replicate(dimension, Rational.ZERO)));
    }

    /**
     * Creates an identity vector; that is, a vector with a given dimension, all of whose coordinates are 0, except for
     * a single coordinate which is 1. Identity matrices are made up of identity vectors. There is no identity vector
     * of dimension 0.
     *
     * <ul>
     *  <li>{@code dimension} must be positive.</li>
     *  <li>{@code i} must be non-negative.</li>
     *  <li>{@code i} must be less than {@code dimension}.</li>
     *  <li>The result is a {@code RationalVector} with one coordinate equal to 1 and the others equal to 0.</li>
     * </ul>
     *
     * Length is {@code dimension}
     *
     * @param dimension the vector's dimension
     * @param i the index of the vector coordinate which is 1
     * @return an identity vector
     */
    public static @NotNull RationalVector identity(int dimension, int i) {
        if (dimension < 1)
            throw new IllegalArgumentException("dimension must be positive");
        if (i < 0)
            throw new IllegalArgumentException("i must be non-negative");
        if (i >= dimension)
            throw new IllegalArgumentException("i must be less than dimension");
        return new RationalVector(toList(insert(replicate(dimension - 1, Rational.ZERO), i, Rational.ONE)));
    }

    /**
     * Determines whether {@code this} is a zero vector
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this}=0
     */
    public boolean isZero() {
        return all(r -> r == Rational.ZERO, coordinates);
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Length is |{@code coordinates}|
     *
     * @return –{@code this}
     */
    public @NotNull RationalVector negate() {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalVector(toList(map(Rational::negate, coordinates)));
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull RationalVector add(@NotNull RationalVector that) {
        if (coordinates.size() != that.coordinates.size())
            throw new ArithmeticException("vectors must have same dimension");
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalVector(toList(zipWith(p -> p.a.add(p.b), coordinates, that.coordinates)));
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull RationalVector subtract(@NotNull RationalVector that) {
        return add(that.negate());
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalVector multiply(@NotNull Rational that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalVector(toList(map(r -> r.multiply(that), coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalVector}.</li>
     *  <li>{@code that} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalVector multiply(@NotNull BigInteger that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalVector(toList(map(r -> r.multiply(that), coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalVector multiply(int that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalVector(toList(map(r -> r.multiply(that), coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} {@code this} is multiplied by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalVector divide(@NotNull Rational that) {
        return multiply(that.invert());
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} {@code this} is multiplied by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalVector divide(@NotNull BigInteger that) {
        return multiply(Rational.of(BigInteger.ONE, that));
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} {@code this} is multiplied by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalVector divide(int that) {
        return multiply(Rational.of(1, that));
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code RationalVector} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that || that != null && getClass() == that.getClass() &&
                coordinates.equals(((RationalVector) that).coordinates);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
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
     * "equal to", respectively. Shortlex ordering is used; shorter {@code RationalVector}s come before longer ones,
     * and two {@code RationalVector}s of the same length are compared left-to-right, element by element.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code RationalVector} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull RationalVector that) {
        return RATIONAL_ITERABLE_COMPARATOR.compare(coordinates, that.coordinates);
    }

    /**
     * Creates a {@code RationalVector} from a {@code String}. A valid {@code String} begins with '[' and ends with
     * ']', and the rest is a possibly-empty list of comma-separated {@code String}s, each of which validly represents
     * a {@code Rational} (see {@link Rational#toString}).
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code RationalVector}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalVector}
     * @return the {@code RationalVector} represented by {@code s}, or an empty {@code Optional} if {@code s} is
     * invalid
     */
    public static @NotNull Optional<RationalVector> read(@NotNull String s) {
        Optional<List<Rational>> ors = Readers.readList(Rational::findIn, s);
        if (!ors.isPresent()) return Optional.empty();
        if (ors.get().isEmpty()) return Optional.of(ZERO_DIMENSIONAL);
        return Optional.of(new RationalVector(ors.get()));
    }

    /**
     * Finds the first occurrence of a {@code RationalVector} in a {@code String}. Returns the {@code RationalVector}
     * and the index at which it was found. Returns an empty {@code Optional} if no {@code RationalVector} is found.
     * Only {@code String}s which could have been emitted by {@link RationalVector#toString} are recognized. The
     * longest possible {@code RationalVector} is parsed.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code RationalVector} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<RationalVector, Integer>> findIn(@NotNull String s) {
        Optional<Pair<List<Rational>, Integer>> op = Readers.findListIn(Rational::findIn, s);
        if (!op.isPresent()) return Optional.empty();
        Pair<List<Rational>, Integer> p = op.get();
        if (p.a.isEmpty()) return Optional.of(new Pair<>(ZERO_DIMENSIONAL, p.b));
        return Optional.of(new Pair<>(new RationalVector(p.a), p.b));
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result is a {@code String} which begins with '[' and ends with ']', and the rest is a possibly-empty
     *  list of comma-separated {@code String}s, each of which validly represents a {@code Rational} (see
     *  {@link Rational#toString}).</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return coordinates.toString();
    }
}
