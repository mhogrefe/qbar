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

/**
 * <p>A univariate polynomial in x with {@link mho.qbar.objects.Rational} coefficients.
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code RationalPolynomial}s using {@code ==}. This is not true for {@code X}.
 *
 * <p>This class uses a little-endian dense representation; in other words, the coefficient of x<sup>i</sup> is located
 * at the ith position of the coefficient list. The list contains no trailing zeros. Zero is represented by the empty
 * list.
 *
 * <p>This class is immutable.
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
    public static final @NotNull RationalPolynomial ONE = new RationalPolynomial(Arrays.asList(Rational.ONE));

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
        return new Iterator<Rational>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < coefficients.size();
            }

            @Override
            public Rational next() {
                return coefficients.get(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Evaluates {@code this} at {@code x}. Explicitly taking powers of x turns out to be faster than Horner's method
     * (except for small {@code RationalPolynomial}s) due to the efficiency of {@code Rational} exponentiation as
     * compared to repeated {@code Rational} multiplication, which has to perform repeated GCD calculations.
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
        return Rational.sum(
                zipWith((c, i) -> c == Rational.ZERO ? Rational.ZERO : x.pow(i).multiply(c), coefficients, rangeUp(0))
        );
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
        if (any(i -> i == null, coefficients))
            throw new NullPointerException();
        int actualSize = coefficients.size();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            if (coefficients.get(i) == Rational.ZERO) {
                actualSize--;
            } else {
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
        return new RationalPolynomial(Arrays.asList(c));
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
        if (p < 0)
            throw new IllegalArgumentException("power cannot be negative");
        if (c == Rational.ZERO) return ZERO;
        if (p == 0 && c == Rational.ONE) return ONE;
        return new RationalPolynomial(toList(concat(replicate(p, Rational.ZERO), Arrays.asList(c))));
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
        //noinspection ConstantConditions
        return this == ZERO ? Optional.<Rational>empty() : Optional.of(last(coefficients));
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
        if (coefficients.size() == 1 && coefficients.get(0).equals(Rational.of(-1))) return ONE;
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
            for (int j = 0; j < that.coefficients.size(); j++) {
                Rational a = coefficients.get(i);
                if (a == Rational.ZERO) continue;
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
        if (this == ZERO) return ZERO;
        if (bits == 0) return this;
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
        if (this == ZERO) return ZERO;
        if (bits == 0) return this;
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
        //noinspection ConstantConditions
        return foldl(RationalPolynomial::multiply, ONE, xs);
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
        if (p < 0)
            throw new ArithmeticException("cannot raise to a negative power");
        if (p == 0) return ONE;
        if (p == 1) return this;
        RationalPolynomial result = ONE;
        RationalPolynomial powerPower = null; // p^2^i
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
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomial} substituted into {@code this}
     * @return {@code this}∘{@code that}
     */
    public @NotNull RationalPolynomial substitute(@NotNull RationalPolynomial that) {
        //noinspection ConstantConditions
        return foldr((c, y) -> y.multiply(that).add(of(c)), ZERO, coefficients);
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
        Optional<Rational> leading = leading();
        return leading.isPresent() && leading.get() == Rational.ONE;
    }

    /**
     * Divides {@code this} by a constant to make it monic. {@code this} cannot be 0.
     *
     * <ul>
     *  <li>{@code this} must be nonzero.</li>
     *  <li>The result is a monic {@code RationalPolynomial}.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return The constant multiple of {@code this} whose leading coefficient is 1.
     */
    public @NotNull RationalPolynomial makeMonic() {
        Optional<Rational> leading = leading();
        if (!leading.isPresent())
            throw new ArithmeticException("cannot make 0 monic");
        return leading.get() == Rational.ONE ? this : divide(leading.get());
    }

    /**
     * Returns a {@code Pair} containing {@code this}'s content and primitive part. The primitive part is a constant
     * multiple of {@code this} whose coefficients are integers with a GCD of 1 and whose leading coefficient is
     * positive, and the content is {@code this} divided by the primitive part.
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
    public @NotNull Pair<Rational, Polynomial> contentAndPrimitive() {
        if (this == ZERO)
            throw new ArithmeticException("cannot find content and primitive part of 0");
        Polynomial primitive = Polynomial.of(Rational.cancelDenominators(coefficients));
        if (primitive.signum() == -1) primitive = primitive.negate();
        return new Pair<>(leading().get().divide(primitive.leading().get()), primitive);
    }

    /**
     * Returns the quotient and remainder when {@code this} is divided by {@code that}. To be more precise, the result
     * is (q, r) such that {@code this}={@code that}×q+r and r=0 or deg(r)<deg({@code that}).
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be 0.</li>
     *  <li>Neither element of the result is null.</li>
     * </ul>
     *
     * @param that the {@code RationalPolynomial} {@code this} is divided by
     * @return ({@code this}/{@code that}, {@code this}%{code that})
     */
    public @NotNull Pair<RationalPolynomial, RationalPolynomial> divide(@NotNull RationalPolynomial that) {
        if (that == ZERO)
            throw new ArithmeticException("division by zero");
        int m = degree();
        int n = that.degree();
        if (m < n) return new Pair<>(ZERO, this);
        List<Rational> q = toList(replicate(m - n + 1, Rational.ZERO));
        List<Rational> r = toList(coefficients);
        for (int k = m - n; k >= 0; k--) {
            q.set(k, r.get(n + k).divide(that.coefficient(n)));
            for (int j = n + k - 1; j >= k; j--) {
                r.set(j, r.get(j).subtract(q.get(k).multiply(that.coefficient(j - k))));
            }
        }
        return new Pair<>(of(q), of(toList(take(n, r))));
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
     * @param that The {@code RationalPolynomial} to be compared with {@code this}
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
     * @param that The {@code RationalPolynomial} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull RationalPolynomial that) {
        if (this == that) return 0;
        int thisSign = signum();
        int thatSign = that.signum();
        if (thisSign > thatSign) return 1;
        if (thisSign < thatSign) return -1;
        List<Rational> thisAbsCoefficients = reverse(abs());
        List<Rational> thatAbsCoefficients = reverse(that.abs());
        int c = RATIONAL_ITERABLE_COMPARATOR.compare(thisAbsCoefficients, thatAbsCoefficients);
        return thisSign == -1 ? -c : c;
    }

    /**
     * Creates a {@code RationalPolynomial} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link mho.qbar.objects.RationalPolynomial#toString}. See that method's tests and
     * demos for examples of valid input. Caution: It's easy to run out of time and memory reading something like
     * {@code "x^1000000000"}.
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
                        coefficient = Rational.of(-1);
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
        if (any(p -> p.a == Rational.ZERO, monomials)) return Optional.empty();
        if (!increasing((Iterable<Integer>) map(p -> p.b, monomials))) return Optional.empty();
        @SuppressWarnings("ConstantConditions")
        int degree = last(monomials).b;
        List<Rational> coefficients = toList(replicate(degree + 1, Rational.ZERO));
        for (Pair<Rational, Integer> monomial : monomials) {
            //noinspection ConstantConditions
            coefficients.set(monomial.b, monomial.a);
        }
        return Optional.of(new RationalPolynomial(coefficients));
    }

    /**
     * Finds the first occurrence of a {@code RationalPolynomial} in a {@code String}. Returns the
     * {@code RationalPolynomial} and the index at which it was found. Returns an empty {@code Optional} if no
     * {@code RationalPolynomial} is found. Only {@code String}s which could have been emitted by
     * {@link mho.qbar.objects.RationalPolynomial#toString} are recognized. The longest possible
     * {@code RationalPolynomial} is parsed. Caution: It's easy to run out of time and memory finding something like
     * {@code "x^1000000000"}.
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
                } else if (coefficient.equals(Rational.of(-1))) {
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
}
