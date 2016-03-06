package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.comparators.ShortlexComparator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A univariate polynomial in x with {@link mho.qbar.objects.Rational} coefficients.</p>
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code RationalPolynomial}s using {@code ==}. This is not true for {@code X}.</p>
 *
 * <p>This class uses a little-endian dense representation; in other words, the coefficient of x<sup>i</sup> is located
 * at the ith position of the coefficient list. The list contains no trailing zeros. Zero is represented by the empty
 * list.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class RationalPolynomial implements
        Comparable<RationalPolynomial>,
        Function<Rational, Rational>,
        Iterable<Rational> {
    /**
     * 0
     */
    public static final @NotNull RationalPolynomial ZERO = new RationalPolynomial(Collections.emptyList());

    /**
     * 1
     */
    public static final @NotNull RationalPolynomial ONE = new RationalPolynomial(
            Collections.singletonList(Rational.ONE)
    );

    /**
     * x
     */
    public static final @NotNull RationalPolynomial X = new RationalPolynomial(
            Arrays.asList(Rational.ZERO, Rational.ONE)
    );

    /**
     * Used by {@link mho.qbar.objects.RationalPolynomial#compareTo}
     */
    private static final Comparator<Iterable<Rational>> RATIONAL_ITERABLE_COMPARATOR = new ShortlexComparator<>();

    /**
     * A {@code Comparator} that compares two {@code RationalPolynomial}s by their degrees, then lexicographically by
     * their coefficients.
     */
    private static final @NotNull Comparator<RationalPolynomial> DEGREE_COEFFICIENT_COMPARATOR = (p, q) -> {
        int c = Integer.compare(p.degree(), q.degree());
        if (c != 0) return c;
        return RATIONAL_ITERABLE_COMPARATOR.compare(p.coefficients, q.coefficients);
    };

    /**
     * The polynomial's coefficients. The coefficient of x<sup>i</sup> is at the ith position.
     */
    private final @NotNull List<Rational> coefficients;

    /**
     * Private constructor for {@code Polynomial}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coefficients} cannot have any null elements and cannot end in a 0.</li>
     *  <li>Any {@code RationalPolynomial} may be constructed with this constructor.</li>
     * </ul>
     *
     * Length is |{@code coefficients}|
     *
     * @param coefficients the polynomial's coefficients
     */
    private RationalPolynomial(@NotNull List<Rational> coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Returns an {@code Iterator} over this {@code RationalPolynomial}'s coefficients, from lowest-degree to
     * highest-degree. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is finite, contains no nulls, and does not end with 0.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return an {@code Iterator} over this {@code RationalPolynomial}'s coefficients
     */
    @Override
    public @NotNull Iterator<Rational> iterator() {
        return new NoRemoveIterable<>(coefficients).iterator();
    }

    /**
     * Evaluates {@code this} at {@code x} using Horner's method.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the argument
     * @return {@code this}({@code x})
     */
    @Override
    public @NotNull Rational apply(@NotNull Rational x) {
        return foldr((c, y) -> y.multiply(x).add(c), Rational.ZERO, coefficients);
    }

    /**
     * Determines whether the coefficients of {@code this} are all integers.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} has integral coefficients.
     */
    public boolean onlyHasIntegralCoefficients() {
        return all(Rational::isInteger, coefficients);
    }

    /**
     * Converts {@code this} to a {@code Polynomial}.
     *
     * <ul>
     *  <li>{@code this} must only have integral coefficients.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return a {@code Polynomial} with the same value as {@code this}
     */
    public @NotNull Polynomial toPolynomial() {
        if (this == ZERO) return Polynomial.ZERO;
        if (this == ONE) return Polynomial.ONE;
        return Polynomial.of(toList(map(Rational::bigIntegerValueExact, coefficients)));
    }

    /**
     * Gets the coefficient of this {@code RationalPolynomial}'s x<sup>{@code i}</sup> term. If {@code i} is greater
     * than this {@code RationalPolynomial}'s degree, 0 is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code i} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param i the power that the coefficient belongs to
     * @return the coefficient of x<sup>{@code i}</sup>
     */
    public @NotNull Rational coefficient(int i) {
        return i < coefficients.size() ? coefficients.get(i) : Rational.ZERO;
    }

    /**
     * Creates a {@code RationalPolynomial} from a list of {@code Rational} coefficients. Throws an exception if any
     * coefficient is null. Makes a defensive copy of {@code coefficients}. Throws out any trailing zeros.
     *
     * <ul>
     *  <li>{@code coefficients} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is at most |{@code coefficients}|
     *
     * @param coefficients the polynomial's coefficients, from least to most significant
     * @return the {@code RationalPolynomial} with the specified coefficients
     */
    public static @NotNull RationalPolynomial of(@NotNull List<Rational> coefficients) {
        if (any(i -> i == null, coefficients)) {
            throw new NullPointerException();
        }
        int actualSize;
        for (actualSize = coefficients.size(); actualSize > 0; actualSize--) {
            if (coefficients.get(actualSize - 1) != Rational.ZERO) {
                break;
            }
        }
        if (actualSize == 0) return ZERO;
        if (actualSize == 1 && coefficients.get(0) == Rational.ONE) return ONE;
        return new RationalPolynomial(toList(take(actualSize, coefficients)));
    }

    /**
     * Creates a constant (possibly zero) {@code RationalPolynomial}.
     *
     * <ul>
     *  <li>{@code c} cannot be null.</li>
     *  <li>The result has degree 0 or –1.</li>
     * </ul>
     *
     * Length is 0 if c is 0, or 1 otherwise
     *
     * @param c the constant
     * @return a constant {@code RationalPolynomial} equal to {@code c}
     */
    public static @NotNull RationalPolynomial of(@NotNull Rational c) {
        if (c == Rational.ZERO) return ZERO;
        if (c == Rational.ONE) return ONE;
        return new RationalPolynomial(Collections.singletonList(c));
    }

    /**
     * Creates either a monomial or 0, i.e. a constant times a power of x.
     *
     * <ul>
     *  <li>{@code c} cannot be null.</li>
     *  <li>{@code p} cannot be negative.</li>
     *  <li>The result has 0 or 1 nonzero terms.</li>
     * </ul>
     *
     * Length is 0 if {@code c} is 0, {@code c}+1 otherwise
     *
     * @param c the monomial's coefficient
     * @param p the monomial's degree if {@code c} is nonzero
     * @return {@code c}x<sup>p</sup>
     */
    public static @NotNull RationalPolynomial of(@NotNull Rational c, int p) {
        if (p < 0) {
            throw new IllegalArgumentException("p cannot be negative. Invalid p: " + p);
        }
        if (c == Rational.ZERO) return ZERO;
        if (p == 0 && c == Rational.ONE) return ONE;
        return new RationalPolynomial(toList(concat(replicate(p, Rational.ZERO), Collections.singletonList(c))));
    }

    /**
     * Returns the maximum bit length of any coefficient, or 0 if {@code this} is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coefficient bit length
     */
    public int maxCoefficientBitLength() {
        if (this == ZERO) return 0;
        //noinspection RedundantCast
        return maximum((Iterable<Integer>) map(Rational::bitLength, coefficients));
    }

    /**
     * Returns this {@code RationalPolynomial}'s degree. We consider 0 to have degree –1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is at least –1.</li>
     * </ul>
     *
     * @return this {@code RationalPolynomial}'s degree
     */
    public int degree() {
        return coefficients.size() - 1;
    }

    /**
     * Returns the leading coefficient of {@code this}, or an empty {@code Optional} is {@code this} is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result may be empty, or it may contain a nonzero {@code Rational}.</li>
     * </ul>
     *
     * @return the leading coefficient
     */
    public @NotNull Optional<Rational> leading() {
        return this == ZERO ? Optional.<Rational>empty() : Optional.of(last(coefficients));
    }

    /**
     * Multiplies {@code this} by a power of x.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code p} cannot be negative.</li>
     * </ul>
     *
     * Length is deg({@code this})+{@code p}+1
     *
     * @param p the power of x that {@code this} is multiplied by
     * @return {@code this}×x<sup>{@code p}</sup>
     */
    public @NotNull RationalPolynomial multiplyByPowerOfX(int p) {
        if (p < 0) {
            throw new ArithmeticException("p cannot be negative. Invalid p: " + p);
        }
        if (this == ZERO || p == 0) return this;
        return new RationalPolynomial(toList(concat(replicate(p, Rational.ZERO), coefficients)));
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is up to max(deg({@code this}), deg({@code that}))+1
     *
     * @param that the {@code Polynomial} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull RationalPolynomial add(@NotNull RationalPolynomial that) {
        if (this == ZERO) return that;
        if (that == ZERO) return this;
        return of(toList(zipWithPadded(Rational::add, Rational.ZERO, Rational.ZERO, coefficients, that.coefficients)));
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return –{@code this}
     */
    public @NotNull RationalPolynomial negate() {
        if (this == ZERO) return ZERO;
        if (coefficients.size() == 1 && coefficients.get(0).equals(Rational.NEGATIVE_ONE)) return ONE;
        return new RationalPolynomial(toList(map(Rational::negate, coefficients)));
    }

    /**
     * Returns the absolute value of {@code this}. In other words, if the leading coefficient of {@code this} is
     * negative, {@code this} is negated.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is either 0 or a {@code RationalPolynomial} with a positive leading coefficient.</li>
     * </ul>
     *
     * @return |{@code this}|
     */
    public @NotNull RationalPolynomial abs() {
        if (this == ZERO || this == ONE) return this;
        return leading().get().signum() == 1 ? this : negate();
    }

    /**
     * Returns the sign of {@code this}; 0 if {@code this} is 0, or the sign of the leading coefficient.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @return sgn(p(∞))
     */
    @SuppressWarnings("JavaDoc")
    public int signum() {
        return this == ZERO ? 0 : leading().get().signum();
    }

    /**
     * Returns the sign of {@code this} when evaluated at {@code x}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x where {@code this} is evaluated
     * @return sgn({@code this}(x))
     */
    @SuppressWarnings("JavaDoc")
    public int signum(@NotNull Rational x) {
        if (this == ZERO) return 0;
        Rational partialResult = foldr((c, y) -> y.multiply(x).add(c), Rational.ZERO, tail(coefficients)).multiply(x);
        return Integer.signum(partialResult.compareTo(head(coefficients).negate()));
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is up to max(deg({@code this}), deg({@code that}))+1
     *
     * @param that the {@code RationalPolynomial} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull RationalPolynomial subtract(@NotNull RationalPolynomial that) {
        if (this == ZERO) return that.negate();
        if (that == ZERO) return this;
        if (this == that) return ZERO;
        return of(
                toList(
                        zipWithPadded(
                                Rational::subtract,
                                Rational.ZERO,
                                Rational.ZERO,
                                coefficients,
                                that.coefficients
                        )
                )
        );
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if either {@code this} or {@code that} is 0, or deg({@code this})+deg({@code that})+1 otherwise
     *
     * @param that the {@code RationalPolynomial} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomial multiply(@NotNull RationalPolynomial that) {
        if (this == ZERO || that == ZERO) return ZERO;
        if (this == ONE) return that;
        if (that == ONE) return this;
        List<Rational> productCoefficients = toList(
                replicate(coefficients.size() + that.coefficients.size() - 1, Rational.ZERO)
        );
        for (int i = 0; i < coefficients.size(); i++) {
            Rational a = coefficients.get(i);
            if (a == Rational.ZERO) continue;
            for (int j = 0; j < that.coefficients.size(); j++) {
                Rational b = that.coefficients.get(j);
                if (b == Rational.ZERO) continue;
                int index = i + j;
                productCoefficients.set(index, productCoefficients.get(index).add(a.multiply(b)));
            }
        }
        if (productCoefficients.size() == 1 && productCoefficients.get(0) == Rational.ONE) return ONE;
        return new RationalPolynomial(productCoefficients);
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if either {@code this} or {@code that} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code Rational} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomial multiply(@NotNull Rational that) {
        if (this == ZERO || that == Rational.ZERO) return ZERO;
        if (this == ONE) return of(that);
        if (that == Rational.ONE) return this;
        List<Rational> productCoefficients = toList(map(c -> c.multiply(that), coefficients));
        if (productCoefficients.size() == 1 && productCoefficients.get(0) == Rational.ONE) return ONE;
        return new RationalPolynomial(productCoefficients);
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if either {@code this} or {@code that} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomial multiply(@NotNull BigInteger that) {
        if (this == ZERO || that.equals(BigInteger.ZERO)) return ZERO;
        if (this == ONE) return of(Rational.of(that));
        if (that.equals(BigInteger.ONE)) return this;
        List<Rational> productCoefficients = toList(map(c -> c.multiply(that), coefficients));
        if (productCoefficients.size() == 1 && productCoefficients.get(0) == Rational.ONE) return ONE;
        return new RationalPolynomial(productCoefficients);
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if either {@code this} or {@code that} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalPolynomial multiply(int that) {
        if (this == ZERO || that == 0) return ZERO;
        if (this == ONE) return of(Rational.of(that));
        if (that == 1) return this;
        List<Rational> productCoefficients = toList(map(c -> c.multiply(that), coefficients));
        if (productCoefficients.size() == 1 && productCoefficients.get(0) == Rational.ONE) return ONE;
        return new RationalPolynomial(productCoefficients);
    }

    /**
     * Returns the quotient of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if {@code this} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code Rational} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalPolynomial divide(@NotNull Rational that) {
        return multiply(that.invert());
    }

    /**
     * Returns the quotient of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if {@code this} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code BigInteger} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalPolynomial divide(@NotNull BigInteger that) {
        return multiply(Rational.of(BigInteger.ONE, that));
    }

    /**
     * Returns the quotient of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if {@code this} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code int} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull RationalPolynomial divide(int that) {
        return multiply(Rational.of(1, that));
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>. Negative
     * {@code bits} corresponds to a right shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomial}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull RationalPolynomial shiftLeft(int bits) {
        if (this == ZERO || bits == 0) return this;
        List<Rational> shiftedCoefficients = toList(map(r -> r.shiftLeft(bits), coefficients));
        if (shiftedCoefficients.size() == 1 && shiftedCoefficients.get(0) == Rational.ONE) return ONE;
        return new RationalPolynomial(shiftedCoefficients);
    }

    /**
     * Returns the right shift of {@code this} by {@code bits}; {@code this}×2<sup>–{@code bits}</sup>. Negative
     * {@code bits} corresponds to a left shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalPolynomial}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @param bits the number of bits to right-shift by
     * @return {@code this}≫{@code bits}
     */
    public @NotNull RationalPolynomial shiftRight(int bits) {
        if (this == ZERO || bits == 0) return this;
        List<Rational> shiftedCoefficients = toList(map(r -> r.shiftRight(bits), coefficients));
        if (shiftedCoefficients.size() == 1 && shiftedCoefficients.get(0) == Rational.ONE) return ONE;
        return new RationalPolynomial(shiftedCoefficients);
    }

    /**
     * Returns the sum of all the {@code RationalPolynomial}s in {@code xs}. If {@code xs} is empty, 0 is returned.
     *
     * <ul>
     *  <li>{@code xs} must be finite and may not contain any nulls.</li>
     *  <li>The result may be any {@code RationalPolynomial}.</li>
     * </ul>
     *
     * Length is at most max({deg(p)|p∈{@code xs}})+1
     *
     * @param xs an {@code Iterable} of {@code RationalPolynomial}s.
     * @return Σxs
     */
    public static @NotNull RationalPolynomial sum(@NotNull Iterable<RationalPolynomial> xs) {
        return of(toList(map(Rational::sum, transposePadded(Rational.ZERO, map(p -> p, xs)))));
    }

    /**
     * Returns the product of all the {@code RationalPolynomial}s in {@code xs}. If {@code xs} is empty, 1 is returned.
     *
     * <ul>
     *  <li>{@code xs} must be finite and may not contain any nulls.</li>
     *  <li>The result may be any {@code RationalPolynomial}.</li>
     * </ul>
     *
     * Length is at most sum({deg(p)|p∈{@code xs}})+1
     *
     * @param xs an {@code Iterable} of {@code RationalPolynomial}s.
     * @return Πxs
     */
    public static @NotNull RationalPolynomial product(@NotNull Iterable<RationalPolynomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(RationalPolynomial::multiply, ONE, sort(DEGREE_COEFFICIENT_COMPARATOR, xs));
    }

    /**
     * Returns the differences between successive {@code RationalPolynomial}s in {@code xs}. If {@code xs} contains a
     * single {@code RationalPolynomial}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code xs} must not be empty and may not contain any nulls.</li>
     *  <li>The result does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code RationalPolynomial}s.
     * @return Δxs
     */
    public static @NotNull Iterable<RationalPolynomial> delta(@NotNull Iterable<RationalPolynomial> xs) {
        if (isEmpty(xs)) {
            throw new IllegalArgumentException("xs must not be empty.");
        }
        if (head(xs) == null) {
            throw new NullPointerException();
        }
        return adjacentPairsWith((x, y) -> y.subtract(x), xs);
    }

    /**
     * Returns {@code this} raised to the power of {@code p}. 0<sup>0</sup> yields 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code p} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 1 if p=0, deg({@code this})×p+1 otherwise
     *
     * @param p the power that {@code this} is raised to
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull RationalPolynomial pow(int p) {
        if (p < 0) {
            throw new ArithmeticException("p cannot be negative. Invalid p: " + p);
        }
        if (p == 0) return ONE;
        if (p == 1) return this;
        RationalPolynomial result = ONE;
        RationalPolynomial powerPower = null; // p^2^i
        for (boolean bit : IntegerUtils.bits(p)) {
            powerPower = powerPower == null ? this : powerPower.multiply(powerPower);
            if (bit) result = result.multiply(powerPower);
        }
        return result;
    }

    /**
     * Substitutes x with {@code that} in {@code this}; if {@code this} is p and {@code that} is q, returns p(q).
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 1 if {@code this}=0 or {@code that}=0, deg({@code this})×deg({@code that})+1 otherwise
     *
     * @param that the {@code RationalPolynomial} substituted into {@code this}
     * @return {@code this}∘{@code that}
     */
    public @NotNull RationalPolynomial substitute(@NotNull RationalPolynomial that) {
        return foldr((c, y) -> y.multiply(that).add(of(c)), ZERO, coefficients);
    }

    /**
     * Returns the derivative of {@code this} with respect to x.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return d{@code this}/dx
     */
    public @NotNull RationalPolynomial differentiate() {
        if (coefficients.size() < 2) {
            return ZERO;
        } else if (coefficients.size() == 2 && last(coefficients) == Rational.ONE) {
            return ONE;
        } else {
            return new RationalPolynomial(
                    toList(zipWith((c, i) -> c.multiply(BigInteger.valueOf(i)), tail(coefficients), rangeUp(1)))
            );
        }
    }

    /**
     * Determines whether {@code this} is monic–whether its leading coefficient is 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is monic
     */
    public boolean isMonic() {
        return !coefficients.isEmpty() && last(coefficients) == Rational.ONE;
    }

    /**
     * Divides {@code this} by a constant to make it monic. {@code this} cannot be zero.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is a monic {@code RationalPolynomial}.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return The constant multiple of {@code this} whose leading coefficient is 1.
     */
    public @NotNull RationalPolynomial makeMonic() {
        Optional<Rational> leading = leading();
        if (!leading.isPresent()) {
            throw new ArithmeticException("this cannot be zero.");
        }
        return leading.get() == Rational.ONE ? this : divide(leading.get());
    }

    /**
     * Returns a {@code Pair} containing a constant and an integer-coefficient polynomial whose product is
     * {@code this}, such that the leading coefficient of the polynomial part is positive and the GCD of its
     * coefficients is 1.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is a {@code Pair} both of whose elements are not null and whose last element has a positive
     *  leading coefficient and no invertible integral constant factors.</li>
     * </ul>
     *
     * @return a constant factor of {@code this} and {@code this} divided by the factor
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Pair<Rational, Polynomial> constantFactor() {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        Polynomial positivePrimitive = Polynomial.of(Rational.cancelDenominators(coefficients));
        if (positivePrimitive.signum() == -1) {
            positivePrimitive = positivePrimitive.negate();
        }
        return new Pair<>(leading().get().divide(positivePrimitive.leading().get()), positivePrimitive);
    }

    /**
     * Returns the quotient and remainder when {@code this} is divided by {@code that}. To be more precise, the result
     * is (q, r) such that {@code this}={@code that}×q+r and deg(r){@literal <}deg({@code that}).
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>Neither element of the result is null.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomial} {@code this} is divided by
     * @return ({@code this}/{@code that}, {@code this}%{code that})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Pair<RationalPolynomial, RationalPolynomial> divide(@NotNull RationalPolynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = degree();
        int n = that.degree();
        if (m < n) return new Pair<>(ZERO, this);
        if (that == ONE) return new Pair<>(this, ZERO);
        List<Rational> q = new ArrayList<>();
        List<Rational> r = toList(coefficients);
        for (int k = m - n; k >= 0; k--) {
            Rational qCoefficient = r.get(n + k).divide(that.coefficient(n));
            q.add(qCoefficient);
            for (int j = n + k - 1; j >= k; j--) {
                r.set(j, r.get(j).subtract(qCoefficient.multiply(that.coefficient(j - k))));
            }
        }
        return new Pair<>(of(reverse(q)), of(toList(take(n, r))));
    }

    /**
     * Determines whether {@code this} is divisible by {@code that}, i.e. whether there exists a
     * {@code RationalPolynomial} p such that {@code p}×{@code that}={@code this}. {@code that} cannot be zero, even
     * when {@code this} is zero.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomial} that {@code this} may or may not be divisible by
     * @return whether {@code this} is divisible by {@code that}
     */
    public boolean isDivisibleBy(@NotNull RationalPolynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        return this == ZERO ||
                degree() >= that.degree() &&
                constantFactor().b.pseudoDivide(that.constantFactor().b).b == Polynomial.ZERO;
    }

    /**
     * Given two {@code RationalPolynomial}s, returns a list of {@code RatinalPolynomial}s with certain useful
     * properties. For example, the last element is a GCD of the two polynomials.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} cannot both be zero.</li>
     *  <li>No element of the result is null.</li>
     * </ul>
     *
     * @param that another {@code RationalPolynomial}
     * @return the remainder sequence of {@code this} and {@code that}
     */
    public @NotNull List<RationalPolynomial> remainderSequence(@NotNull RationalPolynomial that) {
        if (this == ZERO && that == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        List<RationalPolynomial> sequence = new ArrayList<>();
        sequence.add(this);
        if (that == ZERO) return sequence;
        sequence.add(that);
        for (int i = 0; ; i++) {
            RationalPolynomial next = sequence.get(i).divide(sequence.get(i + 1)).b;
            if (next == ZERO) return sequence;
            sequence.add(next);
        }
    }

    /**
     * Given two {@code RationalPolynomial}s, returns a list of {@code RatinalPolynomial}s with certain useful
     * properties. For example, the last element is a GCD of the two polynomials, and the signed remainder sequence of
     * a squarefree polynomial and its derivative is a Sturm chain.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} cannot both be zero.</li>
     *  <li>No element of the result is null.</li>
     * </ul>
     *
     * @param that another {@code RationalPolynomial}
     * @return the signed remainder sequence of {@code this} and {@code that}
     */
    public @NotNull List<RationalPolynomial> signedRemainderSequence(@NotNull RationalPolynomial that) {
        if (this == ZERO && that == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        List<RationalPolynomial> sequence = new ArrayList<>();
        sequence.add(this);
        if (that == ZERO) return sequence;
        sequence.add(that);
        for (int i = 0; ; i++) {
            RationalPolynomial next = sequence.get(i).divide(sequence.get(i + 1)).b.negate();
            if (next == ZERO) return sequence;
            sequence.add(next);
        }
    }

    /**
     * Let i range from 0 to deg({@code this}); this method returns a list that contains, for every i, the sum of the
     * ith powers of the roots of {@code this}.
     *
     * <ul>
     *  <li>{@code this} must be monic.</li>
     *  <li>The result is non-empty and contains no nulls. The first element is equal to deg({@code this}).</li>
     * </ul>
     *
     * @return the first deg({@code this}) Newton sums of {@code this}
     */
    public @NotNull List<Rational> powerSums() {
        if (!isMonic()) {
            throw new IllegalArgumentException("this must be monic. Invalid this: " + this);
        }
        List<Rational> powerSums = new ArrayList<>();
        int n = degree();
        powerSums.add(Rational.of(n));
        if (n == 0) return powerSums;
        int k = degree();
        powerSums.add(coefficient(k - 1).negate());
        if (n == 1) return powerSums;
        for (int i = 2; i <= n; i++) {
            List<Rational> terms = new ArrayList<>();
            for (int j = 1; j < i; j++) {
                terms.add(coefficient(k - j).multiply(powerSums.get(i - j)).negate());
            }
            terms.add(coefficient(k - i).multiply(i).negate());
            powerSums.add(Rational.sum(terms));
        }
        return powerSums;
    }

    /**
     * Given the 0th to dth power sums of a d-degree monic polynomial, where the ith power sum is the sum of the ith
     * powers of the roots, returns the polynomial.
     *
     * <ul>
     *  <li>{@code powerSums} cannot be empty, and its first element must be one less than its length.</li>
     *  <li>The result is monic.</li>
     * </ul>
     *
     * @param powerSums the Newton sums of a monic {@code RationalPolynomial}
     * @return the monic {@code RationalPolynomial} whose Newton sums are {@code powerSums}
     */
    public static @NotNull RationalPolynomial fromPowerSums(@NotNull List<Rational> powerSums) {
        if (powerSums.isEmpty()) {
            throw new IllegalArgumentException("powerSums cannot be empty.");
        }
        if (!head(powerSums).equals(Rational.of(powerSums.size() - 1))) {
            throw new IllegalArgumentException("The first element of powerSums must be one less than its length." +
                    " Invalid powerSums: " + powerSums);
        }
        List<Rational> coefficients = new ArrayList<>();
        coefficients.add(Rational.ONE);
        for (int i = 1; i < powerSums.size(); i++) {
            List<Rational> terms = new ArrayList<>();
            for (int j = 1; j <= i; j++) {
                terms.add(powerSums.get(j).multiply(coefficients.get(i - j)));
            }
            coefficients.add(Rational.sum(terms).negate().divide(i));
        }
        return coefficients.size() == 1 ? ONE : new RationalPolynomial(reverse(coefficients));
    }

    /**
     * Given a {@code List} of {@code Pair}s of {@code Rational}s representing (x, y)-points, returns the
     * minimal-degree {@code RationalPolynomial} passing through those points. The x-values, or the first elements of
     * the pairs, must be unique. A list with duplicates will cause an exception, even if the y-values are the same. If
     * {@code points} is empty, the result is 0.
     *
     * <ul>
     *  <li>{@code points} cannot contain nulls, and neither element of any {@code Pair} can be null. The first
     *  elements of the {@code Pair}s must be unique.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param points the (x, y)-values of the points to be interpolated
     * @return the {@code RationalPolynomial} with the smallest degree that passes through {@code points}
     */
    public static @NotNull RationalPolynomial interpolate(@NotNull List<Pair<Rational, Rational>> points) {
        if (any(p -> p.a == null, points)) {
            throw new NullPointerException();
        }
        if (!unique(map(p -> p.a, points))) {
            throw new IllegalArgumentException();
        }
        int degree = points.size() - 1;
        List<RationalVector> rows = new ArrayList<>();
        //noinspection Convert2streamapi
        for (Pair<Rational, Rational> point : points) {
            rows.add(RationalVector.of(toList(take(degree + 1, iterate(r -> r.multiply(point.a), Rational.ONE)))));
        }
        RationalMatrix matrix = RationalMatrix.fromRows(rows);
        RationalVector yValues = RationalVector.of(toList(map(p -> p.b, points)));
        return of(toList(matrix.solveLinearSystem(yValues).get()));
    }

    /**
     * Returns the Frobenius companion matrix of {@code this}.
     *
     * <ul>
     *  <li>{@code this} must be monic.</li>
     *  <li>The first row of the result has zeros in all but the last column, and the submatrix produced by omitting
     *  the first row and the last column is the identity.</li>
     * </ul>
     *
     * @return the companion matrix of {@code this}
     */
    public @NotNull RationalMatrix companionMatrix() {
        if (this == ONE) {
            return RationalMatrix.zero(0, 0);
        }
        if (!isMonic()) {
            throw new IllegalArgumentException("this must be monic. Invalid this: " + this);
        }
        int degree = degree();
        List<RationalVector> rows = new ArrayList<>();
        List<Rational> row = toList(replicate(degree - 1, Rational.ZERO));
        row.add(coefficients.get(0).negate());
        rows.add(RationalVector.of(row));
        for (int i = 1; i < degree; i++) {
            row.clear();
            for (int j = 1; j < degree; j++) {
                row.add(i == j ? Rational.ONE : Rational.ZERO);
            }
            row.add(coefficients.get(i).negate());
            rows.add(RationalVector.of(row));
        }
        return RationalMatrix.fromRows(rows);
    }

    /**
     * Returns a {@code RationalMatrix} containing the coefficients of a {@code List} of {@code RationalPolynomial}s.
     *
     * <ul>
     *  <li>{@code ps} may not have more elements than one more than the maximum degree of {@code ps}.</li>
     *  <li>The result has a height less than or equal to its width, and its first column, if it exists, does not only
     *  contain zeros.</li>
     * </ul>
     *
     * Size is length({@code ps})×(max({deg(p)|p∈{@code ps}})+1)
     *
     * @param ps a {@code List} of {@code RationalPolynomial}s
     * @return Mat({@code ps})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull RationalMatrix coefficientMatrix(@NotNull List<RationalPolynomial> ps) {
        if (ps.isEmpty()) return RationalMatrix.zero(0, 0);
        int width = maximum(map(RationalPolynomial::degree, ps)) + 1;
        if (ps.size() > width) {
            throw new IllegalArgumentException("ps may not have more elements than one more than the maximum degree" +
                    " of ps. Invalid ps: " + ps);
        }
        List<RationalVector> rows = new ArrayList<>();
        for (RationalPolynomial p : ps) {
            List<Rational> row = new ArrayList<>();
            for (int i = width - 1; i >= 0; i--) {
                row.add(p.coefficient(i));
            }
            rows.add(RationalVector.of(row));
        }
        return RationalMatrix.fromRows(rows);
    }

    /**
     * Returns the reflection of {@code this} across the y-axis. If {@code this} has odd degree, the result is negated
     * as well; this preserves the sign of the leading coefficient. The roots of the result are the negatives of the
     * roots of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return {@code this} reflected across the y-axis
     */
    public @NotNull RationalPolynomial reflect() {
        if (degree() < 1) return this;
        List<Rational> reflectedCoefficients = new ArrayList<>();
        boolean negateResult = degree() % 2 == 0;
        for (int i = 0; i < coefficients.size(); i++) {
            reflectedCoefficients.add(i % 2 == 0 == negateResult ? coefficients.get(i) : coefficients.get(i).negate());
        }
        return of(reflectedCoefficients);
    }

    /**
     * Returns the translation of {@code this} by {@code t}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code t} is not null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param t the amount that {@code this} is translated by in the x-direction
     * @return {@code this}(x–t)
     */
    public @NotNull RationalPolynomial translate(@NotNull Rational t) {
        if (degree() < 1 || t == Rational.ZERO) return this;
        return substitute(new RationalPolynomial(Arrays.asList(t.negate(), Rational.ONE)));
    }

    /**
     * Returns {@code this} stretched in the x-direction by a factor of {@code f}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code f} must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param f the amount that {@code this} is stretched in the x-direction
     * @return {@code this}(x/f)
     */
    public @NotNull RationalPolynomial stretch(@NotNull Rational f) {
        if (f.signum() != 1) {
            throw new ArithmeticException("f must be positive. Invalid f: " + f);
        }
        int degree = degree();
        if (degree < 1 || f == Rational.ONE) return this;
        List<Rational> stretchedCoefficients = new ArrayList<>();
        Rational d = Rational.ONE;
        for (int i = 0; i <= degree; i++) {
            stretchedCoefficients.add(coefficients.get(i).divide(d));
            if (i != degree) {
                d = d.multiply(f);
            }
        }
        return new RationalPolynomial(stretchedCoefficients);
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
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
                coefficients.equals(((RationalPolynomial) that).coefficients);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return coefficients.hashCode();
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. Asymptotic ordering is used; the ordering of two {@code RationalPolynomial}s is the
     * eventual ordering of their values at a sufficiently large, positive argument.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomial} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull RationalPolynomial that) {
        if (this == that) return 0;
        int thisSign = signum();
        int thatSign = that.signum();
        if (thisSign > thatSign) return 1;
        if (thisSign < thatSign) return -1;
        int c = RATIONAL_ITERABLE_COMPARATOR.compare(reverse(abs()), reverse(that.abs()));
        return thisSign == -1 ? -c : c;
    }

    /**
     * Creates a {@code RationalPolynomial} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link mho.qbar.objects.RationalPolynomial#toString}. This method also takes
     * {@code exponentHandler}, which reads an exponent for a {@code String} if the {@code String} is valid.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>{@code exponentHandler} must terminate on all possible {@code String}s without throwing an exception, and
     *  cannot return nulls.</li>
     *  <li>The result may be any {@code Optional<RationalPolynomial>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalPolynomial}.
     * @return the wrapped {@code RationalPolynomial} represented by {@code s}, or {@code empty} if {@code s} is
     * invalid.
     */
    private static @NotNull Optional<RationalPolynomial> genericRead(
            @NotNull String s,
            @NotNull Function<String, Optional<Integer>> exponentHandler
    ) {
        if (s.equals("0")) return Optional.of(ZERO);
        if (s.equals("1")) return Optional.of(ONE);
        if (s.isEmpty() || head(s) == '+') return Optional.empty();

        List<String> monomialStrings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '-' || c == '+') {
                String monomialString = sb.toString();
                if (!monomialString.isEmpty()) {
                    if (head(monomialString) == '+') {
                        monomialString = tail(monomialString);
                    }
                    monomialStrings.add(monomialString);
                    sb = new StringBuilder();
                }
            }
            sb.append(c);
        }
        String monomialString = sb.toString();
        if (!monomialString.isEmpty()) {
            if (head(monomialString) == '+') {
                monomialString = tail(monomialString);
            }
            monomialStrings.add(monomialString);
        }

        List<Pair<Rational, Integer>> monomials = new ArrayList<>();
        for (int i = monomialStrings.size() - 1; i >= 0; i--) {
            monomialString = monomialStrings.get(i);
            int xIndex = monomialString.indexOf('x');
            if (xIndex == -1) {
                Optional<Rational> constant = Rational.read(monomialString);
                if (!constant.isPresent()) return Optional.empty();
                monomials.add(new Pair<>(constant.get(), 0));
            } else {
                Rational coefficient;
                switch (xIndex) {
                    case 0:
                        coefficient = Rational.ONE;
                        break;
                    case 1:
                        if (head(monomialString) != '-') return Optional.empty();
                        monomialString = tail(monomialString);
                        coefficient = Rational.NEGATIVE_ONE;
                        break;
                    default:
                        if (monomialString.charAt(xIndex - 1) != '*') return Optional.empty();
                        String coefficientString = monomialString.substring(0, xIndex - 1);
                        Optional<Rational> oCoefficient = Rational.read(coefficientString);
                        if (!oCoefficient.isPresent()) return Optional.empty();
                        coefficient = oCoefficient.get();
                        // no 1*x, -1*x, 1*x^2, -1*x^2, ... allowed
                        if (coefficient.abs() == Rational.ONE) return Optional.empty();
                        monomialString = monomialString.substring(xIndex);
                        break;
                }
                int power;
                int caretIndex = monomialString.indexOf('^');
                switch (caretIndex) {
                    case -1:
                        if (!monomialString.equals("x")) return Optional.empty();
                        power = 1;
                        break;
                    case 1:
                        String powerString = monomialString.substring(caretIndex + 1);
                        Optional<Integer> oPower = exponentHandler.apply(powerString);
                        if (!oPower.isPresent()) return Optional.empty();
                        power = oPower.get();
                        if (power < 2) return Optional.empty(); // no x^1, x^0, x^-1, ... allowed
                        break;
                    default:
                        return Optional.empty();
                }
                monomials.add(new Pair<>(coefficient, power));
            }
        }
        if (any(p -> p.a == Rational.ZERO, monomials)) return Optional.empty();
        //noinspection RedundantCast
        if (!increasing((Iterable<Integer>) map(p -> p.b, monomials))) return Optional.empty();
        int degree = last(monomials).b;
        List<Rational> coefficients = toList(replicate(degree + 1, Rational.ZERO));
        for (Pair<Rational, Integer> monomial : monomials) {
            coefficients.set(monomial.b, monomial.a);
        }
        return Optional.of(new RationalPolynomial(coefficients));
    }

    /**
     * Creates a {@code RationalPolynomial} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link mho.qbar.objects.RationalPolynomial#toString}. Caution: It's easy to run out
     * of time and memory reading something like {@code "x^1000000000"}. If such an input is possible, consider using
     * {@link Polynomial#read(int, String)} instead.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<RationalPolynomial>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalPolynomial}.
     * @return the wrapped {@code RationalPolynomial} represented by {@code s}, or {@code empty} if {@code s} is
     * invalid.
     */
    public static @NotNull Optional<RationalPolynomial> read(@NotNull String s) {
        return genericRead(s, Readers::readInteger);
    }

    /**
     * Creates an {@code RationalPolynomial} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link mho.qbar.objects.RationalPolynomial#toString}. The input
     * {@code RationalPolynomial} cannot have a degree greater than {@code maxExponent}.
     *
     * <ul>
     *  <li>{@code maxExponent} must be positive.</li>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<RationalPolynomial>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalPolynomial}.
     * @return the wrapped {@code RationalPolynomial} (with degree no greater than {@code maxExponent}) represented by
     * {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<RationalPolynomial> read(int maxExponent, @NotNull String s) {
        if (maxExponent < 1) {
            throw new IllegalArgumentException("maxExponent must be positive. Invalid maxExponent: " + maxExponent);
        }
        return genericRead(
                s,
                powerString -> {
                    Optional<Integer> oPower = Readers.readInteger(powerString);
                    return !oPower.isPresent() || oPower.get() > maxExponent ? Optional.<Integer>empty() : oPower;
                }
        );
    }

    /**
     * Finds the first occurrence of a {@code RationalPolynomial} in a {@code String}. Returns the
     * {@code RationalPolynomial} and the index at which it was found. Returns an empty {@code Optional} if no
     * {@code RationalPolynomial} is found. Only {@code String}s which could have been emitted by
     * {@link mho.qbar.objects.RationalPolynomial#toString} are recognized. The longest possible
     * {@code RationalPolynomial} is parsed. Caution: It's easy to run out of time and memory finding something like
     * {@code "x^1000000000"}. If such an input is possible, consider using
     * {@link RationalPolynomial#findIn(int, String)} instead.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code RationalPolynomial} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<RationalPolynomial, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(RationalPolynomial::read, "*+-/0123456789^x").apply(s);
    }

    /**
     * Finds the first occurrence of a {@code RationalPolynomial} in a {@code String}. Returns the
     * {@code RationalPolynomial} and the index at which it was found. Returns an empty {@code Optional} if no
     * {@code RationalPolynomial} is found. Only{@code String}s which could have been emitted by
     * {@link mho.qbar.objects.RationalPolynomial#toString} are recognized. The longest possible
     * {@code RationalPolynomial} is parsed. The input {@code RationalPolynomial} cannot have a degree greater than
     * {@code maxExponent}.
     *
     * <ul>
     *  <li>{@code maxExponent} can be any {@code int}.</li>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code RationalPolynomial} found in {@code s} (with degree no greater than
     * {@code maxExponent}), and the index at which it was found
     */
    public static @NotNull Optional<Pair<RationalPolynomial, Integer>> findIn(int maxExponent, @NotNull String s) {
        return Readers.genericFindIn(t -> read(maxExponent, t), "*+-/0123456789^x").apply(s);
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        if (this == ZERO) return "0";
        StringBuilder sb = new StringBuilder();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            Rational coefficient = coefficients.get(i);
            if (coefficient == Rational.ZERO) continue;
            String monomialString;
            if (i == 0) {
                monomialString = coefficient.toString();
            } else {
                String power = i == 1 ? "x" : "x^" + i;
                if (coefficient == Rational.ONE) {
                    monomialString = power;
                } else if (coefficient.equals(Rational.NEGATIVE_ONE)) {
                    monomialString = cons('-', power);
                } else {
                    monomialString = coefficient + "*" + power;
                }
            }
            if (i != coefficients.size() - 1 && head(monomialString) != '-') {
                sb.append('+');
            }
            sb.append(monomialString);
        }
        return sb.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code RationalPolynomial} used
     * outside this class.
     */
    public void validate() {
        assertTrue(this, all(r -> r != null, coefficients));
        if (!coefficients.isEmpty()) {
            assertTrue(this, last(coefficients) != Rational.ZERO);
        }
        if (equals(ZERO)) {
            assertTrue(this, this == ZERO);
        }
        if (equals(ONE)) {
            assertTrue(this, this == ONE);
        }
    }
}
