package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.math.MathUtils;
import mho.wheels.misc.Readers;
import mho.wheels.ordering.comparators.ShortlexComparator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static org.junit.Assert.assertTrue;

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
        Function<BigInteger, BigInteger>,
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
        return new Iterator<BigInteger>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < coefficients.size();
            }

            @Override
            public BigInteger next() {
                return coefficients.get(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
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
    public @NotNull Rational apply(@NotNull Rational x) {
        return foldr((c, y) -> x.multiply(y).add(Rational.of(c)), Rational.ZERO, coefficients);
    }

    /**
     * Converts this to a {@code RationalPolynomial}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is a {@code RationalPolynomial} with integral coefficients.</li>
     * </ul>
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
        if (any(i -> i == null, coefficients))
            throw new NullPointerException();
        int actualSize = coefficients.size();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            if (coefficients.get(i).equals(BigInteger.ZERO)) {
                actualSize--;
            } else {
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
        if (p < 0)
            throw new IllegalArgumentException("power cannot be negative");
        if (c.equals(BigInteger.ZERO)) return ZERO;
        if (p == 0 && c.equals(BigInteger.ONE)) return ONE;
        return new Polynomial(toList(concat(replicate(p, BigInteger.ZERO), Collections.singletonList(c))));
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
        if (coefficients.size() == 1 && coefficients.get(0).equals(BigInteger.valueOf(-1))) return ONE;
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
     * @return sgn(p(∞))
     */
    @SuppressWarnings("JavaDoc")
    public int signum() {
        return this == ZERO ? 0 : leading().get().signum();
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
        if (bits < 0)
            throw new ArithmeticException("bits cannot be negative");
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
        if (any(x -> x == null, xs))
            throw new NullPointerException();
        return foldl(Polynomial::multiply, ONE, xs);
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
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
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
        if (p < 0)
            throw new ArithmeticException("cannot raise to a negative power");
        if (p == 0) return ONE;
        if (p == 1) return this;
        Polynomial result = ONE;
        Polynomial powerPower = null; // p^2^i
        for (boolean bit : MathUtils.bits(p)) {
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
        if (coefficients.size() < 2) return ZERO;
        if (equals(X)) return ONE;
        return new Polynomial(
                toList(zipWith((c, i) -> c.multiply(BigInteger.valueOf(i)), tail(coefficients), rangeUp(1)))
        );
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
        Optional<BigInteger> leading = leading();
        return leading.isPresent() && leading.get().equals(BigInteger.ONE);
    }

    /**
     * Determines whether {@code this} is primitive–whether its leading coefficient is positive and the GCD of its
     * coefficients is 1. 0 is not primitive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is primitive
     */
    public boolean isPrimitive() {
        return signum() == 1 && foldl(BigInteger::gcd, BigInteger.ZERO, coefficients).equals(BigInteger.ONE);
    }

    /**
     * Returns a {@code Pair} containing {@code this}'s content and primitive part. The primitive part is a constant
     * multiple of {@code this} whose coefficients have a GCD of 1 and whose leading coefficient is positive, and the
     * content is {@code this} divided by the primitive part.
     *
     * <ul>
     *  <li>{@code this} must be nonzero.</li>
     *  <li>The result is a {@code Pair} both of whose elements are not null, whose first element is nonzero, and whose
     *  last element is primitive.</li>
     * </ul>
     *
     * @return (content({@code this}), primitive({@code this}))
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Pair<BigInteger, Polynomial> contentAndPrimitive() {
        if (this == ZERO)
            throw new ArithmeticException("cannot find content and primitive part of 0");
        if (coefficients.size() == 1) return new Pair<>(coefficients.get(0), ONE);
        BigInteger gcd = foldl(BigInteger::gcd, BigInteger.ZERO, coefficients);
        BigInteger divisor = signum() == -1 ? gcd.negate() : gcd;
        if (divisor.equals(BigInteger.ONE)) {
            return new Pair<>(BigInteger.ONE, this);
        } else {
            return new Pair<>(divisor, new Polynomial(toList(map(c -> c.divide(divisor), coefficients))));
        }
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
        List<BigInteger> thisAbsCoefficients = reverse(abs());
        List<BigInteger> thatAbsCoefficients = reverse(that.abs());
        int c = BIG_INTEGER_ITERABLE_COMPARATOR.compare(thisAbsCoefficients, thatAbsCoefficients);
        return thisSign == -1 ? -c : c;
    }

    /**
     * Creates an {@code Polynomial} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link mho.qbar.objects.Polynomial#toString}. See that method's tests and demos for
     * examples of valid input. Caution: It's easy to run out of time and memory reading something like
     * {@code "x^1000000000"}.
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
                        coefficient = BigInteger.valueOf(-1);
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
                        Optional<Integer> oPower = Readers.readInteger(powerString);
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
        if (!increasing((Iterable<Integer>) map(p -> p.b, monomials))) return Optional.empty();
        int degree = last(monomials).b;
        List<BigInteger> coefficients = toList(replicate(degree + 1, BigInteger.ZERO));
        for (Pair<BigInteger, Integer> monomial : monomials) {
            coefficients.set(monomial.b, monomial.a);
        }
        return Optional.of(new Polynomial(coefficients));
    }

    /**
     * Finds the first occurrence of an {@code Polynomial} in a {@code String}. Returns the {@code Polynomial} and the
     * index at which it was found. Returns an empty {@code Optional} if no {@code Polynomial} is found. Only
     * {@code String}s which could have been emitted by {@link mho.qbar.objects.Polynomial#toString} are recognized.
     * The longest possible {@code Polynomial} is parsed. Caution: It's easy to run out of time and memory finding
     * something like {@code "x^1000000000"}.
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
                } else if (coefficient.equals(BigInteger.valueOf(-1))) {
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
     * Ensures that {@code this} is valid. Must return true for any {@code Polynomial} used outside this class.
     */
    public void validate() {
        if (!coefficients.isEmpty()) {
            assertTrue(toString(), !last(coefficients).equals(BigInteger.ZERO));
        }
        if (equals(ZERO)) assertTrue(toString(), this == ZERO);
        if (equals(ONE)) assertTrue(toString(), this == ONE);
    }
}
