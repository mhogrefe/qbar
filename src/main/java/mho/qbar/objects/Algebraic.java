package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.math.BinaryFraction;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertEquals;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>The {@code Algebraic} class uniquely represents real algebraic numbers. {@code minimalPolynomial} is the number's
 * minimal polynomial, {@code rootIndex} is the number of real roots of {@code minimalPolynomial} less than
 * {@code this}, and {@code rational} is the {@code Rational} equal to {@code this}, or empty if {@code this} is
 * irrational.</p>
 *
 * <p>There is only one instance of {@code ZERO} and one instance of {@code ONE}, so these may be compared with other
 * {@code Algebraic}s using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public class Algebraic implements Comparable<Algebraic> {
    /**
     * 0
     */
    public static final @NotNull Algebraic ZERO = new Algebraic(Rational.ZERO);

    /**
     * 1
     */
    public static final @NotNull Algebraic ONE = new Algebraic(Rational.ONE);

    /**
     * 10
     */
    public static final @NotNull Algebraic TEN = new Algebraic(Rational.TEN);

    /**
     * 2
     */
    public static final @NotNull Algebraic TWO = new Algebraic(Rational.TWO);

    /**
     * -1
     */
    public static final @NotNull Algebraic NEGATIVE_ONE = new Algebraic(Rational.NEGATIVE_ONE);

    /**
     * 1/2
     */
    public static final @NotNull Algebraic ONE_HALF = new Algebraic(Rational.ONE_HALF);

    /**
     * the square root of 2
     */
    public static final @NotNull Algebraic SQRT_TWO = of(Polynomial.read("x^2-2").get(), 1);

    /**
     * φ, the golden ratio
     */
    public static final @NotNull Algebraic PHI = of(Polynomial.read("x^2-x-1").get(), 1);

    /**
     * The minimal polynomial of {@code this}; the unique primitive, irreducible polynomial of minimal degree with
     * positive leading coefficient that has {@code this} as a root
     */
    private final @NotNull Polynomial minimalPolynomial;

    /**
     * The number of real roots of {@code minimalPolynomial} less than {@code this}
     */
    private final int rootIndex;

    /**
     * If {@code this} is rational, the {@code Rational} equal to {@code this}; otherwise, empty
     */
    private final @NotNull Optional<Rational> rational;

    /**
     * An {@code Interval} with finite, binary-fraction bounds that contains {@code this} and no other real root of
     * {@code minimalPolynomial}. This field is redundant, as it can be derived from {@code minimalPolynomial} and
     * {@code rootIndex}.
     */
    private final @NotNull Interval isolatingInterval;

    /**
     * The number of real roots of {@code minimalPolynomial}. This field is redundant, as it can be derived from
     * {@code minimalPolynomial}.
     */
    private final int mpRootCount;

    /**
     * Private constructor of rational {@code Algebraic}s.
     *
     * <ul>
     *  <li>{@code rational} cannot be null.</li>
     *  <li>Any rational {@code Algebraic} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param rational the {@code Rational} equal to {@code this}
     */
    private Algebraic(@NotNull Rational rational) {
        rootIndex = 0;
        minimalPolynomial = Polynomial.fromRoot(rational);
        this.rational = Optional.of(rational);
        isolatingInterval = Interval.of(rational);
        mpRootCount = 1;
    }

    /**
     * Private constructor of irrational {@code Algebraic}s; assumes arguments are valid.
     *
     * <ul>
     *  <li>{@code minimalPolynomial} must be of degree greater than 1, irreducible (see
     *  {@link Polynomial#isIrreducible()}) and must have at least one real root.
     *  <li>{@code rootIndex} cannot be negative.</li>
     *  <li>{@code rootIndex} must be less than the number of real roots of {@code minimalPolynomial}.</li>
     *  <li>{@code isolatingInterval} must be equal to
     *  {@code minimalPolynomial.powerOfTwoIsolatingInterval(rootIndex)} (see
     *  {@link Polynomial#powerOfTwoIsolatingInterval(int)}).</li>
     *  <li>{@code mpRootCount} cannot be negative.</li>
     *  <li>{@code mpRootCount} must be the number of real roots of {@code minimalPolynomial}</li>
     *  <li>Any irrational {@code Algebraic} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param minimalPolynomial the minimal polynomial of {@code this}
     * @param rootIndex the number of real roots of {@code minimalPolynomial} less than {@code this}
     * @param isolatingInterval an {@code Interval} with finite, binary-fraction bounds that contains {@code this} and
     * no other real roots of {@code minimalPolynomial}
     * @param mpRootCount the number of real roots of {@code minimalPolynomial}
     */
    private Algebraic(
            @NotNull Polynomial minimalPolynomial,
            int rootIndex,
            @NotNull Interval isolatingInterval,
            int mpRootCount
    ) {
        this.rootIndex = rootIndex;
        this.minimalPolynomial = minimalPolynomial;
        rational = Optional.empty();
        this.isolatingInterval = isolatingInterval;
        this.mpRootCount = mpRootCount;
    }

    /**
     * Creates an {@code Algebraic} as the {@code rootIndex}th real root of a {@code Polynomial}. {@code rootIndex} is
     * 0-based.
     *
     * <ul>
     *  <li>{@code polynomial} must be nonzero and must have at least one real root.</li>
     *  <li>{@code rootIndex} cannot be negative.</li>
     *  <li>{@code rootIndex} must be less than the number of real roots of {@code polynomial}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param polynomial a {@code Polynomial} of which the number of interest is a real root
     * @param rootIndex the number of real roots of {@code polynomial} less than the number of interest
     * @return the {@code Algebraic} satisfying the above properties
     */
    public static @NotNull Algebraic of(@NotNull Polynomial polynomial, int rootIndex) {
        if (rootIndex < 0) {
            throw new IllegalArgumentException("rootIndex cannot be negative. Invalid rootIndex: " + rootIndex);
        }
        if (polynomial == Polynomial.ZERO) {
            throw new IllegalArgumentException("polynomial cannot be zero.");
        }
        if (polynomial.degree() == 0) {
            throw new IllegalArgumentException("polynomial must have at least one real root. Invalid polynomial: " +
                    polynomial);
        }
        Polynomial squareFree = polynomial.squareFreePart();
        if (squareFree.degree() == 1) {
            if (rootIndex != 0) {
                throw new IllegalArgumentException("rootIndex must be less than the number of real roots of" +
                        " polynomial. rootIndex: " + rootIndex + ", number of real roots of " + polynomial + ": 1");
            }
            Rational r = Rational.of(squareFree.coefficient(0).negate(), squareFree.coefficient(1));
            if (r == Rational.ZERO) return ZERO;
            if (r == Rational.ONE) return ONE;
            return new Algebraic(r);
        }

        int rootCount = squareFree.rootCount();
        if (rootCount == 0) {
            throw new IllegalArgumentException("polynomial must have at least one real root. Invalid polynomial: " +
                    polynomial);
        }
        if (rootIndex >= rootCount) {
            throw new IllegalArgumentException("rootIndex must be less than the number of real roots of polynomial." +
                    " rootIndex: " + rootIndex + ", number of real roots of " + polynomial + ": " + rootCount);
        }
        List<Polynomial> factors = squareFree.factor();
        if (factors.size() == 1) {
            return new Algebraic(squareFree, rootIndex, squareFree.powerOfTwoIsolatingInterval(rootIndex), rootCount);
        }
        List<Pair<Polynomial, Integer>> polyRootPairs = new ArrayList<>();
        List<Real> realRoots = new ArrayList<>();
        List<Interval> isolatingIntervals = new ArrayList<>();
        List<Integer> rootCounts = new ArrayList<>();
        for (Polynomial factor : factors) {
            List<Interval> factorIsolatingIntervals = factor.powerOfTwoIsolatingIntervals();
            int factorRootCount = factor.rootCount();
            for (int i = 0; i < factorRootCount; i++) {
                polyRootPairs.add(new Pair<>(factor, i));
                realRoots.add(Real.root(factor::signum, factorIsolatingIntervals.get(i)));
                isolatingIntervals.add(factorIsolatingIntervals.get(i));
                rootCounts.add(factorRootCount);
            }
        }
        Interval isolatingInterval = squareFree.powerOfTwoIsolatingInterval(rootIndex);
        int matchIndex = Real.root(squareFree::signum, isolatingInterval).match(realRoots);
        Pair<Polynomial, Integer> pair = polyRootPairs.get(matchIndex);
        Polynomial factor = pair.a;
        rootIndex = pair.b;
        if (factor.degree() == 1) {
            Rational r = Rational.of(factor.coefficient(0).negate(), factor.coefficient(1));
            if (r == Rational.ZERO) return ZERO;
            if (r == Rational.ONE) return ONE;
            return new Algebraic(r);
        }
        return new Algebraic(factor, rootIndex, isolatingIntervals.get(matchIndex), rootCounts.get(matchIndex));
    }

    /**
     * Creates an {@code Algebraic} equal to a {@code Rational}.
     *
     * <ul>
     *  <li>{@code rational} cannot be null.</li>
     *  <li>The result is rational.</li>
     * </ul>
     *
     * @param rational a {@code Rational}
     * @return the {@code Algebraic} equal to {@code rational}
     */
    public static @NotNull Algebraic of(@NotNull Rational rational) {
        if (rational == Rational.ZERO) return ZERO;
        if (rational == Rational.ONE) return ONE;
        return new Algebraic(rational);
    }

    /**
     * Creates an {@code Algebraic} equal to a {@code BigInteger}.
     *
     * <ul>
     *  <li>{@code n} cannot be null.</li>
     *  <li>The result is an integer.</li>
     * </ul>
     *
     * @param n a {@code BigInteger}
     * @return the {@code Algebraic} equal to {@code n}
     */
    public static @NotNull Algebraic of(@NotNull BigInteger n) {
        if (n.equals(BigInteger.ZERO)) return ZERO;
        if (n.equals(BigInteger.ONE)) return ONE;
        return new Algebraic(Rational.of(n));
    }

    /**
     * Creates an {@code Algebraic} equal to a {@code long}.
     *
     * <ul>
     *  <li>{@code n} cannot be null.</li>
     *  <li>The result is an integer satisfying –2<sup>63</sup>≤x{@literal <}2<sup>63</sup>.</li>
     * </ul>
     *
     * @param n a {@code BigInteger}
     * @return the {@code Algebraic} equal to {@code n}
     */
    public static @NotNull Algebraic of(long n) {
        if (n == 0L) return ZERO;
        if (n == 1L) return ONE;
        return new Algebraic(Rational.of(n));
    }

    /**
     * Creates an {@code Algebraic} equal to an {@code int}.
     *
     * <ul>
     *  <li>{@code n} cannot be null.</li>
     *  <li>The result is an integer satisfying –2<sup>31</sup>≤x{@literal <}2<sup>31</sup>.</li>
     * </ul>
     *
     * @param n a {@code BigInteger}
     * @return the {@code Algebraic} equal to {@code n}
     */
    public static @NotNull Algebraic of(int n) {
        if (n == 0) return ZERO;
        if (n == 1) return ONE;
        return new Algebraic(Rational.of(n));
    }

    /**
     * Determines whether {@code this} is integral.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether this is an integer
     */
    public boolean isInteger() {
        return rational.isPresent() && rational.get().isInteger();
    }

    /**
     * Rounds {@code this} to an integer according to {@code roundingMode}; see {@link java.math.RoundingMode} for
     * details.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>{@code roundingMode} may be any {@code RoundingMode}.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UNNECESSARY}, {@code this} must be an
     *  integer.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param roundingMode determines the way in which {@code this} is rounded. Options are
     * {@link java.math.RoundingMode#UP}, {@link java.math.RoundingMode#DOWN}, {@link java.math.RoundingMode#CEILING},
     * {@link java.math.RoundingMode#FLOOR}, {@link java.math.RoundingMode#HALF_UP},
     * {@link java.math.RoundingMode#HALF_DOWN}, {@link java.math.RoundingMode#HALF_EVEN}, and
     * {@link java.math.RoundingMode#UNNECESSARY}.
     * @return {@code this}, rounded
     */
    public @NotNull BigInteger bigIntegerValue(@NotNull RoundingMode roundingMode) {
        if (rational.isPresent()) {
            return rational.get().bigIntegerValue(roundingMode);
        } else {
            if (roundingMode == RoundingMode.UNNECESSARY) {
                throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be an integer. Invalid" +
                        " this: " + this);
            }
            return realValue().bigIntegerValue(roundingMode);
        }
    }

    /**
     * Rounds {@code this} to the nearest {@code BigInteger}, breaking ties with the half-even rule (see
     * {@link java.math.RoundingMode#HALF_EVEN}).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return {@code this}, rounded
     */
    public @NotNull BigInteger bigIntegerValue() {
        return bigIntegerValue(RoundingMode.HALF_EVEN);
    }

    /**
     * Returns the floor of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return ⌊{@code this}⌋;
     */
    public @NotNull BigInteger floor() {
        return bigIntegerValue(RoundingMode.FLOOR);
    }

    /**
     * Returns the ceiling of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return ⌈{@code this}⌉
     */
    public @NotNull BigInteger ceiling() {
        return bigIntegerValue(RoundingMode.CEILING);
    }

    /**
     * Converts {@code this} to a {@code BigInteger}. Throws an {@link java.lang.ArithmeticException} if {@code this}
     * is not integral.
     *
     * <ul>
     *  <li>{@code this} must be an integer.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return the {@code BigInteger} value of {@code this}
     */
    public @NotNull BigInteger bigIntegerValueExact() {
        if (rational.isPresent()) {
            return rational.get().bigIntegerValueExact();
        } else {
            throw new ArithmeticException("this must be an integer. Invalid this: " + this);
        }
    }

    /**
     * Converts {@code this} to a {@code byte}. Throws an {@link java.lang.ArithmeticException} if {@code this} is not
     * integral or outside of a {@code byte}'s range.
     *
     * <ul>
     *  <li>{@code this} must be an integer within a {@code byte}'s range.</li>
     *  <li>The result can be any {@code byte}.</li>
     * </ul>
     *
     * @return the {@code byte} value of {@code this}
     */
    public byte byteValueExact() {
        return bigIntegerValueExact().byteValueExact();
    }

    /**
     * Converts {@code this} to a {@code short}. Throws an {@link java.lang.ArithmeticException} if {@code this} is not
     * integral or outside of a {@code short}'s range.
     *
     * <ul>
     *  <li>{@code this} must be an integer within a {@code short}'s range.</li>
     *  <li>The result can be any {@code short}.</li>
     * </ul>
     *
     * @return the {@code short} value of {@code this}
     */
    public short shortValueExact() {
        return bigIntegerValueExact().shortValueExact();
    }

    /**
     * Converts {@code this} to an {@code int}. Throws an {@link java.lang.ArithmeticException} if {@code this} is not
     * integral or outside of an {@code int}'s range.
     *
     * <ul>
     *  <li>{@code this} must be an integer within an {@code int}'s range.</li>
     *  <li>The result can be any {@code int}.</li>
     * </ul>
     *
     * @return the {@code int} value of {@code this}
     */
    public int intValueExact() {
        return bigIntegerValueExact().intValueExact();
    }

    /**
     * Converts {@code this} to a {@code long}. Throws an {@link java.lang.ArithmeticException} if {@code this} is not
     * integral or outside of a {@code long}'s range.
     *
     * <ul>
     *  <li>{@code this} must be an integer within a {@code long}'s range.</li>
     *  <li>The result can be any {@code long}.</li>
     * </ul>
     *
     * @return the {@code long} value of {@code this}
     */
    public long longValueExact() {
        return bigIntegerValueExact().longValueExact();
    }

    /**
     * Determines whether {@code this} is an integer power of 2.
     *
     * <ul>
     *  <li>{@code this} must be positive.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is an integer power of two
     */
    public boolean isIntegerPowerOfTwo() {
        if (signum() != 1) {
            throw new ArithmeticException("this must be positive. Invalid this: " + this);
        }
        return rational.isPresent() && rational.get().isPowerOfTwo();
    }

    /**
     * Rounds {@code this} to the next-highest integer power of two. If {@code this} is an integer power of two, it is
     * returned.
     *
     * <ul>
     *  <li>{@code this} must be positive.</li>
     *  <li>The result is an integer power of two. (That is, it is equal to 2<sup>p</sup> for some integer p, but the
     *  result itself is not necessarily an integer.)</li>
     * </ul>
     *
     * @return the smallest integer power of 2 greater than or equal to {@code this}.
     */
    public @NotNull Rational roundUpToIntegerPowerOfTwo() {
        if (signum() != 1) {
            throw new ArithmeticException("this must be positive. Invalid this: " + this);
        }
        if (rational.isPresent()) {
            return rational.get().roundUpToPowerOfTwo();
        } else {
            return realValue().roundUpToIntegerPowerOfTwo();
        }
    }

    /**
     * Determines whether {@code this} is a binary fraction (whether it is rational and its denominator is a power of
     * 2).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is a binary fraction
     */
    public boolean isBinaryFraction() {
        return rational.isPresent() && rational.get().isBinaryFraction();
    }

    /**
     * Converts {@code this} to a {@code BinaryFraction}. Throws an {@link java.lang.ArithmeticException} if
     * {@code this} is not a binary fraction.
     *
     * <ul>
     *  <li>{@code this} must be a binary fraction (its denominator must be a power of 2.)</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return the {@code BinaryFraction} value of {@code this}
     */
    public @NotNull BinaryFraction binaryFractionValueExact() {
        if (this == ZERO) return BinaryFraction.ZERO;
        if (this == ONE) return BinaryFraction.ONE;
        if (rational.isPresent()) {
            return rational.get().binaryFractionValueExact();
        } else {
            throw new ArithmeticException("this must be a binary fraction. Invalid this: " + this);
        }
    }

    public @NotNull Polynomial minimalPolynomial() {
        return minimalPolynomial;
    }

    public int rootIndex() {
        return rootIndex;
    }

    public int degree() {
        return minimalPolynomial.degree();
    }

    public boolean isRational() {
        return rational.isPresent();
    }

    public boolean isAlgebraicInteger() {
        return minimalPolynomial.isMonic();
    }

    public @NotNull Real realValue() {
        if (rational.isPresent()) {
            return Real.of(rational.get());
        } else {
            return Real.root(minimalPolynomial::signum, isolatingInterval);
        }
    }

    public double doubleValue() {
        if (rational.isPresent()) {
            return rational.get().doubleValue();
        } else {
            return realValue().doubleValue();
        }
    }

    public int signum() {
        if (rational.isPresent()) {
            return rational.get().signum();
        } else {
            return realValue().signum();
        }
    }

    public @NotNull Algebraic negate() {
        if (this == ZERO) return ZERO;
        if (rational.isPresent()) {
            Rational r = rational.get();
            if (r.equals(Rational.NEGATIVE_ONE)) return ONE;
            return new Algebraic(r.negate());
        } else {
            return new Algebraic(
                    minimalPolynomial.reflect(),
                    mpRootCount - rootIndex - 1,
                    isolatingInterval.negate(),
                    mpRootCount
            );
        }
    }

    public @NotNull Algebraic invert() {
        if (this == ZERO) {
            throw new ArithmeticException();
        }
        if (this == ONE) return ONE;
        if (rational.isPresent()) {
            return new Algebraic(rational.get().invert());
        } else {
            Polynomial inverseMP = minimalPolynomial.invertRoots();
            int negativeRootCount = minimalPolynomial.rootCount(Interval.lessThanOrEqualTo(Rational.ZERO));
            int positiveRootCount = mpRootCount - negativeRootCount;
            int inverseRootIndex;
            if (signum() == 1) {
                inverseRootIndex = 2 * negativeRootCount + positiveRootCount - rootIndex - 1;
            } else {
                inverseRootIndex = negativeRootCount - rootIndex - 1;
            }
            Interval inverseIsolatingInterval = inverseMP.powerOfTwoIsolatingInterval(inverseRootIndex);
            return new Algebraic(inverseMP, inverseRootIndex, inverseIsolatingInterval, mpRootCount);
        }
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Object} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Algebraic algebraic = (Algebraic) that;
        if (rational.isPresent() != algebraic.rational.isPresent()) return false;
        if (rational.isPresent()) {
            return rational.get().equals(algebraic.rational.get());
        } else {
            return rootIndex == algebraic.rootIndex && minimalPolynomial.equals(algebraic.minimalPolynomial);
        }
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        if (rational.isPresent()) {
            return 31 + rational.get().hashCode();
        } else {
            return 31 * rootIndex + minimalPolynomial.hashCode();
        }
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that the {@code Algebraic} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull Algebraic that) {
        if (this == that) return 0;
        if (minimalPolynomial.equals(that.minimalPolynomial)) {
            return Integer.compare(rootIndex, that.rootIndex);
        }
        if (rational.isPresent() && that.rational.isPresent()) {
            return rational.get().compareTo(that.rational.get());
        } else if (rational.isPresent()) {
            return -that.realValue().compareTo(rational.get());
        } else if (that.rational.isPresent()) {
            return realValue().compareTo(that.rational.get());
        } else {
            return realValue().compareTo(that.realValue());
        }
    }

    /**
     * Creates an {@code Algebraic} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link Algebraic#toString}. This method also takes {@code polynomialHandler}, which reads
     * a {@code Polynomial} from a {@code String} if the {@code String} is valid.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>{@code exponentHandler} must terminate on all possible {@code String}s without throwing an exception, and
     *  cannot return nulls.</li>
     *  <li>The result may be any {@code Optional<Algebraic>}.</li>
     * </ul>
     *
     * @param s a string representation of an {@code Algebraic}.
     * @return the wrapped {@code Algebraic} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    private static @NotNull Optional<Algebraic> genericRead(
            @NotNull String s,
            @NotNull Function<String, Optional<Polynomial>> polynomialHandler
    ) {
        if (s.startsWith("root ")) {
            s = s.substring(5);
            int ofIndex = s.indexOf(" of ");
            if (ofIndex == -1) return Optional.empty();
            Optional<Integer> oRootIndex = Readers.readInteger(s.substring(0, ofIndex));
            if (!oRootIndex.isPresent()) return Optional.empty();
            int rootIndex = oRootIndex.get();
            if (rootIndex < 0) return Optional.empty();
            Optional<Polynomial> oMinimalPolynomial = polynomialHandler.apply(s.substring(ofIndex + 4));
            if (!oMinimalPolynomial.isPresent()) return Optional.empty();
            Algebraic x;
            try {
                x = of(oMinimalPolynomial.get(), rootIndex);
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
            if (x.toString().equals("root " + s)) {
                return Optional.of(x);
            } else {
                return Optional.empty();
            }
        } else if (s.contains("sqrt(")) {
            BigInteger denominator;
            int slashIndex = s.indexOf('/');
            boolean numeratorParens = false;
            if (slashIndex != -1) {
                if (head(s) == '(') {
                    numeratorParens = true;
                    if (s.charAt(slashIndex - 1) != ')') return Optional.empty();
                }
                Optional<BigInteger> oDenominator = Readers.readBigInteger(s.substring(slashIndex + 1));
                if (!oDenominator.isPresent()) return Optional.empty();
                denominator = oDenominator.get();
                if (denominator.signum() != 1 || denominator.equals(BigInteger.ONE)) return Optional.empty();
                if (numeratorParens) {
                    s = s.substring(1, slashIndex - 1);
                } else {
                    s = s.substring(0, slashIndex);
                }
            } else {
                denominator = BigInteger.ONE;
            }
            int plusIndex = s.indexOf('+');
            int minusIndex = s.indexOf('-', 1);
            BigInteger constant;
            if (plusIndex != -1) {
                Optional<BigInteger> oConstant = Readers.readBigInteger(s.substring(0, plusIndex));
                if (!oConstant.isPresent()) return Optional.empty();
                constant = oConstant.get();
                if (constant.equals(BigInteger.ZERO)) return Optional.empty();
                s = s.substring(plusIndex + 1);
            } else if (minusIndex != -1) {
                Optional<BigInteger> oConstant = Readers.readBigInteger(s.substring(0, minusIndex));
                if (!oConstant.isPresent()) return Optional.empty();
                constant = oConstant.get();
                if (constant.equals(BigInteger.ZERO)) return Optional.empty();
                s = s.substring(minusIndex);
            } else {
                if (numeratorParens) return Optional.empty();
                constant = BigInteger.ZERO;
            }
            BigInteger beforeRadical;
            int starIndex = s.indexOf('*');
            if (starIndex == -1) {
                if (s.isEmpty()) return Optional.empty();
                if (head(s) == '-') {
                    beforeRadical = IntegerUtils.NEGATIVE_ONE;
                    s = tail(s);
                } else {
                    beforeRadical = BigInteger.ONE;
                }
            } else {
                Optional<BigInteger> oBeforeRadical = Readers.readBigInteger(s.substring(0, starIndex));
                if (!oBeforeRadical.isPresent()) return Optional.empty();
                beforeRadical = oBeforeRadical.get();
                if (beforeRadical.equals(BigInteger.ZERO)) return Optional.empty();
                if (beforeRadical.abs().equals(BigInteger.ONE)) return Optional.empty();
                s = s.substring(starIndex + 1);
            }
            if (!s.startsWith("sqrt(")) return Optional.empty();
            if (last(s) != ')') return Optional.empty();
            Optional<BigInteger> oUnderRadical = Readers.readBigInteger(s.substring(5, s.length() - 1));
            if (!oUnderRadical.isPresent()) return Optional.empty();
            BigInteger underRadical = oUnderRadical.get();
            if (underRadical.signum() != 1) return Optional.empty();
            Optional<Algebraic> ox = fromQuadraticFormula(constant, beforeRadical, underRadical, denominator);
            if (!ox.isPresent()) return Optional.empty();
            return ox;
        } else {
            Optional<Rational> or = Rational.read(s);
            if (!or.isPresent()) return Optional.empty();
            return Optional.of(of(or.get()));
        }
    }

    /**
     * Given four {@code BigInteger}s, returns the {@code Algebraic} equal to
     * ({@code constant}+{@code beforeRadical}*sqrt({@code underRadical}))/{@code denominator}. If the arguments are
     * invalid–for example, if they share a nontrivial factor–an empty result is returned.
     *
     * <ul>
     *  <li>{@code constant} may be any {@code BigInteger}.</li>
     *  <li>{@code beforeRadical} cannot be zero.</li>
     *  <li>{@code underRadical} must be positive.</li>
     *  <li>{@code denominator} may be any {@code BigInteger}.</li>
     *  <li>The result is empty or has degree 2.</li>
     * </ul>
     *
     * @param constant the constant added to the radical in the quadratic formula
     * @param beforeRadical the factor the radical is multiplied by in the quadratic formula
     * @param underRadical the integer under the radical in the quadratic formula
     * @param denominator the denominator of the quadratic formula
     * @return the {@code Algebraic} described by a quadratic formula
     */
    private static @NotNull Optional<Algebraic> fromQuadraticFormula(
            @NotNull BigInteger constant,
            @NotNull BigInteger beforeRadical,
            @NotNull BigInteger underRadical,
            @NotNull BigInteger denominator
    ) {
        if (!MathUtils.gcd(Arrays.asList(constant, beforeRadical, denominator)).equals(BigInteger.ONE)) {
            return Optional.empty();
        }
        Rational a = Rational.of(denominator).shiftRight(1);
        Rational b = Rational.of(constant.negate());
        int rootIndex = beforeRadical.signum() == 1 ? 1 : 0;
        BigInteger discriminant = underRadical.multiply(beforeRadical.abs().pow(2));
        if (discriminant.bitLength() >= 32) return Optional.empty();
        if (!MathUtils.largestPerfectPowerFactor(2, discriminant).equals(beforeRadical.abs())) return Optional.empty();
        Rational c = b.pow(2).subtract(Rational.of(discriminant)).divide(a).shiftRight(2);
        Polynomial mp = RationalPolynomial.of(Arrays.asList(c, b, a)).constantFactor().b;
        return Optional.of(of(mp, rootIndex));
    }

    /**
     * Creates an {@code Algebraic} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link Algebraic#toString}. Caution: It's easy to run out of time and memory reading
     * something like {@code "x^1000000000"}. If such an input is possible, consider using
     * {@link Algebraic#read(int, String)} instead.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Algebraic>}.</li>
     * </ul>
     *
     * @param s a string representation of an {@code Algebraic}.
     * @return the wrapped {@code Algebraic} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<Algebraic> read(@NotNull String s) {
        return genericRead(s, Polynomial::read);
    }

    /**
     * Creates an {@code Algebraic} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link Algebraic#toString}. The input {@code Algebraic} cannot have a degree greater than
     * {@code maxDegree}.
     *
     * <ul>
     *  <li>{@code maxDegree} must be at least 2.</li>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Algbraic>}.</li>
     * </ul>
     *
     * @param maxDegree the largest accepted degree
     * @param s a string representation of an {@code Algebraic}.
     * @return the wrapped {@code Algebraic} (with degree no greater than {@code maxDegree}) represented by {@code s},
     * or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<Algebraic> read(int maxDegree, @NotNull String s) {
        if (maxDegree < 2) {
            throw new IllegalArgumentException("maxDegree must be at least 2. Invalid maxDegree: " + maxDegree);
        }
        return genericRead(s, t -> Polynomial.read(maxDegree, t));
    }

    /**
     * Finds the first occurrence of an {@code Algebraic} in a {@code String}. Returns the {@code Algebraic} and the
     * index at which it was found. Returns an empty {@code Optional} if no {@code Algebraic} is found. Only
     * {@code String}s which could have been emitted by {@link Algebraic#toString} are recognized. The longest possible
     * {@code Algebraic} is parsed. Caution: It's easy to run out of time and memory finding something like
     * {@code "x^1000000000"}. If such an input is possible, consider using {@link Algebraic#findIn(int, String)}
     * instead.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code Algebraic} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<Algebraic, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(Algebraic::read, " ()*+-/0123456789^foqrstx").apply(s);
    }

    /**
     * Finds the first occurrence of an {@code Algebraic} in a {@code String}. Returns the {@code Algebraic} and the
     * index at which it was found. Returns an empty {@code Optional} if no {@code Algebraic} is found. Only
     * {@code String}s which could have been emitted by {@link Algebraic#toString} are recognized. The longest possible
     * {@code Algebraic} is parsed. The input {@code Algebraic} cannot have a degree greater than {@code maxDegree}.
     *
     * <ul>
     *  <li>{@code maxDegree} must be at least 2.</li>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param maxDegree the largest accepted degree
     * @param s the input {@code String}
     * @return the first {@code Algebraic} found in {@code s} (with degree no greater than {@code maxDegree}), and the
     * index at which it was found
     */
    public static @NotNull Optional<Pair<Algebraic, Integer>> findIn(int maxDegree, @NotNull String s) {
        return Readers.genericFindIn(t -> read(maxDegree, t), " ()*+-/0123456789^foqrstx").apply(s);
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Algebraic}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        if (rational.isPresent()) {
            return rational.get().toString();
        } else {
            if (minimalPolynomial.degree() == 2) {
                BigInteger a = minimalPolynomial.coefficient(2);
                BigInteger b = minimalPolynomial.coefficient(1);
                BigInteger c = minimalPolynomial.coefficient(0);
                BigInteger discriminant = b.pow(2).subtract(a.multiply(c).shiftLeft(2));
                if (discriminant.bitLength() < 32) {
                    BigInteger beforeRadical = MathUtils.largestPerfectPowerFactor(2, discriminant);
                    BigInteger underRadical = discriminant.divide(beforeRadical.pow(2));
                    BigInteger denominator = a.shiftLeft(1);
                    BigInteger gcd = MathUtils.gcd(Arrays.asList(beforeRadical, denominator, b));
                    beforeRadical = beforeRadical.divide(gcd);
                    BigInteger constant = b.divide(gcd).negate();
                    denominator = denominator.divide(gcd);
                    boolean nonTrivialBeforeRadical = !beforeRadical.equals(BigInteger.ONE);
                    boolean nonTrivialConstant = !constant.equals(BigInteger.ZERO);
                    boolean nonTrivialDenominator = !denominator.equals(BigInteger.ONE);

                    StringBuilder sb = new StringBuilder();
                    if (nonTrivialDenominator && nonTrivialConstant) {
                        sb.append('(');
                    }
                    if (nonTrivialConstant) {
                        sb.append(constant);
                    }
                    if (rootIndex == 0) {
                        sb.append('-');
                    } else if (nonTrivialConstant) {
                        sb.append('+');
                    }
                    if (nonTrivialBeforeRadical) {
                        sb.append(beforeRadical).append('*');
                    }
                    sb.append("sqrt(").append(underRadical).append(')');
                    if (nonTrivialDenominator) {
                        if (nonTrivialConstant) {
                            sb.append(')');
                        }
                        sb.append('/').append(denominator);
                    }
                    return sb.toString();
                }
            }
            return "root " + rootIndex + " of " + minimalPolynomial;
        }
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code Algebraic} used outside this
     * class.
     */
    public void validate() {
        if (rational.isPresent()) {
            assertEquals(this, rootIndex, 0);
            assertEquals(this, minimalPolynomial, Polynomial.fromRoot(rational.get()));
            assertEquals(this, isolatingInterval, Interval.of(rational.get()));
            assertEquals(this, mpRootCount, 1);
        } else {
            assertTrue(this, minimalPolynomial.degree() > 1);
            assertTrue(this, minimalPolynomial.isIrreducible());
            assertTrue(this, rootIndex >= 0);
            assertTrue(this, rootIndex < mpRootCount);
            assertEquals(this, isolatingInterval, minimalPolynomial.powerOfTwoIsolatingInterval(rootIndex));
            assertEquals(this, mpRootCount, minimalPolynomial.rootCount());
        }
        if (equals(ZERO)) {
            assertTrue(this, this == ZERO);
        }
        if (equals(ONE)) {
            assertTrue(this, this == ONE);
        }
    }
}
