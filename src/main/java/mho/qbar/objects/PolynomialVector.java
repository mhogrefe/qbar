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
 * <p>A vector with {@link Polynomial} coordinates. May be zero-dimensional.</p>
 *
 * <p>There is only one instance of {@code ZERO_DIMENSIONAL}, so it may be compared with other
 * {@code PolynomialVector}s using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public class PolynomialVector implements Comparable<PolynomialVector>, Iterable<Polynomial> {
    /**
     * []
     */
    public static final @NotNull PolynomialVector ZERO_DIMENSIONAL = new PolynomialVector(Collections.emptyList());

    /**
     * Used by {@link mho.qbar.objects.PolynomialVector#compareTo}
     */
    private static final @NotNull Comparator<Iterable<Polynomial>> POLYNOMIAL_ITERABLE_COMPARATOR =
            new ShortlexComparator<>();

    /**
     * The vector's coordinates
     */
    private final @NotNull List<Polynomial> coordinates;

    /**
     * Private constructor for {@code PolynomialVector}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>Any {@code PolynomialVector} may be constructed with this constructor.</li>
     * </ul>
     *
     * Length is |{@code coordinates}|
     *
     * @param coordinates the vector's coordinates
     */
    private PolynomialVector(@NotNull List<Polynomial> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Returns an {@code Iterator} over this {@code PolynomialVector}'s coordinates. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>The result is finite and contains no nulls.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @return an {@code Iterator} over this {@code PolynomialVector}'s coordinates
     */
    @Override
    public @NotNull Iterator<Polynomial> iterator() {
        return new NoRemoveIterable<>(coordinates).iterator();
    }

    /**
     * Converts {@code this} to a {@code RationalPolynomialVector}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>The result is a {@code RationalPolynomialVector} with integral polynomial coordinates.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @return a {@code RationalPolynomialVector} with the same value as {@code this}
     */
    public @NotNull RationalPolynomialVector toRationalPolynomialVector() {
        if (this == ZERO_DIMENSIONAL) return RationalPolynomialVector.ZERO_DIMENSIONAL;
        return RationalPolynomialVector.of(toList(map(Polynomial::toRationalPolynomial, coordinates)));
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
    public @NotNull Polynomial get(int i) {
        return coordinates.get(i);
    }

    /**
     * Creates a {@code PolynomialVector} from a list of {@code Polynomial}s. Throws an exception if any element is
     * null. Makes a defensive copy of {@code coordinates}.
     *
     * <ul>
     *  <li>{@code coordinates} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param coordinates the vector's coordinates
     * @return the {@code PolynomialVector} with the specified coordinates
     */
    public static @NotNull PolynomialVector of(@NotNull List<Polynomial> coordinates) {
        if (coordinates.isEmpty()) return ZERO_DIMENSIONAL;
        if (any(a -> a == null, coordinates)) {
            throw new NullPointerException();
        }
        return new PolynomialVector(toList(coordinates));
    }

    /**
     * Creates a one-dimensional {@code PolynomialVector} from a single {@code Polynomial} coordinate.
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
    public static @NotNull PolynomialVector of(@NotNull Polynomial a) {
        return new PolynomialVector(Collections.singletonList(a));
    }

    /**
     * Returns the maximum bit length of any coefficient of any coordinate, or 0 if {@code this} is 0-dimensional.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coordinate bit length
     */
    public int maxCoordinateBitLength() {
        if (this == ZERO_DIMENSIONAL) return 0;
        //noinspection RedundantCast
        return maximum((Iterable<Integer>) map(Polynomial::maxCoefficientBitLength, coordinates));
    }

    /**
     * Returns this {@code PolynomialVector}'s dimension.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return this {@code PolynomialVector}'s dimension
     */
    public int dimension() {
        return coordinates.size();
    }

    /**
     * Determines whether {@code this} is a zero vector.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this}=0
     */
    public boolean isZero() {
        return all(i -> i == Polynomial.ZERO, coordinates);
    }

    /**
     * Creates the zero vector with a given dimension.
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a {@code PolynomialVector} all of whose coordinates are 0.</li>
     * </ul>
     *
     * Length is {@code dimension}
     *
     * @param dimension the zero vector's dimension
     * @return 0<sub>{@code dimension}</sub>
     */
    public static @NotNull PolynomialVector zero(int dimension) {
        if (dimension == 0) {
            return ZERO_DIMENSIONAL;
        } else if (dimension < 0) {
            throw new IllegalArgumentException("dimension cannot be negative. Invalid dimension: " );
        } else {
            return new PolynomialVector(toList(replicate(dimension, Polynomial.ZERO)));
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
     *  <li>The result is a {@code PolynomialVector} with one coordinate equal to 1 and the others equal to 0.</li>
     * </ul>
     *
     * Length is {@code dimension}
     *
     * @param dimension the vector's dimension
     * @param i the index of the vector coordinate which is 1
     * @return a standard basis vector
     */
    public static @NotNull PolynomialVector standard(int dimension, int i) {
        if (dimension < 1) {
            throw new IllegalArgumentException("dimension must be positive. Invalid dimension: " + dimension);
        } else if (i < 0) {
            throw new IllegalArgumentException("i cannot be negative. Invalid i: " + i);
        } else if (i >= dimension) {
            throw new IllegalArgumentException("i must be less than dimension. i: " + i + ", dimension: " + dimension);
        } else {
            return new PolynomialVector(toList(insert(replicate(dimension - 1, Polynomial.ZERO), i, Polynomial.ONE)));
        }
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code PolynomialVector} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull PolynomialVector add(@NotNull PolynomialVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new PolynomialVector(toList(zipWith(Polynomial::add, coordinates, that.coordinates)));
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Length is |{@code coordinates}|
     *
     * @return –{@code this}
     */
    public @NotNull PolynomialVector negate() {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new PolynomialVector(toList(map(Polynomial::negate, coordinates)));
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is |{@code this}|
     *
     * @param that the {@code PolynomialVector} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull PolynomialVector subtract(@NotNull PolynomialVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        return new PolynomialVector(toList(zipWith(Polynomial::subtract, coordinates, that.coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code PolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code Polynomial} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull PolynomialVector multiply(@NotNull Polynomial that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that == Polynomial.ONE) return this;
        return new PolynomialVector(toList(map(i -> i.multiply(that), coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code PolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull PolynomialVector multiply(@NotNull BigInteger that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that.equals(BigInteger.ONE)) return this;
        return new PolynomialVector(toList(map(i -> i.multiply(that), coordinates)));
    }

    /**
     * Returns the scalar product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code PolynomialVector}.</li>
     *  <li>{@code that} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull PolynomialVector multiply(int that) {
        if (this == ZERO_DIMENSIONAL) return ZERO_DIMENSIONAL;
        if (that == 1) return this;
        return new PolynomialVector(toList(map(i -> i.multiply(that), coordinates)));
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>.
     *
     * <ul>
     *  <li>{@code this} can be any {@code PolynomialVector}.</li>
     *  <li>{@code bits} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is dim({@code this})
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull PolynomialVector shiftLeft(int bits) {
        if (bits < 0) {
            throw new ArithmeticException("bits cannot be negative. Invalid bits: " + bits);
        }
        if (this == ZERO_DIMENSIONAL || bits == 0) return this;
        return new PolynomialVector(toList(map(i -> i.shiftLeft(bits), coordinates)));
    }

    /**
     * Returns the sum of all the {@code PolynomialVector}s in {@code xs}.
     *
     * <ul>
     *  <li>{@code xs} must be finite and non-empty, and may not contain any nulls. Every {@code PolynomialVector} in
     *  {@code xs} must have the same dimension.</li>
     *  <li>The result may be any {@code PolynomialVector}.</li>
     * </ul>
     *
     * Length is dim(head({@code xs}))
     *
     * @param xs an {@code Iterable} of {@code PolynomialVector}s.
     * @return Σxs
     */
    public static @NotNull PolynomialVector sum(@NotNull Iterable<PolynomialVector> xs) {
        if (isEmpty(xs)) {
            throw new IllegalArgumentException("xs cannot be empty.");
        } else if (!same(map(PolynomialVector::dimension, xs))) {
            throw new ArithmeticException("Every PolynomialVector in xs must have the same dimension. Invalid xs: " +
                    xs);
        } else {
            return foldl1(PolynomialVector::add, xs);
        }
    }

    /**
     * Returns the differences between successive {@code PolynomialVector}s in {@code xs}. If {@code xs} contains a
     * single {@code PolynomialVector}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code xs} cannot be empty and may not contain any nulls. Every {@code PolynomialVector} in {@code xs} must
     *  have the same dimension.</li>
     *  <li>The result does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code PolynomialVector}s.
     * @return Δxs
     */
    public static @NotNull Iterable<PolynomialVector> delta(@NotNull Iterable<PolynomialVector> xs) {
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
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} must have the same dimension.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code PolynomialVector} {@code this} is being dotted with
     * @return {@code this}⋅{@code that}
     */
    public @NotNull Polynomial dot(@NotNull PolynomialVector that) {
        if (coordinates.size() != that.coordinates.size()) {
            throw new ArithmeticException("this and that must have the same dimension. this: " + this + ", that: " +
                    that);
        }
        return Polynomial.sum(zipWith(Polynomial::multiply, coordinates, that.coordinates));
    }

    /**
     * Returns the square of the length (a.k.a. magnitude, a.k.a. norm) of {@code this}. The length of the
     * zero-dimensional vector is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return ‖{@code this}‖²
     */
    public @NotNull Polynomial squaredLength() {
        return Polynomial.sum(map(x -> x.pow(2), coordinates));
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
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
                coordinates.equals(((PolynomialVector) that).coordinates);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
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
     * "equal to", respectively. Shortlex ordering is used; shorter {@code PolynomialVector}s come before longer ones,
     * and two {@code PolynomialVector}s of the same length are compared left-to-right, element by element.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code PolynomialVector} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull PolynomialVector that) {
        return POLYNOMIAL_ITERABLE_COMPARATOR.compare(coordinates, that.coordinates);
    }

    /**
     * Creates a {@code PolynomialVector} from a {@code String}. A valid {@code String} begins with '[' and ends with
     * ']', and the rest is a possibly-empty list of comma-separated {@code String}s, each of which validly represents
     * a {@code Polynomial}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code PolynomialVector}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code PolynomialVector}
     * @return the {@code PolynomialVector} represented by {@code s}, or an empty {@code Optional} if {@code s} is
     * invalid
     */
    public static @NotNull Optional<PolynomialVector> read(@NotNull String s) {
        Optional<List<Polynomial>> ops = Readers.readList(Polynomial::read).apply(s);
        if (!ops.isPresent()) return Optional.empty();
        if (ops.get().isEmpty()) return Optional.of(ZERO_DIMENSIONAL);
        return Optional.of(new PolynomialVector(ops.get()));
    }

    /**
     * Finds the first occurrence of a {@code PolynomialVector} in a {@code String}. Returns the
     * {@code PolynomialVector} and the index at which it was found. Returns an empty {@code Optional} if no
     * {@code PolynomialVector} is found. Only {@code String}s which could have been emitted by
     * {@link PolynomialVector#toString} are recognized. The longest possible {@code PolynomialVector} is parsed.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code PolynomialVector} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<PolynomialVector, Integer>> findIn(@NotNull String s) {
        Optional<Pair<List<Polynomial>, Integer>> op = Readers.findListIn(Polynomial::read, "*+-0123456789^x", s);
        if (!op.isPresent()) return Optional.empty();
        Pair<List<Polynomial>, Integer> p = op.get();
        if (p.a.isEmpty()) return Optional.of(new Pair<>(ZERO_DIMENSIONAL, p.b));
        return Optional.of(new Pair<>(new PolynomialVector(p.a), p.b));
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code PolynomialVector}.</li>
     *  <li>The result is a {@code String} which begins with '[' and ends with ']', and the rest is a possibly-empty
     *  list of comma-separated {@code String}s, each of which validly represents a {@code Polynomial} (see
     *  {@link Polynomial#toString}).</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return coordinates.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code PolynomialVector} used outside
     * this class.
     */
    public void validate() {
        assertTrue(this, all(r -> r != null, coordinates));
        if (equals(ZERO_DIMENSIONAL)) {
            assertTrue(this, this == ZERO_DIMENSIONAL);
        }
    }
}
