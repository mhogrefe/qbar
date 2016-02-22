package mho.qbar.objects;

import jas.JasApi;
import mho.wheels.concurrency.ResultCache;
import mho.wheels.io.Readers;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.math.MathUtils;
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
 * <p>A univariate polynomial in x with {@link BigInteger} coefficients.</p>
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code Polynomial}s using {@code ==}. This is not true for {@code X}.</p>
 *
 * <p>This class uses a little-endian dense representation; in other words, the coefficient of x<sup>i</sup> is located
 * at the ith position of the coefficient list. The list contains no trailing zeros. Zero is represented by the empty
 * list.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class Polynomial implements
        Comparable<Polynomial>,
        Function<Rational, Rational>,
        Iterable<BigInteger> {
    /**
     * 0
     */
    public static final @NotNull Polynomial ZERO = new Polynomial(Collections.emptyList());

    /**
     * 1
     */
    public static final @NotNull Polynomial ONE = new Polynomial(Collections.singletonList(BigInteger.ONE));

    /**
     * x
     */
    public static final @NotNull Polynomial X = new Polynomial(Arrays.asList(BigInteger.ZERO, BigInteger.ONE));

    /**
     * Used by {@link mho.qbar.objects.Polynomial#compareTo}
     */
    private static final Comparator<Iterable<BigInteger>> BIG_INTEGER_ITERABLE_COMPARATOR = new ShortlexComparator<>();

    /**
     * Whether to cache some results of {@link Polynomial#factor()}
     */
    public static boolean USE_FACTOR_CACHE = true;

    /**
     * A thread-safe cache of some of the results of {@link Polynomial#factor()}
     */
    private static final ResultCache<Polynomial, List<Polynomial>> FACTOR_CACHE =
            new ResultCache<>(Polynomial::factorRaw, p -> p.degree() > 6);

    /**
     * A {@code Comparator} that compares two {@code Polynomial}s by their degrees, then lexicographically by their
     * coefficients.
     */
    private static final @NotNull Comparator<Polynomial> DEGREE_COEFFICIENT_COMPARATOR = (p, q) -> {
        int c = Integer.compare(p.degree(), q.degree());
        if (c != 0) return c;
        return BIG_INTEGER_ITERABLE_COMPARATOR.compare(p.coefficients, q.coefficients);
    };

    /**
     * The polynomial's coefficients. The coefficient of x<sup>i</sup> is at the ith position.
     */
    private final @NotNull List<BigInteger> coefficients;

    /**
     * Private constructor for {@code Polynomial}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coefficients} cannot have any null elements and cannot end in a 0.</li>
     *  <li>Any {@code Polynomial} may be constructed with this constructor.</li>
     * </ul>
     *
     * Length is |{@code coefficients}|
     *
     * @param coefficients the polynomial's coefficients
     */
    private Polynomial(@NotNull List<BigInteger> coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Returns an {@code Iterator} over this {@code Polynomial}'s coefficients, from lowest-degree to highest-degree.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is finite, contains no nulls, and does not end with 0.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return an {@code Iterator} over this {@code Polynomial}'s coefficients
     */
    @Override
    public @NotNull Iterator<BigInteger> iterator() {
        return new NoRemoveIterable<>(coefficients).iterator();
    }

    /**
     * Evaluates {@code this} at {@code x} using Horner's method.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the argument
     * @return {@code this}({@code x})
     */
    public @NotNull BigInteger apply(@NotNull BigInteger x) {
        return foldr((c, y) -> y.multiply(x).add(c), BigInteger.ZERO, coefficients);
    }

    /**
     * Evaluates {@code this} at {@code x} using Horner's method.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the argument
     * @return {@code this}({@code x})
     */
    @Override
    public @NotNull Rational apply(@NotNull Rational x) {
        if (this == ZERO) return Rational.ZERO;
        return Rational.of(specialApply(x), x.getDenominator().pow(degree()));
    }

    /**
     * Given a {@code Rational x} = b/c, return c<sup>deg({@code this})</sup>{@code this}({@code x}). This modification
     * of {@link Polynomial#apply(Rational)} only uses {@code BigInteger} arithmetic.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the argument
     * @return {@code this} evaluated at {@code x}, times the denominator of {@code x} raised to the degree of
     * {@code this}
     */
    public @NotNull BigInteger specialApply(@NotNull Rational x) {
        if (this == ZERO) return BigInteger.ZERO;
        BigInteger numerator = x.getNumerator();
        BigInteger denominator = x.getDenominator();
        BigInteger result = last(coefficients);
        BigInteger multiplier = BigInteger.ONE;
        for (int i = coefficients.size() - 2; i >= 0; i--) {
            multiplier = multiplier.multiply(denominator);
            result = result.multiply(numerator).add(coefficients.get(i).multiply(multiplier));
        }
        return result;
    }

    /**
     * Converts {@code this} to a {@code RationalPolynomial}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is a {@code RationalPolynomial} with integral coefficients.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return a {@code RationalPolynomial} with the same value as {@code this}
     */
    public @NotNull RationalPolynomial toRationalPolynomial() {
        if (this == ZERO) return RationalPolynomial.ZERO;
        if (this == ONE) return RationalPolynomial.ONE;
        return RationalPolynomial.of(toList(map(Rational::of, coefficients)));
    }

    /**
     * Gets the coefficient of this {@code Polynomial}'s x<sup>{@code i}</sup> term. If {@code i} is greater than this
     * {@code Polynomial}'s degree, 0 is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code i} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param i the power that the coefficient belongs to
     * @return the coefficient of x<sup>{@code i}</sup>
     */
    public @NotNull BigInteger coefficient(int i) {
        return i < coefficients.size() ? coefficients.get(i) : BigInteger.ZERO;
    }

    /**
     * Creates a {@code Polynomial} from a list of {@code BigInteger} coefficients. Throws an exception if any
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
     * @return the {@code Polynomial} with the specified coefficients
     */
    public static @NotNull Polynomial of(@NotNull List<BigInteger> coefficients) {
        if (any(i -> i == null, coefficients)) {
            throw new NullPointerException();
        }
        int actualSize;
        for (actualSize = coefficients.size(); actualSize > 0; actualSize--) {
            if (!coefficients.get(actualSize - 1).equals(BigInteger.ZERO)) {
                break;
            }
        }
        if (actualSize == 0) return ZERO;
        if (actualSize == 1 && coefficients.get(0).equals(BigInteger.ONE)) return ONE;
        return new Polynomial(toList(take(actualSize, coefficients)));
    }

    /**
     * Creates a constant (possibly zero) {@code Polynomial}.
     *
     * <ul>
     *  <li>{@code c} cannot be null.</li>
     *  <li>The result has degree 0 or –1.</li>
     * </ul>
     *
     * Length is 0 if c is 0, or 1 otherwise
     *
     * @param c the constant
     * @return a constant {@code Polynomial} equal to {@code c}
     */
    public static @NotNull Polynomial of(@NotNull BigInteger c) {
        if (c.equals(BigInteger.ZERO)) return ZERO;
        if (c.equals(BigInteger.ONE)) return ONE;
        return new Polynomial(Collections.singletonList(c));
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
     * Length is 0 if {@code c} is 0, {@code p}+1 otherwise
     *
     * @param c the monomial's coefficient
     * @param p the monomial's degree if {@code c} is nonzero
     * @return {@code c}x<sup>p</sup>
     */
    public static @NotNull Polynomial of(@NotNull BigInteger c, int p) {
        if (p < 0) {
            throw new IllegalArgumentException("p cannot be negative. Invalid p: " + p);
        }
        if (c.equals(BigInteger.ZERO)) return ZERO;
        if (p == 0 && c.equals(BigInteger.ONE)) return ONE;
        return new Polynomial(toList(concat(replicate(p, BigInteger.ZERO), Collections.singletonList(c))));
    }

    /**
     * Returns the maximum bit length of any coefficient, or 0 if {@code this} is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coefficient bit length
     */
    public int maxCoefficientBitLength() {
        if (this == ZERO) return 0;
        //noinspection RedundantCast
        return maximum((Iterable<Integer>) map(c -> c.abs().bitLength(), coefficients));
    }

    /**
     * Returns this {@code Polynomial}'s degree. We consider 0 to have degree –1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is at least –1.</li>
     * </ul>
     *
     * @return this {@code Polynomial}'s degree
     */
    public int degree() {
        return coefficients.size() - 1;
    }

    /**
     * Returns the leading coefficient of {@code this}, or an empty {@code Optional} is {@code this} is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result may be empty, or it may contain a nonzero {@code BigInteger}.</li>
     * </ul>
     *
     * @return the leading coefficient
     */
    public @NotNull Optional<BigInteger> leading() {
        return this == ZERO ? Optional.<BigInteger>empty() : Optional.of(last(coefficients));
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
    public @NotNull Polynomial add(@NotNull Polynomial that) {
        if (this == ZERO) return that;
        if (that == ZERO) return this;
        return of(
                toList(
                        zipWithPadded(
                                BigInteger::add,
                                BigInteger.ZERO,
                                BigInteger.ZERO,
                                coefficients,
                                that.coefficients
                        )
                )
        );
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return –{@code this}
     */
    public @NotNull Polynomial negate() {
        if (this == ZERO) return ZERO;
        if (coefficients.size() == 1 && coefficients.get(0).equals(IntegerUtils.NEGATIVE_ONE)) return ONE;
        return new Polynomial(toList(map(BigInteger::negate, coefficients)));
    }

    /**
     * Returns the absolute value of {@code this}. In other words, if the leading coefficient of {@code this} is
     * negative, {@code this} is negated.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is either 0 or a {@code Polynomial} with a positive leading coefficient.</li>
     * </ul>
     *
     * @return |{@code this}|
     */
    public @NotNull Polynomial abs() {
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
     * @return sgn({@code this}(∞))
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
    public int signum(@NotNull BigInteger x) {
        return apply(x).signum();
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
        return specialApply(x).signum();
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is up to max(deg({@code this}), deg({@code that}))+1
     *
     * @param that the {@code Polynomial} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull Polynomial subtract(@NotNull Polynomial that) {
        if (this == ZERO) return that.negate();
        if (that == ZERO) return this;
        if (this == that) return ZERO;
        return of(
                toList(
                        zipWithPadded(
                                BigInteger::subtract,
                                BigInteger.ZERO,
                                BigInteger.ZERO,
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
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if either {@code this} or {@code that} is 0, or deg({@code this})+deg({@code that})+1 otherwise
     *
     * @param that the {@code Polynomial} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Polynomial multiply(@NotNull Polynomial that) {
        if (this == ZERO || that == ZERO) return ZERO;
        if (this == ONE) return that;
        if (that == ONE) return this;
        List<BigInteger> productCoefficients = toList(
                replicate(coefficients.size() + that.coefficients.size() - 1, BigInteger.ZERO)
        );
        for (int i = 0; i < coefficients.size(); i++) {
            BigInteger a = coefficients.get(i);
            if (a.equals(BigInteger.ZERO)) continue;
            for (int j = 0; j < that.coefficients.size(); j++) {
                BigInteger b = that.coefficients.get(j);
                if (b.equals(BigInteger.ZERO)) continue;
                int index = i + j;
                productCoefficients.set(index, productCoefficients.get(index).add(a.multiply(b)));
            }
        }
        if (productCoefficients.size() == 1 && productCoefficients.get(0).equals(BigInteger.ONE)) return ONE;
        return new Polynomial(productCoefficients);
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if either {@code this} or {@code that} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Polynomial multiply(@NotNull BigInteger that) {
        if (this == ZERO || that.equals(BigInteger.ZERO)) return ZERO;
        if (this == ONE) return of(that);
        if (that.equals(BigInteger.ONE)) return this;
        List<BigInteger> productCoefficients = toList(map(c -> c.multiply(that), coefficients));
        if (productCoefficients.size() == 1 && productCoefficients.get(0).equals(BigInteger.ONE)) return ONE;
        return new Polynomial(productCoefficients);
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if either {@code this} or {@code that} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Polynomial multiply(int that) {
        if (this == ZERO || that == 0) return ZERO;
        if (this == ONE) return of(BigInteger.valueOf(that));
        if (that == 1) return this;
        List<BigInteger> productCoefficients = toList(map(c -> c.multiply(BigInteger.valueOf(that)), coefficients));
        if (productCoefficients.size() == 1 && productCoefficients.get(0).equals(BigInteger.ONE)) return ONE;
        return new Polynomial(productCoefficients);
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>. Negative
     * {@code bits} are not allowed, even if {@code this} is divisible by a power of 2.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Polynomial}.</li>
     *  <li>{@code bits} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull Polynomial shiftLeft(int bits) {
        if (bits < 0) {
            throw new ArithmeticException("bits cannot be negative. Invalid bits: " + bits);
        }
        if (this == ZERO) return ZERO;
        if (bits == 0) return this;
        List<BigInteger> shiftedCoefficients = toList(map(r -> r.shiftLeft(bits), coefficients));
        if (shiftedCoefficients.size() == 1 && shiftedCoefficients.get(0).equals(BigInteger.ONE)) return ONE;
        return new Polynomial(shiftedCoefficients);
    }

    /**
     * Returns the sum of all the {@code Polynomial}s in {@code xs}. If {@code xs} is empty, 0 is returned.
     *
     * <ul>
     *  <li>{@code xs} must be finite and may not contain any nulls.</li>
     *  <li>The result may be any {@code Polynomial}.</li>
     * </ul>
     *
     * Length is at most max({deg(p)|p∈{@code xs}})+1
     *
     * @param xs an {@code Iterable} of {@code Polynomial}s.
     * @return Σxs
     */
    public static @NotNull Polynomial sum(@NotNull Iterable<Polynomial> xs) {
        return of(toList(map(IterableUtils::sumBigInteger, transposePadded(BigInteger.ZERO, map(p -> p, xs)))));
    }

    /**
     * Returns the product of all the {@code Polynomial}s in {@code xs}. If {@code xs} is empty, 1 is returned.
     *
     * <ul>
     *  <li>{@code xs} must be finite and may not contain any nulls.</li>
     *  <li>The result may be any {@code Polynomial}.</li>
     * </ul>
     *
     * Length is at most sum({deg(p)|p∈{@code xs}})+1
     *
     * @param xs an {@code Iterable} of {@code Polynomial}s.
     * @return Πxs
     */
    public static @NotNull Polynomial product(@NotNull Iterable<Polynomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(Polynomial::multiply, ONE, sort(DEGREE_COEFFICIENT_COMPARATOR, xs));
    }

    /**
     * Returns the differences between successive {@code Polynomial}s in {@code xs}. If {@code xs} contains a single
     * {@code Polynomial}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} must not be empty and may not contain any nulls.</li>
     *  <li>The result does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Polynomial}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Polynomial> delta(@NotNull Iterable<Polynomial> xs) {
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
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code p} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 1 if p=0, deg({@code this})×p+1 otherwise
     *
     * @param p the power that {@code this} is raised to
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull Polynomial pow(int p) {
        if (p < 0) {
            throw new ArithmeticException("p cannot be negative. Invalid p: " + p);
        }
        if (p == 0) return ONE;
        if (p == 1) return this;
        Polynomial result = ONE;
        Polynomial powerPower = null; // p^2^i
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
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} may be any {@code Polynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 1 if {@code this}=0 or {@code that}=0, deg({@code this})×deg({@code that})+1 otherwise
     *
     * @param that the {@code Polynomial} substituted into {@code this}
     * @return {@code this}∘{@code that}
     */
    public @NotNull Polynomial substitute(@NotNull Polynomial that) {
        return foldr((c, y) -> y.multiply(that).add(of(c)), ZERO, coefficients);
    }

    /**
     * Returns the derivative of {@code this} with respect to x.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return d{@code this}/dx
     */
    public @NotNull Polynomial differentiate() {
        if (coefficients.size() < 2) {
            return ZERO;
        } else if (coefficients.size() == 2 && last(coefficients).equals(BigInteger.ONE)) {
            return ONE;
        } else {
            return new Polynomial(
                    toList(zipWith((c, i) -> c.multiply(BigInteger.valueOf(i)), tail(coefficients), rangeUp(1)))
            );
        }
    }

    /**
     * Determines whether {@code this} is monic–whether its leading coefficient is 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is monic
     */
    public boolean isMonic() {
        return !coefficients.isEmpty() && last(coefficients).equals(BigInteger.ONE);
    }

    /**
     * Determines whether {@code this} is primitive–whether the GCD of its coefficients is 1. 0 is not primitive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is primitive
     */
    public boolean isPrimitive() {
        return MathUtils.gcd(coefficients).equals(BigInteger.ONE);
    }

    /**
     * Returns a {@code Pair} containing {@code this}'s content and primitive part. The content is the largest integer
     * that divides all the coefficients of {@code this}, and the primitive part is {@code this} divided by the
     * content. This method is similar to {@link Polynomial#constantFactor()}, except that in the result of this method
     * the first element of the pair must be positive, but the leading coefficient of the second element can be
     * positive or negative.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is a {@code Pair} both of whose elements are not null, whose first element is positive, and
     *  whose last element is primitive.</li>
     * </ul>
     *
     * @return (content({@code this}), primitive({@code this}))
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Pair<BigInteger, Polynomial> contentAndPrimitive() {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (coefficients.size() == 1) {
            BigInteger constant = coefficients.get(0);
            return constant.signum() == 1 ?
                    new Pair<>(constant, ONE) :
                    new Pair<>(constant.negate(), of(IntegerUtils.NEGATIVE_ONE));
        }
        BigInteger content = MathUtils.gcd(coefficients);
        if (content.equals(BigInteger.ONE)) {
            return new Pair<>(BigInteger.ONE, this);
        } else {
            return new Pair<>(content, new Polynomial(toList(map(c -> c.divide(content), coefficients))));
        }
    }

    /**
     * Returns a {@code Pair} containing a constant and polynomial whose product is {@code this}, such that the leading
     * coefficient of the polynomial part is positive and the GCD of its coefficients is 1. This method is similar to
     * {@link Polynomial#contentAndPrimitive()}, except that in the result of this method the first element of the pair
     * can be positive or negative, but the leading coefficient of the second element must be positive.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is a {@code Pair} both of whose elements are not null and whose last element has a positive
     *  leading coefficient and no invertible constant factors.</li>
     * </ul>
     *
     * @return the constant integral factor of {@code this} with the same sign as {@code this} and the largest possible
     * absolute value
     */
    public @NotNull Pair<BigInteger, Polynomial> constantFactor() {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (coefficients.size() == 1) return new Pair<>(coefficients.get(0), ONE);
        BigInteger content = MathUtils.gcd(coefficients);
        BigInteger factor = signum() == -1 ? content.negate() : content;
        if (factor.equals(BigInteger.ONE)) {
            return new Pair<>(BigInteger.ONE, this);
        } else {
            return new Pair<>(factor, new Polynomial(toList(map(c -> c.divide(factor), coefficients))));
        }
    }

    /**
     * Returns the pseudo-quotient and pseudo-remainder when {@code this} is divided by {@code that}. To be more
     * precise, the result is (q, r) such that
     * {@code this}×leading({@code that})<sup>deg({@code a})–deg({@code n})+1</sup>={@code that}×q+r and
     * deg(r){@literal <}deg({@code that}). This is a useful variant of polynomial division that does not require
     * rational arithmetic.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be 0.</li>
     *  <li>The degree of {@code this} must be greater than or equal to the degree of {@code that}.</li>
     *  <li>Neither element of the result is null.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomial} {@code this} is divided by
     * @return ({@code this}/{@code that}, {@code this}%{code that})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Pair<Polynomial, Polynomial> pseudoDivide(@NotNull Polynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = degree();
        int n = that.degree();
        if (m < n) {
            throw new ArithmeticException("The degree of this must be greater than or equal to the degree of that." +
                    " this: " + this + ", that: " + that);
        }
        List<BigInteger> q = new ArrayList<>();
        List<BigInteger> r = toList(coefficients);
        BigInteger thatLeading = that.leading().get();
        List<BigInteger> leadingPowers = new ArrayList<>();
        BigInteger power = BigInteger.ONE;
        for (int i = 0; i < m - n; i++) {
            leadingPowers.add(power);
            power = power.multiply(thatLeading);
        }
        leadingPowers.add(power);
        for (int k = m - n; k >= 0; k--) {
            q.add(r.get(n + k).multiply(leadingPowers.get(k)));
            for (int j = n + k - 1; j >= k; j--) {
                r.set(j, thatLeading.multiply(r.get(j)).subtract(r.get(n + k).multiply(that.coefficients.get(j - k))));
            }
            for (int j = k - 1; j >= 0; j--) {
                r.set(j, thatLeading.multiply(r.get(j)));
            }
        }
        return new Pair<>(of(reverse(q)), of(toList(take(n, r))));
    }

    /**
     * Returns the pseudo-remainder when {@code this} is divided by {@code that}. To be more precise, the result is r
     * such that there exists a q such that
     * {@code this}×leading({@code that})<sup>deg({@code a})–deg({@code n})+1</sup>={@code that}×q+r and
     * deg(r){@literal <}deg({@code that}).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be 0.</li>
     *  <li>The degree of {@code this} must be greater than or equal to the degree of {@code that}.</li>
     *  <li>Neither element of the result is null.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomial} {@code this} is divided by
     * @return {@code this}%{code that}
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Polynomial pseudoRemainder(@NotNull Polynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = degree();
        int n = that.degree();
        if (m < n) {
            throw new ArithmeticException("The degree of this must be greater than or equal to the degree of that." +
                    " this: " + this + ", that: " + that);
        }
        List<BigInteger> r = toList(coefficients);
        BigInteger thatLeading = that.leading().get();
        for (int k = m - n; k >= 0; k--) {
            for (int j = n + k - 1; j >= k; j--) {
                r.set(j, thatLeading.multiply(r.get(j)).subtract(r.get(n + k).multiply(that.coefficients.get(j - k))));
            }
            for (int j = k - 1; j >= 0; j--) {
                r.set(j, thatLeading.multiply(r.get(j)));
            }
        }
        return of(toList(take(n, r)));
    }

    /**
     * Returns a variant of the pseudo-quotient and pseudo-remainder when {@code this} is divided by {@code that}. To
     * be more precise, the result is (q, r) such that
     * {@code this}×|leading({@code that})|<sup>deg({@code a})–deg({@code n})+1</sup>={@code that}×q+r and
     * deg(r){@literal <}deg({@code that}).\
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be 0.</li>
     *  <li>The degree of {@code this} must be greater than or equal to the degree of {@code that}.</li>
     *  <li>Neither element of the result is null.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomial} {@code this} is divided by
     * @return ({@code this}/{@code that}, {@code this}%{code that})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Pair<Polynomial, Polynomial> absolutePseudoDivide(@NotNull Polynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = degree();
        int n = that.degree();
        if (m < n) {
            throw new ArithmeticException("The degree of this must be greater than or equal to the degree of that." +
                    " this: " + this + ", that: " + that);
        }
        List<BigInteger> q = new ArrayList<>();
        List<BigInteger> r = toList(multiply(that.leading().get().abs().pow(m - n + 1)));
        for (int k = m - n; k >= 0; k--) {
            BigInteger qCoefficient = r.get(n + k).divide(that.coefficient(n));
            q.add(qCoefficient);
            for (int j = n + k - 1; j >= k; j--) {
                r.set(j, r.get(j).subtract(qCoefficient.multiply(that.coefficient(j - k))));
            }
        }
        return new Pair<>(of(reverse(q)), of(toList(take(n, r))));
    }

    /**
     * Returns a variant of the pseudo-remainder when {@code this} is divided by {@code that}. To be more precise, the
     * result is r such that there exists a q such that
     * {@code this}×|leading({@code that})|<sup>deg({@code a})–deg({@code n})+1</sup>={@code that}×q+r and
     * deg(r){@literal <}deg({@code that}).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be 0.</li>
     *  <li>The degree of {@code this} must be greater than or equal to the degree of {@code that}.</li>
     *  <li>Neither element of the result is null.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomial} {@code this} is divided by
     * @return {@code this}%{code that}
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Polynomial absolutePseudoRemainder(@NotNull Polynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = degree();
        int n = that.degree();
        if (m < n) {
            throw new ArithmeticException("The degree of this must be greater than or equal to the degree of that." +
                    " this: " + this + ", that: " + that);
        }
        List<BigInteger> r = toList(multiply(that.leading().get().abs().pow(m - n + 1)));
        for (int k = m - n; k >= 0; k--) {
            BigInteger qCoefficient = r.get(n + k).divide(that.coefficient(n));
            for (int j = n + k - 1; j >= k; j--) {
                r.set(j, r.get(j).subtract(qCoefficient.multiply(that.coefficient(j - k))));
            }
        }
        return of(toList(take(n, r)));
    }

    /**
     * Determines whether {@code this} is divisible by {@code that}, i.e. whether there exists a
     * {@code RationalPolynomial} p (with not-necessarily-integral coefficients) such that
     * {@code p}×{@code that}={@code this}. {@code that} cannot be 0, even when {@code this} is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be 0.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} that {@code this} may or may not be divisible by
     * @return whether {@code this} is divisible by {@code that}
     */
    public boolean isDivisibleBy(@NotNull Polynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        return this == ZERO || degree() >= that.degree() && pseudoDivide(that).b == ZERO;
    }

    /**
     * Returns the quotient of {@code this} and {@code that}, assuming that {@code this} is divisible by {@code that}
     * in the ring ℤ[x]. Otherwise, an exception is thrown.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code this} must be divisible by {@code that} and the quotient must have integral coefficients.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} {@code this} is divided by
     * @return {@code this}/{@code that}.
     */
    public @NotNull Polynomial divideExact(@NotNull Polynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (this == ZERO) return ZERO;
        int m = degree();
        int n = that.degree();
        if (m < n) {
            throw new ArithmeticException("this must be divisible by that. this: " + this + ", that: " + that);
        }
        List<BigInteger> q = new ArrayList<>();
        List<BigInteger> r = toList(coefficients);
        for (int k = m - n; k >= 0; k--) {
            BigInteger[] qCoefficient = r.get(n + k).divideAndRemainder(that.coefficient(n));
            if (!qCoefficient[1].equals(BigInteger.ZERO)) {
                throw new ArithmeticException("this must be divisible by that. this: " + this + ", that: " + that);
            }
            q.add(qCoefficient[0]);
            for (int j = n + k - 1; j >= k; j--) {
                r.set(j, r.get(j).subtract(qCoefficient[0].multiply(that.coefficient(j - k))));
            }
        }
        if (any(c -> !c.equals(BigInteger.ZERO), take(n, r))) {
            throw new ArithmeticException("this must be divisible by that. this: " + this + ", that: " + that);
        }
        return of(reverse(q));
    }

    /**
     * Returns the remainder of {@code this} and {@code that}, assuming that both the quotient and the remainder are in
     * the ring ℤ[x]. Otherwise, an exception is thrown.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The quotient and remainder of {@code this} divided by {@code that} must have integral coefficients.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} {@code this} is divided by
     * @return {@code this}%{@code that}
     */
    public @NotNull Polynomial remainderExact(@NotNull Polynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = degree();
        int n = that.degree();
        if (m < n) return this;
        List<BigInteger> r = toList(coefficients);
        for (int k = m - n; k >= 0; k--) {
            BigInteger[] qCoefficient = r.get(n + k).divideAndRemainder(that.coefficient(n));
            if (!qCoefficient[1].equals(BigInteger.ZERO)) {
                throw new ArithmeticException("The quotient and remainder of this divided by that must have integral" +
                        " coefficients.");
            }
            for (int j = n + k - 1; j >= k; j--) {
                r.set(j, r.get(j).subtract(qCoefficient[0].multiply(that.coefficient(j - k))));
            }
        }
        return of(toList(take(n, r)));
    }

    /**
     * Given two {@code Polynomial}s, returns a list of {@code Polynomial}s with certain useful properties. For
     * example, the last element is a GCD of the two polynomials. This particular sequence is inefficient and is only
     * implemented here for testing purposes.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} may be any {@code Polynomial}.</li>
     *  <li>The degree of {@code this} must be greater than or equal to the degree of {@code that}.</li>
     *  <li>{@code this} and {@code that} cannot both be zero.</li>
     *  <li>No element of the result is null.</li>
     * </ul>
     *
     * @param that another {@code RationalPolynomial}
     * @return the trivial pseudo-remainder sequence of {@code this} and {@code that}
     */
    public @NotNull List<Polynomial> trivialPseudoRemainderSequence(@NotNull Polynomial that) {
        if (this == ZERO && that == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        List<Polynomial> sequence = new ArrayList<>();
        sequence.add(this);
        if (that == ZERO) return sequence;
        sequence.add(that);
        for (int i = 0; ; i++) {
            Polynomial next = sequence.get(i).pseudoRemainder(sequence.get(i + 1));
            if (next == ZERO) return sequence;
            sequence.add(next);
        }
    }

    /**
     * Given two {@code Polynomial}s, returns a list of {@code Polynomial}s with certain useful properties. For
     * example, every element in the list is primitive or zero, and the last element is a GCD of the two polynomials.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} may be any {@code Polynomial}.</li>
     *  <li>The degree of {@code this} must be greater than or equal to the degree of {@code that}.</li>
     *  <li>{@code this} and {@code that} cannot both be zero.</li>
     *  <li>No element of the result is null.</li>
     * </ul>
     *
     * @param that another {@code RationalPolynomial}
     * @return the primitive pseudo-remainder sequence of {@code this} and {@code that}
     */
    public @NotNull List<Polynomial> primitivePseudoRemainderSequence(@NotNull Polynomial that) {
        if (this == ZERO && that == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        List<Polynomial> sequence = new ArrayList<>();
        sequence.add(this == ZERO ? ZERO : contentAndPrimitive().b);
        if (that == ZERO) return sequence;
        sequence.add(that.contentAndPrimitive().b);
        for (int i = 0; ; i++) {
            Polynomial next = sequence.get(i).pseudoRemainder(sequence.get(i + 1));
            if (next == ZERO) return sequence;
            sequence.add(next.contentAndPrimitive().b);
        }
    }

    /**
     * Given two {@code Polynomial}s, returns a list of {@code Polynomial}s with certain useful properties. For
     * example, the last element is a GCD of the two polynomials.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} may be any {@code Polynomial}.</li>
     *  <li>The degree of {@code this} must be greater than or equal to the degree of {@code that}.</li>
     *  <li>{@code this} and {@code that} cannot both be zero.</li>
     *  <li>No element of the result is null.</li>
     * </ul>
     *
     * @param that another {@code RationalPolynomial}
     * @return the subresultant sequence of {@code this} and {@code that}
     */
    public @NotNull List<Polynomial> subresultantSequence(@NotNull Polynomial that) {
        if (this == ZERO && that == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        List<Polynomial> sequence = new ArrayList<>();
        sequence.add(this);
        if (that == ZERO) return sequence;
        sequence.add(that);
        BigInteger g = BigInteger.ONE;
        BigInteger h = BigInteger.ONE;
        Polynomial a = this;
        Polynomial b = that;
        while (true) {
            Polynomial r = a.pseudoRemainder(b);
            if (r == ZERO) return sequence;
            int delta = a.degree() - b.degree();
            BigInteger divisor = g.multiply(h.pow(delta));
            g = b.leading().get();
            h = delta > 0 ? g.pow(delta).divide(h.pow(delta - 1)) : g.pow(delta).multiply(h.pow(1 - delta));
            a = b;
            b = of(toList(map(c -> c.divide(divisor), r)));
            sequence.add(b);
        }
    }

    /**
     * Returns the unique primitive GCD with positive leading coefficient of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} may be any {@code Polynomial}.</li>
     *  <li>{@code this} and {@code that} cannot both be zero.</li>
     *  <li>The result is primitive and has positive leading coefficient.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} that {@code this} is GCD'd with
     * @return the primitive polynomial of maximum degree with positive leading coefficient that divides {@code this}
     * and {@code that}
     */
    public @NotNull Polynomial gcd(@NotNull Polynomial that) {
        if (this == ZERO && that == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        if (this == ZERO) return that.constantFactor().b;
        if (that == ZERO) return constantFactor().b;
        if (this == ONE || that == ONE) return ONE;
        Polynomial a;
        Polynomial b;
        if (degree() >= that.degree()) {
            a = this;
            b = that;
        } else {
            a = that;
            b = this;
        }
        BigInteger g = BigInteger.ONE;
        BigInteger h = BigInteger.ONE;
        while (true) {
            Polynomial r = a.pseudoRemainder(b);
            if (r == ZERO) return b.constantFactor().b;
            int delta = a.degree() - b.degree();
            BigInteger divisor = g.multiply(h.pow(delta));
            g = b.leading().get();
            h = delta > 0 ? g.pow(delta).divide(h.pow(delta - 1)) : g.pow(delta).multiply(h.pow(1 - delta));
            a = b;
            b = of(toList(map(c -> c.divide(divisor), r)));
        }
    }

    /**
     * Returns the unique primitive GCD with positive leading coefficient of {@code ps}. The GCD of a set containing
     * only zeros is undefined.
     *
     * <ul>
     *  <li>{@code ps} cannot contain nulls and must contain at least one nonzero {@code Polynomial}.</li>
     *  <li>The result is primitive and has a positive leading coefficient.</li>
     * </ul>
     *
     * @param ps the {@code Polynomial}s whose GCD is sought
     * @return the primitive polynomial of maximum degree with positive leading coefficient that divides all of
     * {@code ps}
     */
    public static @NotNull Polynomial gcd(@NotNull List<Polynomial> ps) {
        if (any(p -> p == null, ps)) {
            throw new NullPointerException();
        }
        List<Polynomial> noZeros = toList(filter(p -> p != ZERO, ps));
        if (noZeros.isEmpty()) {
            throw new ArithmeticException("ps must contain at least one nonzero Polynomial. Invalid ps: " + ps);
        }
        if (noZeros.size() == 1) {
            return head(noZeros).constantFactor().b;
        } else {
            return foldl1(Polynomial::gcd, noZeros);
        }
    }

    /**
     * Returns the unique primitive LCM with positive leading coefficient of {@code this} and {@code that}, or zero if
     * either input is zero.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} may be any {@code Polynomial}.</li>
     *  <li>The result is zero or primitive and with positive leading coefficient.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} that {@code this} is LCM'd with
     * @return the polynomial of minimum degree that has {@code this} and {@code that} as factors and is primitive with
     * positive leading coefficient or zero
     */
    public @NotNull Polynomial lcm(@NotNull Polynomial that) {
        if (this == ZERO || that == ZERO) return ZERO;
        Polynomial ppThis = constantFactor().b;
        Polynomial ppThat = that.constantFactor().b;
        return ppThis.divideExact(ppThis.gcd(ppThat)).multiply(ppThat);
    }

    /**
     * Determines whether {@code this} is relatively prime to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} may be any {@code Polynomial}.</li>
     *  <li>{@code this} and {@code that} cannot both be zero.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} that may be relatively prime to {@code this}
     * @return whether {@code this} and {@code that} have no nonconstant common factors
     */
    public boolean isRelativelyPrimeTo(@NotNull Polynomial that) {
        if (this == ZERO && that == ZERO) {
            throw new ArithmeticException();
        }
        if (degree() == 0 || that.degree() == 0) return true;
        if (this == ZERO || that == ZERO) return false;
        int aLowestPower = findIndex(c -> !c.equals(BigInteger.ZERO), coefficients).get();
        int bLowestPower = findIndex(c -> !c.equals(BigInteger.ZERO), that.coefficients).get();
        return (aLowestPower == 0 || bLowestPower == 0) && gcd(that) == ONE;
    }

    /**
     * Determines whether {@code this} is square-free.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} has no repeated factors
     */
    public boolean isSquareFree() {
        return this != ZERO && isRelativelyPrimeTo(differentiate());
    }

    /**
     * Returns the square-free part of {@code this}, or {@code this} with all repeated factors removed.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is primitive and has positive leading coefficient.</li>
     * </ul>
     *
     * @return the square-free part of {@code this}
     */
    public @NotNull Polynomial squareFreePart() {
        Polynomial ppThis = constantFactor().b;
        return ppThis.divideExact(ppThis.gcd(ppThis.differentiate()));
    }

    /**
     * Returns a list of {@code Polynomial}s whose product is the positive primitive part of {@code this} and each of
     * which is square-free. Uses Yun's algorithm.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is a list of primitive, square-free {@code Polynomial}s with positive leading coefficients.</li>
     * </ul>
     *
     * @return a square-free factorization of {@code this}
     */
    public @NotNull List<Polynomial> squareFreeFactor() {
        List<Polynomial> factors = new ArrayList<>();
        Polynomial p = constantFactor().b;
        while (p != ONE) {
            Polynomial gcd = p.gcd(p.differentiate());
            factors.add(p.divideExact(gcd));
            p = gcd;
        }
        return factors;
    }

    /**
     * The no-cache version of {@link Polynomial#factor()}.
     */
    public @NotNull List<Polynomial> factorRaw() {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (this == ONE) return Collections.emptyList();
        //noinspection RedundantCast
        return sort((Iterable<Polynomial>) map(Polynomial::of, JasApi.factorPolynomial(coefficients)));
    }

    /**
     * Factors {@code this}. The result is sorted (see {@link Polynomial#compareTo(Polynomial)}). 0 cannot be factored.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is weakly increasing. All elements, except possibly the first, are non-constant, primitive,
     *  irreducible, and have a positive leading coefficient.</li>
     * </ul>
     *
     * @return the irreducible factors of {@code this}
     */
    public @NotNull List<Polynomial> factor() {
        return USE_FACTOR_CACHE ? FACTOR_CACHE.get(this) : factorRaw();
    }

    /**
     * Determines whether {@code this} is irreducible–that is, it has a positive leading coefficient and no nontrivial
     * factors, including constant factors.
     *
     * <li>{@code this} cannot be zero.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @return whether {@code this} is irreducible
     */
    public boolean isIrreducible() {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        return this == ONE ||
                degree() > 0 && signum() != -1 && (equals(X) || !coefficient(0).equals(BigInteger.ZERO)) &&
                factor().size() == 1;
    }

    /**
     * Given a {@code List} of {@code Pair}s of {@code BigInteger}s representing (x, y)-points, returns the
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
    public static @NotNull RationalPolynomial interpolate(@NotNull List<Pair<BigInteger, BigInteger>> points) {
        if (any(p -> p.a == null, points)) {
            throw new NullPointerException();
        }
        if (!unique(map(p -> p.a, points))) {
            throw new IllegalArgumentException();
        }
        int degree = points.size() - 1;
        List<Vector> rows = new ArrayList<>();
        //noinspection Convert2streamapi
        for (Pair<BigInteger, BigInteger> point : points) {
            rows.add(Vector.of(toList(take(degree + 1, iterate(r -> r.multiply(point.a), BigInteger.ONE)))));
        }
        Matrix matrix = Matrix.fromRows(rows);
        Vector yValues = Vector.of(toList(map(p -> p.b, points)));
        return RationalPolynomial.of(toList(matrix.solveLinearSystem(yValues).get()));
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
    public @NotNull Matrix companionMatrix() {
        if (this == ONE) {
            return Matrix.zero(0, 0);
        }
        if (!isMonic()) {
            throw new IllegalArgumentException("this must be monic. Invalid this: " + this);
        }
        int degree = degree();
        List<Vector> rows = new ArrayList<>();
        List<BigInteger> row = toList(replicate(degree - 1, BigInteger.ZERO));
        row.add(coefficients.get(0).negate());
        rows.add(Vector.of(row));
        for (int i = 1; i < degree; i++) {
            row.clear();
            for (int j = 1; j < degree; j++) {
                row.add(i == j ? BigInteger.ONE : BigInteger.ZERO);
            }
            row.add(coefficients.get(i).negate());
            rows.add(Vector.of(row));
        }
        return Matrix.fromRows(rows);
    }

    /**
     * Returns a {@code Matrix} containing the coefficients of a {@code List} of {@code Polynomial}s.
     *
     * <ul>
     *  <li>{@code ps} may not have more elements than one more than the maximum degree of {@code ps}.</li>
     *  <li>The result has a height less than or equal to its width, and its first column, if it exists, does not only
     *  contain zeros.</li>
     * </ul>
     *
     * Size is length({@code ps})×(max({deg(p)|p∈{@code ps}})+1)
     *
     * @param ps a {@code List} of {@code Polynomial}s
     * @return Mat({@code ps})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Matrix coefficientMatrix(@NotNull List<Polynomial> ps) {
        if (ps.isEmpty()) return Matrix.zero(0, 0);
        int width = maximum(map(Polynomial::degree, ps)) + 1;
        if (ps.size() > width) {
            throw new IllegalArgumentException("ps may not have more elements than one more than the maximum degree" +
                    " of ps. Invalid ps: " + ps);
        }
        List<Vector> rows = new ArrayList<>();
        for (Polynomial p : ps) {
            List<BigInteger> row = new ArrayList<>();
            for (int i = width - 1; i >= 0; i--) {
                row.add(p.coefficient(i));
            }
            rows.add(Vector.of(row));
        }
        return Matrix.fromRows(rows);
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
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
                coefficients.equals(((Polynomial) that).coefficients);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
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
     * "equal to", respectively. Asymptotic ordering is used; the ordering of two {@code Polynomial}s is the eventual
     * ordering of their values at a sufficiently large, positive argument.
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
    public int compareTo(@NotNull Polynomial that) {
        if (this == that) return 0;
        int thisSign = signum();
        int thatSign = that.signum();
        if (thisSign > thatSign) return 1;
        if (thisSign < thatSign) return -1;
        int c = BIG_INTEGER_ITERABLE_COMPARATOR.compare(reverse(abs()), reverse(that.abs()));
        return thisSign == -1 ? -c : c;
    }

    /**
     * Creates an {@code Polynomial} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link mho.qbar.objects.Polynomial#toString}. This method also takes
     * {@code exponentHandler}, which reads an exponent for a {@code String} if the {@code String} is valid.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>{@code exponentHandler} must terminate on all possible {@code String}s without throwing an exception, and
     *  cannot return nulls.</li>
     *  <li>The result may be any {@code Optional<Polynomial>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Polynomial}.
     * @return the wrapped {@code Polynomial} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    private static @NotNull Optional<Polynomial> genericRead(
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

        List<Pair<BigInteger, Integer>> monomials = new ArrayList<>();
        for (int i = monomialStrings.size() - 1; i >= 0; i--) {
            monomialString = monomialStrings.get(i);
            int xIndex = monomialString.indexOf('x');
            if (xIndex == -1) {
                Optional<BigInteger> constant = Readers.readBigInteger(monomialString);
                if (!constant.isPresent()) return Optional.empty();
                monomials.add(new Pair<>(constant.get(), 0));
            } else {
                BigInteger coefficient;
                switch (xIndex) {
                    case 0:
                        coefficient = BigInteger.ONE;
                        break;
                    case 1:
                        if (head(monomialString) != '-') return Optional.empty();
                        monomialString = tail(monomialString);
                        coefficient = IntegerUtils.NEGATIVE_ONE;
                        break;
                    default:
                        if (monomialString.charAt(xIndex - 1) != '*') return Optional.empty();
                        String coefficientString = monomialString.substring(0, xIndex - 1);
                        Optional<BigInteger> oCoefficient = Readers.readBigInteger(coefficientString);
                        if (!oCoefficient.isPresent()) return Optional.empty();
                        coefficient = oCoefficient.get();
                        // no 1*x, -1*x, 1*x^2, -1*x^2, ... allowed
                        if (coefficient.abs().equals(BigInteger.ONE)) return Optional.empty();
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
        if (any(p -> BigInteger.ZERO.equals(p.a), monomials)) return Optional.empty();
        //noinspection RedundantCast
        if (!increasing((Iterable<Integer>) map(p -> p.b, monomials))) return Optional.empty();
        int degree = last(monomials).b;
        List<BigInteger> coefficients = toList(replicate(degree + 1, BigInteger.ZERO));
        for (Pair<BigInteger, Integer> monomial : monomials) {
            coefficients.set(monomial.b, monomial.a);
        }
        return Optional.of(new Polynomial(coefficients));
    }

    /**
     * Creates an {@code Polynomial} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link mho.qbar.objects.Polynomial#toString}. Caution: It's easy to run out of time and
     * memory reading something like {@code "x^1000000000"}. If such an input is possible, consider using
     * {@link Polynomial#read(int, String)} instead.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Polynomial>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Polynomial}.
     * @return the wrapped {@code Polynomial} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<Polynomial> read(@NotNull String s) {
        return genericRead(s, Readers::readInteger);
    }

    /**
     * Creates an {@code Polynomial} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link mho.qbar.objects.Polynomial#toString}. The input {@code Polynomial} cannot have a
     * degree greater than {@code maxExponent}.
     *
     * <ul>
     *  <li>{@code maxExponent} must be positive.</li>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Polynomial>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Polynomial}.
     * @return the wrapped {@code Polynomial} (with degree no greater than {@code maxExponent}) represented by
     * {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<Polynomial> read(int maxExponent, @NotNull String s) {
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
     * Finds the first occurrence of an {@code Polynomial} in a {@code String}. Returns the {@code Polynomial} and the
     * index at which it was found. Returns an empty {@code Optional} if no {@code Polynomial} is found. Only
     * {@code String}s which could have been emitted by {@link mho.qbar.objects.Polynomial#toString} are recognized.
     * The longest possible {@code Polynomial} is parsed. Caution: It's easy to run out of time and memory finding
     * something like {@code "x^1000000000"}. If such an input is possible, consider using
     * {@link Polynomial#findIn(int, String)} instead.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code Polynomial} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<Polynomial, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(Polynomial::read, "*+-0123456789^x").apply(s);
    }

    /**
     * Finds the first occurrence of an {@code Polynomial} in a {@code String}. Returns the {@code Polynomial} and the
     * index at which it was found. Returns an empty {@code Optional} if no {@code Polynomial} is found. Only
     * {@code String}s which could have been emitted by {@link mho.qbar.objects.Polynomial#toString} are recognized.
     * The longest possible {@code Polynomial} is parsed. The input {@code Polynomial} cannot have a degree greater
     * than {@code maxExponent}.
     *
     * <ul>
     *  <li>{@code maxExponent} can be any {@code int}.</li>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code Polynomial} found in {@code s} (with degree no greater than {@code maxExponent}), and
     * the index at which it was found
     */
    public static @NotNull Optional<Pair<Polynomial, Integer>> findIn(int maxExponent, @NotNull String s) {
        return Readers.genericFindIn(t -> read(maxExponent, t), "*+-0123456789^x").apply(s);
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        if (this == ZERO) return "0";
        StringBuilder sb = new StringBuilder();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            BigInteger coefficient = coefficients.get(i);
            if (coefficient.equals(BigInteger.ZERO)) continue;
            String monomialString;
            if (i == 0) {
                monomialString = coefficient.toString();
            } else {
                String power = i == 1 ? "x" : "x^" + i;
                if (coefficient.equals(BigInteger.ONE)) {
                    monomialString = power;
                } else if (coefficient.equals(IntegerUtils.NEGATIVE_ONE)) {
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
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code Polynomial} used outside this
     * class.
     */
    public void validate() {
        assertTrue(this, all(r -> r != null, coefficients));
        if (!coefficients.isEmpty()) {
            assertTrue(this, !last(coefficients).equals(BigInteger.ZERO));
        }
        if (equals(ZERO)) {
            assertTrue(this, this == ZERO);
        }
        if (equals(ONE)) {
            assertTrue(this, this == ONE);
        }
    }
}
