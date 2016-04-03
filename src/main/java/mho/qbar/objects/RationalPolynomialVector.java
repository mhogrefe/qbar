package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.ordering.comparators.ShortlexComparator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A vector with {@link RationalPolynomial} coordinates. May be zero-dimensional.</p>
 *
 * <p>There is only one instance of {@code ZERO_DIMENSIONAL}, so it may be compared with other
 * {@code RationalPolynomialVector}s using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class RationalPolynomialVector implements
        Comparable<RationalPolynomialVector>,
        Iterable<RationalPolynomial> {
    /**
     * []
     */
    public static final @NotNull RationalPolynomialVector ZERO_DIMENSIONAL =
            new RationalPolynomialVector(Collections.emptyList());

    /**
     * Used by {@link mho.qbar.objects.RationalPolynomialVector#compareTo}
     */
    private static final @NotNull Comparator<Iterable<RationalPolynomial>> RATIONAL_POLYNOMIAL_ITERABLE_COMPARATOR =
            new ShortlexComparator<>();

    /**
     * The vector's coordinates
     */
    private final @NotNull List<RationalPolynomial> coordinates;

    /**
     * Private constructor for {@code RationalPolynomialVector}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>Any {@code RationalPolynomialVector} may be constructed with this constructor.</li>
     * </ul>
     *
     * Length is |{@code coordinates}|
     *
     * @param coordinates the vector's coordinates
     */
    private RationalPolynomialVector(@NotNull List<RationalPolynomial> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Returns an {@code Iterator} over this {@code RationalPolynomialVector}'s coordinates. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>The result is finite and contains no nulls.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @return an {@code Iterator} over this {@code RationalPolynomialVector}'s coordinates
     */
    @Override
    public @NotNull Iterator<RationalPolynomial> iterator() {
        return new NoRemoveIterable<>(coordinates).iterator();
    }

    /**
     * Determines whether the coordinates of {@code this} all have integral coefficients.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} only has coordinates with integral coefficients.
     */
    public boolean onlyHasIntegralCoordinates() {
        return all(RationalPolynomial::onlyHasIntegralCoefficients, coordinates);
    }

    /**
     * Converts {@code this} to a {@code PolynomialVector}.
     *
     * <ul>
     *  <li>{@code this} must only have coordinates with integral coefficients.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @return a {@code PolynomialVector} with the same value as {@code this}
     */
    public @NotNull PolynomialVector toPolynomialVector() {
        if (this == ZERO_DIMENSIONAL) return PolynomialVector.ZERO_DIMENSIONAL;
        return PolynomialVector.of(toList(map(RationalPolynomial::toPolynomial, coordinates)));
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
    public @NotNull RationalPolynomial get(int i) {
        return coordinates.get(i);
    }

    /**
     * Creates a {@code RationalPolynomialVector} from a list of {@code RationalPolynomial}s. Throws an exception if
     * any element is null. Makes a defensive copy of {@code coordinates}.
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param coordinates the vector's coordinates
     * @return the {@code RationalPolynomialVector} with the specified coordinates
     */
    public static @NotNull RationalPolynomialVector of(@NotNull List<RationalPolynomial> coordinates) {
        if (coordinates.isEmpty()) return ZERO_DIMENSIONAL;
        if (any(a -> a == null, coordinates)) {
            throw new NullPointerException();
        }
        return new RationalPolynomialVector(toList(coordinates));
    }

    /**
     * Creates a one-dimensional {@code RationalPolynomialVector} from a single {@code RationalPolynomial} coordinate.
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
    public static @NotNull RationalPolynomialVector of(@NotNull RationalPolynomial a) {
        return new RationalPolynomialVector(Collections.singletonList(a));
    }

    /**
     * Creates a {@code RationalPolynomialVector} from a {@code RationalVector}.
     *
     * <ul>
     *  <li>{@code v} cannot be null.</li>
     *  <li>The result has constant coordinates.</li>
     * </ul>
     *
     * Length is dim({@code v})
     *
     * @param v a {@code Vector}
     * @return the {@code RationalPolynomialVector} with the same coordinates as {@code v}
     */
    public static @NotNull RationalPolynomialVector of(@NotNull RationalVector v) {
        if (v == RationalVector.ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalPolynomialVector(toList(map(RationalPolynomial::of, v)));
    }

    /**
     * Returns the maximum bit length of any coefficient of any coordinate, or 0 if {@code this} is 0-dimensional.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coordinate bit length
     */
    public int maxCoordinateBitLength() {
        if (this == ZERO_DIMENSIONAL) return 0;
        return maximum(map(RationalPolynomial::maxCoefficientBitLength, coordinates));
    }

    /**
     * Returns this {@code RationalPolynomialVector}'s dimension.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return this {@code RationalPolynomialVector}'s dimension
     */
    public int dimension() {
        return coordinates.size();
    }

    /**
     * Determines whether {@code this} is a zero vector.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this}=0
     */
    public boolean isZero() {
        return all(r -> r == RationalPolynomial.ZERO, coordinates);
    }

    /**
     * Creates the zero vector with a given dimension.
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a {@code RationalPolynomialVector} all of whose coordinates are 0.</li>
     * </ul>
     *
     * Length is {@code dimension}
     *
     * @param dimension the zero vector's dimension
     * @return 0<sub>{@code dimension}</sub>
     */
    public static @NotNull RationalPolynomialVector zero(int dimension) {
        if (dimension == 0) {
            return ZERO_DIMENSIONAL;
        } else if (dimension < 0) {
            throw new IllegalArgumentException("dimension cannot be negative. Invalid dimension: " );
        } else {
            return new RationalPolynomialVector(toList(replicate(dimension, RationalPolynomial.ZERO)));
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
     *  <li>The result is a {@code RationalPolynomialVector} with one coordinate equal to 1 and the others equal to
     *  0.</li>
     * </ul>
     *
     * Length is {@code dimension}
     *
     * @param dimension the vector's dimension
     * @param i the index of the vector coordinate which is 1
     * @return a standard basis vector
     */
    public static @NotNull RationalPolynomialVector standard(int dimension, int i) {
        if (dimension < 1) {
            throw new IllegalArgumentException("dimension must be positive. Invalid dimension: " + dimension);
        } else if (i < 0) {
            throw new IllegalArgumentException("i cannot be negative. Invalid i: " + i);
        } else if (i >= dimension) {
            throw new IllegalArgumentException("i must be less than dimension. i: " + i + ", dimension: " + dimension);
        } else {
            return new RationalPolynomialVector(
                    toList(insert(replicate(dimension - 1, RationalPolynomial.ZERO), i, RationalPolynomial.ONE))
            );
        }
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code RationalPolynomialVector} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull RationalPolynomialVector add(@NotNull RationalPolynomialVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalPolynomialVector(toList(zipWith(RationalPolynomial::add, coordinates, that.coordinates)));
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Length is |{@code coordinates}|
     *
     * @return –{@code this}
     */
    public @NotNull RationalPolynomialVector negate() {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalPolynomialVector(toList(map(RationalPolynomial::negate, coordinates)));
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is |{@code this}|
     *
     * @param that the {@code RationalPolynomialVector} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull RationalPolynomialVector subtract(@NotNull RationalPolynomialVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new RationalPolynomialVector(
                toList(zipWith(RationalPolynomial::subtract, coordinates, that.coordinates))
        );
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code RationalPolynomial} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialVector multiply(@NotNull RationalPolynomial that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that == RationalPolynomial.ONE) return this;
        return new RationalPolynomialVector(toList(map(r -> r.multiply(that), coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code Rational} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialVector multiply(@NotNull Rational that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that == Rational.ONE) return this;
        return new RationalPolynomialVector(toList(map(r -> r.multiply(that), coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialVector multiply(@NotNull BigInteger that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that.equals(BigInteger.ONE)) return this;
        return new RationalPolynomialVector(toList(map(r -> r.multiply(that), coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomialVector multiply(int that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that == 1) return this;
        return new RationalPolynomialVector(toList(map(r -> r.multiply(that), coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code Rational} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalPolynomialVector divide(@NotNull Rational that) {
        return multiply(that.invert());
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalPolynomialVector divide(@NotNull BigInteger that) {
        return multiply(Rational.of(BigInteger.ONE, that));
    }

    /**
     * Returns the scalar product of {@code this} and the inverse of {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code int} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalPolynomialVector divide(int that) {
        return multiply(Rational.of(1, that));
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>. Negative
     * {@code bits} corresponds to a right shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull RationalPolynomialVector shiftLeft(int bits) {
        if (this == ZERO_DIMENSIONAL || bits == 0) return this;
        return new RationalPolynomialVector(toList(map(r -> r.shiftLeft(bits), coordinates)));
    }

    /**
     * Returns the right shift of {@code this} by {@code bits}; {@code this}×2<sup>–{@code bits}</sup>. Negative
     * {@code bits} corresponds to a left shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param bits the number of bits to right-shift by
     * @return {@code this}≫{@code bits}
     */
    public @NotNull RationalPolynomialVector shiftRight(int bits) {
        if (this == ZERO_DIMENSIONAL || bits == 0) return this;
        return new RationalPolynomialVector(toList(map(r -> r.shiftRight(bits), coordinates)));
    }

    /**
     * Returns the sum of all the {@code RationalPolynomialVector}s in {@code xs}.
     *
     * <ul>
     *  <li>{@code xs} must be finite and non-empty, and may not contain any nulls. Every
     *  {@code RationalPolynomialVector} in {@code xs} must have the same dimension.</li>
     *  <li>The result may be any {@code RationalPolynomialVector}.</li>
     * </ul>
     *
     * Length is dim(head({@code xs}))
     *
     * @param xs an {@code Iterable} of {@code RationalPolynomialVector}s.
     * @return Σxs
     */
    public static @NotNull RationalPolynomialVector sum(@NotNull Iterable<RationalPolynomialVector> xs) {
        if (isEmpty(xs)) {
            throw new IllegalArgumentException("xs cannot be empty.");
        } else if (!same(map(RationalPolynomialVector::dimension, xs))) {
            throw new ArithmeticException("Every RationalPolynomialVector in xs must have the same dimension." +
                    " Invalid xs: " + xs);
        } else {
            return foldl1(RationalPolynomialVector::add, xs);
        }
    }

    /**
     * Returns the differences between successive {@code RationalPolynomialVector}s in {@code xs}. If {@code xs}
     * contains a single {@code RationalPolynomialVector}, an empty {@code Iterable} is returned. {@code xs} cannot be
     * empty. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} cannot be empty and may not contain any nulls. Every {@code RationalPolynomialVector} in
     *  {@code xs} must have the same dimension.</li>
     *  <li>The result does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code RationalPolynomialVector}s.
     * @return Δxs
     */
    public static @NotNull Iterable<RationalPolynomialVector> delta(@NotNull Iterable<RationalPolynomialVector> xs) {
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
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomialVector} {@code this} is being dotted with
     * @return {@code this}⋅{@code that}
     */
    public @NotNull RationalPolynomial dot(@NotNull RationalPolynomialVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        return RationalPolynomial.sum(zipWith(RationalPolynomial::multiply, coordinates, that.coordinates));
    }

    /**
     * Returns the square of the length (a.k.a. magnitude, a.k.a. norm) of {@code this}. The length of the
     * zero-dimensional vector is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return ‖{@code this}‖²
     */
    public @NotNull RationalPolynomial squaredLength() {
        return RationalPolynomial.sum(map(x -> x.pow(2), coordinates));
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
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
                coordinates.equals(((RationalPolynomialVector) that).coordinates);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
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
     * "equal to", respectively. Shortlex ordering is used; shorter {@code RationalPolynomialVector}s come before
     * longer ones, and two {@code RationalPolynomialVector}s of the same length are compared left-to-right, element by
     * element.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code RationalPolynomialVector} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull RationalPolynomialVector that) {
        return RATIONAL_POLYNOMIAL_ITERABLE_COMPARATOR.compare(coordinates, that.coordinates);
    }

    /**
     * Creates a {@code RationalPolynomialVector} from a {@code String}. A valid {@code String} begins with '[' and
     * ends with ']', and the rest is a possibly-empty list of comma-separated {@code String}s, each of which validly
     * represents a {@code RationalPolynomial} (see {@link RationalPolynomial#toString}).
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code RationalPolynomialVector}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalPolynomialVector}
     * @return the {@code RationalPolynomialVector} represented by {@code s}, or an empty {@code Optional} if {@code s}
     * is invalid
     */
    public static @NotNull Optional<RationalPolynomialVector> read(@NotNull String s) {
        Optional<List<RationalPolynomial>> ops = Readers.readList(RationalPolynomial::read).apply(s);
        if (!ops.isPresent()) return Optional.empty();
        if (ops.get().isEmpty()) return Optional.of(ZERO_DIMENSIONAL);
        return Optional.of(new RationalPolynomialVector(ops.get()));
    }

    /**
     * Finds the first occurrence of a {@code RationalPolynomialVector} in a {@code String}. Returns the
     * {@code RationalPolynomialVector} and the index at which it was found. Returns an empty {@code Optional} if no
     * {@code RationalPolynomialVector} is found. Only {@code String}s which could have been emitted by
     * {@link RationalPolynomialVector#toString} are recognized. The longest possible {@code RationalPolynomialVector}
     * is parsed.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code RationalPolynomialVector} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<RationalPolynomialVector, Integer>> findIn(@NotNull String s) {
        Optional<Pair<List<RationalPolynomial>, Integer>> op =
                Readers.findListIn(RationalPolynomial::read, "*+-/0123456789^x", s);
        if (!op.isPresent()) return Optional.empty();
        Pair<List<RationalPolynomial>, Integer> p = op.get();
        if (p.a.isEmpty()) return Optional.of(new Pair<>(ZERO_DIMENSIONAL, p.b));
        return Optional.of(new Pair<>(new RationalPolynomialVector(p.a), p.b));
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomialVector}.</li>
     *  <li>The result is a {@code String} which begins with '[' and ends with ']', and the rest is a possibly-empty
     *  list of comma-separated {@code String}s, each of which validly represents a {@code RationalPolynomial} (see
     *  {@link RationalPolynomial#toString}).</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return coordinates.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code RationalPolynomialVector} used
     * outside this class.
     */
    public void validate() {
        assertTrue(this, all(r -> r != null, coordinates));
        if (equals(ZERO_DIMENSIONAL)) {
            assertTrue(this, this == ZERO_DIMENSIONAL);
        }
    }
}
