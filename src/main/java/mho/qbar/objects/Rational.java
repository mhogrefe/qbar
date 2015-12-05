package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.NoRemoveIterator;
import mho.wheels.math.BinaryFraction;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.BigDecimalUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.numberUtils.FloatingPointUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p>The {@code Rational} class uniquely represents rational numbers. {@code denominator} is the smallest positive
 * integer d such that {@code this}×d is an integer. {@code numerator} is the smallest integer n such that n/d equals
 * {@code this}. This means that n and d have no positive common factor greater than 1, and d is always positive.
 * Arithmetic algorithms taken from Knuth.</p>
 *
 * <p>There is only one instance of {@code ZERO} and one instance of {@code ONE}, so these may be compared with other
 * {@code Rational}s using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class Rational implements Comparable<Rational> {
    /**
     * 0
     */
    public static final @NotNull Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);

    /**
     * 1
     */
    public static final @NotNull Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

    /**
     * 10
     */
    public static final @NotNull Rational TEN = of(10);

    /**
     * 2
     */
    public static final @NotNull Rational TWO = of(2);

    /**
     * -1
     */
    public static final @NotNull Rational NEGATIVE_ONE = of(-1);

    /**
     * -1
     */
    public static final @NotNull Rational ONE_HALF = of(1, 2);

    /**
     * The smallest positive float value, or 2<sup>–149</sup>
     */
    public static final @NotNull Rational SMALLEST_FLOAT = ofExact(Float.MIN_VALUE).get();

    /**
     * The largest subnormal float value, or (2<sup>23</sup>–1)/2<sup>149</sup>
     */
    public static final @NotNull Rational LARGEST_SUBNORMAL_FLOAT =
            ofExact(predecessor(Float.MIN_NORMAL)).get();

    /**
     * The smallest positive normal float value, or 2<sup>–126</sup>
     */
    public static final @NotNull Rational SMALLEST_NORMAL_FLOAT = ofExact(Float.MIN_NORMAL).get();

    /**
     * The largest finite float value, or 2<sup>128</sup>–2<sup>104</sup>
     */
    public static final @NotNull Rational LARGEST_FLOAT = ofExact(Float.MAX_VALUE).get();

    /**
     * The smallest positive double value, or 2<sup>–1074</sup>
     */
    public static final @NotNull Rational SMALLEST_DOUBLE = ofExact(Double.MIN_VALUE).get();

    /**
     * The largest subnormal double value, or (2<sup>52</sup>–1)/2<sup>1074</sup>
     */
    public static final @NotNull Rational LARGEST_SUBNORMAL_DOUBLE =
            ofExact(predecessor(Double.MIN_NORMAL)).get();

    /**
     * The smallest positive normal double value, or 2<sup>–1022</sup>
     */
    public static final @NotNull Rational SMALLEST_NORMAL_DOUBLE = ofExact(Double.MIN_NORMAL).get();

    /**
     * The largest finite double value, or 2<sup>1024</sup>–2<sup>971</sup>
     */
    public static final @NotNull Rational LARGEST_DOUBLE = ofExact(Double.MAX_VALUE).get();

    /**
     * An {@code Iterable} that contains every harmonic number. Does not support removal.
     *
     * Length is infinite
     */
    public static final @NotNull Iterable<Rational> HARMONIC_NUMBERS =
            scanl(Rational::add, ONE, map(i -> new Rational(BigInteger.ONE, BigInteger.valueOf(i)), rangeUp(2)));

    /**
     * {@code this} times {@code denominator}
     */
    private final @NotNull BigInteger numerator;

    /**
     * The smallest positive {@code BigInteger} d such that {@code this}×d is an integer
     */
    private final @NotNull BigInteger denominator;

    /**
     * Private constructor from {@link BigInteger}s; assumes arguments are valid.
     *
     * <ul>
     *  <li>{@code numerator} cannot be null.</li>
     *  <li>{@code denominator} cannot be null or equal to 0.</li>
     *  <li>{@code numerator} and {@code denominator} cannot have a positive common factor greater than 1.</li>
     *  <li>Any {@code Rational} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param numerator the numerator
     * @param denominator the denominator
     */
    private Rational(@NotNull BigInteger numerator, @NotNull BigInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Returns this {@code Rational}'s numerator.
     *
     * <ul>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return the numerator
     */
    public @NotNull BigInteger getNumerator() {
        return numerator;
    }

    /**
     * Returns this {@code Rational}'s denominator.
     *
     * <ul>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return the denominator
     */
    public @NotNull BigInteger getDenominator() {
        return denominator;
    }

    /**
     * Creates a {@code Rational} from {@code BigInteger}s. Throws an exception if {@code denominator} is zero. Reduces
     * arguments and negates denominator if necessary.
     *
     * <ul>
     *  <li>{@code numerator} cannot be null.</li>
     *  <li>{@code denominator} cannot be null or equal to 0.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param numerator the numerator
     * @param denominator the denominator
     * @return the {@code Rational} corresponding to {@code numerator}/{@code denominator}
     */
    public static @NotNull Rational of(@NotNull BigInteger numerator, @NotNull BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("denominator cannot be equal to 0.");
        }
        if (numerator.equals(BigInteger.ZERO)) return ZERO;
        if (numerator.equals(denominator)) return ONE;
        BigInteger gcd = numerator.gcd(denominator);
        if (denominator.signum() < 0) gcd = gcd.negate();
        return new Rational(numerator.divide(gcd), denominator.divide(gcd));
    }

    /**
     * Creates a {@code Rational} from {@code longs}. Throws an exception if any args are null or {@code denominator}
     * is zero. Reduces arguments and negates {@code denominator} if necessary.
     *
     * <ul>
     *  <li>{@code numerator} can be any {@code long}.</li>
     *  <li>{@code denominator} cannot be equal to 0.</li>
     *  <li>The result is a {@code Rational} whose numerator and denominator both satisfy
     *  –2<sup>63</sup>≤x{@literal <}2<sup>63</sup>.</li>
     * </ul>
     *
     * @param numerator the numerator
     * @param denominator the denominator
     * @return the {@code Rational} corresponding to {@code numerator}/{@code denominator}
     */
    public static @NotNull Rational of(long numerator, long denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("denominator cannot be equal to 0.");
        }
        if (numerator == 0) return ZERO;
        if (numerator == denominator) return ONE;
        long gcd = MathUtils.gcd(numerator, denominator);
        if (denominator < 0) gcd = -gcd;
        return new Rational(BigInteger.valueOf(numerator / gcd), BigInteger.valueOf(denominator / gcd));
    }

    /**
     * Creates a {@code Rational} from {@code ints}. Throws an exception if any args are null or {@code denominator} is
     * zero. Reduces arguments and negates {@code denominator} if necessary.
     *
     * <ul>
     *  <li>{@code numerator} can be any {@code int}.</li>
     *  <li>{@code denominator} cannot be equal to 0.</li>
     *  <li>The result is a {@code Rational} whose numerator and denominator both satisfy
     *  –2<sup>31</sup>≤x{@literal <}2<sup>31</sup>.</li>
     * </ul>
     *
     * @param numerator the numerator
     * @param denominator the denominator
     * @return the {@code Rational} corresponding to {@code numerator}/{@code denominator}
     */
    public static @NotNull Rational of(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("denominator cannot be equal to 0.");
        }
        if (numerator == 0) return ZERO;
        if (numerator == denominator) return ONE;
        int gcd = MathUtils.gcd(numerator, denominator);
        if (denominator < 0) gcd = -gcd;
        return new Rational(BigInteger.valueOf(numerator / gcd), BigInteger.valueOf(denominator / gcd));
    }

    /**
     * Creates a {@code Rational} from a {@code BigInteger}.
     *
     * <ul>
     *  <li>{@code n} cannot be null.</li>
     *  <li>The result is an integral {@code Rational}.</li>
     * </ul>
     *
     * @param n the {@code BigInteger}
     * @return the {@code Rational} equal to {@code n}
     */
    public static @NotNull Rational of(@NotNull BigInteger n) {
        if (n.equals(BigInteger.ZERO)) return ZERO;
        if (n.equals(BigInteger.ONE)) return ONE;
        return new Rational(n, BigInteger.ONE);
    }

    /**
     * Creates a {@code Rational} from a {@code long}.
     *
     * <ul>
     *  <li>{@code n} can be any {@code long}.</li>
     *  <li>The result is an integral {@code Rational} x satisfying –2<sup>63</sup>≤x{@literal <}2<sup>63</sup>.</li>
     * </ul>
     *
     * @param n the {@code long}
     * @return the {@code Rational} equal to {@code n}
     */
    public static @NotNull Rational of(long n) {
        if (n == 0) return ZERO;
        if (n == 1) return ONE;
        return new Rational(BigInteger.valueOf(n), BigInteger.ONE);
    }

    /**
     * Creates a {@code Rational} from an {@code int}.
     *
     * <ul>
     *  <li>{@code n} can be any {@code int}.</li>
     *  <li>The result is an integral {@code Rational} satisfying
     *  –2<sup>31</sup>≤x{@literal <}2<sup>31</sup>.</li>
     * </ul>
     *
     * @param n the {@code int}
     * @return the {@code Rational} corresponding to {@code n}
     */
    public static @NotNull Rational of(int n) {
        if (n == 0) return ZERO;
        if (n == 1) return ONE;
        return new Rational(BigInteger.valueOf(n), BigInteger.ONE);
    }

    /**
     * Creates a {@code Rational} from a {@code BinaryFraction}.
     *
     * <ul>
     *  <li>{@code bf} cannot be null.</li>
     *  <li>The result is a {@code Rational} whose denominator is a power of 2.</li>
     * </ul>
     *
     * @param bf the {@code BinaryFraction}
     * @return the {@code Rational} corresponding to {@code bf}
     */
    public static @NotNull Rational of(@NotNull BinaryFraction bf) {
        if (bf == BinaryFraction.ZERO) return ZERO;
        if (bf == BinaryFraction.ONE) return ONE;
        int exponent = bf.getExponent();
        return exponent >= 0 ?
                of(bf.bigIntegerValueExact()) :
                of(bf.getMantissa(), BigInteger.ONE.shiftLeft(-exponent));
    }

    /**
     * Creates a {@code Rational} from a {@link float}. This method uses the {@code float}'s {@code String}
     * representation, so that 0.1f becomes 1/10 as might be expected. To get {@code float}'s exact,
     * sometimes-counterintuitive value, use {@link Rational#ofExact} instead. Returns empty if the {@code float} is
     * {@code Infinity}, {@code -Infinity}, or {@code NaN}.
     *
     * <ul>
     *  <li>{@code f} may be any {@code float}.</li>
     *  <li>The result is empty or a {@code Rational} whose decimal expansion is equal to the displayed decimal
     *  expansion of some {@code float}. A necessary but not sufficient condition for this is that the denominator is
     *  of the form 2<sup>m</sup>5<sup>n</sup>, with m,n≥0.</li>
     * </ul>
     *
     * @param f the {@code float}
     * @return the {@code Rational} corresponding to {@code f}, or empty if {@code f} is {@code Infinity},
     * {@code -Infinity}, or {@code NaN}
     */
    public static @NotNull Optional<Rational> of(float f) {
        if (f == 0.0) return Optional.of(ZERO);
        if (f == 1.0) return Optional.of(ONE);
        if (Float.isInfinite(f) || Float.isNaN(f)) return Optional.empty();
        return Optional.of(of(new BigDecimal(Float.toString(f))));
    }

    /**
     * Creates a {@code Rational} from a {@link double}. This method uses the {@code double}'s {@code String}
     * representation, so that 0.1 becomes 1/10 as might be expected. To get {@code double}'s exact,
     * sometimes-counterintuitive value, use {@link Rational#ofExact} instead. Returns empty if the {@code double} is
     * {@code Infinity}, {@code -Infinity}, or {@code NaN}.
     *
     * <ul>
     *  <li>{@code d} may be any {@code double}.</li>
     *  <li>The result is empty or a {@code Rational} whose decimal expansion is equal to the displayed decimal
     *  expansion of some {@code double}. A necessary but not sufficient conditions for this is that the denominator is
     *  of the form 2<sup>m</sup>5<sup>n</sup>, with m,n≥0.</li>
     * </ul>
     *
     * @param d the {@code double}
     * @return the {@code Rational} corresponding to {@code d}, or empty if {@code d} is {@code Infinity},
     * {@code -Infinity}, or {@code NaN}
     */
    public static @NotNull Optional<Rational> of(double d) {
        if (d == 0.0) return Optional.of(ZERO);
        if (d == 1.0) return Optional.of(ONE);
        if (Double.isInfinite(d) || Double.isNaN(d)) return Optional.empty();
        return Optional.of(of(BigDecimal.valueOf(d)));
    }

    /**
     * Creates a {@code Rational} from a {@code float}. No rounding occurs; the {@code Rational} has exactly the same
     * value as the {@code float}. For example, {@code of(1.0f/3.0f)} yields 11184811/33554432, not 1/3. Returns empty
     * if the {@code float} is {@code Infinity}, {@code -Infinity}, or {@code NaN}.
     *
     * <ul>
     *  <li>{@code f} may be any {@code float}.</li>
     *  <li>
     *   The result is empty or a {@code Rational} that may be exactly represented as a {@code float}. Here are some,
     *   but not all, of the conditions on the result:
     *   <ul>
     *    <li>The denominator is a power of 2 less than or equal to 2<sup>149</sup>.</li>
     *    <li>The numerator is less than or equal to 2<sup>128</sup>–2<sup>104</sup>.</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param f the {@code float}
     * @return the {@code Rational} corresponding to {@code f}, or empty if {@code f} is {@code Infinity},
     * {@code -Infinity}, or {@code NaN}
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Optional<Rational> ofExact(float f) {
        if (f == 0.0f) return Optional.of(ZERO);
        if (f == 1.0f) return Optional.of(ONE);
        Optional<Pair<Integer, Integer>> ome = toMantissaAndExponent(f);
        if (!ome.isPresent()) return Optional.empty();
        Pair<Integer, Integer> me = ome.get();
        return Optional.of(of(me.a).shiftLeft(me.b));
    }

    /**
     * Creates a {@code Rational} from a {@link double}. No rounding occurs; the {@code Rational} has exactly the same
     * value as the {@code double}. For example, {@code of(1.0/3.0)} yields 6004799503160661/18014398509481984, not
     * 1/3. Returns empty if the {@code double} is {@code Infinity}, {@code -Infinity}, or {@code NaN}.
     *
     * <ul>
     *  <li>{@code d} may be any {@code double}.</li>
     *  <li>
     *   The result is empty or a {@code Rational} that may be exactly represented as a {@code double}. Here are some,
     *   but not all, of the conditions on the result:
     *   <ul>
     *    <li>The denominator is a power of 2 less than or equal to 2<sup>1074</sup>.</li>
     *    <li>The numerator is less than or equal to 2<sup>1024</sup>–2<sup>971</sup>.</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param d the {@code double}
     * @return the {@code Rational} corresponding to {@code d}, or empty if {@code d} is {@code Infinity},
     * {@code -Infinity}, or {@code NaN}
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Optional<Rational> ofExact(double d) {
        if (d == 0.0) return Optional.of(ZERO);
        if (d == 1.0) return Optional.of(ONE);
        Optional<Pair<Long, Integer>> ome = toMantissaAndExponent(d);
        if (!ome.isPresent()) return Optional.empty();
        Pair<Long, Integer> me = ome.get();
        return Optional.of(of(me.a).shiftLeft(me.b));
    }

    /**
     * Creates a {@code Rational} from a {@link BigDecimal}.
     *
     * <ul>
     *  <li>{@code d} may not be null.</li>
     *  <li>The result is a {@code Rational} whose denominator may be written as 2<sup>m</sup>5<sup>n</sup>, with
     *  m,n≥0.</li>
     * </ul>
     *
     * @param d the {@code BigDecimal}
     * @return the {@code Rational} corresponding to {@code d}
     */
    public static @NotNull Rational of(@NotNull BigDecimal d) {
        return of(d.unscaledValue()).divide(TEN.pow(d.scale()));
    }

    /**
     * Determines whether {@code this} is integral.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether this is an integer
     */
    public boolean isInteger() {
        return denominator.equals(BigInteger.ONE);
    }

    /**
     * Rounds {@code this} to an integer according to {@code roundingMode}; see {@link java.math.RoundingMode} for
     * details.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
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
        if (denominator.equals(BigInteger.ONE)) {
            return numerator;
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be an integer. Invalid this: " +
                    this);
        }
        boolean positive = numerator.signum() == 1;
        BigInteger floor;
        BigInteger ceiling;
        if (positive) {
            floor = numerator.divide(denominator);
            ceiling = floor.add(BigInteger.ONE);
        } else {
            ceiling = numerator.divide(denominator);
            floor = ceiling.subtract(BigInteger.ONE);
        }
        switch (roundingMode) {
            case FLOOR:
                return floor;
            case CEILING:
                return ceiling;
            case DOWN:
                return positive ? floor : ceiling;
            case UP:
                return positive ? ceiling : floor;
        }
        switch (compare(fractionalPart(), ONE_HALF)) {
            case LT:
                return floor;
            case GT:
                return ceiling;
            case EQ:
                switch (roundingMode) {
                    case HALF_DOWN:
                        return positive ? floor : ceiling;
                    case HALF_UP:
                        return positive ? ceiling : floor;
                    case HALF_EVEN:
                        return floor.testBit(0) ? ceiling : floor;
                }
            default:
                throw new IllegalStateException("unreachable");
        }
    }

    /**
     * Rounds {@code this} to the nearest {@code BigInteger}, breaking ties with the half-even rule (see
     * {@link java.math.RoundingMode#HALF_EVEN}).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return {@code this}, rounded
     */
    public @NotNull BigInteger bigIntegerValue() {
        return bigIntegerValue(RoundingMode.HALF_EVEN);
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
        if (denominator.equals(BigInteger.ONE)) {
            return numerator;
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
     * Determines whether {@code this} is a power of 2.
     *
     * <ul>
     *  <li>{@code this} must be positive.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is a power of two
     */
    public boolean isPowerOfTwo() {
        if (signum() != 1) {
            throw new ArithmeticException("this must be positive. Invalid this: " + this);
        }
        return denominator.equals(BigInteger.ONE) && IntegerUtils.isPowerOfTwo(numerator) ||
                numerator.equals(BigInteger.ONE) && IntegerUtils.isPowerOfTwo(denominator);
    }

    /**
     * Determines whether {@code this} is a binary fraction (whether its denominator is a power of 2).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is a binary fraction
     */
    public boolean isBinaryFraction() {
        return IntegerUtils.isPowerOfTwo(denominator);
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
        if (!isBinaryFraction()) {
            throw new ArithmeticException("this must be a binary fraction. Invalid this: " + this);
        }
        return BinaryFraction.of(numerator, -denominator.getLowestSetBit());
    }

    /**
     * This method returns the floor of the base-2 logarithm of {@code this}. In other words, every positive
     * {@code Rational} may be written as a×2<sup>b</sup>, where a is a {@code Rational} such that 1≤a{@literal <}2 and
     * b is an integer; this method returns b.
     *
     * <ul>
     *  <li>{@code this} must be positive.</li>
     *  <li>The result could be any integer.</li>
     * </ul>
     *
     * @return ⌊{@code log<sub>2</sub>this}⌋
     */
    public int binaryExponent() {
        if (this == ONE) return 0;
        if (this == ZERO || signum() != 1) {
            throw new IllegalArgumentException("this must be positive. Invalid this: " + this);
        }
        if (gt(numerator, denominator)) {
            return numerator.divide(denominator).bitLength() - 1;
        } else {
            int exponent = -denominator.divide(numerator).bitLength();
            return isPowerOfTwo() ? exponent + 1 : exponent;
        }
    }

    /**
     * Every {@code Rational} has a <i>left-neighboring {@code float}</i>, or the largest {@code float} that is less
     * than or equal to the {@code Rational}; this {@code float} may be -Infinity. Likewise, every {@code Rational}
     * has a <i>right-neighboring {@code float}</i>: the smallest {@code float} greater than or equal to the
     * {@code Rational}. This {@code float} may be Infinity. If {@code this} is exactly equal to some {@code float},
     * the left- and right-neighboring {@code float}s will both be equal to that {@code float} and to each other. This
     * method returns the pair made up of the left- and right-neighboring {@code float}s. If the left-neighboring
     * {@code float} is a zero, it is a positive zero; if the right-neighboring {@code float} is a zero, it is a
     * negative zero. The exception is when {@code this} is equal to zero; then both neighbors are positive zeroes.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is a pair of {@code float}s that are either equal, or the second is the next-largest
     *  {@code float} after the first. Negative zero may not appear in the first slot of the pair, and positive zero
     *  may only appear in the second slot if the first slot also contains a positive zero. Neither slot may contain a
     *  {@code NaN}.</li>
     * </ul>
     *
     * @return The pair of left- and right-neighboring {@code float}s.
     */
    private @NotNull Pair<Float, Float> floatRange() {
        if (this == ZERO) return new Pair<>(0.0f, 0.0f);
        if (this == ONE) return new Pair<>(1.0f, 1.0f);
        if (numerator.signum() == -1) {
            Pair<Float, Float> negativeRange = negate().floatRange();
            return new Pair<>(-negativeRange.b, -negativeRange.a);
        }
        int exponent = binaryExponent();
        if (exponent > Float.MAX_EXPONENT || exponent == Float.MAX_EXPONENT && gt(this, LARGEST_FLOAT)) {
            return new Pair<>(Float.MAX_VALUE, Float.POSITIVE_INFINITY);
        }
        Rational fraction;
        int adjustedExponent;
        if (exponent < Float.MIN_EXPONENT) {
            fraction = shiftRight(MIN_SUBNORMAL_FLOAT_EXPONENT);
            adjustedExponent = 0;
        } else {
            fraction = shiftRight(exponent).subtract(ONE).shiftLeft(FLOAT_FRACTION_WIDTH);
            adjustedExponent = exponent + Float.MAX_EXPONENT;
        }
        float loFloat = Float.intBitsToFloat(
                (adjustedExponent << FLOAT_FRACTION_WIDTH) + fraction.floor().intValueExact()
        );
        float hiFloat = fraction.isInteger() ? loFloat : successor(loFloat);
        return new Pair<>(loFloat, hiFloat);
    }

    /**
     * Determines whether {@code this} is exactly equal to some {@code float}. If true, the {@code float} may be found
     * using {@link Rational#floatValueExact()}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is exactly equal to a {@code float}
     */
    public boolean isEqualToFloat() {
        if (this == ZERO || this == ONE) return true;
        if (numerator.signum() == -1) {
            return negate().isEqualToFloat();
        }
        if (!isBinaryFraction()) return false;
        int exponent = numerator.bitLength() - denominator.bitLength();
        if (exponent > Float.MAX_EXPONENT || exponent == Float.MAX_EXPONENT && gt(this, LARGEST_FLOAT)) {
            return false;
        }
        int shift = exponent < Float.MIN_EXPONENT ? MIN_SUBNORMAL_FLOAT_EXPONENT : exponent - FLOAT_FRACTION_WIDTH;
        return numerator.getLowestSetBit() - denominator.getLowestSetBit() >= shift;
    }

    /**
     * Every {@code Rational} has a <i>left-neighboring {@code double}</i>, or the largest {@code double} that is less
     * than or equal to the {@code Rational}; this {@code double} may be {@code -Infinity}. Likewise, every
     * {@code Rational} has a <i>right-neighboring {@code double}</i>: the smallest {@code double} greater than or
     * equal to the {@code Rational}. This {@code double} may be {@code Infinity}. If {@code this} is exactly equal to
     * some {@code double}, the left- and right-neighboring {@code double}s will both be equal to that {@code double}
     * and to each other. This method returns the pair made up of the left- and right-neighboring {@code double}s. If
     * the left-neighboring {@code double} is a zero, it is a positive zero; if the right-neighboring {@code double} is
     * a zero, it is a negative zero. The exception is when {@code this} is equal to zero; then both neighbors are
     * positive zeroes.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is a pair of {@code double}s that are either equal, or the second is the next-largest
     *  {@code double} after the first. Negative zero may not appear in the first slot of the pair, and positive zero
     *  may only appear in the second slot if the first slot also contains a positive zero. Neither slot may contain a
     *  {@code NaN}.</li>
     * </ul>
     *
     * @return The pair of left- and right-neighboring {@code double}s.
     */
    private @NotNull Pair<Double, Double> doubleRange() {
        if (this == ZERO) return new Pair<>(0.0, 0.0);
        if (this == ONE) return new Pair<>(1.0, 1.0);
        if (numerator.signum() == -1) {
            Pair<Double, Double> negativeRange = negate().doubleRange();
            return new Pair<>(-negativeRange.b, -negativeRange.a);
        }
        int exponent = binaryExponent();
        if (exponent > Double.MAX_EXPONENT || exponent == Double.MAX_EXPONENT && gt(this, LARGEST_DOUBLE)) {
            return new Pair<>(Double.MAX_VALUE, Double.POSITIVE_INFINITY);
        }
        Rational fraction;
        int adjustedExponent;
        if (exponent < Double.MIN_EXPONENT) {
            fraction = shiftRight(MIN_SUBNORMAL_DOUBLE_EXPONENT);
            adjustedExponent = 0;
        } else {
            fraction = shiftRight(exponent).subtract(ONE).shiftLeft(DOUBLE_FRACTION_WIDTH);
            adjustedExponent = exponent + Double.MAX_EXPONENT;
        }
        double loDouble = Double.longBitsToDouble(
                ((long) adjustedExponent << DOUBLE_FRACTION_WIDTH) + fraction.floor().longValueExact()
        );
        double hiDouble = fraction.isInteger() ? loDouble : successor(loDouble);
        return new Pair<>(loDouble, hiDouble);
    }

    /**
     * Determines whether {@code this} is exactly equal to some {@code double}. If true, the {@code double} may be
     * found using {@link Rational#doubleValueExact()}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is exactly equal to a {@code double}
     */
    public boolean isEqualToDouble() {
        if (this == ZERO || this == ONE) return true;
        if (numerator.signum() == -1) {
            return negate().isEqualToDouble();
        }
        if (!isBinaryFraction()) return false;
        int exponent = numerator.bitLength() - denominator.bitLength();
        if (exponent > Double.MAX_EXPONENT || exponent == Double.MAX_EXPONENT && gt(this, LARGEST_DOUBLE)) {
            return false;
        }
        int shift = exponent < Double.MIN_EXPONENT ? MIN_SUBNORMAL_DOUBLE_EXPONENT : exponent - DOUBLE_FRACTION_WIDTH;
        return numerator.getLowestSetBit() - denominator.getLowestSetBit() >= shift;
    }

    /**
     * Rounds {@code this} to a {@code float}. The details of the rounding are specified by {@code roundingMode}.
     * <ul>
     *  <li>{@code RoundingMode.UNNECESSARY}: If {@code this} is exactly equal to a {@code float}, that {@code float}
     *  is returned. Otherwise, an {@link java.lang.ArithmeticException} is thrown. If {@code this} is zero, positive
     *  zero is returned. Negative zero, {@code Infinity}, and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.FLOOR}: The largest {@code float} less than or equal to {@code this} is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Float.MIN_VALUE}, positive zero is returned.
     *  If {@code this} is less than –{@code Float.MAX_VALUE}, {@code -Infinity} is returned. If {@code this} is
     *  greater than or equal to {@code Float.MAX_VALUE}, {@code Float.MAX_VALUE} is returned. Negative zero and
     *  {@code Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.CEILING}: The smallest {@code float} greater than or equal to {@code this} is returned.
     *  If {@code this} is equal to zero, positive zero is returned. If {@code this} is less than zero and greater than
     *  –{@code Float.MIN_VALUE}, negative zero is returned. If {@code this} is greater than {@code Float.MAX_VALUE},
     *  Infinity is returned. If {@code this} is less than or equal to –{@code Float.MAX_VALUE},
     *  –{@code Float.MAX_VALUE} is returned. {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.DOWN}: The first {@code float} going from {@code this} to 0 (possibly equal to
     *  {@code this}) is returned. If {@code this} is greater than or equal to zero and less than
     *  {@code Float.MIN_VALUE}, positive zero is returned. If {@code this} is less than zero and greater than
     *  –{@code Float.MIN_VALUE}, negative zero is returned. If {@code this} is greater than or equal to
     *  {@code Float.MAX_VALUE}, {@code Float.MAX_VALUE} is returned. If {@code this} is less than or equal to
     *  –{@code Float.MAX_VALUE}, –{@code Float.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot
     *  be returned.</li>
     *  <li>{@code RoundingMode.UP}: The first {@code float} going from {@code this} to the infinity of the same sign
     *  (possibly equal to {@code this}) is returned. If {@code this} is equal to zero, positive zero is returned. If
     *  {@code this} is greater than {@code Float.MAX_VALUE}, {@code Infinity} is returned. If {@code this} is less
     *  than {@code Float.MIN_VALUE}, {@code -Infinity} is returned. Negative zero cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_DOWN}: If {@code this} is closest to one {@code float}, that {@code float} is
     *  returned. If there are two closest {@code float}s, the one with the lower absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Float.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Float.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than or equal to {@code Float.MAX_VALUE},
     *  {@code Float.MAX_VALUE} is returned. If {@code this} is less than or equal to –{@code Float.MAX_VALUE},
     *  –{@code Float.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_UP}: If {@code this} is closest to one {@code float}, that {@code float} is
     *  returned. If there are two closest {@code float}s, the one with the higher absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Float.MIN_VALUE}/2, positive zero is
     *  returned. If {@code this} is greater than –{@code Float.MIN_VALUE}/2 and less than zero, negative zero is
     *  returned. If {@code this} is greater than {@code Float.MAX_VALUE}, {@code Infinity} is returned. If
     *  {@code this} is less than –{@code Float.MAX_VALUE}, {@code -Infinity} is returned.</li>
     *  <li>{@code RoundingMode.HALF_EVEN}: If {@code this} is closest to one {@code float}, that {@code float} is
     *  returned. If there are two closest {@code float}s, the one with the unset lowest-order bit is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Float.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Float.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than {@code Float.MAX_VALUE}, {@code Infinity} is
     *  returned. If {@code this} is less than –{@code Float.MAX_VALUE}, {@code -Infinity} is returned.</li>
     * </ul>
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, {@code this} must be exactly equal to a
     *  {@code float}.</li>
     *  <li>The result may be any {@code float} except {@code NaN}.</li>
     * </ul>
     *
     * @param roundingMode specifies the details of how to round {@code this}.
     * @return {@code this}, rounded
     */
    public float floatValue(@NotNull RoundingMode roundingMode) {
        Pair<Float, Float> floatRange = floatRange();
        if (floatRange.a.equals(floatRange.b)) return floatRange.a;
        switch (roundingMode) {
            case UNNECESSARY:
                throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exactly equal to a" +
                        " float. Invalid this: " + this);
            case FLOOR:
                return floatRange.a;
            case CEILING:
                return floatRange.b;
            case DOWN:
                return floatRange.a < 0 ? floatRange.b : floatRange.a;
            case UP:
                return floatRange.a < 0 ? floatRange.a : floatRange.b;
        }
        if (floatRange.a == Float.NEGATIVE_INFINITY) {
            if (roundingMode == RoundingMode.HALF_UP || roundingMode == RoundingMode.HALF_EVEN) {
                return Float.NEGATIVE_INFINITY;
            } else {
                return -Float.MAX_VALUE;
            }
        }
        if (floatRange.b == Float.POSITIVE_INFINITY) {
            if (roundingMode == RoundingMode.HALF_UP || roundingMode == RoundingMode.HALF_EVEN) {
                return Float.POSITIVE_INFINITY;
            } else {
                return Float.MAX_VALUE;
            }
        }
        Rational midway = ofExact(floatRange.a).get().add(ofExact(floatRange.b).get()).shiftRight(1);
        switch (compare(this, midway)) {
            case LT:
                return floatRange.a;
            case GT:
                return floatRange.b;
            case EQ:
                switch (roundingMode) {
                    case HALF_DOWN:
                        return floatRange.a < 0 ? floatRange.b : floatRange.a;
                    case HALF_UP:
                        return floatRange.a < 0 ? floatRange.a : floatRange.b;
                    case HALF_EVEN:
                        return (Float.floatToIntBits(floatRange.a) & 1) == 0 ? floatRange.a : floatRange.b;
                }
            default:
                throw new IllegalStateException("unreachable");
        }
    }

    /**
     * Rounds {@code this} to a {@code float} using {@code RoundingMode.HALF_EVEN}. If {@code this} is closest to one
     * {@code float}, that {@code float} is returned. If there are two closest {@code float}s, the one with the unset
     * lowest-order bit is returned. If {@code this} is greater than or equal to zero and less than or equal to
     * {@code Float.MIN_VALUE}/2, positive zero is returned. If {@code this} is greater than or equal to
     * –{@code Float.MIN_VALUE}/2 and less than zero, negative zero is returned. If {@code this} is greater than
     * {@code Float.MAX_VALUE}, Infinity is returned. If {@code this} is less than –{@code Float.MAX_VALUE},
     * {@code -Infinity} is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result may be any {@code float} except {@code NaN}.</li>
     * </ul>
     *
     * @return {@code this}, rounded
     */
    public float floatValue() {
        return floatValue(RoundingMode.HALF_EVEN);
    }

    /**
     * Returns a {@code float} exactly equal to {@code this}. Throws an {@code ArithmeticException} if {@code this} is
     * not exactly equal to a {@code float}.
     *
     * <ul>
     *  <li>{@code this} must be a {@code Rational} equal to a {@code float}.</li>
     *  <li>The result is not {@code NaN}, infinite, or negative 0.</li>
     * </ul>
     *
     * @return {@code this}, in {@code float} form
     */
    public float floatValueExact() {
        return floatValue(RoundingMode.UNNECESSARY);
    }

    /**
     * Rounds {@code this} to a {@code double}. The details of the rounding are specified by {@code roundingMode}.
     * <ul>
     *  <li>{@code RoundingMode.UNNECESSARY}: If {@code this} is exactly equal to a {@code double}, that {@code double}
     *  is returned. Otherwise, an {@code ArithmeticException} is thrown. If {@code this} is zero, positive zero is
     *  returned. Negative zero, {@code Infinity}, and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.FLOOR}: The largest {@code double} less than or equal to {@code this} is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Double.MIN_VALUE}, positive zero is
     *  returned. If {@code this} is less than –{@code Double.MAX_VALUE}, {@code -Infinity} is returned. If
     *  {@code this} is greater than or equal to {@code Double.MAX_VALUE}, {@code Double.MAX_VALUE} is returned.
     *  Negative zero and {@code Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.CEILING}: The smallest {@code double} greater than or equal to {@code this} is
     *  returned. If {@code this} is equal to zero, positive zero is returned. If {@code this} is less than zero and
     *  greater than –{@code Double.MIN_VALUE}, negative zero is returned. If {@code this} is greater than
     *  {@code Double.MAX_VALUE}, {@code Infinity} is returned. If {@code this} is less than or equal to
     *  –{@code Double.MAX_VALUE}, –{@code Double.MAX_VALUE} is returned. {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.DOWN}: The first {@code double} going from {@code this} to 0 (possibly equal to
     *  {@code this}) is returned. If {@code this} is greater than or equal to zero and less than
     *  {@code Double.MIN_VALUE}, positive zero is returned. If {@code this} is greater than –{@code Double.MIN_VALUE}
     *  and less than zero, negative zero is returned. If {@code this} is greater than or equal to
     *  {@code Double.MAX_VALUE}, {@code Double.MAX_VALUE} is returned. If {@code this} is less than or equal to
     *  –{@code Double.MAX_VALUE}, –{@code Double.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot
     *  be returned.</li>
     *  <li>{@code RoundingMode.UP}: The first {@code double} going from {@code this} to the infinity of the same sign
     *  (possibly equal to {@code this}) is returned. If {@code this} is equal to zero, positive zero is returned. If
     *  {@code this} is greater than {@code Double.MAX_VALUE}, {@code Infinity} is returned. If {@code this} is less
     *  than {@code Double.MIN_VALUE}, {@code -Infinity} is returned. Negative zero cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_DOWN}: If {@code this} is closest to one {@code double}, that {@code double} is
     *  returned. If there are two closest {@code double}s, the one with the lower absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Double.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Double.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than or equal to {@code Double.MAX_VALUE},
     *  {@code Double.MAX_VALUE} is returned. If {@code this} is less than or equal to –{@code Double.MAX_VALUE},
     *  –{@code Double.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_UP}: If {@code this} is closest to one {@code double}, that {@code double} is
     *  returned. If there are two closest {@code double}s, the one with the higher absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Double.MIN_VALUE}/2, positive zero is
     *  returned. If {@code this} is greater than –{@code Double.MIN_VALUE}/2 and less than zero, negative zero is
     *  returned. If {@code this} is greater than {@code Double.MAX_VALUE}, {@code Infinity} is returned. If
     *  {@code this} is less than –{@code Double.MAX_VALUE}, {@code -Infinity} is returned.</li>
     *  <li>{@code RoundingMode.HALF_EVEN}: If {@code this} is closest to one {@code double}, that {@code double} is
     *  returned. If there are two closest {@code double}s, the one with the unset lowest-order bit is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Double.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Double.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than {@code Double.MAX_VALUE}, Infinity is returned. If
     *  {@code this} is less than –{@code Double.MAX_VALUE}, {@code -Infinity} is returned.</li>
     * </ul>
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, {@code this} must be exactly equal to a
     *  {@code double}.</li>
     *  <li>The result may be any {@code double} except {@code NaN}.</li>
     * </ul>
     *
     * @param roundingMode specifies the details of how to round {@code this}.
     * @return {@code this}, rounded
     */
    public double doubleValue(@NotNull RoundingMode roundingMode) {
        Pair<Double, Double> doubleRange = doubleRange();
        if (doubleRange.a.equals(doubleRange.b)) return doubleRange.a;
        switch (roundingMode) {
            case UNNECESSARY:
                throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exactly equal to a" +
                        " double. Invalid this: " + this);
            case FLOOR:
                return doubleRange.a;
            case CEILING:
                return doubleRange.b;
            case DOWN:
                return doubleRange.a < 0 ? doubleRange.b : doubleRange.a;
            case UP:
                return doubleRange.a < 0 ? doubleRange.a : doubleRange.b;
        }
        if (doubleRange.a == Double.NEGATIVE_INFINITY) {
            if (roundingMode == RoundingMode.HALF_UP || roundingMode == RoundingMode.HALF_EVEN) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return -Double.MAX_VALUE;
            }
        }
        if (doubleRange.b == Double.POSITIVE_INFINITY) {
            if (roundingMode == RoundingMode.HALF_UP || roundingMode == RoundingMode.HALF_EVEN) {
                return Double.POSITIVE_INFINITY;
            } else {
                return Double.MAX_VALUE;
            }
        }
        Rational midway = ofExact(doubleRange.a).get().add(ofExact(doubleRange.b).get()).shiftRight(1);
        switch (compare(this, midway)) {
            case LT:
                return doubleRange.a;
            case GT:
                return doubleRange.b;
            case EQ:
                switch (roundingMode) {
                    case HALF_DOWN:
                        return doubleRange.a < 0 ? doubleRange.b : doubleRange.a;
                    case HALF_UP:
                        return doubleRange.a < 0 ? doubleRange.a : doubleRange.b;
                    case HALF_EVEN:
                        return (Double.doubleToLongBits(doubleRange.a) & 1L) == 0L ? doubleRange.a : doubleRange.b;
                }
            default:
                throw new IllegalStateException("unreachable");
        }
    }

    /**
     * Rounds {@code this} to a {@code double} using {@code RoundingMode.HALF_EVEN}. If {@code this} is closest to one
     * {@code double}, that {@code double} is returned. If there are two closest {@code double}s, the one with the
     * unset lowest-order bit is returned. If {@code this} is greater than or equal to zero and less than or equal to
     * {@code Double.MIN_VALUE}/2, positive zero is returned. If {@code this} is greater than or equal to
     * –{@code Double.MIN_VALUE}/2 and less than zero, negative zero is returned. If {@code this} is greater than
     * {@code Double.MAX_VALUE}, Infinity is returned. If {@code this} is less than –{@code Double.MAX_VALUE},
     * –Infinity is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result may be any {@code double} except {@code NaN}.</li>
     * </ul>
     *
     * @return {@code this}, rounded
     */
    public double doubleValue() {
        return doubleValue(RoundingMode.HALF_EVEN);
    }

    /**
     * Returns a {@code double} exactly equal to {@code this}. Throws an {@code ArithmeticException} if {@code this} is
     * not exactly equal to a {@code double}.
     *
     * <ul>
     *  <li>{@code this} must be a {@code Rational} equal to a {@code double}.</li>
     *  <li>The result is not {@code NaN}, infinite, or negative 0.</li>
     * </ul>
     *
     * @return {@code this}, in {@code double} form
     */
    public double doubleValueExact() {
        return doubleValue(RoundingMode.UNNECESSARY);
    }

    /**
     * Determines whether {@code this} has a terminating digit expansion in a particular base.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param base the base in which we are interesting in expanding {@code this}
     * @return whether {@code this} has a terminating base expansion in base-{@code base}
     */
    public boolean hasTerminatingBaseExpansion(@NotNull BigInteger base) {
        if (lt(base, IntegerUtils.TWO)) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        if (isInteger()) return true;
        BigInteger remainder = denominator;
        for (BigInteger baseFactor : nub(MathUtils.primeFactors(base))) {
            while (remainder.mod(baseFactor).equals(BigInteger.ZERO)) {
                remainder = remainder.divide(baseFactor);
            }
        }
        return remainder.equals(BigInteger.ONE);
    }

    /**
     * Rounds {@code this} to a {@link java.math.BigDecimal} with a specified rounding mode (see documentation for
     * {@code java.math.RoundingMode} for details) and with a specified precision (number of significant digits), or
     * to full precision if {@code precision} is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code precision} cannot be negative.</li>
     *  <li>{@code roundingMode} may be any {@code RoundingMode}.</li>
     *  <li>If {@code precision} is 0, then {@code this} must be a {@code Rational} whose decimal expansion is
     *  terminating; that is, its denominator must only have 2 or 5 as prime factors.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, then {@code precision} must be at least as
     *  large as the number of digits in {@code this}'s decimal expansion.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param precision the precision with which to round {@code this}. 0 indicates full precision.
     * @param roundingMode specifies the details of how to round {@code this}.
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueByPrecision(int precision, @NotNull RoundingMode roundingMode) {
        MathContext context = new MathContext(precision, roundingMode);
        BigDecimal result = new BigDecimal(numerator).divide(new BigDecimal(denominator), context);
        if (precision != 0) {
            result = BigDecimalUtils.setPrecision(result, precision);
        }
        return result;
    }

    /**
     * Rounds {@code this} to a {@link java.math.BigDecimal} with a specified rounding mode (see documentation for
     * {@code java.math.RoundingMode} for details) and with a specified scale (number digits after the decimal point).
     * Scale may be negative; for example, {@code 1E+1} has a scale of –1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>{@code roundingMode} may be any {@code RoundingMode}.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, then {@code scale} must be at least as large
     *  as the smallest n such that {@code this}×10<sup>n</sup> is an integer (and such an n must exist).</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param scale the scale with which to round {@code this}.
     * @param roundingMode specifies the details of how to round {@code this}.
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueByScale(int scale, @NotNull RoundingMode roundingMode) {
        return new BigDecimal(multiply(TEN.pow(scale)).bigIntegerValue(roundingMode), scale);
    }

    /**
     * Rounds {@code this} to a {@code BigDecimal} with a specified precision (number of significant digits), or to
     * full precision if {@code precision} is 0. {@code RoundingMode.HALF_EVEN} is used for rounding.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code precision} cannot be negative.</li>
     *  <li>If {@code precision} is 0, then {@code this} must be a {@code Rational} whose decimal expansion is
     *  terminating; that is, its denominator must only have 2 or 5 as prime factors.</li>
     *  <li>The result is a {@code BigDecimal} with minimal scale. That is, the scale is the smallest non-negative n
     *  such that {@code this}×10<sup>n</sup> is an integer.</li>
     * </ul>
     *
     * @param precision the precision with which to round {@code this}. 0 indicates full precision.
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueByPrecision(int precision) {
        return bigDecimalValueByPrecision(precision, RoundingMode.HALF_EVEN);
    }

    /**
     * Rounds {@code this} to a {@code BigDecimal} with a specified scale (number digits after the decimal point).
     * Scale may be negative; for example, {@code 1E+1} has a scale of –1. {@code RoundingMode.HALF_EVEN} is used for
     * rounding.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param scale the scale with which to round {@code this}.
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueByScale(int scale) {
        return bigDecimalValueByScale(scale, RoundingMode.HALF_EVEN);
    }

    /**
     * Returns a {@code BigDecimal} exactly equal to {@code this}. Throws an {@code ArithmeticException} if
     * {@code this} cannot be represented as a terminating decimal.
     *
     * <ul>
     *  <li>{@code this} must be a {@code Rational} whose decimal expansion is terminating; that is, its denominator
     *  must only have 2 or 5 as prime factors.</li>
     *  <li>The result is a canonical {@code BigDecimal} (see {@link BigDecimalUtils#isCanonical(BigDecimal)}.)</li>
     * </ul>
     *
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueExact() {
        //noinspection BigDecimalMethodWithoutRoundingCalled
        return new BigDecimal(numerator).divide(new BigDecimal(denominator));
    }

    /**
     * Returns the bit length of {@code this}, or the sum of the bit lengths of the numerator and denominator.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return the bit length of {@code this}
     */
    public int bitLength() {
        return numerator.abs().bitLength() + denominator.bitLength();
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull Rational add(@NotNull Rational that) {
        if (this == ZERO) return that;
        if (that == ZERO) return this;
        BigInteger denominatorGcd = denominator.gcd(that.denominator);
        if (denominatorGcd.equals(BigInteger.ONE)) {
            BigInteger sumNumerator = numerator.multiply(that.denominator).add(denominator.multiply(that.numerator));
            if (sumNumerator.equals(BigInteger.ZERO)) return ZERO;
            BigInteger sumDenominator = denominator.multiply(that.denominator);
            if (sumNumerator.equals(sumDenominator)) return ONE;
            return new Rational(sumNumerator, sumDenominator);
        } else {
            BigInteger sumNumerator = numerator.multiply(that.denominator.divide(denominatorGcd))
                    .add(that.numerator.multiply(denominator.divide(denominatorGcd)));
            if (sumNumerator.equals(BigInteger.ZERO)) return ZERO;
            BigInteger gcd = sumNumerator.gcd(denominatorGcd);
            sumNumerator = sumNumerator.divide(gcd);
            BigInteger sumDenominator = denominator.divide(denominatorGcd).multiply(that.denominator.divide(gcd));
            if (sumNumerator.equals(sumDenominator)) return ONE;
            return new Rational(sumNumerator, sumDenominator);
        }
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return –{@code this}
     */
    public @NotNull Rational negate() {
        if (this == ZERO) return ZERO;
        BigInteger negativeNumerator = numerator.negate();
        if (negativeNumerator.equals(denominator)) return ONE;
        return new Rational(negativeNumerator, denominator);
    }

    /**
     * Returns the absolute value of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is a non-negative {@code Rational}.</li>
     * </ul>
     *
     * @return |{@code this}|
     */
    public @NotNull Rational abs() {
        if (this == ZERO || this == ONE) return this;
        if (numerator.equals(IntegerUtils.NEGATIVE_ONE) && denominator.equals(BigInteger.ONE)) return ONE;
        return new Rational(numerator.abs(), denominator);
    }

    /**
     * Returns the sign of {@code this}: 1 if positive, –1 if negative, 0 if equal to 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is –1, 0, or 1.</li>
     * </ul>
     *
     * @return sgn({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public int signum() {
        return numerator.signum();
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull Rational subtract(@NotNull Rational that) {
        if (this == ZERO) return that.negate();
        if (that == ZERO) return this;
        if (this == that) return ZERO;
        BigInteger denominatorGcd = denominator.gcd(that.denominator);
        if (denominatorGcd.equals(BigInteger.ONE)) {
            BigInteger diffNumerator =
                    numerator.multiply(that.denominator).subtract(denominator.multiply(that.numerator));
            if (diffNumerator.equals(BigInteger.ZERO)) return ZERO;
            BigInteger diffDenominator = denominator.multiply(that.denominator);
            if (diffNumerator.equals(diffDenominator)) return ONE;
            return new Rational(diffNumerator, diffDenominator);
        } else {
            BigInteger diffNumerator = numerator.multiply(that.denominator.divide(denominatorGcd))
                    .subtract(that.numerator.multiply(denominator.divide(denominatorGcd)));
            if (diffNumerator.equals(BigInteger.ZERO)) return ZERO;
            BigInteger gcd = diffNumerator.gcd(denominatorGcd);
            diffNumerator = diffNumerator.divide(gcd);
            BigInteger diffDenominator = denominator.divide(denominatorGcd).multiply(that.denominator.divide(gcd));
            if (diffNumerator.equals(diffDenominator)) return ONE;
            return new Rational(diffNumerator, diffDenominator);
        }
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Rational}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Rational multiply(@NotNull Rational that) {
        if (this == ZERO || that == ZERO) return ZERO;
        if (this == ONE) return that;
        if (that == ONE) return this;
        return of(numerator.multiply(that.getNumerator()), denominator.multiply(that.getDenominator()));
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
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Rational multiply(@NotNull BigInteger that) {
        if (this == ZERO || that.equals(BigInteger.ZERO)) return ZERO;
        if (numerator.equals(BigInteger.ONE) && denominator.equals(that) ||
                numerator.equals(IntegerUtils.NEGATIVE_ONE) && denominator.equals(that.negate())) return ONE;
        BigInteger gcd = denominator.gcd(that);
        return new Rational(numerator.multiply(that.divide(gcd)), denominator.divide(gcd));
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code that} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Rational multiply(int that) {
        if (this == ZERO || that == 0) return ZERO;
        if (numerator.equals(BigInteger.ONE) && denominator.equals(BigInteger.valueOf(that))) return ONE;
        if (numerator.equals(IntegerUtils.NEGATIVE_ONE) && denominator.equals(BigInteger.valueOf(that).negate()))
            return ONE;
        BigInteger gcd = denominator.gcd(BigInteger.valueOf(that));
        return new Rational(numerator.multiply(BigInteger.valueOf(that).divide(gcd)), denominator.divide(gcd));
    }

    /**
     * Returns the multiplicative inverse of {@code this}.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is a non-zero {@code Rational}.</li>
     * </ul>
     *
     * @return 1/{@code this}
     */
    public @NotNull Rational invert() {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (this == ONE) return ONE;
        if (numerator.signum() == -1) {
            return new Rational(denominator.negate(), numerator.negate());
        } else {
            return new Rational(denominator, numerator);
        }
    }

    /**
     * Returns the quotient of {@code a} and {@code b}.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Rational}.</li>
     *  <li>{@code that} cannot zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that {@code Rational} that {@code this} is divide by
     * @return {@code this}/{@code that}
     */
    public @NotNull Rational divide(@NotNull Rational that) {
        return multiply(that.invert());
    }

    /**
     * Returns the quotient of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the divisor
     * @return {@code this}/{@code that}
     */
    public @NotNull Rational divide(@NotNull BigInteger that) {
        if (that.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (this == ZERO) return ZERO;
        if (denominator.equals(BigInteger.ONE) && numerator.equals(that)) return ONE;
        BigInteger gcd = numerator.gcd(that);
        if (that.signum() == -1) gcd = gcd.negate();
        return new Rational(numerator.divide(gcd), denominator.multiply(that.divide(gcd)));
    }

    /**
     * Returns the quotient of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the divisor
     * @return {@code this}/{@code that}
     */
    public @NotNull Rational divide(int that) {
        if (that == 0) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (this == ZERO) return ZERO;
        if (denominator.equals(BigInteger.ONE) && numerator.equals(BigInteger.valueOf(that))) return ONE;
        BigInteger gcd = numerator.gcd(BigInteger.valueOf(that));
        if (that < 0) gcd = gcd.negate();
        return new Rational(numerator.divide(gcd), denominator.multiply(BigInteger.valueOf(that).divide(gcd)));
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>. Negative
     * {@code bits} corresponds to a right shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Rational}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull Rational shiftLeft(int bits) {
        if (this == ZERO) return ZERO;
        if (bits == 0) return this;
        if (bits < 0) return shiftRight(-bits);
        int denominatorTwos = denominator.getLowestSetBit();
        if (bits <= denominatorTwos) {
            BigInteger shifted = denominator.shiftRight(bits);
            if (numerator.equals(shifted)) return ONE;
            return new Rational(numerator, shifted);
        } else {
            BigInteger shiftedNumerator = numerator.shiftLeft(bits - denominatorTwos);
            BigInteger shiftedDenominator = denominator.shiftRight(denominatorTwos);
            if (shiftedNumerator.equals(shiftedDenominator)) return ONE;
            return new Rational(shiftedNumerator, shiftedDenominator);
        }
    }

    /**
     * Returns the right shift of {@code this} by {@code bits}; {@code this}×2<sup>–{@code bits}</sup>. Negative
     * {@code bits} corresponds to a left shift.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Rational}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to right-shift by
     * @return {@code this}≫{@code bits}
     */
    public @NotNull Rational shiftRight(int bits) {
        if (this == ZERO) return ZERO;
        if (bits == 0) return this;
        if (bits < 0) return shiftLeft(-bits);
        int numeratorTwos = numerator.getLowestSetBit();
        if (bits <= numeratorTwos) {
            BigInteger shifted = numerator.shiftRight(bits);
            if (shifted.equals(denominator)) return ONE;
            return new Rational(shifted, denominator);
        } else {
            BigInteger shiftedNumerator = numerator.shiftRight(numeratorTwos);
            BigInteger shiftedDenominator = denominator.shiftLeft(bits - numeratorTwos);
            if (shiftedNumerator.equals(shiftedDenominator)) return ONE;
            return new Rational(shiftedNumerator, shiftedDenominator);
        }
    }

    /**
     * Returns the sum of all the {@code Rational}s in {@code xs}. If {@code xs} is empty, 0 is returned.
     *
     * <ul>
     *  <li>{@code xs} must be finite and may not contain any nulls.</li>
     *  <li>The result may be any {@code Rational}.</li>
     * </ul>
     *
     * @param xs an {@code Iterable} of {@code Rational}s.
     * @return Σxs
     */
    public static @NotNull Rational sum(@NotNull Iterable<Rational> xs) {
        if (any(x -> x == null, xs))
            throw new NullPointerException();
        return foldl(Rational::add, ZERO, xs);
    }

    /**
     * Returns the product of all the {@code Rational}s in {@code xs}. If {@code xs} is empty, 1 is returned.
     *
     * <ul>
     *  <li>{@code xs} must be finite and may not contain any nulls.</li>
     *  <li>The result may be any {@code Rational}.</li>
     * </ul>
     *
     * @param xs an {@code Iterable} of {@code Rational}s.
     * @return Πxs
     */
    public static @NotNull Rational product(@NotNull Iterable<Rational> xs) {
        if (any(x -> x == null, xs))
            throw new NullPointerException();
        List<Rational> denominatorSorted = sort(
                (x, y) -> {
                    Ordering o = compare(x.getDenominator(), y.getDenominator());
                    if (o == EQ) {
                        o = compare(x.getNumerator().abs(), y.getNumerator().abs());
                    }
                    if (o == EQ) {
                        o = compare(x.getNumerator().signum(), y.getNumerator().signum());
                    }
                    return o.toInt();
                },
                xs
        );
        return foldl(Rational::multiply, ONE, denominatorSorted);
    }

    /**
     * Returns the differences between successive {@code Rational}s in {@code xs}. If {@code xs} contains a single
     * {@code Rational}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} must not be empty and may not contain any nulls.</li>
     *  <li>The result is does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Rational}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Rational> delta(@NotNull Iterable<Rational> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return adjacentPairsWith((x, y) -> y.subtract(x), xs);
    }

    /**
     * The {@code n}th harmonic number, or the sum of the first {@code n} reciprocals.
     *
     * <ul>
     *  <li>{@code n} must be positive.</li>
     *  <li>The result is a harmonic number.</li>
     * </ul>
     *
     * @param n the index of a harmonic number
     * @return H<sub>{@code n}</sub>
     */
    public static @NotNull Rational harmonicNumber(int n) {
        if (n < 1)
            throw new ArithmeticException("harmonic number must have positive index");
        return sum(map(i -> i == 1 ? ONE : new Rational(BigInteger.ONE, BigInteger.valueOf(i)), range(1, n)));
    }

    /**
     * Returns {@code this} raised to the power of {@code p}. 0<sup>0</sup> yields 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code p} may be any {@code int}.</li>
     *  <li>If {@code p}{@literal <}0, {@code this} cannot be 0.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power that {@code this} is raised to
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull Rational pow(int p) {
        if (p == 0) return ONE;
        if (p == 1) return this;
        if (p < 0) {
            return invert().pow(-p);
        }
        if (this == ZERO || this == ONE) return this;
        if (this.equals(ONE.negate()) && p % 2 == 0) return ONE;
        BigInteger pNumerator = numerator.pow(p);
        BigInteger pDenominator = denominator.pow(p);
        if (pDenominator.signum() == -1) {
            pNumerator = pNumerator.negate();
            pDenominator = pDenominator.negate();
        }
        return new Rational(pNumerator, pDenominator);
    }

    /**
     * Returns the floor of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
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
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return ⌈{@code this}⌉
     */
    public @NotNull BigInteger ceiling() {
        return bigIntegerValue(RoundingMode.CEILING);
    }

    /**
     * Returns the fractional part of {@code this}; {@code this}–⌊{@code this}⌋.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is a {@code Rational} x such that 0≤x{@literal <}1.</li>
     * </ul>
     *
     * @return the fractional part of {@code this}
     */
    public @NotNull Rational fractionalPart() {
        if (denominator.equals(BigInteger.ONE)) return ZERO;
        return new Rational(numerator.mod(denominator), denominator);
    }

    /**
     * Rounds {@code this} a rational number that is an integer multiple of 1/{@code denominator} according to
     * {@code roundingMode}; see documentation for {@link java.math.RoundingMode} for details.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code denominator} must be positive.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, {@code this}'s denominator must divide
     *  {@code denominator}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param denominator the denominator which represents the precision that {@code this} is rounded to.
     * @param roundingMode determines the way in which {@code this} is rounded. Options are {@code RoundingMode.UP},
     *                     {@code RoundingMode.DOWN}, {@code RoundingMode.CEILING}, {@code RoundingMode.FLOOR},
     *                     {@code RoundingMode.HALF_UP}, {@code RoundingMode.HALF_DOWN},
     *                     {@code RoundingMode.HALF_EVEN}, and {@code RoundingMode.UNNECESSARY}.
     * @return {@code this}, rounded to an integer multiple of 1/{@code denominator}
     */
    public @NotNull Rational roundToDenominator(@NotNull BigInteger denominator, @NotNull RoundingMode roundingMode) {
        if (denominator.signum() != 1)
            throw new ArithmeticException("must round to a positive denominator");
        return of(multiply(denominator).bigIntegerValue(roundingMode)).divide(denominator);
    }

    /**
     * Finds the continued fraction of {@code this}. If we pretend that the result is an array called a of length n,
     * then {@code this}=a[0]+1/(a[1]+1/(a[2]+...+1/a[n-1])...). Every rational number has two such representations;
     * this method returns the shortest one.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is non-null and non-empty. None of its elements are null. The first element may be any
     *  {@code BigInteger}; the remaining elements, if any, are all positive. If the result has more than one element,
     *  the last element is greater than 1.</li>
     * </ul>
     *
     * Length is O(log({@code denominator}))
     *
     * @return the continued-fraction-representation of {@code this}
     */
    public @NotNull List<BigInteger> continuedFraction() {
        List<BigInteger> continuedFraction = new ArrayList<>();
        Rational remainder = this;
        while (true) {
            BigInteger floor = remainder.floor();
            continuedFraction.add(floor);
            remainder = remainder.subtract(of(floor));
            if (remainder == ZERO) break;
            remainder = remainder.invert();
        }
        return continuedFraction;
    }

    /**
     * Returns the {@code Rational} corresponding to a continued fraction. Every rational number has two continued-
     * fraction representations; either is accepted.
     *
     * <ul>
     *  <li>{@code continuedFraction} must be non-null and non-empty. All elements but the first must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param continuedFraction a continued fraction
     * @return a[0]+1/(a[1]+1/(a[2]+...+1/a[n-1])...)
     */
    public static @NotNull Rational fromContinuedFraction(@NotNull List<BigInteger> continuedFraction) {
        if (continuedFraction.isEmpty())
            throw new IllegalArgumentException("continued fraction may not be empty");
        BigInteger lastElement = continuedFraction.get(continuedFraction.size() - 1);
        if (continuedFraction.size() > 1 && lastElement.signum() != 1)
            throw new IllegalArgumentException("all continued fraction elements but the first must be positive");
        Rational x = of(lastElement);
        for (int i = continuedFraction.size() - 2; i >= 0; i--) {
            if (i != 0 && continuedFraction.get(i).signum() != 1)
                throw new IllegalArgumentException("all continued fraction elements but the first must be positive");
            x = x.invert().add(of(continuedFraction.get(i)));
        }
        return x;
    }

    /**
     * Returns the convergents, or rational approximations of {@code this} formed by truncating its continued fraction
     * at various points. The first element of the result is the floor of {@code this}, and the last element is
     * {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is a non-null, finite, non-empty {@code Iterable} that consists of the convergents of its last
     *  element.</li>
     * </ul>
     *
     * Length is O(log({@code denominator}))
     *
     * @return the convergents of {@code this}.
     */
    public @NotNull Iterable<Rational> convergents() {
        return map(cf -> fromContinuedFraction(toList(cf)), tail(inits(continuedFraction())));
    }

    /**
     * Returns the positional expansion of (non-negative) {@code this} in a given base, in the form of a triple of
     * {@code BigInteger} lists. The first list contains the digits before the decimal point, the second list contains
     * the non-repeating part of the digits after the decimal point, and the third list contains the repeating digits.
     * The digits are given in the usual order: most-significant first. The first two lists may be empty, but the third
     * is always non-empty (if the digits terminate, the third list contains a single zero). The sign of {@code this}
     * is ignored.
     *
     * <ul>
     *  <li>{@code this} cannot be negative.</li>
     *  <li>{@code base} must be greater than 1.</li>
     *  <li>The elements of the result are all non-null. The elements of the elements are all non-negative. The first
     *  element does not begin with a zero. The last element is non-empty, and is not equal to [{@code base}–1] (i.e. a
     *  decimal like 0.4999... is never returned; 0.5000... is preferred). The second and third lists are minimal; that
     *  is, the sequence (second)(third)(third)(third)... cannot be represented in a more compact way. The lists [1, 2]
     *  and [3, 1, 2] are not minimal, because the sequence [1, 2, 3, 1, 2, 3, 1, 2, ...] can be represented by [] and
     *  [1, 2, 3]. The lists [] and [1, 2, 1, 2] are not minimal either, because the sequence
     *  [1, 2, 1, 2, 1, 2, ...] can be represented by [] and [1, 2].</li>
     * </ul>
     *
     * @param base the base of the positional expansion
     * @return a triple containing the digits before the decimal point, the non-repeating digits after the decimal
     * point, and the repeating digits.
     */
    public @NotNull Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> positionalNotation(
            @NotNull BigInteger base
    ) {
        if (signum() == -1)
            throw new IllegalArgumentException("this cannot be negative");
        BigInteger floor = floor();
        List<BigInteger> beforeDecimal = IntegerUtils.bigEndianDigits(base, floor);
        Rational fractionalPart = subtract(of(floor));
        BigInteger numerator = fractionalPart.numerator;
        BigInteger denominator = fractionalPart.denominator;
        BigInteger remainder = numerator.multiply(base);
        int index = 0;
        Integer repeatingIndex;
        List<BigInteger> afterDecimal = new ArrayList<>();
        Map<BigInteger, Integer> remainders = new HashMap<>();
        while (true) {
            remainders.put(remainder, index);
            BigInteger digit = remainder.divide(denominator);
            afterDecimal.add(digit);
            remainder = remainder.subtract(denominator.multiply(digit)).multiply(base);
            repeatingIndex = remainders.get(remainder);
            if (repeatingIndex != null) break;
            index++;
        }
        List<BigInteger> nonrepeating = new ArrayList<>();
        List<BigInteger> repeating = new ArrayList<>();
        for (int i = 0; i < repeatingIndex; i++) {
            nonrepeating.add(afterDecimal.get(i));
        }
        for (int i = repeatingIndex; i < afterDecimal.size(); i++) {
            repeating.add(afterDecimal.get(i));
        }
        return new Triple<>(beforeDecimal, nonrepeating, repeating);
    }

    /**
     * Creates a {@code Rational} from a base expansion.
     *
     * <ul>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>{@code beforeDecimalPoint} must only contain elements greater than or equal to zero and less than
     *  {@code base}.</li>
     *  <li>{@code nonRepeating} must only contain elements greater than or equal to zero and less than
     *  {@code base}.</li>
     *  <li>{@code repeating} must only contain elements greater than or equal to zero and less than
     *  {@code base}. It must also be non-empty; to input a terminating expansion, use one (or more) zeros.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param base the base of the positional expansion
     * @param beforeDecimalPoint the digits before the decimal point
     * @param nonRepeating the non-repeating portion of the digits after the decimal point
     * @param repeating the repeating portion of the digits after the decimal point
     * @return (beforeDecimalPoint).(nonRepeating)(repeating)(repeating)(repeating)..._(base)
     */
    public static @NotNull Rational fromPositionalNotation(
            @NotNull BigInteger base,
            @NotNull List<BigInteger> beforeDecimalPoint,
            @NotNull List<BigInteger> nonRepeating,
            @NotNull List<BigInteger> repeating
    ) {
        if (repeating.isEmpty())
            throw new IllegalArgumentException("repeating must be nonempty");
        BigInteger floor = IntegerUtils.fromBigEndianDigits(base, beforeDecimalPoint);
        BigInteger nonRepeatingInteger = IntegerUtils.fromBigEndianDigits(base, nonRepeating);
        BigInteger repeatingInteger = IntegerUtils.fromBigEndianDigits(base, repeating);
        Rational nonRepeatingPart = of(nonRepeatingInteger, base.pow(nonRepeating.size()));
        Rational repeatingPart = of(repeatingInteger, base.pow(repeating.size()).subtract(BigInteger.ONE))
                .divide(base.pow(nonRepeating.size()));
        return of(floor).add(nonRepeatingPart).add(repeatingPart);
    }

    /**
     * Returns the digits of (non-negative) {@code this} in a given base. The return value is a pair consisting of the
     * digits before the decimal point (in a list) and the digits after the decimal point (in a possibly-infinite
     * {@code Iterable}). Trailing zeroes are not included.
     *
     * <ul>
     *  <li>{@code this} cannot be negative.</li>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>Both of the result's elements are non-null. The first element does not begin with a zero. The second
     *  element is either finite or eventually repeating.</li>
     *  <li>All the elements of both of the result's elements only contain non-negative {@code BigInteger}s less than
     *  {@code base}. The second element does not contain an infinite sequence of zeros or an infinite sequence of
     *  {@code base}–1.</li>
     * </ul>
     *
     * @param base the base of the digits
     * @return a pair consisting of the digits before the decimal point and the digits after
     */
    public @NotNull Pair<List<BigInteger>, Iterable<BigInteger>> digits(@NotNull BigInteger base) {
        if (signum() == -1)
            throw new IllegalArgumentException("this cannot be negative");
        BigInteger floor = floor();
        List<BigInteger> beforeDecimal = IntegerUtils.bigEndianDigits(base, floor);
        Rational fractionalPart = subtract(of(floor));
        BigInteger numerator = fractionalPart.numerator;
        BigInteger denominator = fractionalPart.denominator;
        final BigInteger firstRemainder = numerator.multiply(base);
        Iterable<BigInteger> afterDecimal = () -> new NoRemoveIterator<BigInteger>() {
            private boolean knownRepeating;
            private int index;
            private @Nullable Integer repeatingIndex;
            private @NotNull BigInteger remainder;
            private @Nullable Map<BigInteger, Integer> remainders;
            {
                knownRepeating = false;
                index = 0;
                repeatingIndex = null;
                remainder = firstRemainder;
                remainders = new HashMap<>();
            }

            @Override
            public boolean hasNext() {
                return knownRepeating || repeatingIndex == null;
            }

            @Override
            public BigInteger next() {
                if (!knownRepeating) {
                    remainders.put(remainder, index);
                }
                BigInteger digit = remainder.divide(denominator);
                remainder = remainder.subtract(denominator.multiply(digit)).multiply(base);
                if (!knownRepeating) {
                    repeatingIndex = remainders.get(remainder);
                    if (repeatingIndex != null && !remainder.equals(BigInteger.ZERO)) {
                        knownRepeating = true;
                        remainders = null;
                    } else {
                        index++;
                    }
                }
                return digit;
            }
        };
        return new Pair<>(beforeDecimal, skipLastIf(i -> i.equals(BigInteger.ZERO), afterDecimal));
    }

    /**
     * Converts {@code this} to a {@code String} in any base greater than 1. {@code this} must have a terminating
     * expansion in the base. If the base is 36 or less, the digits are '0' through '9' followed by 'A' through 'Z'. If
     * the base is greater than 36, the digits are written in decimal and each digit is surrounded by parentheses. If
     * {@code this} has a fractional part, a decimal point is used. Zero is represented by "0" if the base is 36 or
     * less, or "(0)" otherwise. There are no leading zeroes before the decimal point (unless {@code this} is less than
     * 1, in which case there is exactly one zero) and no trailing zeroes after. Scientific notation is not used. If
     * {@code this} is negative, the result will contain a leading '-'.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>{@code this} must have a terminating base-{@code base} expansion.</li>
     *  <li>The result is a {@code String} representing a {@code Rational} in the manner previously described. See the
     *  unit test and demo for further reference.</li>
     * </ul>
     *
     * @param base the base of the output digits
     * @return a {@code String} representation of {@code this} in base {@code base}
     */
    public @NotNull String toStringBase(@NotNull BigInteger base) {
        if (!hasTerminatingBaseExpansion(base))
            throw new ArithmeticException(this + " has a non-terminating base-" + base + " expansion");
        boolean smallBase = le(base, BigInteger.valueOf(36));
        Pair<List<BigInteger>, Iterable<BigInteger>> digits = abs().digits(base);
        Function<BigInteger, String> digitFunction;
        if (smallBase) {
            digitFunction = i -> Character.toString(IntegerUtils.toDigit(i.intValueExact()));
        } else {
            digitFunction = i -> "(" + i + ")";
        }
        String beforeDecimal;
        if (digits.a.isEmpty()) {
            beforeDecimal = smallBase ? "0" : "(0)";
        } else {
            beforeDecimal = concatStrings(map(digitFunction, digits.a));
        }
        String result;
        if (isEmpty(digits.b)) {
            result = beforeDecimal;
        } else {
            String afterDecimal = concatStrings(map(digitFunction, digits.b));
            result = beforeDecimal + "." + afterDecimal;
        }
        return signum() == -1 ? "-" + result : result;
    }

    /**
     * Converts {@code this} to a {@code String} in any base greater than 1, rounding to {@code scale} digits after the
     * decimal point. A scale of 0 indicates rounding to an integer, and a negative scale indicates rounding to a
     * positive power of {@code base}. All rounding is done towards 0 (truncation), so that the displayed digits are
     * unaltered. If the result is an approximation (that is, not all digits are displayed) and the scale is positive,
     * an ellipsis ("...") is appended. If the base is 36 or less, the digits are '0' through '9' followed by 'A'
     * through 'Z'. If the base is greater than 36, the digits are written in decimal and each digit is surrounded by
     * parentheses. If {@code this} has a fractional part, a decimal point is used. Zero is represented by "0" if the
     * base is 36 or less, or "(0)" otherwise. There are no leading zeroes before the decimal point (unless
     * {@code this} is less than 1, in which case there is exactly one zero) and no trailing zeroes after (unless an
     * ellipsis is present, in which case there may be any number of trailing zeroes). Scientific notation is not used.
     * If {@code this} is negative, the result will contain a leading '-'.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>The result is a {@code String} representing a {@code Rational} in the manner previously described. See the
     *  unit test and demo for further reference.</li>
     * </ul>
     *
     * @param base the base of the output digits
     * @param scale the maximum number of digits after the decimal point in the result. If {@code this} has a
     *              terminating base-{@code base} expansion, the actual number of digits after the decimal point may be
     *              less.
     * @return a {@code String} representation of {@code this} in base {@code base}
     */
    public @NotNull String toStringBase(@NotNull BigInteger base, int scale) {
        if (lt(base, IntegerUtils.TWO))
            throw new IllegalArgumentException("base must be at least 2");
        BigInteger power = scale >= 0 ? base.pow(scale) : base.pow(-scale);
        Rational scaled = scale >= 0 ? multiply(power) : divide(power);
        boolean ellipsis = scale > 0 && !scaled.isInteger();
        Rational rounded = of(scaled.bigIntegerValue(RoundingMode.DOWN));
        rounded = scale >= 0 ? rounded.divide(power) : rounded.multiply(power);
        String result = rounded.toStringBase(base);
        if (ellipsis) {
            //pad with trailing zeroes if necessary
            int dotIndex = result.indexOf('.');
            if (dotIndex == -1) {
                dotIndex = result.length();
                result = result + ".";
            }
            if (le(base, BigInteger.valueOf(36))) {
                int missingZeroes = scale - result.length() + dotIndex + 1;
                result += replicate(missingZeroes, '0');
            } else {
                int missingZeroes = scale;
                for (int i = dotIndex + 1; i < result.length(); i++) {
                    if (result.charAt(i) == '(') missingZeroes--;
                }
                result += concatStrings(replicate(missingZeroes, "(0)"));
            }
            result += "...";
        }
        return result;
    }

    /**
     * Converts a {@code String} written in some base to a {@code Rational}. If the base is 36 or less, the digits are
     * '0' through '9' followed by 'A' through 'Z'. If the base is greater than 36, the digits are written in decimal
     * and each digit is surrounded by parentheses (in this case, the {@code String} representing the digit cannot be
     * empty and no leading zeroes are allowed unless the digit is 0). The empty {@code String} represents 0. Leading
     * zeroes are permitted. If the {@code String} is invalid, an exception is thrown.
     *
     * <ul>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>{@code s} must either be composed of the digits '0' through '9' and 'A' through 'Z', or a sequence of
     *  non-negative decimal integers, each surrounded by parentheses. {@code s} may contain a single decimal point '.'
     *  anywhere outside of parentheses. In any case there may be an optional leading '-'. {@code s} may also be empty,
     *  but "-" is not permitted. Scientific notation is not accepted.</li>
     *  <li>If {@code base} is between 2 and 36, {@code s} may only include the corresponding characters, the optional
     *  '.', and the optional leading '-'. If {@code base} is greater than 36, {@code s} must be composed of decimal
     *  integers surrounded by parentheses (with the optional leading '-'), each integer being non-negative and less
     *  than {@code base}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param base the base that the {@code s} is written in
     * @param s the input {@code String}
     * @return the {@code Rational} represented by {@code s}
     */
    public static @NotNull Rational fromStringBase(@NotNull BigInteger base, @NotNull String s) {
        if (lt(base, IntegerUtils.TWO))
            throw new IllegalArgumentException("base must be at least 2");
        if (s.isEmpty()) return ZERO;
        try {
            if (s.equals("-") || s.equals(".") || s.equals("-."))
                throw new IllegalArgumentException("invalid String");
            boolean negative = head(s) == '-';
            if (negative) s = tail(s);
            boolean smallBase = le(base, BigInteger.valueOf(36));
            Function<String, List<BigInteger>> undigitFunction;
            if (smallBase) {
                undigitFunction = t -> toList(map(c -> BigInteger.valueOf(IntegerUtils.fromDigit(c)), fromString(t)));
            } else {
                undigitFunction = t -> {
                    if (t.isEmpty()) return Collections.emptyList();
                    if (head(t) != '(' || last(t) != ')' || t.contains("()"))
                        throw new IllegalArgumentException("invalid String");
                    t = tail(init(t));
                    return toList(
                            map(
                                    u -> {
                                        Optional<BigInteger> oi = Readers.readBigInteger(u);
                                        if (!oi.isPresent())
                                            throw new IllegalArgumentException("improperly-formatted digit");
                                        return oi.get();
                                    },
                                    Arrays.asList(t.split("\\)\\("))
                            )
                    );
                };
            }
            Rational result;
            int dotIndex = s.indexOf(".");
            if (dotIndex == -1) {
                result = of(IntegerUtils.fromBigEndianDigits(base, undigitFunction.apply(s)));
            } else {
                List<BigInteger> beforeDecimal = undigitFunction.apply(take(dotIndex, s));
                List<BigInteger> afterDecimal = undigitFunction.apply(drop(dotIndex + 1, s));
                result = fromPositionalNotation(
                        base,
                        beforeDecimal,
                        afterDecimal,
                        Collections.singletonList(BigInteger.ZERO)
                );
            }
            return negative ? result.negate() : result;
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Multiplies a {@code List} of {@code Rational}s by some positive constant to yield a {@code List} of
     * {@code BigInteger}s with no common factor. This gives a canonical representation of {@code Rational} lists
     * considered equivalent under multiplication by positive {@code Rational}s.
     *
     * <ul>
     *  <li>{@code xs} cannot contain any nulls.</li>
     *  <li>The result is a {@code List} of {@code BigInteger}s whose GCD is 0 or 1.</li>
     * </ul>
     *
     * @param xs the {@code List} of {@code Rational}s
     * @return a canonical representation of {@code xs} as a {@code List} of {@code BigInteger}s
     */
    @SuppressWarnings("ConstantConditions")
    public static @NotNull List<BigInteger> cancelDenominators(@NotNull List<Rational> xs) {
        BigInteger lcm = foldl(MathUtils::lcm, BigInteger.ONE, map(Rational::getDenominator, xs));
        List<BigInteger> canceled = toList(map(x -> x.multiply(lcm).getNumerator(), xs));
        BigInteger gcd = foldl(BigInteger::gcd, BigInteger.ZERO, canceled);
        return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE) ?
                canceled :
                toList(map(x -> x.divide(gcd), canceled));
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
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
        if (that == null || Rational.class != that.getClass()) return false;
        Rational r = (Rational) that;
        return denominator.equals(r.denominator) && numerator.equals(r.numerator);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return 31 * numerator.hashCode() + denominator.hashCode();
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code Rational} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull Rational that) {
        if (this == that) return 0;
        int thisSign = signum();
        int thatSign = that.signum();
        if (thisSign > thatSign) return 1;
        if (thisSign < thatSign) return -1;
        return numerator.multiply(that.denominator).compareTo(that.numerator.multiply(denominator));
    }

    /**
     * Creates a {@code Rational} from a {@code String}. Valid {@code String}s are of the form {@code a.toString()} or
     * {@code a.toString() + "/" + b.toString()}, where {@code a} and {@code b} are some {@code BigInteger}s,
     * {@code b}{@literal >}0, and {@code a} and {@code b} are coprime. If the {@code String} is invalid, the method
     * returns {@code Optional.empty()} without throwing an exception; this aids composability.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code Rational}, or be empty.</li>
     * </ul>
     *
     * @param s a {@code String} representation of a {@code Rational}
     * @return the {@code Rational} represented by {@code s}, or an empty {@code Optional} if {@code s} is invalid
     */
    public static @NotNull Optional<Rational> read(@NotNull String s) {
        if (s.isEmpty()) return Optional.empty();
        int slashIndex = s.indexOf("/");
        if (slashIndex == -1) {
            Optional<BigInteger> n = Readers.readBigInteger(s);
            return n.map(Rational::of);
        } else {
            Optional<BigInteger> numerator = Readers.readBigInteger(s.substring(0, slashIndex));
            if (!numerator.isPresent()) return Optional.empty();
            Optional<BigInteger> denominator = Readers.readBigInteger(s.substring(slashIndex + 1));
            if (!denominator.isPresent() || denominator.get().equals(BigInteger.ZERO)) return Optional.empty();
            Rational candidate = of(numerator.get(), denominator.get());
            return candidate.toString().equals(s) ? Optional.of(candidate) : Optional.<Rational>empty();
        }
    }

    /**
     * Finds the first occurrence of a {@code Rational} in a {@code String}. Returns the {@code Rational} and the index
     * at which it was found. Returns an empty {@code Optional} if no {@code Rational} is found. Only {@code String}s
     * which could have been emitted by {@link Rational#toString} are recognized. The longest possible {@code Rational}
     * is parsed.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code Rational} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<Rational, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(Rational::read, "-/0123456789").apply(s);
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Rational}.</li>
     *  <li>The result is a {@code String} in one of two forms: {@code a.toString()} or {@code a.toString() + "/" +
     *  b.toString()}, where {@code a} and {@code b} are some {@code BigInteger}s such that {@code b} is positive and
     *  {@code a} and {@code b} have no positive common factors greater than 1.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        if (denominator.equals(BigInteger.ONE)) {
            return numerator.toString();
        } else {
            return numerator.toString() + "/" + denominator.toString();
        }
    }

    /**
     * Ensures that {@code this} is valid. Must return true for any {@code Rational} used outside this class.
     */
    public void validate() {
        assertEquals(toString(), numerator.gcd(denominator), BigInteger.ONE);
        assertEquals(toString(), denominator.signum(), 1);
        if (equals(ZERO)) assertTrue(toString(), this == ZERO);
        if (equals(ONE)) assertTrue(toString(), this == ONE);
    }
}
