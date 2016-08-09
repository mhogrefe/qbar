package mho.qbar.objects;

import jas.JasApi;
import mho.wheels.concurrency.ResultCache;
import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
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
            new ResultCache<>(Polynomial::factorRaw, p -> p.degree() > 6, Function.identity());

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
     * A {@code HashMap} used by {@link Polynomial#addRoots(Polynomial)}.
     */
    private static final @NotNull Map<Variable, MultivariatePolynomial> ADD_ROOTS_SUB_MAP;
    static {
        Variable a = Variable.of(0);
        Variable b = Variable.of(1);
        MultivariatePolynomial difference = MultivariatePolynomial.of(
                Arrays.asList(
                        new Pair<>(Monomial.of(b), BigInteger.ONE),
                        new Pair<>(Monomial.of(a), IntegerUtils.NEGATIVE_ONE)
                )
        );
        ADD_ROOTS_SUB_MAP = new HashMap<>();
        ADD_ROOTS_SUB_MAP.put(a, difference);
    }

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
     *  <li>{@code x} cannot be null.</li>
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
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the argument
     * @return {@code this}({@code x})
     */
    @Override
    public @NotNull Rational apply(@NotNull Rational x) {
        if (this == ZERO) return Rational.ZERO;
        if (degree() == 0) return Rational.of(coefficients.get(0));
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
        if (degree() == 0) return coefficients.get(0);
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
     * Evaluates {@code this} at {@code x}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the argument
     * @return {@code this}({@code x})
     */
    public @NotNull Algebraic apply(@NotNull Algebraic x) {
        if (this == ZERO) return Algebraic.ZERO;
        if (degree() == 0) return Algebraic.of(coefficients.get(0));
        if (x.isRational()) {
            return Algebraic.of(apply(x.rationalValueExact()));
        }
        return toRationalPolynomial().apply(x);
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
     * Returns a degree-1 {@code Polynomial} whose root is a given {@code BigInteger}.
     *
     * <ul>
     *  <li>{@code i} cannot be null.</li>
     *  <li>The result is monic and has degree 1.</li>
     * </ul>
     *
     * @param i the root of a {@code Polynomial}
     * @return the minimal polynomial of {@code i}
     */
    public static @NotNull Polynomial fromRoot(@NotNull BigInteger i) {
        return new Polynomial(Arrays.asList(i.negate(), BigInteger.ONE));
    }

    /**
     * Returns a degree-1 {@code Polynomial} whose root is a given {@code Rational}.
     *
     * <ul>
     *  <li>{@code r} cannot be null.</li>
     *  <li>The result is primitive and has degree 1.</li>
     * </ul>
     *
     * @param r the root of a {@code Polynomial}
     * @return the minimal polynomial of {@code r}
     */
    public static @NotNull Polynomial fromRoot(@NotNull Rational r) {
        return new Polynomial(Arrays.asList(r.getNumerator().negate(), r.getDenominator()));
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
     * @return deg({@code this})
     */
    @SuppressWarnings("JavaDoc")
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
     * Multiplies {@code this} by a power of x.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code p} cannot be negative.</li>
     * </ul>
     *
     * Length is deg({@code this})+{@code p}+1
     *
     * @param p the power of x that {@code this} is multiplied by
     * @return {@code this}×x<sup>{@code p}</sup>
     */
    public @NotNull Polynomial multiplyByPowerOfX(int p) {
        if (p < 0) {
            throw new ArithmeticException("p cannot be negative. Invalid p: " + p);
        }
        if (this == ZERO || p == 0) return this;
        return new Polynomial(toList(concat(replicate(p, BigInteger.ZERO), coefficients)));
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
     *  <li>{@code that} may be any {@code int}.</li>
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
     * Returns the {@code this} divided by {@code that}, assuming that {@code that} divides each coefficient of
     * {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code that must divide each coefficient of {@code this}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if {@code this} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code BigInteger} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull Polynomial divideExact(@NotNull BigInteger that) {
        if (that.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (this == ZERO) return ZERO;
        if (that.equals(BigInteger.ONE)) return this;
        List<BigInteger> quotientCoefficients = toList(map(c -> c.divide(that), coefficients));
        if (quotientCoefficients.size() == 1 && quotientCoefficients.get(0).equals(BigInteger.ONE)) return ONE;
        return new Polynomial(quotientCoefficients);
    }

    /**
     * Returns the {@code this} divided by {@code that}, assuming that {@code that} divides each coefficient of
     * {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code that must divide each coefficient of {@code this}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 0 if {@code this} is 0, or deg({@code this}))+1 otherwise
     *
     * @param that the {@code int} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull Polynomial divideExact(int that) {
        if (that == 0) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (this == ZERO) return ZERO;
        if (that == 1) return this;
        List<BigInteger> quotientCoefficients = toList(map(c -> c.divide(BigInteger.valueOf(that)), coefficients));
        if (quotientCoefficients.size() == 1 && quotientCoefficients.get(0).equals(BigInteger.ONE)) return ONE;
        return new Polynomial(quotientCoefficients);
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
        if (this == ZERO || bits == 0) return this;
        List<BigInteger> shiftedCoefficients = toList(map(r -> r.shiftLeft(bits), coefficients));
        if (shiftedCoefficients.size() == 1 && shiftedCoefficients.get(0).equals(BigInteger.ONE)) return ONE;
        return new Polynomial(shiftedCoefficients);
    }

    /**
     * Returns the sum of all the {@code Polynomial}s in {@code xs}. If {@code xs} is empty, 0 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code Polynomial}.</li>
     * </ul>
     *
     * Length is at most max({deg(p)|p∈{@code xs}})+1
     *
     * @param xs a {@code List} of {@code Polynomial}s.
     * @return Σxs
     */
    public static @NotNull Polynomial sum(@NotNull List<Polynomial> xs) {
        //noinspection RedundantCast
        return of(
                toList(
                        map(
                                is -> sumBigInteger(toList(is)),
                                (Iterable<Iterable<BigInteger>>) transposePadded(BigInteger.ZERO, map(p -> p, xs))
                        )
                )
        );
    }

    /**
     * Returns the product of all the {@code Polynomial}s in {@code xs}. If {@code xs} is empty, 1 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code Polynomial}.</li>
     * </ul>
     *
     * Length is at most sum({deg(p)|p∈{@code xs}})+1
     *
     * @param xs a {@code List} of {@code Polynomial}s.
     * @return Πxs
     */
    public static @NotNull Polynomial product(@NotNull List<Polynomial> xs) {
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
        if (p == 0 || this == ONE) return ONE;
        if (this == ZERO) return ZERO;
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
                    toList(
                            zipWith(
                                    (c, i) -> c.multiply(BigInteger.valueOf(i)),
                                    tail(coefficients),
                                    ExhaustiveProvider.INSTANCE.positiveIntegers()
                            )
                    )
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
            return new Pair<>(content, divideExact(content));
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
     *  <li>The result is a {@code Pair} whose first element is nonzero and whose second element has a positive leading
     *  coefficient and no invertible constant factors.</li>
     * </ul>
     *
     * @return the constant integral factor of {@code this} with the same sign as {@code this} and the largest possible
     * absolute value, along with {@code this} divided by that factor
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
            return new Pair<>(factor, divideExact(factor));
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
     * deg(r){@literal <}deg({@code that}).
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
     * Returns a variant of the pseudo-quotient and pseudo-remainder when {@code this} is divided by {@code that}. To
     * be more precise, the result is (q, r) such that
     * {@code this}×leading({@code that})<sup>d</sup>={@code that}×q+r and deg(r){@literal <}deg({@code that}), where
     * d is the smallest even integer greater than or equal to deg({@code a})–deg({@code n})+1.
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
    public @NotNull Pair<Polynomial, Polynomial> evenPseudoDivide(@NotNull Polynomial that) {
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
        int d = m - n + 1;
        if ((d & 1) != 0) d++;
        List<BigInteger> r = toList(multiply(that.leading().get().pow(d)));
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
     * {@code this}×leading({@code that})<sup>d</sup>={@code that}×q+r and deg(r){@literal <}deg({@code that}), where
     * d is the smallest even integer greater than or equal to deg({@code a})–deg({@code n})+1.
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
    public @NotNull Polynomial evenPseudoRemainder(@NotNull Polynomial that) {
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = degree();
        int n = that.degree();
        if (m < n) {
            throw new ArithmeticException("The degree of this must be greater than or equal to the degree of that." +
                    " this: " + this + ", that: " + that);
        }
        int d = m - n + 1;
        if ((d & 1) != 0) d++;
        List<BigInteger> r = toList(multiply(that.leading().get().pow(d)));
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
        if (that == ONE) return this;
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
        if (that == ONE) return ZERO;
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
     *  <li>{@code this} cannot be zero, must have a positive leading coefficient, and must be primitive.</li>
     *  <li>The result is a list of primitive, square-free {@code Polynomial}s with positive leading coefficients.</li>
     * </ul>
     *
     * @return a square-free factorization of {@code this}
     */
    public @NotNull List<Polynomial> squareFreeFactor() {
        List<Polynomial> factors = new ArrayList<>();
        Polynomial p = this;
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
        if (degree() < 1) return Collections.singletonList(this);
        List<Polynomial> factors = new ArrayList<>();
        Pair<BigInteger, Polynomial> cf = constantFactor();
        if (!cf.a.equals(BigInteger.ONE)) {
            factors.add(of(cf.a));
        }
        Map<Polynomial, Integer> factorMultiset = new HashMap<>();
        for (Polynomial p : cf.b.squareFreeFactor()) {
            Integer frequency = factorMultiset.get(p);
            if (frequency == null) {
                frequency = 0;
            }
            factorMultiset.put(p, frequency + 1);
        }
        for (Map.Entry<Polynomial, Integer> entry : factorMultiset.entrySet()) {
            List<Polynomial> fs = toList(map(Polynomial::of, JasApi.factorSquareFree(toList(entry.getKey()))));
            for (int i = 0; i < entry.getValue(); i++) {
                factors.addAll(fs);
            }
        }
        return sort(factors);
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
     * Returns a square {@code PolynomialMatrix} derived from a {@code List} of {@code Polynomial}s.
     *
     * <ul>
     *  <li>{@code ps} may not have more elements than one more than the maximum degree of {@code ps}.</li>
     *  <li>The result is square, and all elements not in the last column are constant.</li>
     * </ul>
     *
     * Size is length({@code ps})×length({@code ps})
     *
     * @param ps a {@code List} of {@code Polynomial}s
     * @return Mat({@code ps})*
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull PolynomialMatrix augmentedCoefficientMatrix(@NotNull List<Polynomial> ps) {
        if (ps.isEmpty()) return PolynomialMatrix.zero(0, 0);
        Matrix coefficientMatrix = coefficientMatrix(ps);
        int m = ps.size();
        return PolynomialMatrix.of(
                coefficientMatrix.submatrix(
                        m == 0 ?
                                Collections.emptyList() :
                                toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m - 1)),
                        m < 2 ?
                                Collections.emptyList() :
                                toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m - 2))
                )
        ).augment(PolynomialMatrix.fromColumns(Collections.singletonList(PolynomialVector.of(ps))));
    }

    /**
     * Returns the polynomial determinant of a {@code List} of {@code Polynomial}s.
     *
     * <ul>
     *  <li>{@code ps} may not have more elements than one more than the maximum degree of {@code ps}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param ps a {@code List} of {@code Polynomial}s
     * @return pdet({@code ps})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Polynomial determinant(@NotNull List<Polynomial> ps) {
        Matrix coefficientMatrix = coefficientMatrix(ps);
        int m = ps.size();
        int n = coefficientMatrix.width();
        if (m == n) return of(coefficientMatrix.determinant());
        List<BigInteger> detCoefficients = new ArrayList<>();
        for (int i = 0; i <= n - m; i++) {
            Matrix sub = coefficientMatrix.submatrix(
                    toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m - 1)),
                    toList(
                            concat(
                                    m < 2 ?
                                            Collections.emptyList() :
                                            ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m - 2),
                                    Collections.singletonList(n - i - 1)
                            )
                    )
            );
            detCoefficients.add(sub.determinant());
        }
        return of(detCoefficients);
    }

    /**
     * Returns the Sylvester matrix of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is a Sylvester matrix.</li>
     * </ul>
     *
     * Size is (length({@code this})+length({@code that})–2)×(length({@code this})+length({@code that})–2)
     *
     * @param that a {@code Polynomial}
     * @return S<sub>{@code this},{@code that}</sub>
     */
    public @NotNull Matrix sylvesterMatrix(@NotNull Polynomial that) {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int thisDegree = degree();
        int thatDegree = that.degree();
        List<BigInteger> thisCoefficients = reverse(coefficients);
        List<BigInteger> thatCoefficients = reverse(that.coefficients);
        List<Vector> rows = new ArrayList<>();
        for (int i = 0; i < thatDegree; i++) {
            List<BigInteger> row = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                row.add(BigInteger.ZERO);
            }
            row.addAll(thisCoefficients);
            for (int j = 0; j < thatDegree - i - 1; j++) {
                row.add(BigInteger.ZERO);
            }
            rows.add(Vector.of(row));
        }
        for (int i = 0; i < thisDegree; i++) {
            List<BigInteger> row = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                row.add(BigInteger.ZERO);
            }
            row.addAll(thatCoefficients);
            for (int j = 0; j < thisDegree - i - 1; j++) {
                row.add(BigInteger.ZERO);
            }
            rows.add(Vector.of(row));
        }
        return Matrix.fromRows(rows);
    }

    /**
     * Returns the resultant of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that a {@code Polynomial}
     * @return Res({@code this},{@code that})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull BigInteger resultant(@NotNull Polynomial that) {
        return sylvesterMatrix(that).determinant();
    }

    /**
     * Returns the {@code j}th Sylvester-Habicht matrix of {@code this} and {@code that}. This matrix is useful in
     * defining subresultants and subresultant coefficients.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code this} must have a degree greater than {@code that}.</li>
     *  <li>{@code j} cannot be negative.</li>
     *  <li>{@code j} cannot be greater than the degree of {@code that}.</li>
     *  <li>The result is a Sylvester-Habicht matrix.</li>
     * </ul>
     *
     * Size is (length({@code this})+length({@code that})–2–2j)×(length({@code this})+length({@code that})–2–j)
     *
     * @param that a {@code Polynomial}
     * @param j the index of the matrix
     * @return SyHa<sub>j</sub>({@code this}, {@code that})
     */
    public @NotNull Matrix sylvesterHabichtMatrix(@NotNull Polynomial that, int j) {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int thisDegree = degree();
        int thatDegree = that.degree();
        if (thisDegree <= thatDegree) {
            throw new IllegalArgumentException("this must have a degree greater than that. this: " +
                    this + ", that: " + that);
        }
        if (j < 0) {
            throw new IllegalArgumentException("j cannot be negative. Invalid j: " + j);
        }
        if (j > thatDegree) {
            throw new IllegalArgumentException("j cannot be greater than the degree of that. j: " + j + ", that: " +
                    that);
        }
        List<BigInteger> thisCoefficients = reverse(coefficients);
        List<BigInteger> thatCoefficients = reverse(that.coefficients);
        List<Vector> rows = new ArrayList<>();
        for (int i = 0; i < thatDegree - j; i++) {
            List<BigInteger> row = new ArrayList<>();
            for (int k = 0; k < i; k++) {
                row.add(BigInteger.ZERO);
            }
            row.addAll(thisCoefficients);
            for (int k = 0; k < thatDegree - j - i - 1; k++) {
                row.add(BigInteger.ZERO);
            }
            rows.add(Vector.of(row));
        }
        for (int i = 0; i < thisDegree - j; i++) {
            List<BigInteger> row = new ArrayList<>();
            for (int k = 0; k < thisDegree - j - i - 1; k++) {
                row.add(BigInteger.ZERO);
            }
            row.addAll(thatCoefficients);
            for (int k = 0; k < i; k++) {
                row.add(BigInteger.ZERO);
            }
            rows.add(Vector.of(row));
        }
        return Matrix.fromRows(rows);
    }

    /**
     * Returns the {@code j}th signed subresultant coefficient of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code this} must have a degree greater than {@code that}.</li>
     *  <li>{@code j} cannot be negative.</li>
     *  <li>{@code j} cannot be greater than the degree of {@code this}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that a {@code Polynomial}
     * @param j the index of the coefficient
     * @return sRes<sub>j</sub>({@code this}, {@code that})
     */
    public @NotNull BigInteger signedSubresultantCoefficient(@NotNull Polynomial that, int j) {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int thisDegree = degree();
        int thatDegree = that.degree();
        if (j < 0) {
            throw new IllegalArgumentException("j cannot be negative. Invalid j: " + j);
        } else if (j <= thatDegree) {
            Matrix sylvesterHabichtMatrix = sylvesterHabichtMatrix(that, j);
            List<Integer> range = toList(
                    ExhaustiveProvider.INSTANCE.rangeIncreasing(0, sylvesterHabichtMatrix.height() - 1)
            );
            return sylvesterHabichtMatrix.submatrix(range, range).determinant();
        } else if (j < thisDegree - 1) {
            return BigInteger.ZERO;
        } else if (j == thisDegree - 1) {
            return that.leading().get();
        } else if (j == thisDegree) {
            return leading().get();
        } else {
            throw new IllegalArgumentException("j cannot be greater than the degree of this. j: " + j + ", this: " +
                    that);
        }
    }

    /**
     * Returns the {@code j}th Sylvester-Habicht polynomial matrix of {@code this} and {@code that}. This matrix is
     * useful in defining subresultants and subresultant coefficients.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code this} must have a degree greater than {@code that}.</li>
     *  <li>{@code j} cannot be negative.</li>
     *  <li>{@code j} cannot be greater than the degree of {@code that}.</li>
     *  <li>The result is a Sylvester-Habicht polynomial matrix.</li>
     * </ul>
     *
     * Size is (length({@code this})+length({@code that})–2–2j)×(length({@code this})+length({@code that})–2–j)
     *
     * @param that a {@code Polynomial}
     * @param j the index of the matrix
     * @return SyHaPol<sub>j</sub>({@code this}, {@code that})
     */
    public @NotNull PolynomialMatrix sylvesterHabichtPolynomialMatrix(@NotNull Polynomial that, int j) {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int thisDegree = degree();
        int thatDegree = that.degree();
        if (thisDegree <= thatDegree) {
            throw new IllegalArgumentException("this must have a degree greater than that. this: " +
                    this + ", that: " + that);
        }
        if (j < 0) {
            throw new IllegalArgumentException("j cannot be negative. Invalid j: " + j);
        }
        if (j > thatDegree) {
            throw new IllegalArgumentException("j cannot be greater than the degree of that. j: " + j + ", that: " +
                    that);
        }
        Matrix shm = sylvesterHabichtMatrix(that, j);
        Matrix left = shm.submatrix(
                shm.height() == 0 ?
                        Collections.emptyList() :
                        toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, shm.height() - 1)),
                shm.height() < 2 ?
                        Collections.emptyList() :
                        toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, shm.height() - 2))
        );
        List<Polynomial> ps = new ArrayList<>();
        for (int i = thatDegree - 1 - j; i >= 0; i--) {
            ps.add(multiplyByPowerOfX(i));
        }
        for (int i = 0; i < thisDegree - j; i++) {
            ps.add(that.multiplyByPowerOfX(i));
        }
        PolynomialMatrix lastColumn = PolynomialMatrix.fromColumns(Collections.singletonList(PolynomialVector.of(ps)));
        return PolynomialMatrix.of(left).augment(lastColumn);
    }

    /**
     * Returns the {@code j}th signed subresultant of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code this} must have a degree greater than {@code that}.</li>
     *  <li>{@code j} cannot be negative.</li>
     *  <li>{@code j} cannot be greater than the degree of {@code this}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that a {@code Polynomial}
     * @param j the index of the subresultant
     * @return sResP<sub>j</sub>({@code this}, {@code that})
     */
    public @NotNull Polynomial signedSubresultant(@NotNull Polynomial that, int j) {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int thisDegree = degree();
        int thatDegree = that.degree();
        if (j < 0) {
            throw new IllegalArgumentException("j cannot be negative. Invalid j: " + j);
        } else if (j <= thatDegree) {
            return sylvesterHabichtPolynomialMatrix(that, j).determinant();
        } else if (j < thisDegree - 1) {
            return ZERO;
        } else if (j == thisDegree - 1) {
            return that;
        } else if (j == thisDegree) {
            return this;
        } else {
            throw new IllegalArgumentException("j cannot be greater than the degree of this. j: " + j + ", this: " +
                    that);
        }
    }

    /**
     * Returns the signed subresultant sequence of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code this} must have a degree greater than {@code that}.</li>
     *  <li>The result contains no nulls.</li>
     * </ul>
     *
     * @param that a {@code Polynomial}
     * @return sResP<sub>deg({@code this})</sub>, ..., sResP<sub>0</sub>
     */
    public @NotNull List<Polynomial> signedSubresultantSequence(@NotNull Polynomial that) {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int p = degree();
        int q = that.degree();
        if (p <= q) {
            throw new IllegalArgumentException("this must have a degree greater than that. this: " +
                    this + ", that: " + that);
        }
        List<Polynomial> sResP = toList(replicate(p + 2, ZERO));
        List<BigInteger> s = toList(replicate(p + 1, BigInteger.ZERO));
        List<BigInteger> t = toList(s);
        BigInteger thatLeading = that.leading().get();
        boolean rps = MathUtils.reversePermutationSign(p - q);
        sResP.set(p + 1, this);
        sResP.set(p, that);
        Polynomial xp = that.multiply(thatLeading.pow(p - q - 1));
        sResP.set(q + 1, rps ? xp : xp.negate());
        s.set(p, BigInteger.ONE);
        BigInteger xi = thatLeading.pow(p - q);
        s.set(q, rps ? xi : xi.negate());
        t.set(p, BigInteger.ONE);
        t.set(p - 1, thatLeading);
        int i = p + 1;
        int j = p;
        while (sResP.get(j) != ZERO) {
            int k = sResP.get(j).degree();
            BigInteger denominator = s.get(j).multiply(t.get(i - 1));
            if (k == j - 1) {
                s.set(j - 1, t.get(j - 1));
                sResP.set(
                        k,
                        sResP.get(i).multiply(s.get(j - 1).pow(2)).remainderExact(sResP.get(j)).negate()
                                .divideExact(denominator)
                );
            } else if (k < j - 1) {
                s.set(j - 1, BigInteger.ZERO);
                for (int d = 1; d < j - k; d++) {
                    xi = t.get(j - 1).multiply(t.get(j - d)).divide(s.get(j));
                    t.set(j - d - 1, (d & 1) == 0 ? xi : xi.negate());
                }
                s.set(k, t.get(k));
                sResP.set(k + 1, sResP.get(j).multiply(s.get(k)).divideExact(t.get(j - 1)));
                sResP.set(
                        k,
                        sResP.get(i).multiply(t.get(j - 1).multiply(s.get(k))).remainderExact(sResP.get(j)).negate()
                                .divideExact(denominator)
                );
            }
            if (k > 0) {
                t.set(k - 1, sResP.get(k).leading().orElse(BigInteger.ONE));
            }
            i = j;
            j = k;
        }
        return reverse(tail(sResP));
    }

    /**
     * Returns the signed pseudo-remainder sequence of {@code this} and {@code that}, all of whose elements are
     * primitive. When applied to a polynomial and its derivative, the result is a Sturm sequence.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} and {@code that} cannot both be zero.</li>
     *  <li>The degree of {@code this} must be greater than or equal to the degree of {@code that}.</li>
     *  <li>The result contains no nulls.</li>
     * </ul>
     *
     * @param that a {@code Polynomial}
     * @return the primitive pseudo-remainder sequence of {@code this} and {@code that}
     */
    public @NotNull List<Polynomial> primitiveSignedPseudoRemainderSequence(@NotNull Polynomial that) {
        if (this == ZERO && that == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        List<Polynomial> sequence = new ArrayList<>();
        sequence.add(this == ZERO ? ZERO : contentAndPrimitive().b);
        if (that == ZERO) return sequence;
        sequence.add(that.contentAndPrimitive().b);
        for (int i = 0; ; i++) {
            Polynomial next = sequence.get(i).absolutePseudoRemainder(sequence.get(i + 1)).negate();
            if (next == ZERO) return sequence;
            sequence.add(next.contentAndPrimitive().b);
        }
    }

    /**
     * Returns a variation of the signed subresultant sequence which omits some terms while preserving its
     * Sturm-sequence property. When applied to a polynomial and its derivative, the result is a Sturm sequence.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code this} must have a degree greater than {@code that}.</li>
     *  <li>The result contains no nulls, and the degrees of its elements are strictly decreasing.</li>
     * </ul>
     *
     * @param that a {@code Polynomial}
     * @return the abbreviated signed subresultant sequence of {@code this} and {@code that}
     */
    public @NotNull List<Polynomial> abbreviatedSignedSubresultantSequence(@NotNull Polynomial that) {
        List<Polynomial> abbreviated = new ArrayList<>();
        Polynomial previous = null;
        for (Polynomial p : signedSubresultantSequence(that)) {
            if (p != ZERO) {
                if (previous == null || p.degree() < previous.degree()) {
                    abbreviated.add(p);
                    previous = p;
                } else if (p.signum() != previous.signum()) {
                    abbreviated.remove(abbreviated.size() - 1);
                    previous = abbreviated.isEmpty() ? null : last(abbreviated);
                }
            }
        }
        return abbreviated;
    }

    /**
     * Finds a {@code Rational} r greater than the absolute value of any real root of {@code this}, applies some
     * transformation to r, and then returns the {@code Interval} [–r, r].
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>{@code postProcessor} should not decrease the value of any positive {@code Rational}.</li>
     *  <li>The result is symmetric about 0 and has finite bounds.</li>
     * </ul>
     *
     * @param postProcessor a function that transforms the upper limit of the bounding interval
     * @return an {@code Interval} containing all real roots of {@code this}
     */
    private @NotNull Interval rootBoundHelper(@NotNull Function<Rational, Rational> postProcessor) {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero");
        }
        if (degree() < 1) return Interval.ZERO;
        BigInteger denominator = leading().get().abs();
        //noinspection RedundantCast
        Rational max = maximum(
                (Iterable<Rational>) map(c -> Rational.of(c.abs(), denominator), init(coefficients))
        ).add(Rational.ONE);
        max = postProcessor.apply(max);
        return Interval.of(max.negate(), max);
    }

    /**
     * Returns an {@code Interval} with finite bounds that contains all the real roots of {@code this}. This is a
     * modified Cauchy's bound.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is symmetric about 0 and has finite bounds.</li>
     * </ul>
     *
     * @return an {@code Interval} containing all real roots of {@code this}
     */
    public @NotNull Interval rootBound() {
        return rootBoundHelper(Function.identity());
    }

    /**
     * Returns an {@code Interval} with finite bounds that contains all the real roots of {@code this}. Additionally,
     * the upper bound is a power of two and the lower bound is its negative, which may speed up the bisection of the
     * interval.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is symmetric about 0 and has finite bounds. The upper bound is a power of two.</li>
     * </ul>
     *
     * @return an {@code Interval} containing all real roots of {@code this}
     */
    public @NotNull Interval powerOfTwoRootBound() {
        if (degree() == 0) {
            return Interval.of(Rational.NEGATIVE_ONE, Rational.ONE);
        }
        return rootBoundHelper(r -> Rational.of(r.roundUpToPowerOfTwo()));
    }

    /**
     * Counts the sign changes, ignoring zeros, when evaluating each polynomial of a Sturm sequence at a point.
     *
     * <ul>
     *  <li>{@code signChangeMap} must either be null or a correct mapping from some {@code Rational}s to sign
     *  changes.</li>
     *  <li>{@code sturmSequence} must be a Sturm sequence.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @param signChangeMap a mapping from {@code Rational}s to sign changes. If {@code signChangeMap} is not null, it
     * is used as a cache of sign changes.
     * @param sturmSequence a Sturm sequence
     * @param x a point
     * @return the number of sign changes in map(p({@code x}), p∈{@code sturmSequence})
     */
    private static int signChanges(
            Map<Rational, Integer> signChangeMap,
            @NotNull List<Polynomial> sturmSequence,
            @NotNull Rational x
    ) {
        if (signChangeMap != null) {
            Integer cachedChanges = signChangeMap.get(x);
            if (cachedChanges != null) {
                return cachedChanges;
            }
        }
        int changes = 0;
        int previousSign = 0;
        for (Polynomial p : sturmSequence) {
            int sign = p.signum(x);
            if (sign != 0) {
                if (sign == -previousSign) {
                    changes++;
                }
                previousSign = sign;
            }
        }
        if (signChangeMap != null) {
            signChangeMap.put(x, changes);
        }
        return changes;
    }

    /**
     * Returns the number of real roots of {@code this} in a given interval. {@code this} must be squarefree, so there
     * is no possibility of multiple roots.
     *
     * <ul>
     *  <li>{@code this} must be squarefree.</li>
     *  <li>{@code interval} cannot be null.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @param interval the interval which we are checking for roots
     * @return the number of roots in {@code interval} of {@code this}
     */
    public int rootCount(@NotNull Interval interval) {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (degree() < 1) return 0;
        if (!interval.isFinitelyBounded()) {
            interval = interval.intersection(powerOfTwoRootBound()).orElse(Interval.ZERO);
        }
        Rational lower = interval.getLower().get();
        Rational upper = interval.getUpper().get();
        int rootCount = signum(lower) == 0 ? 1 : 0;
        if (lower.equals(upper)) {
            return rootCount;
        }
        List<Polynomial> sturmSequence = primitiveSignedPseudoRemainderSequence(differentiate());
        rootCount += signChanges(null, sturmSequence, lower) - signChanges(null, sturmSequence, upper);
        return rootCount;
    }

    /**
     * Returns the number of real roots of {@code this}. {@code this} must be squarefree, so there is no possibility of
     * multiple roots.
     *
     * <ul>
     *  <li>{@code this} must be squarefree.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @return the number of real roots of {@code this}
     */
    public int rootCount() {
        return rootCount(powerOfTwoRootBound());
    }

    /**
     * Given a bound on all the real roots of {@code this} and a 0-based root index, returns an interval that contains
     * the {@code rootIndex}th real root of {@code this} and no other real root.
     *
     * <ul>
     *  <li>{@code this} must be squarefree.</li>
     *  <li>{@code signChangeMap} must either be null or a correct mapping from some {@code Rational}s to sign
     *  changes.</li>
     *  <li>{@code rootBound} must contain all real roots of {@code this} and have finite bounds.</li>
     *  <li>{@code rootIndex} cannot be negative.</li>
     *  <li>{@code rootIndex} must be less than the number of real roots of {@code this}.</li>
     *  <li>The result has finite bounds.</li>
     * </ul>
     *
     * @param signChangeMap a mapping from {@code Rational}s to sign changes. If {@code signChangeMap} is not null, it
     * is used as a cache of sign changes.
     * @param rootBound an {@code Interval} containing all real roots of {@code this}
     * @param rootIndex the index of a real of root of {@code this}, starting from 0
     * @return an interval that contains the {@code rootIndex}th real root of {@code this} and no other real root
     */
    private @NotNull Interval isolatingIntervalHelper(
            Map<Rational, Integer> signChangeMap,
            @NotNull List<Polynomial> sturmSequence,
            @NotNull Interval rootBound,
            int rootIndex
    ) {
        if (rootIndex < 0) {
            throw new ArithmeticException("rootIndex cannot be negative. Invalid rootIndex: " + rootIndex);
        }
        Rational lower = rootBound.getLower().get();
        Rational upper = rootBound.getUpper().get();
        boolean rootAtLower = signum(lower) == 0;
        int rootCount = rootAtLower ? 1 : 0;
        int lowerSignChanges = signChanges(signChangeMap, sturmSequence, lower);
        int upperSignChanges = signChanges(signChangeMap, sturmSequence, upper);
        rootCount += lowerSignChanges - upperSignChanges;
        if (rootIndex >= rootCount) {
            throw new ArithmeticException("rootIndex must be less than the number of real roots of this. rootIndex: " +
                    rootIndex + ", number of real roots of this: " + rootCount);
        }
        if (rootCount == 1) {
            return rootBound;
        }
        while (true) {
            Rational mid = lower.add(upper).shiftRight(1);
            int lowerMidRootCount = rootAtLower ? 1 : 0;
            int midSignChanges = signChanges(signChangeMap, sturmSequence, mid);
            lowerMidRootCount += lowerSignChanges - midSignChanges;
            if (rootIndex < lowerMidRootCount) {
                rootBound = Interval.of(lower, mid);
                rootCount = lowerMidRootCount;
                if (rootCount == 1) {
                    return rootBound;
                }
                upper = mid;
            } else {
                rootBound = Interval.of(mid, upper);
                boolean rootAtMid = signum(mid) == 0;
                if (rootAtMid) {
                    rootIndex++;
                    rootCount++;
                }
                rootIndex -= lowerMidRootCount;
                rootCount -= lowerMidRootCount;
                if (rootCount == 1) {
                    return rootBound;
                }
                lower = mid;
                rootAtLower = rootAtMid;
                lowerSignChanges = midSignChanges;
            }
        }
    }

    /**
     * Given a 0-based root index, returns an interval that contains the {@code rootIndex}th real root of {@code this}
     * and no other real root.
     *
     * <ul>
     *  <li>{@code this} must be squarefree.</li>
     *  <li>{@code rootIndex} cannot be negative.</li>
     *  <li>{@code rootIndex} must be less than the number of real roots of {@code this}.</li>
     *  <li>The result has finite bounds.</li>
     * </ul>
     *
     * @param rootIndex the index of a real of root of {@code this}, starting from 0
     * @return an interval that contains the {@code rootIndex}th real root of {@code this} and no other real root
     */
    public @NotNull Interval isolatingInterval(int rootIndex) {
        return isolatingIntervalHelper(
                null,
                primitiveSignedPseudoRemainderSequence(differentiate()),
                rootBound(),
                rootIndex
        );
    }

    /**
     * Given a 0-based root index, returns an interval that contains the {@code rootIndex}th real root of {@code this}
     * and no other real root. The interval may be larger than the one given by
     * {@link Polynomial#isolatingInterval(int)}, but its bounds are binary fractions and may have smaller numerators
     * and denominators.
     *
     * <ul>
     *  <li>{@code this} must be squarefree.</li>
     *  <li>{@code rootIndex} cannot be negative.</li>
     *  <li>{@code rootIndex} must be less than the number of real roots of {@code this}.</li>
     *  <li>The results are finite and are binary fractions.</li>
     * </ul>
     *
     * @param rootIndex the index of a real of root of {@code this}, starting from 0
     * @return an interval that contains the {@code rootIndex}th real root of {@code this} and no other real root
     */
    public @NotNull Interval powerOfTwoIsolatingInterval(int rootIndex) {
        return isolatingIntervalHelper(
                null,
                primitiveSignedPseudoRemainderSequence(differentiate()),
                powerOfTwoRootBound(),
                rootIndex
        );
    }

    /**
     * Given a bound on all the real roots of {@code this}, returns a {@code List} of {@code Interval}s such that for
     * each real root of {@code this}, there is exactly one {@code Interval} that contains that root and no others.
     *
     * <ul>
     *  <li>{@code this} must be squarefree.</li>
     *  <li>{@code rootBound} must contain all real roots of {@code this} and have finite bounds.</li>
     *  <li>Every element of the result has finite bounds.</li>
     * </ul>
     *
     * @param rootBound an {@code Interval} containing all real roots of {@code this}
     * @return a list of root-isolating {@code Interval}s of {@code this}
     */
    private @NotNull List<Interval> isolatingIntervalsHelper(@NotNull Interval rootBound) {
        List<Polynomial> sturmSequence = primitiveSignedPseudoRemainderSequence(differentiate());
        Map<Rational, Integer> signChangeMap = new HashMap<>();
        Rational lower = rootBound.getLower().get();
        Rational upper = rootBound.getUpper().get();
        boolean rootAtLower = signum(lower) == 0;
        int rootCount = rootAtLower ? 1 : 0;
        int lowerSignChanges = signChanges(signChangeMap, sturmSequence, lower);
        int upperSignChanges = signChanges(signChangeMap, sturmSequence, upper);
        rootCount += lowerSignChanges - upperSignChanges;
        List<Interval> isolatingIntervals = new ArrayList<>();
        for (int i = 0; i < rootCount; i++) {
            isolatingIntervals.add(isolatingIntervalHelper(signChangeMap, sturmSequence, rootBound, i));
        }
        return isolatingIntervals;
    }

    /**
     * Returns a {@code List} of {@code Interval}s such that for each real root of {@code this}, there is exactly one
     * {@code Interval} that contains that root and no others.
     *
     * <ul>
     *  <li>{@code this} must be squarefree.</li>
     *  <li>Every element of the result has finite bounds.</li>
     * </ul>
     *
     * @return a list of root-isolating {@code Interval}s of {@code this}
     */
    public @NotNull List<Interval> isolatingIntervals() {
        return isolatingIntervalsHelper(rootBound());
    }

    /**
     * Returns a {@code List} of {@code Interval}s such that for each real root of {@code this}, there is exactly one
     * {@code Interval} that contains that root and no others. The intervals may be larger than the ones given by
     * {@link Polynomial#isolatingIntervals()}, but their bounds are binary fractions and may have smaller numerators
     * and denominators.
     *
     * <ul>
     *  <li>{@code this} must be squarefree.</li>
     *  <li>Every element of the result has finite bounds, and all bounds are binary fractions.</li>
     * </ul>
     *
     * @return a list of root-isolating {@code Interval}s of {@code this}
     */
    public @NotNull List<Interval> powerOfTwoIsolatingIntervals() {
        return isolatingIntervalsHelper(powerOfTwoRootBound());
    }

    /**
     * Returns the reflection of {@code this} across the y-axis. If {@code this} has odd degree, the result is negated
     * as well; this preserves the sign of the leading coefficient. The roots of the result are the negatives of the
     * roots of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return (–1)<sup>deg({@code this})</sup>{@code this}(–x)
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Polynomial reflect() {
        if (degree() < 1) return this;
        List<BigInteger> reflectedCoefficients = new ArrayList<>();
        boolean negateResult = degree() % 2 == 0;
        for (int i = 0; i < coefficients.size(); i++) {
            reflectedCoefficients.add(i % 2 == 0 == negateResult ? coefficients.get(i) : coefficients.get(i).negate());
        }
        return of(reflectedCoefficients);
    }

    /**
     * Returns the translation of {@code this} by {@code t}. If {@code this} is irreducible, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code t} is not null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param t the amount that {@code this} is translated by in the x-direction
     * @return {@code this}(x–t)
     */
    public @NotNull Polynomial translate(@NotNull BigInteger t) {
        if (degree() < 1 || t.equals(BigInteger.ZERO)) return this;
        return substitute(fromRoot(t));
    }

    /**
     * Returns {@code this} translated by {@code t} and multiplied by
     * denominator({@code t})<sup>degree({@code this})</sup>.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code t} is not null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param t the amount that {@code this} is translated by in the x-direction
     * @return denominator({@code t})<sup>deg({@code this})</sup>{@code this}(x–t)
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Polynomial specialTranslate(@NotNull Rational t) {
        if (degree() < 1 || t == Rational.ZERO) return this;
        BigInteger denominator = t.getDenominator();
        BigInteger mutliplier = BigInteger.ONE;
        Polynomial r = fromRoot(t);
        Polynomial result = of(last(coefficients));
        for (int i = degree() - 1; i >= 0; i--) {
            mutliplier = mutliplier.multiply(denominator);
            result = result.multiply(r).add(of(coefficients.get(i).multiply(mutliplier)));
        }
        return result;
    }

    /**
     * Returns {@code this} translated by {@code t} and scaled to either zero or a primitive {@code Polynomial} with a
     * positive leading coefficient.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code t} is not null.</li>
     *  <li>The result is either zero or primitive with a positive leading coefficient.</li>
     * </ul>
     *
     * @param t the amount that {@code this} is translated by in the x-direction
     * @return {@code this}(x–t), scaled
     */
    public @NotNull Polynomial positivePrimitiveTranslate(@NotNull Rational t) {
        if (this == ZERO) return ZERO;
        if (degree() == 0) return ONE;
        return specialTranslate(t).constantFactor().b;
    }

    /**
     * Stretches a polynomial in the x-direction by a positive factor {@code f}, possibly scaling it vertically as well
     * in order for its coefficients to remain integers.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code f} must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param f a positive scaling factor
     * @return numerator({@code f})<sup>deg({@code this})</sup>{@code this}(x/f)
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Polynomial stretch(@NotNull Rational f) {
        if (f.signum() != 1) {
            throw new ArithmeticException("f must be positive. Invalid f: " + f);
        }
        int degree = degree();
        if (degree < 1 || f == Rational.ONE) return this;
        BigInteger numerator = f.getNumerator();
        BigInteger denominator = f.getDenominator();
        BigInteger multiplier = numerator.pow(degree);
        List<BigInteger> resultCoefficients = new ArrayList<>();
        for (int i = 0; i < coefficients.size(); i++) {
            resultCoefficients.add(coefficients.get(i).multiply(multiplier));
            if (i != degree) {
                multiplier = multiplier.divide(numerator).multiply(denominator);
            }
        }
        return new Polynomial(resultCoefficients);
    }

    /**
     * Returns {@code this} stretched in the x-direction by a positive factor {@code f} and scaled to either zero or a
     * primitive {@code Polynomial} with a positive leading coefficient.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code f} must be positive.</li>
     *  <li>The result is either zero or primitive with a positive leading coefficient.</li>
     * </ul>
     *
     * @param f a positive scaling factor
     * @return {@code this}(x/t), scaled
     */
    public @NotNull Polynomial positivePrimitiveStretch(@NotNull Rational f) {
        if (f.signum() != 1) {
            throw new ArithmeticException("f must be positive. Invalid f: " + f);
        }
        if (this == ZERO) return ZERO;
        if (degree() == 0) return ONE;
        return stretch(f).constantFactor().b;
    }

    /**
     * Returns {@code this} stretched so that the real roots of {@code this} are multiplied by 2<sup>bits</sup>.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code bits} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the power of two that the roots of {@code this} are multiplied by
     * @return (2<sup>deg({@code this)</sup>){@code this}(x/2<sup>{@code bits}</sup>)
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Polynomial shiftRootsLeft(int bits) {
        if (bits < 0) {
            throw new IllegalArgumentException("bits cannot be negative. Invalid bits: " + bits);
        }
        int degree = degree();
        if (degree < 1 || bits == 0) return this;
        int exponent = bits * degree;
        List<BigInteger> resultCoefficients = new ArrayList<>();
        for (BigInteger coefficient : coefficients) {
            resultCoefficients.add(coefficient.shiftLeft(exponent));
            exponent -= bits;
        }
        return new Polynomial(resultCoefficients);
    }

    /**
     * Returns {@code this} stretched so that the real roots of {@code this} are divided by 2<sup>bits</sup>.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code bits} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the power of two that the roots of {@code this} are divided by
     * @return {@code this}(2<sup>{@code bits}</sup>x)
     */
    public @NotNull Polynomial shiftRootsRight(int bits) {
        if (bits < 0) {
            throw new IllegalArgumentException("bits cannot be negative. Invalid bits: " + bits);
        }
        int degree = degree();
        if (degree < 1 || bits == 0) return this;
        int exponent = 0;
        List<BigInteger> resultCoefficients = new ArrayList<>();
        for (BigInteger coefficient : coefficients) {
            resultCoefficients.add(coefficient.shiftLeft(exponent));
            exponent += bits;
        }
        return new Polynomial(resultCoefficients);
    }

    /**
     * Returns {@code this} stretched by a factor of 2<sup>{@code bits}</sup> and scaled to either zero or a primitive
     * {@code Polynomial} with a positive leading coefficient.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code bits} cannot be negative.</li>
     *  <li>The result is either zero or primitive with a positive leading coefficient.</li>
     * </ul>
     *
     * @param bits the power of two that the roots of {@code this} are multiplied by
     * @return {@code this}(x/2<sup>{@code bits}</sup>), scaled
     */
    public @NotNull Polynomial positivePrimitiveShiftRootsLeft(int bits) {
        if (bits < 0) {
            throw new IllegalArgumentException("bits cannot be negative. Invalid bits: " + bits);
        }
        if (this == ZERO) return ZERO;
        if (degree() == 0) return ONE;
        return shiftRootsLeft(bits).constantFactor().b;
    }

    /**
     * Returns {@code this} stretched by a factor of 2<sup>–{@code bits}</sup> and scaled to either zero or a primitive
     * {@code Polynomial} with a positive leading coefficient.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code bits} cannot be negative.</li>
     *  <li>The result is either zero or primitive with a positive leading coefficient.</li>
     * </ul>
     *
     * @param bits the power of two that the roots of {@code this} are divided by
     * @return {@code this}(2<sup>{@code bits}</sup>x), scaled
     */
    public @NotNull Polynomial positivePrimitiveShiftRootsRight(int bits) {
        if (bits < 0) {
            throw new IllegalArgumentException("bits cannot be negative. Invalid bits: " + bits);
        }
        if (this == ZERO) return ZERO;
        if (degree() == 0) return ONE;
        return shiftRootsRight(bits).constantFactor().b;
    }

    /**
     * Returns a {@code Polynomial} whose roots are the inverses of the roots of {@code this}. If {@code this} has a
     * positive leading coefficient, so does the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is zero or has a positive leading coefficient.</li>
     * </ul>
     *
     * @return |x<sup>deg({@code this})</sup>{@code this}(1/x)|
     */
    public @NotNull Polynomial invertRoots() {
        if (degree() < 1) return abs();
        return of(reverse(coefficients)).abs();
    }

    /**
     * Returns a {@code Polynomial} whose roots are the {@code r}th roots of the roots of {@code this}. If {@code this}
     * has a positive leading coefficient, so does the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code r} must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param r the index of the root
     * @return {@code this}(x<sup>{@code r}</sup>)
     */
    public @NotNull Polynomial rootRoots(int r) {
        if (r < 1) {
            throw new IllegalArgumentException();
        }
        if (r == 1 || degree() < 1) return this;
        List<BigInteger> spacer = toList(replicate(r - 1, BigInteger.ZERO));
        return of(toList(intercalate(spacer, map(Collections::singletonList, coefficients))));
    }

    /**
     * Undoes the action of {@link Polynomial#rootRoots(int)}. If {@code this} is of the form p(x<sup>{@code r}</sup>),
     * returns p; otherwise, returns empty.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code r} must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param r the index of the root
     * @return p such that p(x<sup>{@code r}</sup>)={@code this}, if p exists
     */
    public @NotNull Optional<Polynomial> undoRootRoots(int r) {
        if (r < 1) {
            throw new IllegalArgumentException();
        }
        int degree = degree();
        if (degree < 1 || r == 1) return Optional.of(this);
        if (degree % r != 0) return Optional.empty();
        List<BigInteger> resultCoefficients = new ArrayList<>();
        int counter = 0;
        for (BigInteger c : coefficients) {
            if (counter == 0) {
                resultCoefficients.add(c);
                counter = r - 1;
            } else {
                if (!c.equals(BigInteger.ZERO)) return Optional.empty();
                counter--;
            }
        }
        return Optional.of(of(resultCoefficients));
    }

    /**
     * Returns a {@code Polynomial} whose roots are the sums of each pair of roots of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is primitive and has a positive leading coefficient.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} whose roots are being added to the roots of {@code this}
     * @return a {@code Polynomial} whose roots are the sums of the roots of {@code this} and {@code that}
     */
    public @NotNull Polynomial addRoots(@NotNull Polynomial that) {
        if (degree() < 1 || that.degree() < 1) return ONE;
        Variable a = Variable.of(0);
        return MultivariatePolynomial.of(this, a)
                .resultant(MultivariatePolynomial.of(that, a).substitute(ADD_ROOTS_SUB_MAP), a).constantFactor().b;
    }

    /**
     * Returns a {@code Polynomial} whose roots are the products of each pair of roots of {@code this} and
     * {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is primitive and has a positive leading coefficient.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} whose roots are being multiplied by the roots of {@code this}
     * @return a {@code Polynomial} whose roots are the products of the roots of {@code this} and {@code that}
     */
    public @NotNull Polynomial multiplyRoots(@NotNull Polynomial that) {
        if (degree() < 1 || that.degree() < 1) return ONE;
        Variable a = Variable.of(0);
        List<Pair<Monomial, BigInteger>> terms = new ArrayList<>();
        List<Integer> exponentVector = new ArrayList<>();
        exponentVector.add(that.coefficients.size());
        exponentVector.add(-1);
        for (BigInteger coefficient : that.coefficients) {
            exponentVector.set(0, exponentVector.get(0) - 1);
            exponentVector.set(1, exponentVector.get(1) + 1);
            terms.add(new Pair<>(Monomial.of(exponentVector), coefficient));
        }
        return MultivariatePolynomial.of(this, a).resultant(MultivariatePolynomial.of(terms), a).constantFactor().b;
    }

    /**
     * Expresses powers, from x<sup>0</sup>, to x<sup>{@code maxPower}</sup>, of any root of {@code this} as
     * polynomials in the root. The polynomials all have degrees less that the degree of {@code this}.
     *
     * <ul>
     *  <li>{@code this} must be monic and non-constant.</li>
     *  <li>{@code maxPower} cannot be negative.</li>
     *  <li>The result is nonempty and contains no nulls.</li>
     * </ul>
     *
     * Length is {@code maxPower}+1
     *
     * @param maxPower the maximum power of a root of {@code this}
     * @return x<sup>0</sup>, ..., x<sup>{@code maxPower}</sup> over the quotient ring ℤ[x]/{@code this}
     */
    public @NotNull List<Polynomial> powerTable(int maxPower) {
        if (maxPower < 0) {
            throw new IllegalArgumentException("maxPower cannot be negative. Invalid maxPower: " + maxPower);
        }
        if (!isMonic()) {
            throw new UnsupportedOperationException("this must be monic. Invalid this: " + this);
        }
        int degree = degree();
        if (degree < 1) {
            throw new UnsupportedOperationException("this cannot be constant. Invalid this: " + this);
        }
        List<Polynomial> powers = new ArrayList<>();
        for (int p = 0; p < degree; p++) {
            powers.add(of(BigInteger.ONE, p));
            if (p == maxPower) {
                return powers;
            }
        }
        Polynomial xDeg = of(toList(map(BigInteger::negate, init(coefficients))));
        powers.add(xDeg);
        Polynomial power = xDeg;
        for (int p = degree + 1; p <= maxPower; p++) {
            BigInteger highestCoefficient = power.coefficient(degree - 1);
            List<BigInteger> newCoefficients = new ArrayList<>(degree);
            newCoefficients.add(BigInteger.ZERO);
            for (int i = 0; i < degree - 1; i++) {
                newCoefficients.add(power.coefficient(i));
            }
            power = of(newCoefficients).add(xDeg.multiply(highestCoefficient));
            powers.add(power);
        }
        return powers;
    }

    /**
     * Expresses the {@code p}th power of any root of {@code this} as a polynomial in the root. The polynomial has a
     * degree less that the degree of {@code this}.
     *
     * <ul>
     *  <li>{@code this} must be monic and non-constant.</li>
     *  <li>{@code p} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power that a root of {@code this} is being raised to
     * @return x<sup>{@code p}</sup> over the quotient ring ℤ[x]/{@code this}
     */
    public @NotNull Polynomial rootPower(int p) {
        if (p < 0) {
            throw new IllegalArgumentException("p cannot be negative. Invalid p: " + p);
        }
        if (!isMonic()) {
            throw new UnsupportedOperationException("this must be monic. Invalid this: " + this);
        }
        int degree = degree();
        if (degree < 1) {
            throw new UnsupportedOperationException("this cannot be constant. Invalid this: " + this);
        }
        if (p < degree) {
            return of(BigInteger.ONE, p);
        }
        Polynomial power = ONE;
        for (boolean bit : IntegerUtils.bigEndianBits(p)) {
            power = power.pow(2);
            if (bit) {
                power = power.multiplyByPowerOfX(1);
            }
            power = power.remainderExact(this);
        }
        return power;
    }

    /**
     * The real roots of {@code this}, in ascending order.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is increasing and has no duplicates.</li>
     * </ul>
     *
     * @return all real x such that {@code this}(x)=0.
     */
    public @NotNull List<Algebraic> realRoots() {
        return Algebraic.roots(this);
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
     * Creates a {@code Polynomial} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link mho.qbar.objects.Polynomial#toString}. This method also takes
     * {@code exponentHandler}, which reads an exponent from a {@code String} if the {@code String} is valid.
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
    private static @NotNull Optional<Polynomial> genericReadStrict(
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
                Optional<BigInteger> constant = Readers.readBigIntegerStrict(monomialString);
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
                        Optional<BigInteger> oCoefficient = Readers.readBigIntegerStrict(coefficientString);
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
     * Creates a {@code Polynomial} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link mho.qbar.objects.Polynomial#toString}. Caution: It's easy to run out of time and
     * memory reading something like {@code "x^1000000000"}. If such an input is possible, consider using
     * {@link Polynomial#readStrict(int, String)} instead.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Polynomial>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Polynomial}.
     * @return the wrapped {@code Polynomial} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<Polynomial> readStrict(@NotNull String s) {
        return genericReadStrict(s, Readers::readIntegerStrict);
    }

    /**
     * Creates a {@code Polynomial} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link mho.qbar.objects.Polynomial#toString}. The input {@code Polynomial} cannot have a
     * degree greater than {@code maxExponent}.
     *
     * <ul>
     *  <li>{@code maxExponent} must be positive.</li>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Polynomial>}.</li>
     * </ul>
     *
     * @param maxExponent the largest accepted exponent
     * @param s a string representation of a {@code Polynomial}
     * @return the wrapped {@code Polynomial} (with degree no greater than {@code maxExponent}) represented by
     * {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<Polynomial> readStrict(int maxExponent, @NotNull String s) {
        if (maxExponent < 1) {
            throw new IllegalArgumentException("maxExponent must be positive. Invalid maxExponent: " + maxExponent);
        }
        return genericReadStrict(
                s,
                powerString -> {
                    Optional<Integer> oPower = Readers.readIntegerStrict(powerString);
                    return !oPower.isPresent() || oPower.get() > maxExponent ? Optional.<Integer>empty() : oPower;
                }
        );
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
