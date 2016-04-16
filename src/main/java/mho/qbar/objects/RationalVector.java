package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.ordering.Ordering;
import mho.wheels.ordering.comparators.ShortlexComparator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A vector with {@link Rational} coordinates. May be zero-dimensional.</p>
 *
 * <p>There is only one instance of {@code ZERO_DIMENSIONAL}, so it may be compared with other {@code RationalVector}s
 * using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class RationalVector implements Comparable<RationalVector>, Iterable<Rational> {
    /**
     * []
     */
    public static final @NotNull RationalVector ZERO_DIMENSIONAL = new RationalVector(Collections.emptyList());

    /**
     * Used by {@link mho.qbar.objects.RationalVector#compareTo}
     */
    private static final @NotNull Comparator<Iterable<Rational>> RATIONAL_ITERABLE_COMPARATOR =
            new ShortlexComparator<>();

    /**
     * The vector's coordinates
     */
    private final @NotNull List<Rational> coordinates;

    /**
     * Private constructor for {@code RationalVector}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>Any {@code RationalVector} may be constructed with this constructor.</li>
     * </ul>
     *
     * Length is |{@code coordinates}|
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
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result is finite and contains no nulls.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @return an {@code Iterator} over this {@code RationalVector}'s coordinates
     */
    @Override
    public @NotNull Iterator<Rational> iterator() {
        return new NoRemoveIterable<>(coordinates).iterator();
    }

    /**
     * Determines whether the coordinates of {@code this} are all integers.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} only has integral coordinates.
     */
    public boolean onlyHasIntegralCoordinates() {
        return all(Rational::isInteger, coordinates);
    }

    /**
     * Converts {@code this} to a {@code Vector}.
     *
     * <ul>
     *  <li>{@code this} must only have integral coordinates.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @return a {@code Vector} with the same value as {@code this}
     */
    public @NotNull Vector toVector() {
        if (this == ZERO_DIMENSIONAL) return Vector.ZERO_DIMENSIONAL;
        return Vector.of(toList(map(Rational::bigIntegerValueExact, coordinates)));
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
    public @NotNull Rational get(int i) {
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
     * Length is dim({@code this})
     *
     * @param coordinates the vector's coordinates
     * @return the {@code RationalVector} with the specified coordinates
     */
    public static @NotNull RationalVector of(@NotNull List<Rational> coordinates) {
        if (coordinates.isEmpty()) return ZERO_DIMENSIONAL;
        if (any(a -> a == null, coordinates)) {
            throw new NullPointerException();
        }
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
        return new RationalVector(Collections.singletonList(a));
    }

    /**
     * Returns the maximum bit length of any coordinate, or 0 if {@code this} is 0-dimensional.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coordinate bit length
     */
    public int maxCoordinateBitLength() {
        if (this == ZERO_DIMENSIONAL) return 0;
        return maximum(map(Rational::bitLength, coordinates));
    }

    /**
     * Returns this {@code RationalVector}'s dimension.
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
     * Determines whether {@code this} is a zero vector.
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
     * Creates the zero vector with a given dimension.
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a {@code RationalVector} all of whose coordinates are 0.</li>
     * </ul>
     *
     * Length is {@code dimension}
     *
     * @param dimension the zero vector's dimension
     * @return 0<sub>{@code dimension}</sub>
     */
    public static @NotNull RationalVector zero(int dimension) {
        if (dimension == 0) {
            return ZERO_DIMENSIONAL;
        } else if (dimension < 0) {
            throw new IllegalArgumentException("dimension cannot be negative. Invalid dimension: " );
        } else {
            return new RationalVector(toList(replicate(dimension, Rational.ZERO)));
        }
    }

    /**
     * Creates an standard basis vector; that is, a vector with a given dimension, all of whose coordinates are 0,
     * except for a single coordinate which is 1. Identity matrices are made up of standard basis vectors. There is no
     * standard basis vector of dimension 0.
     *
     * <ul>
     *  <li>{@code dimension} must be positive.</li>
     *  <li>{@code i} cannot be negative.</li>
     *  <li>{@code i} must be less than {@code dimension}.</li>
     *  <li>The result is a {@code RationalVector} with one coordinate equal to 1 and the others equal to 0.</li>
     * </ul>
     *
     * Length is {@code dimension}
     *
     * @param dimension the vector's dimension
     * @param i the index of the vector coordinate which is 1
     * @return a standard basis vector
     */
    public static @NotNull RationalVector standard(int dimension, int i) {
        if (dimension < 1) {
            throw new IllegalArgumentException("dimension must be positive. Invalid dimension: " + dimension);
        } else if (i < 0) {
            throw new IllegalArgumentException("i cannot be negative. Invalid i: " + i);
        } else if (i >= dimension) {
            throw new IllegalArgumentException("i must be less than dimension. i: " + i + ", dimension: " + dimension);
        } else {
            return new RationalVector(toList(insert(replicate(dimension - 1, Rational.ZERO), i, Rational.ONE)));
        }
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
     * Length is dim({@code this})
     *
     * @param that the {@code RationalVector} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull RationalVector add(@NotNull RationalVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalVector(toList(zipWith(Rational::add, coordinates, that.coordinates)));
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
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is |{@code this}|
     *
     * @param that the {@code RationalVector} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull RationalVector subtract(@NotNull RationalVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalVector(toList(zipWith(Rational::subtract, coordinates, that.coordinates)));
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
     * Length is dim({@code this})
     *
     * @param that the {@code Rational} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalVector multiply(@NotNull Rational that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that == Rational.ONE) return this;
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
     * Length is dim({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalVector multiply(@NotNull BigInteger that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that.equals(BigInteger.ONE)) return this;
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
     * Length is dim({@code this})
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalVector multiply(int that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that == 1) return this;
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
     * Length is dim({@code this})
     *
     * @param that the {@code Rational} {@code this} is divided by
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
     * Length is dim({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is divided by
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
     * Length is dim({@code this})
     *
     * @param that the {@code int} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalVector divide(int that) {
        return multiply(Rational.of(1, that));
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>. Negative
     * {@code bits} corresponds to a right shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalVector}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull RationalVector shiftLeft(int bits) {
        if (this == ZERO_DIMENSIONAL || bits == 0) return this;
        return new RationalVector(toList(map(r -> r.shiftLeft(bits), coordinates)));
    }

    /**
     * Returns the right shift of {@code this} by {@code bits}; {@code this}×2<sup>–{@code bits}</sup>. Negative
     * {@code bits} corresponds to a left shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalVector}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param bits the number of bits to right-shift by
     * @return {@code this}≫{@code bits}
     */
    public @NotNull RationalVector shiftRight(int bits) {
        if (this == ZERO_DIMENSIONAL || bits == 0) return this;
        return new RationalVector(toList(map(r -> r.shiftRight(bits), coordinates)));
    }

    /**
     * Returns the sum of all the {@code RationalVector}s in {@code xs}.
     *
     * <ul>
     *  <li>{@code xs} must be finite and non-empty, and may not contain any nulls. Every {@code RationalVector} in
     *  {@code xs} must have the same dimension.</li>
     *  <li>The result may be any {@code RationalVector}.</li>
     * </ul>
     *
     * Length is dim(head({@code xs}))
     *
     * @param xs an {@code Iterable} of {@code RationalVector}s.
     * @return Σxs
     */
    public static @NotNull RationalVector sum(@NotNull Iterable<RationalVector> xs) {
        if (isEmpty(xs)) {
            throw new IllegalArgumentException("xs cannot be empty.");
        } else if (!same(map(RationalVector::dimension, xs))) {
            throw new ArithmeticException("Every RationalVector in xs must have the same dimension. Invalid xs: " +
                    xs);
        } else {
            return foldl1(RationalVector::add, xs);
        }
    }

    /**
     * Returns the differences between successive {@code RationalVector}s in {@code xs}. If {@code xs} contains a
     * single {@code RationalVector}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code xs} cannot be empty and may not contain any nulls. Every {@code RationalVector} in {@code xs} must
     *  have the same dimension.</li>
     *  <li>The result does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code RationalVector}s.
     * @return Δxs
     */
    public static @NotNull Iterable<RationalVector> delta(@NotNull Iterable<RationalVector> xs) {
        if (isEmpty(xs)) {
            throw new IllegalArgumentException("xs cannot be empty.");
        } else if (head(xs) == null) {
            throw new NullPointerException();
        }
        return adjacentPairsWith((x, y) -> y.subtract(x), xs);
    }

    /**
     * Returns the dot product of {@code this} and {@code that}. The dot product of the zero-dimensional vector with
     * itself is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} {@code this} is being dotted with
     * @return {@code this}⋅{@code that}
     */
    public @NotNull Rational dot(@NotNull RationalVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        return Rational.sum(zipWith(Rational::multiply, coordinates, that.coordinates));
    }

    /**
     * Determines whether the angle between {@code this} and {@code that} is less than, equal to, or greater than a
     * right angle. Zero vectors are a right angle to any other vector.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalVector} which, along with {@code this}, creates the angle under consideration
     * @return whether the angle between {@code this} and {@code that} is acute ({@code LT}), right ({@code EQ}), or
     * obtuse ({@code GT}).
     */
    public @NotNull Ordering rightAngleCompare(@NotNull RationalVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        List<Rational> products = toList(zipWith(Rational::multiply, coordinates, that.coordinates));
        return Ordering.fromInt(-Rational.sumSign(products));
    }

    /**
     * Returns the square of the length (a.k.a. magnitude, a.k.a. norm) of {@code this}. The actual length may be
     * irrational. The length of the zero-dimensional vector is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return ‖{@code this}‖²
     */
    public @NotNull Rational squaredLength() {
        return Rational.sum(map(x -> x.pow(2), coordinates));
    }

    /**
     * Multiplies {@code this} by some positive constant to yield a {@code RationalVector} with integer coordinates
     * having no common factor. This gives a canonical representation of {@code RationalVector}s considered equivalent
     * under multiplication by positive {@code Rational}s. Another canonical representation is given by
     * {@link mho.qbar.objects.RationalVector#reduce}; unlike this representation, it is invariant under negation.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result is a {@code RationalVector} with integer coordinates whose GCD is 0 or 1.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @return a canonical representation of {@code this}
     */
    public @NotNull RationalVector cancelDenominators() {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalVector(toList(map(Rational::of, Rational.cancelDenominators(coordinates))));
    }

    /**
     * Returns the pivot of {@code this}, or the first nonzero coordinate. Since the pivot may not exist, this method
     * returns an {@code Optional}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result may be empty, or it may contain a nonzero {@code Rational}.</li>
     * </ul>
     *
     * @return {@code this}'s pivot
     */
    public @NotNull Optional<Rational> pivot() {
        return find(r -> r != Rational.ZERO, coordinates);
    }

    /**
     * Checks whether this is reduced, <i>i.e.</i> whether the pivot, if it exists, is 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether this is reduced (see {@link RationalVector#reduce()})
     */
    public boolean isReduced() {
        Optional<Rational> pivot = pivot();
        return !pivot.isPresent() || pivot.get() == Rational.ONE;
    }

    /**
     * Multiplies {@code this} by some nonzero constant to yield a {@code RationalVector} whose pivot, if it exists, is
     * 1. This gives a canonical representation of {@code RationalVector}s considered equivalent under multiplication
     * by nonzero {@code Rational}s. Another canonical representation is given by
     * {@link mho.qbar.objects.RationalVector#cancelDenominators}; unlike this representation, it is not invariant
     * under negation. Reduction is used when row-reducing matrices.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalVector}.</li>
     *  <li>The result is a {@code RationalVector} whose pivot, if it exists, is 1.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @return a canonical representation of {@code this}
     */
    public @NotNull RationalVector reduce() {
        Optional<Rational> pivot = pivot();
        if (!pivot.isPresent() || pivot.get() == Rational.ONE) return this;
        return divide(pivot.get());
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
     * @param that The {@code Object} to be compared with {@code this}
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
    public static @NotNull Optional<RationalVector> readStrict(@NotNull String s) {
        Optional<List<Rational>> ors = Readers.readListStrict(Rational::readStrict).apply(s);
        if (!ors.isPresent()) return Optional.empty();
        if (ors.get().isEmpty()) return Optional.of(ZERO_DIMENSIONAL);
        return Optional.of(new RationalVector(ors.get()));
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

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code RationalVector} used outside
     * this class.
     */
    public void validate() {
        assertTrue(this, all(r -> r != null, coordinates));
        if (equals(ZERO_DIMENSIONAL)) {
            assertTrue(this, this == ZERO_DIMENSIONAL);
        }
    }
}
